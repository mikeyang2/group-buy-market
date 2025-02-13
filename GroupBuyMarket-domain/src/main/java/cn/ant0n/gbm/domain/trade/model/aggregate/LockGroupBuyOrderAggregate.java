package cn.ant0n.gbm.domain.trade.model.aggregate;

import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderEntity;
import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderListEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LockGroupBuyOrderAggregate {
    private GroupBuyOrderListEntity groupBuyOrderListEntity;
    private GroupBuyOrderEntity groupBuyOrderEntity;
    private Boolean isMaster;
}
