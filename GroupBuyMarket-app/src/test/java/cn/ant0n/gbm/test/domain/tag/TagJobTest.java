package cn.ant0n.gbm.test.domain.tag;

import cn.ant0n.gbm.domain.tag.service.ITagService;
import cn.ant0n.gbm.infrastructure.redis.IRedisService;
import cn.ant0n.gbm.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBitSet;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TagJobTest {


    @Resource
    private ITagService tagService;
    @Resource
    private IRedisService redisService;


    @Test
    public void test_01(){
        tagService.execTagBatchJob("RQ_KJHKL98UU78H66554GFDV", "10001");
    }


    @Test
    public void test_02(){
        String cacheKey = Constants.Redis.TAGS.concat("RQ_KJHKL98UU78H66554GFDV");
        RBitSet bitSet = redisService.getBitSet(cacheKey);
        log.info("用户:{},在tagId:{}标签状态:{}", "yangxiaocan", "RQ_KJHKL98UU78H66554GFDV", bitSet.get(redisService.getIndexFromUserId("yangxiaocan")));
    }
}
