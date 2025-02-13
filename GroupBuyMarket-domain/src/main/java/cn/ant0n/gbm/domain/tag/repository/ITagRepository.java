package cn.ant0n.gbm.domain.tag.repository;

import cn.ant0n.gbm.domain.tag.model.entity.CrowdTagsJobEntity;

import java.util.List;

public interface ITagRepository {
    CrowdTagsJobEntity queryCrowdTagsJobEntity(String tagId, String batchId);

    int insertCrowdTagsDetails(List<String> userIds, String tagId);

    void updateCrowdTagsStatistics(String tagId, int size);
}
