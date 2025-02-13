package cn.ant0n.gbm.domain.trade.service.index;

import cn.ant0n.gbm.domain.trade.model.entity.MarketTeamsDetailEntity;
import cn.ant0n.gbm.domain.trade.model.valobj.TeamStatisticVO;
import cn.ant0n.gbm.domain.trade.repository.ITradeRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class MarketIndexService implements IMarketIndexService {

    @Resource
    private ITradeRepository tradeRepository;

    @Override
    public TeamStatisticVO queryGroupBuyMarketTeamStatistic(String activityId) {
        return tradeRepository.queryGroupBuyMarketTeamStatistic(activityId);
    }

    @Override
    public List<MarketTeamsDetailEntity> queryMarketTeamsDetail(String activityId) {
        List<MarketTeamsDetailEntity> marketTeamsDetailEntities = tradeRepository.queryMarketTeamsDetail(activityId);
        Date nowDate = new Date();
        for(MarketTeamsDetailEntity marketTeamsDetailEntity : marketTeamsDetailEntities){
            marketTeamsDetailEntity.setValidTimeCountdown(MarketTeamsDetailEntity.differenceDateTime2Str(nowDate, marketTeamsDetailEntity.getEndTime()));
        }
        return marketTeamsDetailEntities;
    }
}
