package cn.ant0n.gbm.domain.tag.model.entity;

import cn.ant0n.gbm.domain.tag.model.valobj.CrowdTagsJobStatusEnum;
import cn.ant0n.gbm.domain.tag.model.valobj.TagsTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrowdTagsJobEntity {
    private String tagId;
    private String batchId;
    private TagsTypeEnum tagType;
    private String tagRule;
    private Date startTime;
    private Date endTime;
    private CrowdTagsJobStatusEnum status;
}
