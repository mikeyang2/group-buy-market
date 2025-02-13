package cn.ant0n.gbm.domain.trade.service.settlement;

import cn.ant0n.gbm.domain.activity.model.entity.GroupBuyTeamsEntity;
import cn.ant0n.gbm.domain.trade.model.entity.TradeSuccessEntity;
import cn.ant0n.gbm.domain.trade.model.valobj.TeamStatisticVO;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface ITradeSettlementOrderService {
    void settlementMarketOrder(TradeSuccessEntity tradeSuccessEntity) throws ExecutionException, InterruptedException, TimeoutException;

    TeamStatisticVO queryTeamStatisticVO(String activityId);

    List<GroupBuyTeamsEntity> queryGroupBuyTeamsEntity(String activityId, String userId);

    void settlementRefund(String outTradeNo);
}
