package cn.ant0n.gbm.infrastructure.dao;

import cn.ant0n.gbm.infrastructure.dao.po.GroupBuyOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface IGroupBuyOrderDao {
    List<GroupBuyOrder> queryExistGroupBuyOrderList(String activityId);

    void insert(GroupBuyOrder groupBuyOrder);

    int joinGroupUpdate(GroupBuyOrder groupBuyOrder);

    GroupBuyOrder queryGroupBuyOrder(String teamId);

    int incrCompleteCount(String teamId);

    int update2Complete(String teamId);

    int cancelOrder(String teamId);

    Integer queryParticipateCountByAcitivityId(String activityId);

    Integer queryCompleteGroupCount(String activityId);

    Integer queryInProgressGroupCount(String activityId);

    List<GroupBuyOrder> queryGroupBuyProgress(String activityId);
}
