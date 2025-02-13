package cn.ant0n.gbm.domain.activity.service.trial.task;

import cn.ant0n.gbm.domain.activity.model.valobj.ProductSkuVO;
import cn.ant0n.gbm.domain.activity.repository.IActivityRepository;

import javax.annotation.Resource;
import java.util.concurrent.Callable;

public class QueryProductSkuVOFromDBThreadTask implements Callable<ProductSkuVO> {

    private final IActivityRepository activityRepository;
    private final String productId;

    public QueryProductSkuVOFromDBThreadTask(IActivityRepository activityRepository, String productId) {
        this.activityRepository = activityRepository;
        this.productId = productId;
    }

    @Override
    public ProductSkuVO call() throws Exception {
        return activityRepository.queryProductByProductId(productId);
    }
}
