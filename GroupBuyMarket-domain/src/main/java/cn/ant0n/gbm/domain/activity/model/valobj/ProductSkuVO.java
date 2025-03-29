package cn.ant0n.gbm.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSkuVO {
    private String productId;
    private String productName;
    private BigDecimal price;
    private String test;
    private String test2;
}
