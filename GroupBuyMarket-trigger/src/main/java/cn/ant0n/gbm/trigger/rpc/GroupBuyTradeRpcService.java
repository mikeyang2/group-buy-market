package cn.ant0n.gbm.trigger.rpc;

import cn.ant0n.gbm.api.dto.GroupBuyTradeRequestDTO;
import cn.ant0n.gbm.api.dto.GroupBuyTradeResponseDTO;
import cn.ant0n.gbm.api.request.Request;
import cn.ant0n.gbm.api.response.Response;
import cn.ant0n.gbm.api.rpc.IGroupBuyTradeRpcService;
import cn.ant0n.gbm.domain.activity.model.entity.MarketProductEntity;
import cn.ant0n.gbm.domain.activity.model.entity.TrialBalanceEntity;
import cn.ant0n.gbm.domain.activity.service.IIndexGroupBuyMarketService;
import cn.ant0n.gbm.domain.trade.model.aggregate.LockGroupBuyOrderAggregate;
import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderEntity;
import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderListEntity;
import cn.ant0n.gbm.domain.trade.model.entity.TradeFactorEntity;
import cn.ant0n.gbm.domain.trade.service.ITradeService;
import cn.ant0n.gbm.domain.trade.service.rule.factory.TradeLogicFilterChainFactory;
import cn.ant0n.gbm.types.enums.ResponseCode;
import cn.ant0n.gbm.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

@DubboService(version = "1.0.0")
@Slf4j
public class GroupBuyTradeRpcService implements IGroupBuyTradeRpcService {

    @Resource
    private ITradeService tradeService;
    @Resource
    private IIndexGroupBuyMarketService iIndexGroupBuyMarketService;
    @Resource
    private TradeLogicFilterChainFactory factory;

    @Override
    public Response<GroupBuyTradeResponseDTO> groupBuyTrade(Request<GroupBuyTradeRequestDTO> request) {
        try{
            String userId = request.getData().getUserId();
            String productId = request.getData().getProductId();
            String teamId = request.getData().getTeamId();
            String source = request.getData().getSource();
            String channel = request.getData().getChannel();
            String outTradeNo = request.getData().getOutTradeNo();
            Integer productQuota = request.getData().getProductQuota();

            //幂等(远程调用-防止网抖的重复调用,或者用户重复调用)
            GroupBuyOrderListEntity groupBuyOrderListEntity = tradeService.queryLockedGroupBuyOrderList(userId, outTradeNo);
            if(null != groupBuyOrderListEntity){
                return Response.<GroupBuyTradeResponseDTO>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(null).build();
            }

            // 检查活动时间与状态，检查拼团-团是否过了valid_time, 检查用户是否达到活动规定的最大拼团限定
            TradeFactorEntity tradeFactorEntity = new TradeFactorEntity();
            tradeFactorEntity.setUserId(userId);
            tradeFactorEntity.setTeamId(teamId);
            tradeFactorEntity.setProductId(productId);
            tradeFactorEntity.setSource(source);
            tradeFactorEntity.setChannel(channel);
            factory.filter(tradeFactorEntity);

            //拼团优惠试算
            MarketProductEntity marketProductEntity = MarketProductEntity.builder()
                    .userId(userId)
                    .productId(productId)
                    .source(source)
                    .productQuota(productQuota)
                    .channel(channel).build();
            TrialBalanceEntity trialBalanceEntity = iIndexGroupBuyMarketService.indexMarketTrial(marketProductEntity);

            LockGroupBuyOrderAggregate aggregate = new LockGroupBuyOrderAggregate();
            aggregate.setIsMaster(StringUtils.isBlank(teamId));
            if(aggregate.getIsMaster()){
                GroupBuyOrderEntity groupBuyOrderEntity = new GroupBuyOrderEntity();
                groupBuyOrderEntity.setTeamId(RandomStringUtils.randomNumeric(8));
                groupBuyOrderEntity.setActivityId(trialBalanceEntity.getActivityId());
                groupBuyOrderEntity.setSource(source);
                groupBuyOrderEntity.setChannel(channel);
                groupBuyOrderEntity.setOriginalPrice(trialBalanceEntity.getOriginalPrice());
                groupBuyOrderEntity.setDeductionPrice(trialBalanceEntity.getDeductionPrice());
                groupBuyOrderEntity.setPayPrice(trialBalanceEntity.getPayPrice());
                groupBuyOrderEntity.setTargetCount(trialBalanceEntity.getTargetCount());
                groupBuyOrderEntity.setCompleteCount(0);
                groupBuyOrderEntity.setLockCount(1);
                groupBuyOrderEntity.setStatus((short)0);


                // 日期处理
                Date currentDate = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.add(Calendar.MINUTE, trialBalanceEntity.getValidTime());

                groupBuyOrderEntity.setStartTime(currentDate);
                groupBuyOrderEntity.setEndTime(calendar.getTime());

                aggregate.setGroupBuyOrderEntity(groupBuyOrderEntity);
            }
            GroupBuyOrderListEntity groupBuyOrderList = new GroupBuyOrderListEntity();
            groupBuyOrderList.setUserId(userId);
            groupBuyOrderList.setOrderId(RandomStringUtils.randomNumeric(12));
            groupBuyOrderList.setActivityId(trialBalanceEntity.getActivityId());
            groupBuyOrderList.setStartTime(trialBalanceEntity.getStartTime());
            groupBuyOrderList.setEndTime(trialBalanceEntity.getEndTime());
            groupBuyOrderList.setProductId(productId);
            groupBuyOrderList.setSource(source);
            groupBuyOrderList.setChannel(channel);
            groupBuyOrderList.setOriginalPrice(trialBalanceEntity.getOriginalPrice());
            groupBuyOrderList.setDeductionPrice(trialBalanceEntity.getDeductionPrice());
            groupBuyOrderList.setStatus((short)0);
            groupBuyOrderList.setOutTradeNo(outTradeNo);
            if(aggregate.getIsMaster()){
                groupBuyOrderList.setTeamId(aggregate.getGroupBuyOrderEntity().getTeamId());
            }
            else{
                groupBuyOrderList.setTeamId(teamId);
            }
            aggregate.setGroupBuyOrderListEntity(groupBuyOrderList);

            tradeService.lockGroupBuyOrder(aggregate);
            GroupBuyTradeResponseDTO response = new GroupBuyTradeResponseDTO();
            response.setOrderId(groupBuyOrderList.getOrderId());
            response.setPayPrice(trialBalanceEntity.getPayPrice());
            return Response.<GroupBuyTradeResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(response).build();
        }catch (AppException e){
            return Response.<GroupBuyTradeResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .data(null).build();
        }catch (Exception e){
            return Response.<GroupBuyTradeResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null).build();
        }
    }
}
