package cn.ant0n.gbm.infrastructure.dao;

import cn.ant0n.gbm.infrastructure.dao.po.ScPA;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IScPADao {
    String queryActivityId(ScPA scPAReq);
}
