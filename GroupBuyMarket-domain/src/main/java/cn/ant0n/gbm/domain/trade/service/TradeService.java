package cn.ant0n.gbm.domain.trade.service;

import cn.ant0n.gbm.domain.trade.model.aggregate.LockGroupBuyOrderAggregate;
import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderEntity;
import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderListEntity;
import cn.ant0n.gbm.domain.trade.repository.ITradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class TradeService implements ITradeService {

    @Resource
    private ITradeRepository tradeRepository;

    @Override
    public GroupBuyOrderListEntity queryLockedGroupBuyOrderList(String userId, String outTradeNo) {
        return tradeRepository.queryLockedGroupBuyOrderList(userId, outTradeNo);
    }

    @Override
    public void lockGroupBuyOrder(LockGroupBuyOrderAggregate aggregate) {
        if(aggregate.getIsMaster()){
            tradeRepository.lockGroupBuyOrderMaster(aggregate);
        }
        else{
            tradeRepository.lockGroupBuyOrderSlave(aggregate);
        }
    }

    @Override
    public Map<String, GroupBuyOrderEntity> queryExistGroupBuyOrderList(String activityId) {
        return tradeRepository.queryExistGroupBuyOrderList(activityId);
    }
}
