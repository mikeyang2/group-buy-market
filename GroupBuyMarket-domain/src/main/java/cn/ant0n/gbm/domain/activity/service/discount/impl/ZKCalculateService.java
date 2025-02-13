package cn.ant0n.gbm.domain.activity.service.discount.impl;

import cn.ant0n.gbm.domain.activity.model.entity.MarketProductEntity;
import cn.ant0n.gbm.domain.activity.service.discount.AbstractDiscountCalculateService;
import cn.ant0n.gbm.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service("ZK")
public class ZKCalculateService extends AbstractDiscountCalculateService {


    @Override
    protected boolean filterTag(MarketProductEntity marketProductEntity, DefaultActivityStrategyFactory.DynamicContext dynamicContext) {
        // 人群过滤-待做
        return true;
    }

    @Override
    protected BigDecimal doCalculate(DefaultActivityStrategyFactory.DynamicContext dynamicContext) {
        log.info("折扣策略计算-策略:{}", dynamicContext.getGroupBuyActivityDiscountVO().getGroupBuyDiscount().getDiscountPlan());

        BigDecimal originalPrice = dynamicContext.getProductSkuVO().getPrice();
        String discountExpr = dynamicContext.getGroupBuyActivityDiscountVO().getGroupBuyDiscount().getDiscountExpr();
        return originalPrice.multiply(new BigDecimal(discountExpr));
    }
}
