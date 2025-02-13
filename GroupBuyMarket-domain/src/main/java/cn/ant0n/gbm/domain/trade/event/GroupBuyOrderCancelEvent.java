package cn.ant0n.gbm.domain.trade.event;

import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderRefundEntity;
import cn.ant0n.gbm.types.response.BaseEvent;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;

public class GroupBuyOrderCancelEvent extends BaseEvent<GroupBuyOrderRefundEntity> {


    public static final String topic = "GroupBuyOrderCancelEvent";


    public static GroupBuyOrderCancelEvent create(GroupBuyOrderRefundEntity entity) {
        GroupBuyOrderCancelEvent event = new GroupBuyOrderCancelEvent();
        event.setId(RandomStringUtils.randomNumeric(12));
        event.setData(entity);
        event.setTimestamp(new Date());
        return event;
    }
}
