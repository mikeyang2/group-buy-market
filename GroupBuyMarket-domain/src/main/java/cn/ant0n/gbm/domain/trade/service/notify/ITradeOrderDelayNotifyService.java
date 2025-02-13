package cn.ant0n.gbm.domain.trade.service.notify;

public interface ITradeOrderDelayNotifyService {

    boolean isOrderCompleted(String teamId);


    void cancelGroupBuyOrder(String teamId);
}
