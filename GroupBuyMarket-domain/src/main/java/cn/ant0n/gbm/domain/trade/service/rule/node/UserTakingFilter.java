package cn.ant0n.gbm.domain.trade.service.rule.node;

import cn.ant0n.gbm.domain.trade.model.entity.LogicFilterResultEntity;
import cn.ant0n.gbm.domain.trade.model.entity.TradeFactorEntity;
import cn.ant0n.gbm.domain.trade.repository.ITradeRepository;
import cn.ant0n.gbm.domain.trade.service.rule.AbstractTradeLogicSupport;
import cn.ant0n.gbm.domain.trade.service.rule.factory.TradeLogicFilterChainFactory;
import cn.ant0n.gbm.types.design.framework.tree.StrategyHandler;
import cn.ant0n.gbm.types.enums.ResponseCode;
import cn.ant0n.gbm.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class UserTakingFilter extends AbstractTradeLogicSupport<TradeFactorEntity, TradeLogicFilterChainFactory.DynamicContext, LogicFilterResultEntity> {

    @Resource
    private ITradeRepository tradeRepository;

    @Override
    protected LogicFilterResultEntity doApply(TradeFactorEntity requestParameter, TradeLogicFilterChainFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        String activityId = dynamicContext.getGroupBuyActivityEntity().getActivityId();
        String userId = requestParameter.getUserId();
        Integer takeCount = dynamicContext.getGroupBuyActivityEntity().getTakeLimitCount();;

        Integer count = tradeRepository.queryUserTakeCount(userId, activityId);
        if(takeCount != null && count >= takeCount){
            throw new AppException(ResponseCode.ACTIVITY_TAKE_COUNT.getCode(), ResponseCode.ACTIVITY_TAKE_COUNT.getInfo());
        }
        return new LogicFilterResultEntity();
    }

    @Override
    public StrategyHandler<TradeFactorEntity, TradeLogicFilterChainFactory.DynamicContext, LogicFilterResultEntity> get(TradeFactorEntity requestParameter, TradeLogicFilterChainFactory.DynamicContext dynamicContext) {
        return null;
    }
}
