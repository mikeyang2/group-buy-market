package cn.ant0n.gbm.infrastructure.dao;

import cn.ant0n.gbm.infrastructure.dao.po.GroupBuyOrderList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IGroupBuyOrderListDao {
    GroupBuyOrderList queryLockedGroupBuyOrderList(GroupBuyOrderList groupBuyOrderListReq);

    void insert(GroupBuyOrderList groupBuyOrderList);

    Integer queryUserTakeCount(@Param("userId") String userId, @Param("activityId") String activityId);

    GroupBuyOrderList queryGroupBuyOrderListByOutTradeNo(String outTradeNo);

    int update2Complete(String outTradeNo);

    List<GroupBuyOrderList> queryGroupBuyOrderListByTeamId(String teamId);

    int cancel(String teamId);

    List<GroupBuyOrderList> queryGroupBuyTeamsEntity(GroupBuyOrderList groupBuyOrderListReq);

    List<GroupBuyOrderList> queryGroupBuyListByTeamIdsLimit1(List<String> teamIds);
}
