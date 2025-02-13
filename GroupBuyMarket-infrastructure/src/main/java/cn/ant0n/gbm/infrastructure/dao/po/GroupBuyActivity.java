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
public class GroupBuyActivity {
    private Long id;
    private String activityId;
    private String activityName;
    private String discountId;
    private Short groupType;
    private Integer takeLimitCount;
    private Integer target;
    private Integer validTime;
    private Short status;
    private Date startTime;
    private Date endTime;
    private String tagId;
    private String tagScope;
    private Date createTime;
    private Date updateTime;
}
