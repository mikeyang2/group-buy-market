package cn.ant0n.gbm.infrastructure.adapter.repository;

import cn.ant0n.gbm.domain.activity.model.valobj.*;
import cn.ant0n.gbm.domain.activity.repository.IActivityRepository;
import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyActivityEntity;
import cn.ant0n.gbm.infrastructure.dao.IGroupBuyActivityDao;
import cn.ant0n.gbm.infrastructure.dao.IGroupBuyDiscountDao;
import cn.ant0n.gbm.infrastructure.dao.IProductDao;
import cn.ant0n.gbm.infrastructure.dao.IScPADao;
import cn.ant0n.gbm.infrastructure.dao.po.GroupBuyActivity;
import cn.ant0n.gbm.infrastructure.dao.po.GroupBuyDiscount;
import cn.ant0n.gbm.infrastructure.dao.po.Product;
import cn.ant0n.gbm.infrastructure.dao.po.ScPA;
import cn.ant0n.gbm.infrastructure.redis.IRedisService;
import cn.ant0n.gbm.types.common.Constants;
import org.redisson.api.RBitSet;
import org.redisson.api.RTopic;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class ActivityRepository implements IActivityRepository {

    @Resource
    private IProductDao productDao;
    @Resource
    private IScPADao scPADao;
    @Resource
    private IGroupBuyActivityDao groupBuyActivityDao;
    @Resource
    private IGroupBuyDiscountDao groupBuyDiscountDao;
    @Resource
    private IRedisService redisService;
    @Resource(name = "dccValueTopic")
    private RTopic topic;

    @Override
    public ProductSkuVO queryProductByProductId(String productId) {
        Product productRes = productDao.queryProductByProductId(productId);
        return ProductSkuVO.builder()
                .productId(productId)
                .price(productRes.getPrice())
                .productName(productRes.getProductName()).build();
    }

    @Override
    public GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(String productId, String source, String channel) {
        String cacheKey = Constants.Redis.GROUP_BUY_ACTIVITY.concat(productId).concat(":").concat(source).concat(":").concat(channel);
        GroupBuyActivity groupBuyActivityRes = redisService.getValue(cacheKey);
        if (groupBuyActivityRes == null) {
            ScPA scPAReq = ScPA.builder()
                    .productId(productId)
                    .source(source)
                    .channel(channel).build();
            String activityId = scPADao.queryActivityId(scPAReq);
            groupBuyActivityRes = groupBuyActivityDao.queryGroupBuyActivityByActivityId(activityId);
            redisService.setValue(cacheKey, groupBuyActivityRes);
        }

        GroupBuyDiscount groupBuyDiscountRes =  groupBuyDiscountDao.queryGroupBuyDiscountByDiscountId(groupBuyActivityRes.getDiscountId());
        GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount = GroupBuyActivityDiscountVO.GroupBuyDiscount.builder()
                .discountName(groupBuyDiscountRes.getDiscountName())
                .discountDesc(groupBuyDiscountRes.getDiscountDesc())
                .discountType(DiscountTypeEnum.create(groupBuyDiscountRes.getDiscountType()))
                .discountPlan(groupBuyDiscountRes.getDiscountPlan())
                .discountExpr(groupBuyDiscountRes.getDiscountExpr())
                .tagId(groupBuyDiscountRes.getTagId()).build();


        return GroupBuyActivityDiscountVO.builder()
                .activityId(groupBuyActivityRes.getActivityId())
                .activityName(groupBuyActivityRes.getActivityName())
                .source(source)
                .channel(channel)
                .productId(productId)
                .groupBuyDiscount(groupBuyDiscount)
                .groupType(GroupTypeEnum.create(groupBuyActivityRes.getGroupType()))
                .takeLimitCount(groupBuyActivityRes.getTakeLimitCount())
                .target(groupBuyActivityRes.getTarget())
                .validTime(groupBuyActivityRes.getValidTime())
                .status(ActivityStatusEnum.create(groupBuyActivityRes.getStatus()))
                .startTime(groupBuyActivityRes.getStartTime())
                .endTime(groupBuyActivityRes.getEndTime())
                .tagId(groupBuyActivityRes.getTagId())
                .tagScope(groupBuyActivityRes.getTagScope()).build();
    }

    @Override
    public boolean queryIdCrowdRange(String userId, String tagId) {
        String cacheKey = Constants.Redis.TAGS.concat(tagId);
        RBitSet bitSet = redisService.getBitSet(cacheKey);
        return bitSet.get(redisService.getIndexFromUserId(userId));
    }

    @Override
    public void downgrade(String path, String changeValue) {
        String message = path.concat(Constants.SPLIT).concat(changeValue);
        topic.publish(message);
    }

    @Override
    public String queryActivityIdFromScP(String productId, String source, String channel) {
        String cacheKey = Constants.Redis.GROUP_BUY_ACTIVITY.concat(productId).concat(":").concat(source).concat(":").concat(channel);
        GroupBuyActivity groupBuyActivity = redisService.getValue(cacheKey);
        if (groupBuyActivity != null) {
            return groupBuyActivity.getActivityId();
        }
        ScPA scPAReq = ScPA.builder()
                .productId(productId)
                .source(source)
                .channel(channel).build();
        String activityId = scPADao.queryActivityId(scPAReq);;
        groupBuyActivity = groupBuyActivityDao.queryGroupBuyActivityByActivityId(activityId);
        redisService.setValue(cacheKey, groupBuyActivity);
        return activityId;
    }

    @Override
    public GroupBuyActivityEntity queryGroupBuyActivityEntity(String productId, String source, String channel) {
        String cacheKey = Constants.Redis.GROUP_BUY_ACTIVITY.concat(productId).concat(":").concat(source).concat(":").concat(channel);
        GroupBuyActivity groupBuyActivity = redisService.getValue(cacheKey);
        if (groupBuyActivity != null) {
            return GroupBuyActivityEntity.builder()
                    .activityId(groupBuyActivity.getActivityId())
                    .startTime(groupBuyActivity.getStartTime())
                    .endTime(groupBuyActivity.getEndTime())
                    .validTime(groupBuyActivity.getValidTime())
                    .takeLimitCount(groupBuyActivity.getTakeLimitCount())
                    .status(cn.ant0n.gbm.domain.trade.model.valobj.ActivityStatusEnum.create(groupBuyActivity.getStatus())).build();
        }
        ScPA scPAReq = ScPA.builder()
                .productId(productId)
                .source(source)
                .channel(channel).build();
        String activityId = scPADao.queryActivityId(scPAReq);;
        groupBuyActivity = groupBuyActivityDao.queryGroupBuyActivityByActivityId(activityId);
        redisService.setValue(cacheKey, groupBuyActivity);
        return GroupBuyActivityEntity.builder()
                .activityId(groupBuyActivity.getActivityId())
                .startTime(groupBuyActivity.getStartTime())
                .endTime(groupBuyActivity.getEndTime())
                .validTime(groupBuyActivity.getValidTime())
                .takeLimitCount(groupBuyActivity.getTakeLimitCount())
                .status(cn.ant0n.gbm.domain.trade.model.valobj.ActivityStatusEnum.create(groupBuyActivity.getStatus())).build();
    }



}
