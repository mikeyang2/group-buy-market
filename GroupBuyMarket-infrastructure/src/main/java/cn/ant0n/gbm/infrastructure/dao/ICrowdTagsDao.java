package cn.ant0n.gbm.infrastructure.dao;

import cn.ant0n.gbm.infrastructure.dao.po.CrowdTags;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ICrowdTagsDao {
    void updateCrowdTagsStatistics(CrowdTags crowdTagsReq);
}
