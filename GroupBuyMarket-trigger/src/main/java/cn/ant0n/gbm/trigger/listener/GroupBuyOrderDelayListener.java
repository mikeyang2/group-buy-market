package cn.ant0n.gbm.trigger.listener;


import cn.ant0n.gbm.domain.trade.service.notify.ITradeOrderDelayNotifyService;
import cn.ant0n.gbm.types.exception.AppException;
import cn.ant0n.gbm.types.response.BaseEvent;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Slf4j
@RocketMQMessageListener(topic = "GroupBuyOrderDelayEvent", consumerGroup = "group-buy-order-delay-group")
public class GroupBuyOrderDelayListener implements RocketMQListener<String> {

    @Resource
    private ITradeOrderDelayNotifyService tradeOrderDelayNotifyService;

    @Override
    public void onMessage(String s) {
        try{
            BaseEvent<String> message = JSON.parseObject(s, new TypeReference<BaseEvent<String>>(){});
            if(message == null) return;
            String teamId = message.getData();
            boolean isComplete = tradeOrderDelayNotifyService.isOrderCompleted(teamId);
            if(isComplete) return;

            tradeOrderDelayNotifyService.cancelGroupBuyOrder(teamId);
            log.info("拼团交易-发送退款消息成功!");
        }catch (AppException e){
            log.info("拼团交易-异常!, msg:{}", e.getMessage());
        }catch (Exception e){
            throw e;
        }
    }
}
