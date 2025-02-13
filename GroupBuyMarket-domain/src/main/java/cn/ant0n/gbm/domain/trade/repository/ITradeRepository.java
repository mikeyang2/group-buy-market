package cn.ant0n.gbm.domain.trade.repository;

import cn.ant0n.gbm.domain.activity.model.entity.GroupBuyTeamsEntity;
import cn.ant0n.gbm.domain.trade.model.aggregate.LockGroupBuyOrderAggregate;
import cn.ant0n.gbm.domain.trade.model.aggregate.SettlementMarketOrderAggregate;
import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderEntity;
import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderListEntity;
import cn.ant0n.gbm.domain.trade.model.entity.MarketTeamsDetailEntity;
import cn.ant0n.gbm.domain.trade.model.valobj.TeamStatisticVO;

import java.util.List;
import java.util.Map;

public interface ITradeRepository {
    GroupBuyOrderListEntity queryLockedGroupBuyOrderList(String userId, String outTradeNo);

    Map<String, GroupBuyOrderEntity> queryExistGroupBuyOrderList(String activityId);

    void lockGroupBuyOrderMaster(LockGroupBuyOrderAggregate aggregate);

    void lockGroupBuyOrderSlave(LockGroupBuyOrderAggregate aggregate);

    Integer queryUserTakeCount(String userId, String activityId);

    GroupBuyOrderEntity queryGroupBuyOrderEntity(String teamId);

    GroupBuyOrderListEntity queryGroupBuyOrderListByOutTradeNo(String outTradeNo);

    void settlementMarketOrder(SettlementMarketOrderAggregate aggregate);

    List<GroupBuyOrderListEntity> queryGroupBuyOrderListByTeamID(String teamId);

    void cancelGroupBuyOrder(List<GroupBuyOrderListEntity> groupBuyOrderListEntities, String teamId);

    TeamStatisticVO queryTeamStatisticVO(String activityId);

    List<GroupBuyTeamsEntity> queryGroupBuyTeamsEntity(String activityId, String userId, int i);

    TeamStatisticVO queryGroupBuyMarketTeamStatistic(String activityId);

    List<MarketTeamsDetailEntity> queryMarketTeamsDetail(String activityId);

    void settlementRefund(String outTradeNo);
}
