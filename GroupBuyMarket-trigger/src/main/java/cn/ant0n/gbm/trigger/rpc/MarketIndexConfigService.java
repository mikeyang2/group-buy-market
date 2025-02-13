package cn.ant0n.gbm.trigger.rpc;

import cn.ant0n.gbm.api.dto.ProductMarketRequestDTO;
import cn.ant0n.gbm.api.dto.ProductMarketResponseDTO;
import cn.ant0n.gbm.api.request.Request;
import cn.ant0n.gbm.api.response.Response;
import cn.ant0n.gbm.api.rpc.IMarketIndexConfigService;
import cn.ant0n.gbm.domain.activity.model.entity.MarketProductEntity;
import cn.ant0n.gbm.domain.activity.model.entity.TrialBalanceEntity;
import cn.ant0n.gbm.domain.activity.service.IIndexGroupBuyMarketService;
import cn.ant0n.gbm.domain.trade.model.entity.MarketTeamsDetailEntity;
import cn.ant0n.gbm.domain.trade.model.valobj.TeamStatisticVO;
import cn.ant0n.gbm.domain.trade.service.index.IMarketIndexService;
import cn.ant0n.gbm.types.enums.ResponseCode;
import cn.ant0n.gbm.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@DubboService(version = "1.0.0")
@Slf4j
public class MarketIndexConfigService implements IMarketIndexConfigService {

    @Resource
    private IIndexGroupBuyMarketService indexGroupBuyMarketService;
    @Resource
    private IMarketIndexService marketIndexService;

    @Override
    public Response<ProductMarketResponseDTO> queryMarketIndexConfig(Request<ProductMarketRequestDTO> request) {
        ProductMarketRequestDTO productMarketRequestDTO = request.getData();
        log.info("拼团交易-查询拼团首页配置信息, userId:{}, productId:{}", productMarketRequestDTO.getUserId(), productMarketRequestDTO.getProductId());
        try{
            // 参数校验
            String userId = productMarketRequestDTO.getUserId();
            String productId = productMarketRequestDTO.getProductId();
            String source = productMarketRequestDTO.getSource();
            String channel = productMarketRequestDTO.getChannel();
            if(StringUtils.isBlank(userId) || StringUtils.isBlank(productId) || StringUtils.isBlank(source) || StringUtils.isBlank(channel)){
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }

            // 拼团优惠折扣计算
            MarketProductEntity marketProductEntity = MarketProductEntity.builder()
                    .userId(userId)
                    .productId(productId)
                    .source(source)
                    .productQuota(1)
                    .channel(channel).build();
            TrialBalanceEntity trialBalanceEntity = indexGroupBuyMarketService.indexMarketTrial(marketProductEntity);
            if(!trialBalanceEntity.getIsEnable()){
                return Response.<ProductMarketResponseDTO>builder()
                        .code(ResponseCode.DISABLE.getCode())
                        .info(ResponseCode.DISABLE.getInfo())
                        .data(null).build();
            }
            // 查询拼团活动统计
            TeamStatisticVO teamStatisticVO = marketIndexService.queryGroupBuyMarketTeamStatistic(trialBalanceEntity.getActivityId());
            ProductMarketResponseDTO.TeamStatistic teamStatistic = ProductMarketResponseDTO.TeamStatistic.builder()
                    .inProgressGroupCount(teamStatisticVO.getInProgressGroupCount())
                    .completeGroupCount(teamStatisticVO.getCompleteGroupCount())
                    .participateCount(teamStatisticVO.getParticipateCount())
                    .originalPrice(trialBalanceEntity.getOriginalPrice())
                    .deductionPrice(trialBalanceEntity.getDeductionPrice())
                    .payPrice(trialBalanceEntity.getPayPrice()).build();

            // 查询当前拼团-团列表数据
            List<MarketTeamsDetailEntity> marketTeamsDetailEntities = marketIndexService.queryMarketTeamsDetail(trialBalanceEntity.getActivityId());
            List<ProductMarketResponseDTO.Team> teams = new ArrayList<>();
            for(MarketTeamsDetailEntity marketTeamsDetailEntity : marketTeamsDetailEntities){
                ProductMarketResponseDTO.Team team = new ProductMarketResponseDTO.Team();
                team.setTeamId(marketTeamsDetailEntity.getTeamId());
                team.setUserId(marketTeamsDetailEntity.getUserId());
                team.setRemainCount(marketTeamsDetailEntity.getRemainCount());
                team.setStartTime(marketTeamsDetailEntity.getStartTime());
                team.setEndTime(marketTeamsDetailEntity.getEndTime());
                team.setValidTimeCountdown(marketTeamsDetailEntity.getValidTimeCountdown());

                teams.add(team);
            }

            ProductMarketResponseDTO response = ProductMarketResponseDTO.builder()
                    .teamStatistic(teamStatistic)
                    .groupBuyTeams(teams).build();
            if(trialBalanceEntity.getIsVisible()){
                return Response.<ProductMarketResponseDTO>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(response).build();
            }
            return Response.<ProductMarketResponseDTO>builder()
                    .code(ResponseCode.VISIBLE.getCode())
                    .info(ResponseCode.VISIBLE.getInfo())
                    .data(response).build();
        }catch (AppException e){
            return Response.<ProductMarketResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .data(null).build();
        }catch (Exception e){
            return Response.<ProductMarketResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null).build();
        }
    }
}
