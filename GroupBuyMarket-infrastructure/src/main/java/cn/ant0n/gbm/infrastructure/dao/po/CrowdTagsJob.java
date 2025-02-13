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
public class CrowdTagsJob {
    private Integer id;
    private String tagId;
    private String batchId;
    private Short tagType;
    private String tagRule;
    private Date startTime;
    private Date endTime;
    private Short status;
    private Date createTime;
    private Date updateTime;
}
