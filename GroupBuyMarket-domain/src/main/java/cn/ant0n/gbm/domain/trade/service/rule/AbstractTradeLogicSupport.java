package cn.ant0n.gbm.domain.trade.service.rule;


import cn.ant0n.gbm.domain.trade.service.rule.factory.TradeLogicFilterChainFactory;
import cn.ant0n.gbm.types.design.framework.tree.AbstractMultiThreadStrategyRouter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public abstract class AbstractTradeLogicSupport<TradeFactorEntity, DynamicContext, LogicFilterResultEntity> extends AbstractMultiThreadStrategyRouter<TradeFactorEntity, TradeLogicFilterChainFactory.DynamicContext, LogicFilterResultEntity> {


    @Override
    protected void multiThread(TradeFactorEntity requestParameter, TradeLogicFilterChainFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {}


}
