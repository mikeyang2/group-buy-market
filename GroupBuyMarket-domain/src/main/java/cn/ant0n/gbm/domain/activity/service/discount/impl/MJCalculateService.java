package cn.ant0n.gbm.domain.activity.service.discount.impl;

import cn.ant0n.gbm.domain.activity.model.entity.MarketProductEntity;
import cn.ant0n.gbm.domain.activity.service.discount.AbstractDiscountCalculateService;
import cn.ant0n.gbm.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import cn.ant0n.gbm.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service("MJ")
public class MJCalculateService extends AbstractDiscountCalculateService {


    @Override
    protected boolean filterTag(MarketProductEntity marketProductEntity, DefaultActivityStrategyFactory.DynamicContext dynamicContext) {
        // 折扣标签人群过滤
        String tagId = dynamicContext.getGroupBuyActivityDiscountVO().getGroupBuyDiscount().getTagId();
        return activityRepository.queryIdCrowdRange(marketProductEntity.getUserId(), tagId);
    }

    @Override
    protected BigDecimal doCalculate(DefaultActivityStrategyFactory.DynamicContext dynamicContext) {
        log.info("折扣策略计算-策略:{}", dynamicContext.getGroupBuyActivityDiscountVO().getGroupBuyDiscount().getDiscountPlan());

        Integer productQuota = dynamicContext.getProductQuota();
        BigDecimal originalPrice = dynamicContext.getProductSkuVO().getPrice();
        originalPrice = originalPrice.multiply(new BigDecimal(productQuota));
        String discountExpr = dynamicContext.getGroupBuyActivityDiscountVO().getGroupBuyDiscount().getDiscountExpr();
        BigDecimal discountTarget = new BigDecimal(discountExpr.split(Constants.SPLIT)[0]);
        BigDecimal deductionPrice = new BigDecimal(discountExpr.split(Constants.SPLIT)[1]);

        if(originalPrice.compareTo(discountTarget) < 0){
            dynamicContext.setDeductionPrice(new BigDecimal("0"));
            dynamicContext.setPayPrice(originalPrice);
            return originalPrice;
        }

        BigDecimal payPrice = originalPrice.subtract(deductionPrice);
        if(payPrice.compareTo(BigDecimal.ZERO) <= 0){
            payPrice = new BigDecimal(Constants.MIN_DEDUCTION_PRICE);
            dynamicContext.setDeductionPrice(originalPrice.subtract(payPrice));
            dynamicContext.setPayPrice(payPrice);
            return payPrice;
        }
        dynamicContext.setDeductionPrice(deductionPrice);
        dynamicContext.setPayPrice(payPrice);
        return payPrice;
    }
}
