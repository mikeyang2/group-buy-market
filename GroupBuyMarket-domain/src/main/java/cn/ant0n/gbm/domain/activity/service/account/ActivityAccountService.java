package cn.ant0n.gbm.domain.activity.service.account;

import cn.ant0n.gbm.domain.activity.repository.IActivityRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ActivityAccountService implements IActivityAccountService{

    @Resource
    private IActivityRepository activityRepository;

    @Override
    public String queryActivityIdFromScP(String productId, String source, String channel) {
        return activityRepository.queryActivityIdFromScP(productId, source, channel);
    }
}
