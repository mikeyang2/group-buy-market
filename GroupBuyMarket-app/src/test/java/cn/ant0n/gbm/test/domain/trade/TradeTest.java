package cn.ant0n.gbm.test.domain.trade;

import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderEntity;
import cn.ant0n.gbm.domain.trade.model.entity.LogicFilterResultEntity;
import cn.ant0n.gbm.domain.trade.model.entity.TradeFactorEntity;
import cn.ant0n.gbm.domain.trade.model.entity.TradeSuccessEntity;
import cn.ant0n.gbm.domain.trade.service.ITradeService;
import cn.ant0n.gbm.domain.trade.service.rule.factory.TradeLogicFilterChainFactory;
import cn.ant0n.gbm.domain.trade.service.settlement.ITradeSettlementOrderService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeTest {

    @Resource
    private ITradeService tradeService;
    @Resource
    private TradeLogicFilterChainFactory factory;
    @Resource
    private ITradeSettlementOrderService tradeSettlementOrderService;


    @Test
    public void test_01(){
        Map<String, GroupBuyOrderEntity> groupBuyOrderGroup = tradeService.queryExistGroupBuyOrderList("100123");
        log.info("结果为:{}", JSON.toJSONString(groupBuyOrderGroup));
    }

    @Test
    public void test_02() throws ExecutionException, InterruptedException, TimeoutException {
        TradeFactorEntity tradeFactorEntity = new TradeFactorEntity();
        tradeFactorEntity.setUserId("yangxiaocan");
        tradeFactorEntity.setTeamId("31401338");
        tradeFactorEntity.setProductId("1001");
        tradeFactorEntity.setSource("s01");
        tradeFactorEntity.setChannel("c01");
        LogicFilterResultEntity logicFilterResultEntity = factory.filter(tradeFactorEntity);
    }

    @Test
    public void test_03() throws ExecutionException, InterruptedException, TimeoutException {
        TradeSuccessEntity tradeSuccessEntity = new TradeSuccessEntity();
        tradeSuccessEntity.setUserId("y3");
        tradeSuccessEntity.setOutTradeNo("o3");
        tradeSuccessEntity.setOutTradeTime(new Date());

        tradeSettlementOrderService.settlementMarketOrder(tradeSuccessEntity);
    }
}
