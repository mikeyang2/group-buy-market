package cn.ant0n.gbm.domain.tag.service;

public interface ITagService {

    /**
     * 执行人群标签采集任务
     * @param tagId
     * @param batchId
     */
    void execTagBatchJob(String tagId, String batchId);
}
