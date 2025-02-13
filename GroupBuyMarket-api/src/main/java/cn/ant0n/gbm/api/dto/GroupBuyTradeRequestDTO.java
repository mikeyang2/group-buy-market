package cn.ant0n.gbm.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyTradeRequestDTO implements Serializable {
    private String userId;
    private String teamId;
    private String outTradeNo;
    private String productId;
    private String source;
    private String channel;
    private Integer productQuota;
}
