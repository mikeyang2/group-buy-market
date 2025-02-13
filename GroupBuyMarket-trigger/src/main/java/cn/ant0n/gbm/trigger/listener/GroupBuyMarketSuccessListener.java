package cn.ant0n.gbm.trigger.listener;

import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyMarketSuccessEntity;
import cn.ant0n.gbm.types.response.BaseEvent;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RocketMQMessageListener(topic = "GroupBuyMarketSuccessEvent", consumerGroup = "group-buy-consumer-group")
public class GroupBuyMarketSuccessListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        try{
            BaseEvent<GroupBuyMarketSuccessEntity> message = JSON.parseObject(s, new TypeReference<BaseEvent<GroupBuyMarketSuccessEntity>>(){});
            if(message == null) return;
            List<GroupBuyMarketSuccessEntity.UserEntity> userEntities = message.getData().getUserEntities();
            for (GroupBuyMarketSuccessEntity.UserEntity userEntity : userEntities) {
                log.info("拼团交易结果- productId:{}, openId:{}, orderId:{}", userEntity.getProductId(), userEntity.getUserId(), userEntity.getOutTradeNo());
            }
        }catch (Exception e){
            throw e;
        }
    }
}
