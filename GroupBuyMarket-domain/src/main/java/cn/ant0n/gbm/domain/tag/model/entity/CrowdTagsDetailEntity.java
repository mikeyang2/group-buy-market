package cn.ant0n.gbm.domain.tag.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrowdTagsDetailEntity {
    private String tagId;
    private String userId;
}
