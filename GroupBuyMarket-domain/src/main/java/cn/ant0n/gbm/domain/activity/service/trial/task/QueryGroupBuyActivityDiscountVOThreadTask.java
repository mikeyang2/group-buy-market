package cn.ant0n.gbm.domain.activity.service.trial.task;

import cn.ant0n.gbm.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.ant0n.gbm.domain.activity.repository.IActivityRepository;

import java.util.concurrent.Callable;

public class QueryGroupBuyActivityDiscountVOThreadTask implements Callable<GroupBuyActivityDiscountVO> {

    private final IActivityRepository activityRepository;
    private final String productId;
    private final String source;
    private final String channel;

    public QueryGroupBuyActivityDiscountVOThreadTask(IActivityRepository activityRepository, String productId, String source, String channel) {
        this.activityRepository = activityRepository;
        this.productId = productId;
        this.source = source;
        this.channel = channel;
    }

    @Override
    public GroupBuyActivityDiscountVO call() throws Exception {
        return activityRepository.queryGroupBuyActivityDiscountVO(productId, source, channel);
    }
}
