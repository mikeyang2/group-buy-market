package cn.ant0n.gbm.test.infrastructure.trade;


import cn.ant0n.gbm.domain.trade.model.entity.GroupBuyOrderListEntity;
import cn.ant0n.gbm.domain.trade.repository.ITradeRepository;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeRepositoryTest {
    @Resource
    private ITradeRepository tradeRepository;


    @Test
    public void test_queryList(){
        GroupBuyOrderListEntity groupBuyOrderListEntity = tradeRepository.queryLockedGroupBuyOrderList("yangxiaocan", "1450");
        log.info("结果为:{}", JSON.toJSONString(groupBuyOrderListEntity));
    }
}
