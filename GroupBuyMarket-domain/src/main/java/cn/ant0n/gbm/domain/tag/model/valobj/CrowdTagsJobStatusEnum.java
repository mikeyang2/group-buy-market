package cn.ant0n.gbm.domain.tag.model.valobj;

import cn.ant0n.gbm.domain.activity.model.valobj.ActivityStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CrowdTagsJobStatusEnum {

    INIT((short)0, "初始"),
    PLAN((short)1, "计划-进入执行状态"),
    RESET((short)2, "重置"),
    COMPLETE((short)3, "完成");

    private final Short code;
    private final String desc;

    public static CrowdTagsJobStatusEnum create(Short code){
        switch (code){
            case 0:
                return CrowdTagsJobStatusEnum.INIT;
            case 1:
                return CrowdTagsJobStatusEnum.PLAN;
            case 2:
                return CrowdTagsJobStatusEnum.RESET;
            case 3:
                return CrowdTagsJobStatusEnum.COMPLETE;
            default:
                return CrowdTagsJobStatusEnum.INIT;
        }
    }
}
