package cn.ant0n.gbm.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyDiscount {
    private Long id;
    private String discountId;
    private String discountName;
    private String discountDesc;
    private Short discountType;
    private String discountPlan;
    private String discountExpr;
    private String tagId;
    private Date createTime;
    private Date updateTime;
}
