package cn.ant0n.gbm.test;

import cn.ant0n.gbm.domain.trade.event.GroupBuyOrderDelayEvent;
import cn.ant0n.gbm.domain.trade.model.entity.MarketTeamsDetailEntity;
import cn.ant0n.gbm.domain.trade.model.valobj.TeamStatisticVO;
import cn.ant0n.gbm.domain.trade.repository.ITradeRepository;
import cn.ant0n.gbm.domain.trade.service.index.IMarketIndexService;
import cn.ant0n.gbm.domain.trade.service.settlement.ITradeSettlementOrderService;
import cn.ant0n.gbm.infrastructure.adapter.event.EventPublisher;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private ITradeRepository tradeRepository;
    @Resource
    private ITradeSettlementOrderService tradeSettlementOrderService;
    @Resource
    private IMarketIndexService marketIndexService;

    @Test
    public void test_01() throws InterruptedException {
        eventPublisher.publishDelivery(GroupBuyOrderDelayEvent.topic, GroupBuyOrderDelayEvent.create("46168757"), 5);
        new CountDownLatch(1).await();
    }

    @Test
    public void test_02()  {
        List<MarketTeamsDetailEntity> marketTeamsDetailEntities = marketIndexService.queryMarketTeamsDetail("100301");
        log.info("结果为:{}", JSON.toJSONString(marketTeamsDetailEntities));
    }

    @Test
    public void test_03()  {
        TeamStatisticVO teamStatisticVO = marketIndexService.queryGroupBuyMarketTeamStatistic("100301");
        log.info("结果为:{}", JSON.toJSONString(teamStatisticVO));
    }


}
