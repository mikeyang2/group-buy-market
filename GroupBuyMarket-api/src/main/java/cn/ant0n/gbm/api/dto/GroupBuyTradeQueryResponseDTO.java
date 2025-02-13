package cn.ant0n.gbm.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyTradeQueryResponseDTO {
    private BigDecimal originalPrice;
    /** 折扣价格 */
    private BigDecimal deductionPrice;
    /** 拼团开始时间 */
    private Date startTime;
    /** 拼团结束时间 */
    private Date endTime;
    /** 是否可见拼团 */
    private Boolean isVisible;
    /** 是否可参与进团 */
    private Boolean isEnable;

    private List<GroupBuyOrderItem> groupBuyOrderItems;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GroupBuyOrderItem{
        private String teamId;
        private Integer remainCount;
    }
}
