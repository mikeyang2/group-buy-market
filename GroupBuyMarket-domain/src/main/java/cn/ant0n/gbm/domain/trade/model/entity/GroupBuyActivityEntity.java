package cn.ant0n.gbm.domain.trade.model.entity;

import cn.ant0n.gbm.domain.trade.model.valobj.ActivityStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyActivityEntity {
    private String activityId;
    private Date startTime;
    private Date endTime;
    private ActivityStatusEnum status;
    private Integer validTime;
    private Integer takeLimitCount;
}
