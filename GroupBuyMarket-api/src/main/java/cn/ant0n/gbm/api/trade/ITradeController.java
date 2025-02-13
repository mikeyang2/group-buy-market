package cn.ant0n.gbm.api.trade;

import cn.ant0n.gbm.api.dto.GroupBuyTradeQueryRequestDTO;
import cn.ant0n.gbm.api.dto.GroupBuyTradeQueryResponseDTO;
import cn.ant0n.gbm.api.dto.GroupBuyTradeRequestDTO;
import cn.ant0n.gbm.api.dto.GroupBuyTradeResponseDTO;
import cn.ant0n.gbm.api.response.Response;

public interface ITradeController {

    Response<GroupBuyTradeResponseDTO> groupBuyTrade(GroupBuyTradeRequestDTO request);

    Response<GroupBuyTradeQueryResponseDTO> queryGroupBuyOrder(GroupBuyTradeQueryRequestDTO request);
}
