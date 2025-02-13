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
public class CrowdTagsDetail {
    private Integer id;
    private String tagId;
    private String userId;
    private Date createTime;
    private Date updateTime;
}
