package cn.ant0n.gbm.domain.activity.service.discount;


import cn.ant0n.gbm.domain.activity.model.entity.MarketProductEntity;
import cn.ant0n.gbm.domain.activity.model.valobj.DiscountTypeEnum;
import cn.ant0n.gbm.domain.activity.repository.IActivityRepository;
import cn.ant0n.gbm.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.ant0n.gbm.types.common.Constants;

import javax.annotation.Resource;
import java.math.BigDecimal;

public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService {


    @Resource
    protected IActivityRepository activityRepository;

    @Override
    public BigDecimal calculate(MarketProductEntity marketProductEntity, DefaultActivityStrategyFactory.DynamicContext dynamicContext) {
        return doCalculate(dynamicContext);
    }

    protected abstract boolean filterTag(MarketProductEntity marketProductEntity, DefaultActivityStrategyFactory.DynamicContext dynamicContext);

    protected abstract BigDecimal doCalculate(DefaultActivityStrategyFactory.DynamicContext dynamicContext);
}
