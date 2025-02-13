package cn.ant0n.gbm.domain.activity.service.trial.node;

import cn.ant0n.gbm.domain.activity.model.entity.MarketProductEntity;
import cn.ant0n.gbm.domain.activity.model.entity.TrialBalanceEntity;
import cn.ant0n.gbm.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.ant0n.gbm.domain.activity.model.valobj.ProductSkuVO;
import cn.ant0n.gbm.domain.activity.repository.IActivityRepository;
import cn.ant0n.gbm.domain.activity.service.discount.IDiscountCalculateService;
import cn.ant0n.gbm.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import cn.ant0n.gbm.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.ant0n.gbm.domain.activity.service.trial.task.QueryGroupBuyActivityDiscountVOThreadTask;
import cn.ant0n.gbm.domain.activity.service.trial.task.QueryProductSkuVOFromDBThreadTask;
import cn.ant0n.gbm.types.design.framework.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.*;

@Slf4j
@Component
public class MarketNode extends AbstractGroupBuyMarketSupport<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> {

    @Resource
    private TagNode tagNode;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    @Resource
    private IActivityRepository activityRepository;
    @Resource
    private ApplicationContext applicationContext;


    @Override
    public StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> get(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) {
        return tagNode;
    }

    @Override
    protected void multiThread(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        QueryProductSkuVOFromDBThreadTask queryProductSkuVOFromDBThreadTask = new QueryProductSkuVOFromDBThreadTask(activityRepository, requestParameter.getProductId());
        QueryGroupBuyActivityDiscountVOThreadTask queryGroupBuyActivityDiscountVOThreadTask = new QueryGroupBuyActivityDiscountVOThreadTask(
                activityRepository,
                requestParameter.getProductId(),
                requestParameter.getSource(),
                requestParameter.getChannel()
        );

        CompletableFuture<ProductSkuVO> productSkuVOCompletableFuture = CompletableFuture.supplyAsync(() ->{
            try {
                return queryProductSkuVOFromDBThreadTask.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        CompletableFuture<GroupBuyActivityDiscountVO> groupBuyActivityDiscountVOCompletableFuture = CompletableFuture.supplyAsync(() ->{
            try {
                return queryGroupBuyActivityDiscountVOThreadTask.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        dynamicContext.setProductSkuVO(productSkuVOCompletableFuture.get());
        dynamicContext.setGroupBuyActivityDiscountVO(groupBuyActivityDiscountVOCompletableFuture.get());
        log.info("拼团商品查询试算服务-MarketNode:userId:{}, 异步多线程加载数据【ProductSkuVO, GroupBuyActivityDiscountV】完成", requestParameter.getUserId());
    }

    @Override
    protected TrialBalanceEntity doApply(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        log.info("MARKET节点");
        IDiscountCalculateService discountCalculateService = applicationContext.getBean(dynamicContext.getGroupBuyActivityDiscountVO().getGroupBuyDiscount().getDiscountPlan(), IDiscountCalculateService.class);
        discountCalculateService.calculate(requestParameter, dynamicContext);
        log.info("折扣价格为:{}", dynamicContext.getDeductionPrice());
        return router(requestParameter, dynamicContext);
    }
}
