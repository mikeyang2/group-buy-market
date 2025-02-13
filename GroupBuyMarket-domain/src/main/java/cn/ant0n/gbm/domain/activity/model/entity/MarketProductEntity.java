package cn.ant0n.gbm.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 营销商品实体信息，通过这样一个信息获取商品优惠信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketProductEntity {

    /** 用户ID */
    private String userId;
    /** 商品ID */
    private String productId;
    /** 渠道 */
    private String source;
    /** 来源 */
    private String channel;

    private Integer productQuota;

}
