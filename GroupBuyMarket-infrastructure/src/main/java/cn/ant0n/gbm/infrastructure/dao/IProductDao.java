package cn.ant0n.gbm.infrastructure.dao;

import cn.ant0n.gbm.infrastructure.dao.po.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IProductDao {
    Product queryProductByProductId(String productId);
}
