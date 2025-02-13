package cn.ant0n.gbm.test.domain.activity;

import cn.ant0n.gbm.domain.activity.model.entity.MarketProductEntity;
import cn.ant0n.gbm.domain.activity.model.entity.TrialBalanceEntity;
import cn.ant0n.gbm.domain.activity.service.IIndexGroupBuyMarketService;
import cn.ant0n.gbm.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RTopic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ActivityTest {

    @Resource
    private DefaultActivityStrategyFactory activityStrategyFactory;
    @Resource
    private RTopic topic;
    @Resource
    private IIndexGroupBuyMarketService iIndexGroupBuyMarketService;


    @Test
    public void test_rule_tree() throws ExecutionException, InterruptedException, TimeoutException {
        TrialBalanceEntity trialBalanceEntity = iIndexGroupBuyMarketService.indexMarketTrial(new MarketProductEntity("yangxiaocan", "1001", "s01", "c01"));
    }

    @Test
    public void test_topic() throws InterruptedException {
        topic.publish("cutRange,3");
        new CountDownLatch(1).await();
    }
}
