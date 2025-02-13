package cn.ant0n.gbm.domain.trade.service.rule.node;

import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderEntity;
import cn.ant0n.gbm.domain.trade.model.entity.LogicFilterResultEntity;
import cn.ant0n.gbm.domain.trade.model.entity.TradeFactorEntity;
import cn.ant0n.gbm.domain.trade.repository.ITradeRepository;
import cn.ant0n.gbm.domain.trade.service.rule.AbstractTradeLogicSupport;
import cn.ant0n.gbm.domain.trade.service.rule.factory.TradeLogicFilterChainFactory;
import cn.ant0n.gbm.types.design.framework.tree.StrategyHandler;
import cn.ant0n.gbm.types.enums.ResponseCode;
import cn.ant0n.gbm.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class GroupAvailableFilter extends AbstractTradeLogicSupport<TradeFactorEntity, TradeLogicFilterChainFactory.DynamicContext, LogicFilterResultEntity> {


    @Resource
    private UserTakingFilter userTakingFilter;
    @Resource
    private ITradeRepository tradeRepository;

    @Override
    protected LogicFilterResultEntity doApply(TradeFactorEntity requestParameter, TradeLogicFilterChainFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        if(!StringUtils.isBlank(requestParameter.getTeamId())){
            GroupBuyOrderEntity groupBuyOrderEntity = tradeRepository.queryGroupBuyOrderEntity(requestParameter.getTeamId());
            if(groupBuyOrderEntity.getStatus() != 0){
                throw new AppException(ResponseCode.GROUP_STATUS.getCode(), ResponseCode.GROUP_STATUS.getInfo());
            }
        }
        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<TradeFactorEntity, TradeLogicFilterChainFactory.DynamicContext, LogicFilterResultEntity> get(TradeFactorEntity requestParameter, TradeLogicFilterChainFactory.DynamicContext dynamicContext) {
        return userTakingFilter;
    }
}
