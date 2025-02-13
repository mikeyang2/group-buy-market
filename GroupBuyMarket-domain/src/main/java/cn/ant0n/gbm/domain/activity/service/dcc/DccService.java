package cn.ant0n.gbm.domain.activity.service.dcc;

import cn.ant0n.gbm.domain.activity.repository.IActivityRepository;
import cn.ant0n.gbm.types.annotations.DCCValue;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DccService implements IDccService {

    @Resource
    private IActivityRepository activityRepository;

    @Override
    public void downgrade(String path, String changeValue) {
       activityRepository.downgrade(path, changeValue);
    }
}
