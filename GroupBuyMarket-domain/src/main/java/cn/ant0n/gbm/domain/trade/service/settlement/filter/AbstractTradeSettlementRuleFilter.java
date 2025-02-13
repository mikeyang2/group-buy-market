package cn.ant0n.gbm.domain.trade.service.settlement.filter;


import cn.ant0n.gbm.domain.trade.service.settlement.filter.factory.TradeSettlementRuleFilterFactory;
import cn.ant0n.gbm.types.design.framework.tree.AbstractMultiThreadStrategyRouter;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public abstract class AbstractTradeSettlementRuleFilter<TradeSettlementFilterFactorEntity, DynamicContext, TradeSettlementFilterResultEntity> extends AbstractMultiThreadStrategyRouter<TradeSettlementFilterFactorEntity, TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementFilterResultEntity> {
    @Override
    protected void multiThread(TradeSettlementFilterFactorEntity requestParameter, TradeSettlementRuleFilterFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {}


}
