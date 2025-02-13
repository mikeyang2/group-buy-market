package cn.ant0n.gbm.infrastructure.dao;

import cn.ant0n.gbm.infrastructure.dao.po.CrowdTagsDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICrowdTagsDetailDao {
    void insertCrowdTagsDetail(CrowdTagsDetail crowdTagsDetail);
}
