package cn.ant0n.gbm.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyOrderRefundEntity {
    private List<RefundItem> refundItems = new ArrayList<>();

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RefundItem{
        private String userId;
        private String outTradeNo;
    }

    public void add(String userId, String outTradeNo){
        this.refundItems.add(new RefundItem(userId,outTradeNo));
    }
}
