package cn.ant0n.gbm.domain.trade.service.settlement.filter.impl;

import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderEntity;
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
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class OutTradeValidTimeFilter extends AbstractTradeSettlementRuleFilter<TradeSettlementFilterFactorEntity, TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementFilterResultEntity> {

    @Resource
    private ITradeRepository tradeRepository;

    @Override
    protected TradeSettlementFilterResultEntity doApply(TradeSettlementFilterFactorEntity requestParameter, TradeSettlementRuleFilterFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        GroupBuyOrderListEntity groupBuyOrderListEntity = dynamicContext.getGroupBuyOrderListEntity();
        String teamId = groupBuyOrderListEntity.getTeamId();
        GroupBuyOrderEntity groupBuyOrderEntity = tradeRepository.queryGroupBuyOrderEntity(teamId);

        Date startTime = groupBuyOrderEntity.getStartTime();
        Date endTime = groupBuyOrderEntity.getEndTime();
        if(requestParameter.getOutTradeTime().after(endTime) || requestParameter.getOutTradeTime().before(startTime)) {
            log.info("拼团交易-交易时间不在该团有效期内, outTradeNo:{}", requestParameter.getOutTradeNo());
            // 交由拼团单同意退款
            throw new AppException(ResponseCode.GROUP_TIME.getCode(), ResponseCode.GROUP_TIME.getInfo());
        }
        return TradeSettlementFilterResultEntity.builder()
                .groupBuyOrderEntity(groupBuyOrderEntity)
                .build();
    }

    @Override
    public StrategyHandler<TradeSettlementFilterFactorEntity, TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementFilterResultEntity> get(TradeSettlementFilterFactorEntity requestParameter, TradeSettlementRuleFilterFactory.DynamicContext dynamicContext) {
        return null;
    }
}
