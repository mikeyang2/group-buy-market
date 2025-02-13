package cn.ant0n.gbm.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityStatusEnum {
    CREATE((short)0, "创建"),
    ENABLE((short)1, "生效"),
    TIME_OUT((short)2, "过期"),
    DISABLE((short)3, "作废");
    private final Short code;
    private final String desc;

    public static ActivityStatusEnum create(Short code){
        switch (code){
            case 0:
                return ActivityStatusEnum.CREATE;
            case 1:
                return ActivityStatusEnum.ENABLE;
            case 2:
                return ActivityStatusEnum.TIME_OUT;
            case 3:
                return ActivityStatusEnum.DISABLE;
            default:
                return ActivityStatusEnum.CREATE;
        }
    }
}
