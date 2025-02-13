package cn.ant0n.gbm.domain.trade.event;

import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyMarketSuccessEntity;
import cn.ant0n.gbm.types.response.BaseEvent;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;

public class GroupBuyMarketSuccessEvent extends BaseEvent<GroupBuyMarketSuccessEntity> {

    public static String topic = "GroupBuyMarketSuccessEvent";

    public static GroupBuyMarketSuccessEvent create(GroupBuyMarketSuccessEntity entity) {
        GroupBuyMarketSuccessEvent event = new GroupBuyMarketSuccessEvent();
        event.setId(RandomStringUtils.randomNumeric(12));
        event.setData(entity);
        event.setTimestamp(new Date());
        return event;
    }
}
