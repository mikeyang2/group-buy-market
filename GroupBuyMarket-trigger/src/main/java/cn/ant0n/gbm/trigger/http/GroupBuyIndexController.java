package cn.ant0n.gbm.trigger.http;


import cn.ant0n.gbm.api.dto.ProductMarketRequestDTO;
import cn.ant0n.gbm.api.dto.ProductMarketResponseDTO;
import cn.ant0n.gbm.api.response.Response;
import cn.ant0n.gbm.api.trade.IGroupBuyIndexController;
import cn.ant0n.gbm.domain.activity.model.entity.GroupBuyTeamsEntity;
import cn.ant0n.gbm.domain.activity.model.entity.MarketProductEntity;
import cn.ant0n.gbm.domain.activity.model.entity.TrialBalanceEntity;
import cn.ant0n.gbm.domain.trade.model.valobj.TeamStatisticVO;
import cn.ant0n.gbm.domain.activity.service.IIndexGroupBuyMarketService;
import cn.ant0n.gbm.domain.trade.service.settlement.ITradeSettlementOrderService;
import cn.ant0n.gbm.types.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController()
@Slf4j
@RequestMapping("/gbm/backend/api/index")
public class GroupBuyIndexController implements IGroupBuyIndexController {

    @Resource
    private IIndexGroupBuyMarketService indexGroupBuyMarketService;
    @Resource
    private ITradeSettlementOrderService tradeSettlementOrderService;


    @Override
    @RequestMapping(method = RequestMethod.POST, value = "/config")
    public Response<ProductMarketResponseDTO> queryGroupBuyIndexConfig(@RequestBody ProductMarketRequestDTO request) {
        try{
            String userId = request.getUserId();
            String productId = request.getProductId();
            String source = request.getSource();
            String channel = request.getChannel();

            MarketProductEntity marketProductEntity = MarketProductEntity.builder()
                    .userId(userId)
                    .productId(productId)
                    .source(source)
                    .channel(channel).build();

            /* 拼团折扣优惠试算**/
            TrialBalanceEntity trialBalanceEntity = indexGroupBuyMarketService.indexMarketTrial(marketProductEntity);

            ProductMarketResponseDTO.TeamStatistic teamStatistic = ProductMarketResponseDTO.TeamStatistic.builder().build();

            TeamStatisticVO teamStatisticVO = tradeSettlementOrderService.queryTeamStatisticVO(trialBalanceEntity.getActivityId());
            teamStatistic.setParticipateCount(teamStatisticVO.getParticipateCount());
            teamStatistic.setCompleteGroupCount(teamStatisticVO.getCompleteGroupCount());
            teamStatistic.setInProgressGroupCount(teamStatisticVO.getInProgressGroupCount());

            List<ProductMarketResponseDTO.Team> teams = new ArrayList<>();
            List<GroupBuyTeamsEntity> groupBuyTeamsEntities = tradeSettlementOrderService.queryGroupBuyTeamsEntity(trialBalanceEntity.getActivityId(), userId);
            for(GroupBuyTeamsEntity groupBuyTeamsEntity : groupBuyTeamsEntities){
                ProductMarketResponseDTO.Team team = new ProductMarketResponseDTO.Team();
                team.setUserId(groupBuyTeamsEntity.getUserId());
                team.setTeamId(groupBuyTeamsEntity.getTeamId());
                team.setStartTime(groupBuyTeamsEntity.getStartTime());
                team.setEndTime(groupBuyTeamsEntity.getEndTime());
                team.setRemainCount(groupBuyTeamsEntity.getRemainCount());
                team.setValidTimeCountdown(groupBuyTeamsEntity.getValidTimeCountdown());

                teams.add(team);
            }
            return Response.<ProductMarketResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(ProductMarketResponseDTO.builder()
                            .teamStatistic(teamStatistic)
                            .groupBuyTeams(teams).build()).build();
        }catch (Exception e){
            return Response.<ProductMarketResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null).build();
        }
    }
}
