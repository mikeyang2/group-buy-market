package cn.ant0n.gbm.domain.trade.service.rule.node;

import cn.ant0n.gbm.domain.activity.repository.IActivityRepository;
import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyActivityEntity;
import cn.ant0n.gbm.domain.trade.model.entity.LogicFilterResultEntity;
import cn.ant0n.gbm.domain.trade.model.entity.TradeFactorEntity;
import cn.ant0n.gbm.domain.trade.model.valobj.ActivityStatusEnum;
import cn.ant0n.gbm.domain.trade.service.rule.AbstractTradeLogicSupport;
import cn.ant0n.gbm.domain.trade.service.rule.factory.TradeLogicFilterChainFactory;
import cn.ant0n.gbm.types.design.framework.tree.StrategyHandler;
import cn.ant0n.gbm.types.enums.ResponseCode;
import cn.ant0n.gbm.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class ActivityAvailableFilter extends AbstractTradeLogicSupport<TradeFactorEntity, TradeLogicFilterChainFactory.DynamicContext, LogicFilterResultEntity> {

    @Resource
    private IActivityRepository activityRepository;
    @Resource
    private GroupAvailableFilter groupAvailableFilter;


    @Override
    protected LogicFilterResultEntity doApply(TradeFactorEntity requestParameter, TradeLogicFilterChainFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        GroupBuyActivityEntity groupBuyActivityEntity = activityRepository.queryGroupBuyActivityEntity(requestParameter.getProductId(), requestParameter.getSource(), requestParameter.getChannel());

        Date startTime = groupBuyActivityEntity.getStartTime();
        Date endTime = groupBuyActivityEntity.getEndTime();
        Date nowTime = new Date();
        if(startTime.after(nowTime) || endTime.before(nowTime)) {
            throw new AppException(ResponseCode.ACTIVITY_TIME.getCode(), ResponseCode.ACTIVITY_TIME.getInfo());
        }
        if(!ActivityStatusEnum.ENABLE.equals(groupBuyActivityEntity.getStatus())){
            throw new AppException(ResponseCode.ACTIVITY_STATUS.getCode(), ResponseCode.ACTIVITY_STATUS.getInfo());
        }
        dynamicContext.setGroupBuyActivityEntity(groupBuyActivityEntity);
        dynamicContext.setNowDate(nowTime);
        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<TradeFactorEntity, TradeLogicFilterChainFactory.DynamicContext, LogicFilterResultEntity> get(TradeFactorEntity requestParameter, TradeLogicFilterChainFactory.DynamicContext dynamicContext) {
        return groupAvailableFilter;
    }
}
