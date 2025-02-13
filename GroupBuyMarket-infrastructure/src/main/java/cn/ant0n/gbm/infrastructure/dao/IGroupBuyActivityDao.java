package cn.ant0n.gbm.infrastructure.dao;

import cn.ant0n.gbm.infrastructure.dao.po.GroupBuyActivity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IGroupBuyActivityDao {
    GroupBuyActivity queryGroupBuyActivityByActivityId(String activityId);
}
