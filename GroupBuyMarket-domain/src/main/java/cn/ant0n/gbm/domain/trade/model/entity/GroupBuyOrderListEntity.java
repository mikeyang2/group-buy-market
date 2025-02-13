package cn.ant0n.gbm.domain.trade.model.entity;

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
public class GroupBuyOrderListEntity {
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
}
