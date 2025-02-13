package cn.ant0n.gbm.api.rpc;

import cn.ant0n.gbm.api.dto.ProductMarketRequestDTO;
import cn.ant0n.gbm.api.dto.ProductMarketResponseDTO;
import cn.ant0n.gbm.api.request.Request;
import cn.ant0n.gbm.api.response.Response;

public interface IMarketIndexConfigService {

    Response<ProductMarketResponseDTO> queryMarketIndexConfig(Request<ProductMarketRequestDTO> request);
}
