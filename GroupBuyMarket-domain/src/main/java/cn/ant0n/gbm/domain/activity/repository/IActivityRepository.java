package cn.ant0n.gbm.domain.activity.repository;

import cn.ant0n.gbm.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.ant0n.gbm.domain.activity.model.valobj.ProductSkuVO;
import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyActivityEntity;

public interface IActivityRepository {
    ProductSkuVO queryProductByProductId(String productId);

    GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(String productId, String source, String channel);

    boolean queryIdCrowdRange(String userId, String tagId);

    void downgrade(String path, String changeValue);

    String queryActivityIdFromScP(String productId, String source, String channel);

    GroupBuyActivityEntity queryGroupBuyActivityEntity(String productId, String source, String channel);

}
