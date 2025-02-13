package cn.ant0n.gbm.domain.activity.service.trial.node;

import cn.ant0n.gbm.domain.activity.model.entity.MarketProductEntity;
import cn.ant0n.gbm.domain.activity.model.entity.TrialBalanceEntity;
import cn.ant0n.gbm.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import cn.ant0n.gbm.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.ant0n.gbm.types.design.framework.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EndNode extends AbstractGroupBuyMarketSupport<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> {


    @Override
    public StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> get(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) {
        return null;
    }

    @Override
    protected TrialBalanceEntity doApply(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) {
        log.info("拼团商品查询试算服务-动态上下文查询测试, ProductSkuVO:{}, GroupBuyActivityDiscountV:{}", dynamicContext.getProductSkuVO(), dynamicContext.getGroupBuyActivityDiscountVO());
        return TrialBalanceEntity.builder()
                .targetCount(dynamicContext.getGroupBuyActivityDiscountVO().getTarget())
                .payPrice(dynamicContext.getPayPrice())
                .isEnable(!dynamicContext.getIsDisable())
                .isVisible(!dynamicContext.getIsNotVisible())
                .originalPrice(dynamicContext.getProductSkuVO().getPrice())
                .deductionPrice(dynamicContext.getDeductionPrice())
                .startTime(dynamicContext.getGroupBuyActivityDiscountVO().getStartTime())
                .endTime(dynamicContext.getGroupBuyActivityDiscountVO().getEndTime())
                .validTime(dynamicContext.getGroupBuyActivityDiscountVO().getValidTime())
                .activityId(dynamicContext.getGroupBuyActivityDiscountVO().getActivityId()).build();
    }
}
