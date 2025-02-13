package cn.ant0n.gbm.api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyTradeQueryRequestDTO {
    private String userId;
    private String productId;
    private String source;
    private String channel;
}
