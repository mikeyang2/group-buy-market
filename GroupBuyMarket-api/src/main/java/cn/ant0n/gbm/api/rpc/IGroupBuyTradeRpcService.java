package cn.ant0n.gbm.api.rpc;

import cn.ant0n.gbm.api.dto.GroupBuyTradeRequestDTO;
import cn.ant0n.gbm.api.dto.GroupBuyTradeResponseDTO;
import cn.ant0n.gbm.api.request.Request;
import cn.ant0n.gbm.api.response.Response;

public interface IGroupBuyTradeRpcService {

    Response<GroupBuyTradeResponseDTO> groupBuyTrade(Request<GroupBuyTradeRequestDTO> request);
}
