package cn.ant0n.gbm.domain.activity.model.valobj;

import cn.ant0n.gbm.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyActivityDiscountVO {
    private String activityId;
    private String activityName;
    private String source;
    private String channel;
    private String productId;
    private GroupBuyDiscount groupBuyDiscount;
    private GroupTypeEnum groupType;
    private Integer takeLimitCount;
    private Integer target;
    private Integer validTime;
    private ActivityStatusEnum status;
    private Date startTime;
    private Date endTime;
    private String tagId;
    private String tagScope;


    public boolean isDisable(){
        return Constants.DISABLE.equals(tagScope);
    }

    public boolean isNotVisible(){
        return Constants.NOT_VISIBLE.equals(tagScope);
    }



    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GroupBuyDiscount{
        /**
         * 折扣标题
         */
        private String discountName;

        /**
         * 折扣描述
         */
        private String discountDesc;

        /**
         * 折扣类型（0:base、1:tag）
         */
        private DiscountTypeEnum discountType;

        /**
         * 营销优惠计划（ZJ:直减、MJ:满减、N元购）
         */
        private String discountPlan;

        /**
         * 营销优惠表达式
         */
        private String discountExpr;

        /**
         * 人群标签，特定优惠限定
         */
        private String tagId;

    }
}
