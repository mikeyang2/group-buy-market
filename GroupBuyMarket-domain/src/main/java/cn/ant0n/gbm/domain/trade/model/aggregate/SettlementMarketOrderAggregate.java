package cn.ant0n.gbm.domain.trade.model.aggregate;

import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettlementMarketOrderAggregate {
    private GroupBuyOrderEntity groupBuyOrderEntity;
    private String userId;
    private String outTradeNo;
}
