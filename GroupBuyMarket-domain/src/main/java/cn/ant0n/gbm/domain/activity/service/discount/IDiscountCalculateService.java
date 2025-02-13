package cn.ant0n.gbm.domain.activity.service.discount;

import cn.ant0n.gbm.domain.activity.model.entity.MarketProductEntity;
import cn.ant0n.gbm.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;

import java.math.BigDecimal;

public interface IDiscountCalculateService {

    /**
     * 计算折扣
     * @param dynamicContext 上下文
     */
    BigDecimal calculate(MarketProductEntity marketProductEntity, DefaultActivityStrategyFactory.DynamicContext dynamicContext);
}
