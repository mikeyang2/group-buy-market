package cn.ant0n.gbm.domain.activity.service.trial.node;

import cn.ant0n.gbm.domain.activity.model.entity.MarketProductEntity;
import cn.ant0n.gbm.domain.activity.model.entity.TrialBalanceEntity;
import cn.ant0n.gbm.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import cn.ant0n.gbm.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.ant0n.gbm.types.design.framework.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class RootNode extends AbstractGroupBuyMarketSupport<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> {

    @Resource
    private SwitchNode switchNode;

    @Override
    protected TrialBalanceEntity doApply(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        log.info("ROOT节点");
        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> get(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) {
        return switchNode;
    }
}
