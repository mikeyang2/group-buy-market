package cn.ant0n.gbm.test.infrastructure;

import cn.ant0n.gbm.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.ant0n.gbm.domain.activity.model.valobj.ProductSkuVO;
import cn.ant0n.gbm.domain.activity.repository.IActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityRepositoryTest {

    @Resource
    private IActivityRepository activityRepository;

    @Test
    public void test_01(){
        ProductSkuVO productSkuVO = activityRepository.queryProductByProductId("1001");
        log.info("ProductSkuVO:{}", productSkuVO);
    }

    @Test
    public void test_02(){
        GroupBuyActivityDiscountVO groupBuyActivityDiscountVO = activityRepository.queryGroupBuyActivityDiscountVO("1001", "s01", "c01");
        log.info("GroupBuyActivityDiscountVO:{}", groupBuyActivityDiscountVO);
    }
}
