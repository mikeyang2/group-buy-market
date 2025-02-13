package cn.ant0n.gbm.domain.trade.service.index;

import cn.ant0n.gbm.domain.trade.model.entity.MarketTeamsDetailEntity;
import cn.ant0n.gbm.domain.trade.model.valobj.TeamStatisticVO;

import java.util.List;

public interface IMarketIndexService {

    TeamStatisticVO queryGroupBuyMarketTeamStatistic(String activityId);

    List<MarketTeamsDetailEntity> queryMarketTeamsDetail(String activityId);
}
