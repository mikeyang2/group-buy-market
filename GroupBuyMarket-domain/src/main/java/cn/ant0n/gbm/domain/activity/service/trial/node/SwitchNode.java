package cn.ant0n.gbm.domain.activity.service.trial.node;

import cn.ant0n.gbm.domain.activity.model.entity.MarketProductEntity;
import cn.ant0n.gbm.domain.activity.model.entity.TrialBalanceEntity;
import cn.ant0n.gbm.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import cn.ant0n.gbm.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.ant0n.gbm.types.annotations.DCCValue;
import cn.ant0n.gbm.types.design.framework.tree.StrategyHandler;
import cn.ant0n.gbm.types.enums.ResponseCode;
import cn.ant0n.gbm.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class SwitchNode extends AbstractGroupBuyMarketSupport<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> {

    @Resource
    private MarketNode marketNode;

    @DCCValue(path = "downgradeSwitch", value = "0")
    private String downgradeSwitch;
    @DCCValue(path = "cutRange", value = "100")
    private String cutRange;

    @Override
    public StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> get(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) {
        return marketNode;
    }

    @Override
    protected TrialBalanceEntity doApply(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        log.info("SWITCH节点");
        if("1".equals(downgradeSwitch)){
            log.info("拼团系统-活动已降级");
            throw new AppException(ResponseCode.REFUSE.getCode(), ResponseCode.REFUSE.getInfo());
        }
        log.info("拼团系统-活动正常,未降级");
//        Integer curRangeNum = Integer.valueOf(cutRange);
//        Integer userIdHashCode = Math.abs(requestParameter.getUserId().hashCode());
//        userIdHashCode %= 100;
//        if(curRangeNum < userIdHashCode){
//            log.info("拼团系统-该用户:{},被切量限制", requestParameter.getUserId());
//            throw new AppException(ResponseCode.REFUSE.getCode(), ResponseCode.REFUSE.getInfo());
//        }
//        log.info("拼团系统-该用户:{},通过切量限制", requestParameter.getUserId());
        return router(requestParameter, dynamicContext);
    }
}
