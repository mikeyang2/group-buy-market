package cn.ant0n.gbm.domain.trade.event;


import cn.ant0n.gbm.types.response.BaseEvent;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;

public class GroupBuyOrderDelayEvent extends BaseEvent<String> {


    public static String topic = "GroupBuyOrderDelayEvent";

    public static GroupBuyOrderDelayEvent create(String teamId) {
        GroupBuyOrderDelayEvent event = new GroupBuyOrderDelayEvent();
        event.setId(RandomStringUtils.randomNumeric(12));
        event.setData(teamId);
        event.setTimestamp(new Date());
        return event;
    }
}
