package cn.ant0n.gbm.domain.trade.service.settlement.filter.factory;

import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderEntity;
import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderListEntity;
import cn.ant0n.gbm.domain.trade.model.entity.TradeSettlementFilterFactorEntity;
import cn.ant0n.gbm.domain.trade.model.entity.TradeSettlementFilterResultEntity;
import cn.ant0n.gbm.domain.trade.service.settlement.filter.impl.OutTradeNoExistFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class TradeSettlementRuleFilterFactory {


    @Resource
    private OutTradeNoExistFilter outTradeNoExistFilter;

    public TradeSettlementFilterResultEntity filter(TradeSettlementFilterFactorEntity tradeSettlementFilterFactorEntity) throws ExecutionException, InterruptedException, TimeoutException {
        return outTradeNoExistFilter.apply(tradeSettlementFilterFactorEntity, new DynamicContext());
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext{
        private GroupBuyOrderListEntity groupBuyOrderListEntity;
        private GroupBuyOrderEntity groupBuyOrderEntity;
    }
}
