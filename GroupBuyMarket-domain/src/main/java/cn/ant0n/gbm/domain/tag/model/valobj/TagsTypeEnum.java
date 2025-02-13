package cn.ant0n.gbm.domain.tag.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TagsTypeEnum {

    PARTICIPATION((short)0, "参与量"),
    AMOUNT((short)1, "消费金额");

    private final Short code;
    private final String desc;


    public static TagsTypeEnum create(Short code){
        switch (code){
            case 0:
                return TagsTypeEnum.PARTICIPATION;
            case 1:
                return TagsTypeEnum.AMOUNT;
            default:
                return TagsTypeEnum.PARTICIPATION;
        }
    }
}
