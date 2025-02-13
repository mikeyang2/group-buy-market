package cn.ant0n.gbm.test.infrastructure.tag;

import cn.ant0n.gbm.infrastructure.dao.ICrowdTagsDetailDao;
import cn.ant0n.gbm.infrastructure.dao.po.CrowdTagsDetail;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TagRepositoryTest {

    @Resource
    private ICrowdTagsDetailDao crowdTagsDetailDao;

}
