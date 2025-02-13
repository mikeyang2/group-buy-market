package cn.ant0n.gbm.domain.tag.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrowdTagsEntity {
    private String tagId;
    private String tagName;
    private String tagDesc;
    private Integer statistics;
}
