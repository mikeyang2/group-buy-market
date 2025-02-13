package cn.ant0n.gbm.domain.trade.service;

import cn.ant0n.gbm.domain.trade.model.aggregate.LockGroupBuyOrderAggregate;
import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderEntity;
import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderListEntity;


import java.util.Map;

public interface ITradeService {

    /**
     * 查询userId, teamId下被锁定的拼团订单(幂等,网抖)
     * @param userId
     * @param teamId
     * @return
     */
    GroupBuyOrderListEntity queryLockedGroupBuyOrderList(String userId, String teamId);

    void lockGroupBuyOrder(LockGroupBuyOrderAggregate aggregate);

    Map<String, GroupBuyOrderEntity> queryExistGroupBuyOrderList(String activityId);




}
