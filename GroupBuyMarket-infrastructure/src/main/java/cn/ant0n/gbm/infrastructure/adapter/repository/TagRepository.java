package cn.ant0n.gbm.infrastructure.adapter.repository;

import cn.ant0n.gbm.domain.tag.model.entity.CrowdTagsJobEntity;
import cn.ant0n.gbm.domain.tag.model.valobj.CrowdTagsJobStatusEnum;
import cn.ant0n.gbm.domain.tag.model.valobj.TagsTypeEnum;
import cn.ant0n.gbm.domain.tag.repository.ITagRepository;
import cn.ant0n.gbm.infrastructure.dao.ICrowdTagsDao;
import cn.ant0n.gbm.infrastructure.dao.ICrowdTagsDetailDao;
import cn.ant0n.gbm.infrastructure.dao.ICrowdTagsJobDao;
import cn.ant0n.gbm.infrastructure.dao.po.CrowdTags;
import cn.ant0n.gbm.infrastructure.dao.po.CrowdTagsDetail;
import cn.ant0n.gbm.infrastructure.dao.po.CrowdTagsJob;
import cn.ant0n.gbm.infrastructure.redis.IRedisService;
import cn.ant0n.gbm.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBitSet;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class TagRepository implements ITagRepository {


    @Resource
    private ICrowdTagsJobDao crowdTagsJobDao;
    @Resource
    private ICrowdTagsDetailDao crowdTagsDetailDao;
    @Resource
    private ICrowdTagsDao crowdTagsDao;
    @Resource
    private IRedisService redisService;



    @Override
    public CrowdTagsJobEntity queryCrowdTagsJobEntity(String tagId, String batchId) {
        CrowdTagsJob crowdTagsJobReq = CrowdTagsJob.builder()
                .tagId(tagId)
                .batchId(batchId).build();

        CrowdTagsJob crowdTagsJobRes = crowdTagsJobDao.queryCrowdTagsJob(crowdTagsJobReq);
        if(null == crowdTagsJobRes) return null;
        return CrowdTagsJobEntity.builder()
                .tagId(tagId)
                .batchId(batchId)
                .tagType(TagsTypeEnum.create(crowdTagsJobRes.getTagType()))
                .tagRule(crowdTagsJobRes.getTagRule())
                .startTime(crowdTagsJobRes.getStartTime())
                .endTime(crowdTagsJobRes.getEndTime())
                .status(CrowdTagsJobStatusEnum.create(crowdTagsJobRes.getStatus())).build();
    }

    @Override
    public int insertCrowdTagsDetails(List<String> userIds, String tagId) {
        int count = 0;
        for (String userId : userIds) {
            CrowdTagsDetail crowdTagsDetail = new CrowdTagsDetail();
            crowdTagsDetail.setTagId(tagId);
            crowdTagsDetail.setUserId(userId);
            try{
                crowdTagsDetailDao.insertCrowdTagsDetail(crowdTagsDetail);
                count++;
                String cacheKey = Constants.Redis.TAGS.concat(tagId);
                RBitSet bitSet = redisService.getBitSet(cacheKey);
                bitSet.set(redisService.getIndexFromUserId(userId), true);
            }catch (Exception e){
                log.warn("更新人群标签信息异常", e);
            }
        }
        return count;
    }

    @Override
    public void updateCrowdTagsStatistics(String tagId, int size) {
        CrowdTags crowdTagsReq = new CrowdTags();
        crowdTagsReq.setTagId(tagId);
        crowdTagsReq.setStatistics(size);
        crowdTagsDao.updateCrowdTagsStatistics(crowdTagsReq);
    }
}
