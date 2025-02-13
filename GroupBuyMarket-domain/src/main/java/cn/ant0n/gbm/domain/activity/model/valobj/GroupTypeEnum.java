package cn.ant0n.gbm.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupTypeEnum {
    AUTO((short)0, "自动成团"),
    TARGET((short)1, "达成目标拼团人数");
    private final Short code;
    private final String desc;

    public static GroupTypeEnum create(Short code){
        switch (code){
            case 0:
                return GroupTypeEnum.AUTO;
            case 1:
                return GroupTypeEnum.TARGET;
            default:
                return GroupTypeEnum.AUTO;
        }
    }
}
