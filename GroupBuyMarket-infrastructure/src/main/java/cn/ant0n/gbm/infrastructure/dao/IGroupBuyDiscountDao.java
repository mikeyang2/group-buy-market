package cn.ant0n.gbm.infrastructure.dao;

import cn.ant0n.gbm.infrastructure.dao.po.GroupBuyDiscount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IGroupBuyDiscountDao {
    GroupBuyDiscount queryGroupBuyDiscountByDiscountId(String discountId);
}
