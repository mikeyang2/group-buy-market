package cn.ant0n.gbm.domain.trade.event;

import cn.ant0n.gbm.types.response.BaseEvent;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;

public class SettlementRefundEvent extends BaseEvent<String> {
    public final static String topic = "SettlementRefundEvent";


    public static SettlementRefundEvent create(String outTradeNo){
        SettlementRefundEvent event = new SettlementRefundEvent();
        event.setId(RandomStringUtils.randomNumeric(12));
        event.setData(outTradeNo);
        event.setTimestamp(new Date());
        return event;
    }
}
