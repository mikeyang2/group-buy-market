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
public class ScPA {
    private Long id;
    private String source;
    private String channel;
    private String productId;
    private String activityId;
    private Date createTime;
    private Date updateTime;
}
