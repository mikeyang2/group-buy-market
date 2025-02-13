package cn.ant0n.gbm.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiscountTypeEnum {
    BASE((short)0, "普适"),
    TAG((short)1, "目标人群");
    private final Short code;
    private final String desc;


    public static DiscountTypeEnum create(Short code){
        switch (code){
            case 0:
                return DiscountTypeEnum.BASE;
            case 1:
                return DiscountTypeEnum.TAG;
            default:
                return DiscountTypeEnum.BASE;
        }
    }
}
