package cn.ant0n.gbm.domain.trade.service.settlement.filter.impl;

import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderListEntity;
import cn.ant0n.gbm.domain.trade.model.entity.TradeSettlementFilterFactorEntity;
import cn.ant0n.gbm.domain.trade.model.entity.TradeSettlementFilterResultEntity;
import cn.ant0n.gbm.domain.trade.repository.ITradeRepository;
import cn.ant0n.gbm.domain.trade.service.settlement.filter.AbstractTradeSettlementRuleFilter;
import cn.ant0n.gbm.domain.trade.service.settlement.filter.factory.TradeSettlementRuleFilterFactory;
import cn.ant0n.gbm.types.design.framework.tree.StrategyHandler;
import cn.ant0n.gbm.types.enums.ResponseCode;
import cn.ant0n.gbm.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class OutTradeNoExistFilter extends AbstractTradeSettlementRuleFilter<TradeSettlementFilterFactorEntity, TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementFilterResultEntity> {

    @Resource
    private ITradeRepository tradeRepository;
    @Resource
    private OutTradeValidTimeFilter outTradeValidTimeFilter;

    @Override
    protected TradeSettlementFilterResultEntity doApply(TradeSettlementFilterFactorEntity requestParameter, TradeSettlementRuleFilterFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        GroupBuyOrderListEntity groupBuyOrderListEntity = tradeRepository.queryGroupBuyOrderListByOutTradeNo(requestParameter.getOutTradeNo());
        if(null == groupBuyOrderListEntity){
            log.warn("拼团交易-外部订单:{}, 已经成团或者过期", requestParameter.getOutTradeNo());
            throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
        }
        dynamicContext.setGroupBuyOrderListEntity(groupBuyOrderListEntity);
        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<TradeSettlementFilterFactorEntity, TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementFilterResultEntity> get(TradeSettlementFilterFactorEntity requestParameter, TradeSettlementRuleFilterFactory.DynamicContext dynamicContext) {
        return outTradeValidTimeFilter;
    }
}
