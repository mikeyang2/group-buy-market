package cn.ant0n.gbm.domain.trade.service.notify;

import cn.ant0n.gbm.domain.activity.repository.IActivityRepository;
import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderEntity;
import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderListEntity;
import cn.ant0n.gbm.domain.trade.repository.ITradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TradeOrderDelayNotifyService implements ITradeOrderDelayNotifyService {

    @Resource
    private ITradeRepository tradeRepository;
    @Resource
    private IActivityRepository activityRepository;

    @Override
    public boolean isOrderCompleted(String teamId) {
        GroupBuyOrderEntity groupBuyOrderEntity = tradeRepository.queryGroupBuyOrderEntity(teamId);
        if(Objects.equals(groupBuyOrderEntity.getTargetCount(), groupBuyOrderEntity.getCompleteCount()) && groupBuyOrderEntity.getStatus() == (short)1){
            log.info("拼团交易-团:{}完成目标拼单!", teamId);
            return true;
        }
        log.info("拼团交易-团:{}未完成目标拼单...", teamId);
        return false;
    }

    @Override
    public void cancelGroupBuyOrder(String teamId) {

        List<GroupBuyOrderListEntity> groupBuyOrderListEntities = tradeRepository.queryGroupBuyOrderListByTeamID(teamId);
        tradeRepository.cancelGroupBuyOrder(groupBuyOrderListEntities, teamId);
    }
}
