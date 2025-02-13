package cn.ant0n.gbm.infrastructure.adapter.repository;

import cn.ant0n.gbm.domain.activity.model.entity.GroupBuyTeamsEntity;
import cn.ant0n.gbm.domain.trade.event.GroupBuyMarketSuccessEvent;
import cn.ant0n.gbm.domain.trade.event.GroupBuyOrderCancelEvent;
import cn.ant0n.gbm.domain.trade.event.GroupBuyOrderDelayEvent;
import cn.ant0n.gbm.domain.trade.event.SettlementRefundEvent;
import cn.ant0n.gbm.domain.trade.model.aggregate.LockGroupBuyOrderAggregate;
import cn.ant0n.gbm.domain.trade.model.aggregate.SettlementMarketOrderAggregate;
import cn.ant0n.gbm.domain.trade.model.entity.*;
import cn.ant0n.gbm.domain.trade.model.valobj.TeamStatisticVO;
import cn.ant0n.gbm.domain.trade.repository.ITradeRepository;
import cn.ant0n.gbm.infrastructure.adapter.event.EventPublisher;
import cn.ant0n.gbm.infrastructure.dao.IGroupBuyOrderDao;
import cn.ant0n.gbm.infrastructure.dao.IGroupBuyOrderListDao;
import cn.ant0n.gbm.infrastructure.dao.po.GroupBuyOrder;
import cn.ant0n.gbm.infrastructure.dao.po.GroupBuyOrderList;
import cn.ant0n.gbm.infrastructure.redis.IRedisService;
import cn.ant0n.gbm.types.common.Constants;
import cn.ant0n.gbm.types.enums.ResponseCode;
import cn.ant0n.gbm.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class TradeRepository implements ITradeRepository {

    @Resource
    private IGroupBuyOrderListDao groupBuyOrderListDao;
    @Resource
    private IRedisService redisService;
    @Resource
    private IGroupBuyOrderDao groupBuyOrderDao;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public GroupBuyOrderListEntity queryLockedGroupBuyOrderList(String userId, String outTradeNo) {
        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();
        groupBuyOrderListReq.setUserId(userId);
        groupBuyOrderListReq.setOutTradeNo(outTradeNo);

        GroupBuyOrderList groupBuyOrderListRes = groupBuyOrderListDao.queryLockedGroupBuyOrderList(groupBuyOrderListReq);
        if(null == groupBuyOrderListRes) return null;
        return GroupBuyOrderListEntity.builder()
                .userId(userId)
                .orderId(groupBuyOrderListRes.getOrderId())
                .activityId(groupBuyOrderListRes.getActivityId())
                .startTime(groupBuyOrderListRes.getStartTime())
                .endTime(groupBuyOrderListRes.getEndTime())
                .productId(groupBuyOrderListRes.getProductId())
                .source(groupBuyOrderListRes.getSource())
                .channel(groupBuyOrderListRes.getChannel())
                .originalPrice(groupBuyOrderListRes.getOriginalPrice())
                .deductionPrice(groupBuyOrderListRes.getDeductionPrice())
                .status(groupBuyOrderListRes.getStatus())
                .outTradeNo(outTradeNo)
                .teamId(groupBuyOrderListRes.getTeamId()).build();
    }

    @Override
    public Map<String, GroupBuyOrderEntity> queryExistGroupBuyOrderList(String activityId) {
        String cacheKey = Constants.Redis.GROUP_BUY_ORDER.concat(activityId);
        Map<String, GroupBuyOrderEntity> groupBuyOrderGroup = redisService.getMap(cacheKey);
        if(!groupBuyOrderGroup.isEmpty()) return groupBuyOrderGroup;

        List<GroupBuyOrder> groupBuyOrders = groupBuyOrderDao.queryExistGroupBuyOrderList(activityId);
        for(GroupBuyOrder groupBuyOrder : groupBuyOrders){
            GroupBuyOrderEntity groupBuyOrderEntity = new GroupBuyOrderEntity();
            groupBuyOrderEntity.setTeamId(groupBuyOrder.getTeamId());
            groupBuyOrderEntity.setTargetCount(groupBuyOrder.getTargetCount());
            groupBuyOrderEntity.setLockCount(groupBuyOrder.getLockCount());
            groupBuyOrderEntity.setStartTime(groupBuyOrder.getStartTime());
            groupBuyOrderEntity.setEndTime(groupBuyOrder.getEndTime());

            groupBuyOrderGroup.put(groupBuyOrderEntity.getTeamId(), groupBuyOrderEntity);
        }
        return groupBuyOrderGroup;
    }

    @Override
    public void lockGroupBuyOrderMaster(LockGroupBuyOrderAggregate aggregate) {
        GroupBuyOrderEntity groupBuyOrderEntity = aggregate.getGroupBuyOrderEntity();
        GroupBuyOrderListEntity groupBuyOrderListEntity = aggregate.getGroupBuyOrderListEntity();
        GroupBuyOrder groupBuyOrder = new GroupBuyOrder();
        groupBuyOrder.setTeamId(groupBuyOrderEntity.getTeamId());
        groupBuyOrder.setActivityId(groupBuyOrderEntity.getActivityId());
        groupBuyOrder.setSource(groupBuyOrderEntity.getSource());
        groupBuyOrder.setChannel(groupBuyOrderEntity.getChannel());
        groupBuyOrder.setOriginalPrice(groupBuyOrderEntity.getOriginalPrice());
        groupBuyOrder.setDeductionPrice(groupBuyOrderEntity.getDeductionPrice());
        groupBuyOrder.setPayPrice(groupBuyOrderEntity.getPayPrice());
        groupBuyOrder.setStartTime(groupBuyOrderEntity.getStartTime());
        groupBuyOrder.setEndTime(groupBuyOrderEntity.getEndTime());
        groupBuyOrder.setLockCount(groupBuyOrderEntity.getLockCount());
        groupBuyOrder.setCompleteCount(groupBuyOrderEntity.getCompleteCount());
        groupBuyOrder.setTargetCount(groupBuyOrderEntity.getTargetCount());
        groupBuyOrder.setStatus(groupBuyOrderEntity.getStatus());

        GroupBuyOrderList groupBuyOrderList = new GroupBuyOrderList();
        groupBuyOrderList.setUserId(groupBuyOrderListEntity.getUserId());
        groupBuyOrderList.setOrderId(groupBuyOrderListEntity.getOrderId());
        groupBuyOrderList.setActivityId(groupBuyOrderListEntity.getActivityId());
        groupBuyOrderList.setTeamId(groupBuyOrderListEntity.getTeamId());
        groupBuyOrderList.setStartTime(groupBuyOrderListEntity.getStartTime());
        groupBuyOrderList.setEndTime(groupBuyOrderListEntity.getEndTime());
        groupBuyOrderList.setProductId(groupBuyOrderListEntity.getProductId());
        groupBuyOrderList.setSource(groupBuyOrderListEntity.getSource());
        groupBuyOrderList.setChannel(groupBuyOrderListEntity.getChannel());
        groupBuyOrderList.setOriginalPrice(groupBuyOrderListEntity.getOriginalPrice());
        groupBuyOrderList.setDeductionPrice(groupBuyOrderListEntity.getDeductionPrice());
        groupBuyOrderList.setStatus(groupBuyOrderListEntity.getStatus());
        groupBuyOrderList.setOutTradeNo(groupBuyOrderListEntity.getOutTradeNo());

        transactionTemplate.execute(status -> {
            try{
               groupBuyOrderDao.insert(groupBuyOrder);
               groupBuyOrderListDao.insert(groupBuyOrderList);
               return 1;
            }catch (Exception e){
                status.setRollbackOnly();
                log.warn("拼团系统-锁定拼团单失败", e);
                throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
            }
        });

        eventPublisher.publishDelivery(GroupBuyOrderDelayEvent.topic, GroupBuyOrderDelayEvent.create(groupBuyOrderEntity.getTeamId()), 5);
    }

    @Override
    public void lockGroupBuyOrderSlave(LockGroupBuyOrderAggregate aggregate) {
        GroupBuyOrderEntity groupBuyOrderEntity = aggregate.getGroupBuyOrderEntity();
        GroupBuyOrderListEntity groupBuyOrderListEntity = aggregate.getGroupBuyOrderListEntity();
        GroupBuyOrder groupBuyOrder = new GroupBuyOrder();
        groupBuyOrder.setTeamId(groupBuyOrderListEntity.getTeamId());

        GroupBuyOrderList groupBuyOrderList = new GroupBuyOrderList();
        groupBuyOrderList.setUserId(groupBuyOrderListEntity.getUserId());
        groupBuyOrderList.setOrderId(groupBuyOrderListEntity.getOrderId());
        groupBuyOrderList.setActivityId(groupBuyOrderListEntity.getActivityId());
        groupBuyOrderList.setTeamId(groupBuyOrderListEntity.getTeamId());
        groupBuyOrderList.setStartTime(groupBuyOrderListEntity.getStartTime());
        groupBuyOrderList.setEndTime(groupBuyOrderListEntity.getEndTime());
        groupBuyOrderList.setProductId(groupBuyOrderListEntity.getProductId());
        groupBuyOrderList.setSource(groupBuyOrderListEntity.getSource());
        groupBuyOrderList.setChannel(groupBuyOrderListEntity.getChannel());
        groupBuyOrderList.setOriginalPrice(groupBuyOrderListEntity.getOriginalPrice());
        groupBuyOrderList.setDeductionPrice(groupBuyOrderListEntity.getDeductionPrice());
        groupBuyOrderList.setStatus(groupBuyOrderListEntity.getStatus());
        groupBuyOrderList.setOutTradeNo(groupBuyOrderListEntity.getOutTradeNo());

        transactionTemplate.execute(status -> {
            try{
                int count = groupBuyOrderDao.joinGroupUpdate(groupBuyOrder);
                if(count == 0){
                    status.setRollbackOnly();
                    log.warn("拼团系统-该团已取消或者满员");
                    throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
                }
                groupBuyOrderListDao.insert(groupBuyOrderList);
                return 1;
            }catch (Exception e){
                status.setRollbackOnly();
                log.warn("拼团系统-锁定拼团单失败", e);
                throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
            }
        });
    }

    @Override
    public Integer queryUserTakeCount(String userId, String activityId) {
        return groupBuyOrderListDao.queryUserTakeCount(userId, activityId);
    }

    @Override
    public GroupBuyOrderEntity queryGroupBuyOrderEntity(String teamId) {

        GroupBuyOrder groupBuyOrder = groupBuyOrderDao.queryGroupBuyOrder(teamId);
        GroupBuyOrderEntity groupBuyOrderEntity = GroupBuyOrderEntity.builder()
                .teamId(teamId)
                .activityId(groupBuyOrder.getActivityId())
                .source(groupBuyOrder.getSource())
                .channel(groupBuyOrder.getChannel())
                .deductionPrice(groupBuyOrder.getDeductionPrice())
                .originalPrice(groupBuyOrder.getOriginalPrice())
                .payPrice(groupBuyOrder.getPayPrice())
                .targetCount(groupBuyOrder.getTargetCount())
                .completeCount(groupBuyOrder.getCompleteCount())
                .startTime(groupBuyOrder.getStartTime())
                .status(groupBuyOrder.getStatus())
                .endTime(groupBuyOrder.getEndTime()).build();

        return groupBuyOrderEntity;
    }

    @Override
    public GroupBuyOrderListEntity queryGroupBuyOrderListByOutTradeNo(String outTradeNo) {
        GroupBuyOrderList groupBuyOrderList = groupBuyOrderListDao.queryGroupBuyOrderListByOutTradeNo(outTradeNo);
        if(null == groupBuyOrderList) return null;
        return GroupBuyOrderListEntity.builder()
                .teamId(groupBuyOrderList.getTeamId()).build();
    }

    @Override
    /**
     * 更新用户拼团单状态为1(消费成功)
     * 更新拼团记录单 complete_count++
     * 根据target和complete判断是否当前单为最后一单，为则变更记录单状态为1(完成)并发送MQ消息
     */
    public void settlementMarketOrder(SettlementMarketOrderAggregate aggregate) {
        String userId = aggregate.getUserId();
        String outTradeNo = aggregate.getOutTradeNo();
        GroupBuyOrderEntity groupBuyOrderEntity = aggregate.getGroupBuyOrderEntity();

        transactionTemplate.execute(status -> {
            try{
                int count = groupBuyOrderListDao.update2Complete(outTradeNo);
                if(count == 0){
                    status.setRollbackOnly();
                    throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
                }
                count = groupBuyOrderDao.incrCompleteCount(groupBuyOrderEntity.getTeamId());
                if(count == 0){
                    status.setRollbackOnly();
                    throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
                }
                if((groupBuyOrderEntity.getTargetCount() - groupBuyOrderEntity.getCompleteCount()) == 1){
                    count = groupBuyOrderDao.update2Complete(groupBuyOrderEntity.getTeamId());
                    if(count == 0){
                        status.setRollbackOnly();
                        throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
                    }
                }
                return 1;
            }catch (Exception e){
                status.setRollbackOnly();
                log.warn("拼团交易-订单结算异常", e);
                throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
            }
        });

        if((groupBuyOrderEntity.getTargetCount() - groupBuyOrderEntity.getCompleteCount()) == 1){
            log.info("拼团交易-团:{}完成", groupBuyOrderEntity.getTeamId());
            List<GroupBuyOrderList> groupBuyOrderLists = groupBuyOrderListDao.queryGroupBuyOrderListByTeamId(groupBuyOrderEntity.getTeamId());

            GroupBuyMarketSuccessEntity groupBuyMarketSuccessEntity = new GroupBuyMarketSuccessEntity();
            for(GroupBuyOrderList groupBuyOrderList : groupBuyOrderLists){
                groupBuyMarketSuccessEntity.add(groupBuyOrderList.getUserId(), groupBuyOrderList.getProductId(), groupBuyOrderList.getOutTradeNo());
            }

            GroupBuyMarketSuccessEvent event = GroupBuyMarketSuccessEvent.create(groupBuyMarketSuccessEntity);

            eventPublisher.publish(GroupBuyMarketSuccessEvent.topic, event);
        }
        
    }

    @Override
    public List<GroupBuyOrderListEntity> queryGroupBuyOrderListByTeamID(String teamId) {
        List<GroupBuyOrderList> groupBuyOrderLists = groupBuyOrderListDao.queryGroupBuyOrderListByTeamId(teamId);

        List<GroupBuyOrderListEntity> groupBuyOrderListEntities = new ArrayList<>();
        for(GroupBuyOrderList groupBuyOrderList : groupBuyOrderLists){
            GroupBuyOrderListEntity groupBuyOrderListEntity = new GroupBuyOrderListEntity();
            groupBuyOrderListEntity.setUserId(groupBuyOrderList.getUserId());
            groupBuyOrderListEntity.setOutTradeNo(groupBuyOrderList.getOutTradeNo());

            groupBuyOrderListEntities.add(groupBuyOrderListEntity);
        }
        return groupBuyOrderListEntities;
    }

    @Override
    public void cancelGroupBuyOrder(List<GroupBuyOrderListEntity> groupBuyOrderListEntities, String teamId) {

        GroupBuyOrderRefundEntity groupBuyOrderRefundEntity = new GroupBuyOrderRefundEntity();
        for(GroupBuyOrderListEntity groupBuyOrderListEntity : groupBuyOrderListEntities){
            groupBuyOrderRefundEntity.add(groupBuyOrderListEntity.getUserId(), groupBuyOrderListEntity.getOutTradeNo());

        }
        transactionTemplate.execute(status -> {
            try{
                int count = groupBuyOrderDao.cancelOrder(teamId);
                if(count != 1){
                    status.setRollbackOnly();
                    throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
                }
                count = groupBuyOrderListDao.cancel(teamId);
                if(count != 3){
                    status.setRollbackOnly();
                    throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
                }
                return 1;
            }catch (Exception e){
                status.setRollbackOnly();
                log.warn("拼团交易-取消团失败", e);
                throw e;
            }
        });

        eventPublisher.publish(GroupBuyOrderCancelEvent.topic, GroupBuyOrderCancelEvent.create(groupBuyOrderRefundEntity));
    }

    @Override
    public TeamStatisticVO queryTeamStatisticVO(String activityId) {
        Integer participateCount = groupBuyOrderDao.queryParticipateCountByAcitivityId(activityId);
        Integer completeGroupCount = groupBuyOrderDao.queryCompleteGroupCount(activityId);
        Integer inProgressGroupCount = groupBuyOrderDao.queryInProgressGroupCount(activityId);
        return new TeamStatisticVO(participateCount == null ? 0 : participateCount, completeGroupCount == null ? 0 : completeGroupCount, inProgressGroupCount == null ? 0 : inProgressGroupCount);
    }

    @Override
    public List<GroupBuyTeamsEntity> queryGroupBuyTeamsEntity(String activityId, String userId, int randomCount) {
        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();;
        groupBuyOrderListReq.setUserId(userId);
        groupBuyOrderListReq.setActivityId(activityId);
        groupBuyOrderListReq.setCount(2 * randomCount);

        List<GroupBuyOrderList> groupBuyOrderLists = groupBuyOrderListDao.queryGroupBuyTeamsEntity(groupBuyOrderListReq);
        if(groupBuyOrderLists == null || groupBuyOrderLists.isEmpty()) return null;
        if(groupBuyOrderLists.size() > randomCount){
            Collections.shuffle(groupBuyOrderLists);
            groupBuyOrderLists = groupBuyOrderLists.subList(0, randomCount);
        }

        Set<String> teamIds = groupBuyOrderLists.stream()
                .map(GroupBuyOrderList::getTeamId)
                .filter(teamId -> teamId != null && !teamId.isEmpty())
                .collect(Collectors.toSet());

        List<GroupBuyOrder> groupBuyOrders = groupBuyOrderDao.queryGroupBuyProgress(activityId);
        if(groupBuyOrders == null || groupBuyOrders.isEmpty()) return null;
        Map<String, GroupBuyOrder> groupBuyOrderMap = groupBuyOrders.stream()
                .collect(Collectors.toMap(GroupBuyOrder::getTeamId, groupBuyOrder -> groupBuyOrder));

        List<GroupBuyTeamsEntity> groupBuyTeamsEntities = new ArrayList<>();
        for(GroupBuyOrderList groupBuyOrderList : groupBuyOrderLists){
            GroupBuyTeamsEntity groupBuyTeamsEntity = new GroupBuyTeamsEntity();
            String teamId = groupBuyOrderList.getTeamId();
            GroupBuyOrder groupBuyOrder = groupBuyOrderMap.get(teamId);
            if(groupBuyOrder == null) continue;
            groupBuyTeamsEntity.setTeamId(groupBuyOrder.getTeamId());
            groupBuyTeamsEntity.setUserId(groupBuyOrderList.getUserId());
            groupBuyTeamsEntity.setStartTime(groupBuyOrder.getStartTime());
            groupBuyTeamsEntity.setEndTime(groupBuyOrder.getEndTime());
            groupBuyTeamsEntity.setValidTimeCountdown(GroupBuyTeamsEntity.differenceDateTime2Str(groupBuyOrder.getStartTime(), groupBuyOrder.getEndTime()));
            groupBuyTeamsEntity.setRemainCount(groupBuyOrder.getTargetCount() - groupBuyOrder.getLockCount());

            groupBuyTeamsEntities.add(groupBuyTeamsEntity);
        }
        return groupBuyTeamsEntities;
    }

    @Override
    public TeamStatisticVO queryGroupBuyMarketTeamStatistic(String activityId) {
        String cacheKey = Constants.Redis.GROUP_BUY_TEAM_STATISTIC.concat(activityId);
        Map<String, Integer> statisticGroup = redisService.getMap(cacheKey);
        if(!statisticGroup.isEmpty()){
            return TeamStatisticVO.builder()
                    .participateCount(statisticGroup.get(Constants.PARTICIPATE))
                    .inProgressGroupCount(statisticGroup.get(Constants.PROGRESS))
                    .completeGroupCount(statisticGroup.get(Constants.COMPLETE)).build();
        }
        Integer inProgressGroupCount = groupBuyOrderDao.queryInProgressGroupCount(activityId);
        Integer completeGroupCount = groupBuyOrderDao.queryCompleteGroupCount(activityId);
        Integer participateCount = groupBuyOrderDao.queryParticipateCountByAcitivityId(activityId);

        TeamStatisticVO teamStatisticVO = TeamStatisticVO.builder()
                .inProgressGroupCount(inProgressGroupCount == null ? 0 : inProgressGroupCount)
                .completeGroupCount(completeGroupCount == null ? 0 : completeGroupCount)
                .participateCount(participateCount == null ? 0 : participateCount).build();

        statisticGroup.put(Constants.PROGRESS, teamStatisticVO.getInProgressGroupCount());
        statisticGroup.put(Constants.COMPLETE, teamStatisticVO.getCompleteGroupCount());
        statisticGroup.put(Constants.PARTICIPATE, teamStatisticVO.getParticipateCount());
        return teamStatisticVO;
    }

    @Override
    public List<MarketTeamsDetailEntity> queryMarketTeamsDetail(String activityId) {
        String cacheKey = Constants.Redis.GROUP_BUY_TEAM_DETAIL.concat(activityId);
        Map<String, MarketTeamsDetailEntity> marketTeamsDetailEntityMap = redisService.getMap(cacheKey);
        if(!marketTeamsDetailEntityMap.isEmpty()){
            List<MarketTeamsDetailEntity> marketTeamsDetailEntities = new ArrayList<>(marketTeamsDetailEntityMap.values());
            if(marketTeamsDetailEntities.size() > 10){
                Collections.shuffle(marketTeamsDetailEntities);
                return marketTeamsDetailEntities.subList(0, 10);
            }
            return marketTeamsDetailEntities;
        }


        List<GroupBuyOrder> groupBuyOrders = groupBuyOrderDao.queryGroupBuyProgress(activityId);
        if(groupBuyOrders == null || groupBuyOrders.isEmpty()) return null;
        List<String> teamIds = groupBuyOrders.stream()
                .map(GroupBuyOrder::getTeamId)
                .collect(Collectors.toList());
        List<GroupBuyOrderList> groupBuyOrderLists = groupBuyOrderListDao.queryGroupBuyListByTeamIdsLimit1(teamIds);

        Map<String, GroupBuyOrderList> groupBuyOrderListMap = groupBuyOrderLists.stream()
                .collect(Collectors.toMap(GroupBuyOrderList::getTeamId, groupBuyOrderList -> groupBuyOrderList, (existing, replacement) -> existing));
        List<MarketTeamsDetailEntity> marketTeamsDetailEntities = new ArrayList<>();
        for(GroupBuyOrder groupBuyOrder : groupBuyOrders){
            String teamId = groupBuyOrder.getTeamId();
            GroupBuyOrderList groupBuyOrderList = groupBuyOrderListMap.get(teamId);
            MarketTeamsDetailEntity marketTeamsDetailEntity = new MarketTeamsDetailEntity();
            marketTeamsDetailEntity.setTeamId(teamId);
            marketTeamsDetailEntity.setUserId(groupBuyOrderList.getUserId());
            marketTeamsDetailEntity.setRemainCount(groupBuyOrder.getTargetCount() - groupBuyOrder.getLockCount());
            marketTeamsDetailEntity.setStartTime(groupBuyOrder.getStartTime());
            marketTeamsDetailEntity.setEndTime(groupBuyOrder.getEndTime());
            marketTeamsDetailEntities.add(marketTeamsDetailEntity);
        }
        String lockKey = Constants.Redis.GROUP_BUY_TEAM_DETAIL_LOCK.concat(activityId);
        RLock lock = redisService.getLock(lockKey);
        try{
            boolean isLock = lock.tryLock(3, TimeUnit.SECONDS);
            if(!isLock) return marketTeamsDetailEntities;
            for(GroupBuyOrder groupBuyOrder : groupBuyOrders){
                String teamId = groupBuyOrder.getTeamId();
                GroupBuyOrderList groupBuyOrderList = groupBuyOrderListMap.get(teamId);

                MarketTeamsDetailEntity marketTeamsDetailEntity = new MarketTeamsDetailEntity();
                marketTeamsDetailEntity.setTeamId(teamId);
                marketTeamsDetailEntity.setUserId(groupBuyOrderList.getUserId());
                marketTeamsDetailEntity.setRemainCount(groupBuyOrder.getTargetCount() - groupBuyOrder.getLockCount());
                marketTeamsDetailEntity.setStartTime(groupBuyOrder.getStartTime());
                marketTeamsDetailEntity.setEndTime(groupBuyOrder.getEndTime());

                marketTeamsDetailEntityMap.put(teamId, marketTeamsDetailEntity);
            }
        }catch (Exception e){
            throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
        }finally {
            lock.unlock();
        }
        return marketTeamsDetailEntities;
    }

    @Override
    public void settlementRefund(String outTradeNo) {
        eventPublisher.publish(SettlementRefundEvent.topic, SettlementRefundEvent.create(outTradeNo));
    }
}
