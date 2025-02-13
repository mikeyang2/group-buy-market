package cn.ant0n.gbm.infrastructure.dao.po;

import cn.ant0n.gbm.infrastructure.dao.po.base.Page;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyOrderList extends Page {
    private Integer id;
    private String userId;
    private String teamId;
    private String orderId;
    private String activityId;
    private Date startTime;
    private Date endTime;
    private String productId;
    private String source;
    private String channel;
    private BigDecimal originalPrice;
    private BigDecimal deductionPrice;
    private Short status;
    private String outTradeNo;
    private Date createTime;
    private Date updateTime;
}
