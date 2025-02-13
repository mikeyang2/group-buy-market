package cn.ant0n.gbm.domain.activity.service.trial.factory;

import cn.ant0n.gbm.domain.activity.model.entity.MarketProductEntity;
import cn.ant0n.gbm.domain.activity.model.entity.TrialBalanceEntity;
import cn.ant0n.gbm.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.ant0n.gbm.domain.activity.model.valobj.ProductSkuVO;
import cn.ant0n.gbm.domain.activity.service.trial.node.RootNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Component
public class DefaultActivityStrategyFactory {

    private final RootNode rootNode;

    public DefaultActivityStrategyFactory(RootNode rootNode) {
        this.rootNode = rootNode;
    }


    public TrialBalanceEntity apply(MarketProductEntity marketProductEntity) throws ExecutionException, InterruptedException, TimeoutException {
        return rootNode.apply(marketProductEntity, new DynamicContext());
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class DynamicContext{
        private ProductSkuVO productSkuVO;
        private GroupBuyActivityDiscountVO groupBuyActivityDiscountVO;
        private BigDecimal deductionPrice;
        private Boolean isDisable;
        private Boolean isNotVisible;
        private BigDecimal payPrice;
        private Integer productQuota;
    }
}
