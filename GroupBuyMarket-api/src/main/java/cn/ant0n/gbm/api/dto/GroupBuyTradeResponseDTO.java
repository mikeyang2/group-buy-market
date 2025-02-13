package cn.ant0n.gbm.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyTradeResponseDTO implements Serializable {
    private String orderId;
    private BigDecimal payPrice;
}
