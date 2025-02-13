package cn.ant0n.gbm.domain.activity.service;

import cn.ant0n.gbm.domain.activity.model.entity.MarketProductEntity;
import cn.ant0n.gbm.domain.activity.model.entity.TrialBalanceEntity;
import cn.ant0n.gbm.domain.activity.repository.IActivityRepository;
import cn.ant0n.gbm.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.ant0n.gbm.types.enums.ResponseCode;
import cn.ant0n.gbm.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class IndexGroupBuyMarketService implements IIndexGroupBuyMarketService{

    @Resource
    private DefaultActivityStrategyFactory factory;
    @Resource
    private IActivityRepository activityRepository;

    @Override
    public TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) {
        try{
            return factory.apply(marketProductEntity);
        }catch (Exception e){
            log.warn("拼团系统-折扣试算异常", e);
            throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
        }
    }

}
