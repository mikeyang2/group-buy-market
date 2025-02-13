package cn.ant0n.gbm.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "成功"),
    UN_ERROR("0001", "未知失败"),
    ILLEGAL_PARAMETER("0002", "非法参数"),
    REFUSE("0003", "活动太火爆，请稍后在试"),
    ACTIVITY_TIME("0004", "不在活动时间内"),
    ACTIVITY_STATUS("0005", "活动失效"),
    ACTIVITY_TAKE_COUNT("0006", "已达到最大参与限额次数"),
    GROUP_TIME("0007", "团已经过期"),
    GROUP_STATUS("0008", "团失效"),
    DISABLE("0009", "未获得参与拼团资格"),
    VISIBLE("0010", "拼团活动可见，但无法参与")
    ;

    private String code;
    private String info;

}
