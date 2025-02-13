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
public class GroupBuyOrderEntity {
    private String teamId;
    private String activityId;
    private String source;
    private String channel;
    private BigDecimal originalPrice;
    private BigDecimal deductionPrice;
    private BigDecimal payPrice;
    private Integer targetCount;
    private Integer completeCount;
    private Integer lockCount;
    private Short status;
    private Date startTime;
    private Date endTime;
}
