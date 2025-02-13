package cn.ant0n.gbm.domain.tag.service;

import cn.ant0n.gbm.domain.tag.model.entity.CrowdTagsJobEntity;
import cn.ant0n.gbm.domain.tag.repository.ITagRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagService implements ITagService {

    @Resource
    private ITagRepository tagRepository;


    @Override
    public void execTagBatchJob(String tagId, String batchId) {

        CrowdTagsJobEntity crowdTagsJobEntity = tagRepository.queryCrowdTagsJobEntity(tagId, batchId);
        if(null == crowdTagsJobEntity) return;

        // 模拟该tagId下人群数据采集
        List<String> userIds = new ArrayList<>();
        userIds.add("test1");
        userIds.add("yangxiaocan");

        /**
         * 1. 更新DB统计数量
         * 2. redis bitMap写入数据 tagId->userId
         */
        int count = tagRepository.insertCrowdTagsDetails(userIds, tagId);
        tagRepository.updateCrowdTagsStatistics(tagId, count);
    }
}
