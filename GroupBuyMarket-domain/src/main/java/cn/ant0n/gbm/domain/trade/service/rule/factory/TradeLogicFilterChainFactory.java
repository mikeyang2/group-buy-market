package cn.ant0n.gbm.domain.trade.service.rule.factory;

import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyActivityEntity;
import cn.ant0n.gbm.domain.trade.model.entity.LogicFilterResultEntity;
import cn.ant0n.gbm.domain.trade.model.entity.TradeFactorEntity;
import cn.ant0n.gbm.domain.trade.service.rule.node.ActivityAvailableFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Component
public class TradeLogicFilterChainFactory {

    @Resource
    private ActivityAvailableFilter activityAvailableFilter;



    public LogicFilterResultEntity filter(TradeFactorEntity tradeFactorEntity) throws ExecutionException, InterruptedException, TimeoutException {
        return activityAvailableFilter.apply(tradeFactorEntity, new DynamicContext());
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext{
        private GroupBuyActivityEntity groupBuyActivityEntity;
        private Date nowDate;
    }
}
