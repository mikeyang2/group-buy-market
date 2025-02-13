package cn.ant0n.gbm.trigger.listener;

import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyMarketSuccessEntity;
import cn.ant0n.gbm.domain.trade.model.entity.TradeSuccessEntity;
import cn.ant0n.gbm.domain.trade.service.settlement.ITradeSettlementOrderService;
import cn.ant0n.gbm.types.enums.ResponseCode;
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
@RocketMQMessageListener(topic = "GroupBuySettlementEvent", consumerGroup = "group-buy-settlement-group")
public class GroupBuySettlementListener implements RocketMQListener<String> {

    @Resource
    private ITradeSettlementOrderService tradeSettlementOrderService;

    @Override
    public void onMessage(String s) {
        BaseEvent<TradeSuccessEntity> message = JSON.parseObject(s, new TypeReference<BaseEvent<TradeSuccessEntity>>(){});
        if(message == null) return;
        TradeSuccessEntity tradeSuccessEntity = message.getData();
        try{
            log.info("拼团交易结算-接收到拼团交易结算MQ");
            tradeSettlementOrderService.settlementMarketOrder(tradeSuccessEntity);
        }catch (AppException e){
            if(e.getCode().equals(ResponseCode.GROUP_TIME.getCode())){
                tradeSettlementOrderService.settlementRefund(tradeSuccessEntity.getOutTradeNo());
            }
            else{
                throw e;
            }
        } catch (Exception e){
            log.warn("拼团交易结算-接收拼团交易结算MQ异常", e);
        }
    }
}
