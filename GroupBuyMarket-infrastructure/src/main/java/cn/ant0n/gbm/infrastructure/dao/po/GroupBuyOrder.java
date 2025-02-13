package cn.ant0n.gbm.infrastructure.dao.po;

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
public class GroupBuyOrder {
    private Integer id;
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
    private Date createTime;
    private Date updateTime;
}
