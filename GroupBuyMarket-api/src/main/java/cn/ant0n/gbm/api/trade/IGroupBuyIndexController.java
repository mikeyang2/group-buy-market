package cn.ant0n.gbm.api.trade;

import cn.ant0n.gbm.api.dto.ProductMarketRequestDTO;
import cn.ant0n.gbm.api.dto.ProductMarketResponseDTO;
import cn.ant0n.gbm.api.response.Response;

public interface IGroupBuyIndexController {

    Response<ProductMarketResponseDTO> queryGroupBuyIndexConfig(ProductMarketRequestDTO request);
}
