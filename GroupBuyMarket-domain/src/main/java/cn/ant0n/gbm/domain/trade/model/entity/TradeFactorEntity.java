package cn.ant0n.gbm.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeFactorEntity {
    private String userId;
    private String source;
    private String channel;
    private String productId;
    private String teamId;
}
