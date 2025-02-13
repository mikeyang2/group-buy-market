package cn.ant0n.gbm.domain.trade.service.settlement;

import cn.ant0n.gbm.domain.activity.model.entity.GroupBuyTeamsEntity;
import cn.ant0n.gbm.domain.trade.model.aggregate.SettlementMarketOrderAggregate;
import cn.ant0n.gbm.domain.trade.model.entity.*;
import cn.ant0n.gbm.domain.trade.model.valobj.TeamStatisticVO;
import cn.ant0n.gbm.domain.trade.repository.ITradeRepository;
import cn.ant0n.gbm.domain.trade.service.settlement.filter.factory.TradeSettlementRuleFilterFactory;
import cn.ant0n.gbm.types.enums.ResponseCode;
import cn.ant0n.gbm.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class TradeSettlementOrderService implements ITradeSettlementOrderService{

    @Resource
    private ITradeRepository tradeRepository;
    @Resource
    private TradeSettlementRuleFilterFactory factory;


    @Override
    public void settlementMarketOrder(TradeSuccessEntity tradeSuccessEntity) throws ExecutionException, InterruptedException, TimeoutException {
        log.info("拼团交易-支付结算, userId:{}, outTradeNo:{}", tradeSuccessEntity.getUserId(), tradeSuccessEntity.getOutTradeNo());


        TradeSettlementFilterFactorEntity tradeSettlementFilterFactorEntity = TradeSettlementFilterFactorEntity.builder()
                .userId(tradeSuccessEntity.getUserId())
                .outTradeNo(tradeSuccessEntity.getOutTradeNo())
                .outTradeTime(tradeSuccessEntity.getOutTradeTime()).build();

        // 检查交易时间是否在团有效期内
        TradeSettlementFilterResultEntity tradeSettlementFilterResultEntity = factory.filter(tradeSettlementFilterFactorEntity);
        GroupBuyOrderEntity groupBuyOrderEntity = tradeSettlementFilterResultEntity.getGroupBuyOrderEntity();
        SettlementMarketOrderAggregate aggregate = SettlementMarketOrderAggregate.builder()
                        .outTradeNo(tradeSuccessEntity.getOutTradeNo())
                                .userId(tradeSuccessEntity.getUserId())
                                        .groupBuyOrderEntity(groupBuyOrderEntity).build();

        tradeRepository.settlementMarketOrder(aggregate);
    }

    @Override
    public TeamStatisticVO queryTeamStatisticVO(String activityId) {
        return tradeRepository.queryTeamStatisticVO(activityId);
    }

    @Override
    public List<GroupBuyTeamsEntity> queryGroupBuyTeamsEntity(String activityId, String userId) {
        List<GroupBuyTeamsEntity> groupBuyTeamsEntities = tradeRepository.queryGroupBuyTeamsEntity(activityId, userId, 10);
        return groupBuyTeamsEntities;
    }

    @Override
    public void settlementRefund(String outTradeNo) {
        tradeRepository.settlementRefund(outTradeNo);
    }
}
