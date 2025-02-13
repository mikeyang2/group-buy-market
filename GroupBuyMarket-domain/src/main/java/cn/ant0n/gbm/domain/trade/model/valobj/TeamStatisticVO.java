package cn.ant0n.gbm.domain.trade.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamStatisticVO {
    private Integer participateCount;
    private Integer completeGroupCount;
    private Integer inProgressGroupCount;
}
