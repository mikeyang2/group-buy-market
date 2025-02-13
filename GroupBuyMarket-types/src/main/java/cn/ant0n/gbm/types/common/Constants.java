package cn.ant0n.gbm.types.common;

public class Constants {

    public final static String SPLIT = ",";
    public final static String MIN_DEDUCTION_PRICE = "0.01";
    public final static String DISABLE = "2";
    public final static String NOT_VISIBLE = "1";
    public final static String PARTICIPATE = "participate";
    public final static String COMPLETE = "complete";
    public final static String PROGRESS = "progress";


    public static class Redis{
        public final static String TAGS = "gbm:tags:";
        public final static String GROUP_BUY_ORDER = "gbm:group_buy_order:";
        public final static String GROUP_BUY_ACTIVITY = "gbm:group_buy_activity:";
        public final static String GROUP_BUY_ORDER_TEAM = "gbm:group_buy_order:team:";
        public final static String GROUP_BUY_ORDER_IN_PROGRESS_GROUP_NUM = "gbm:group_buy_order:in_progress_group_num:";
        public final static String GROUP_BUY_ORDER_COMPLETE_GROUP_NUM = "gbm:group_buy_order:complete_group_num:";
        public final static String GROUP_BUY_ORDER_PARTICIPATE_NUM = "gbm:group_buy_order:participate_num:";
        public final static String GROUP_BUY_TEAM_STATISTIC = "gbm:group_buy_team_statistic:";
        public final static String GROUP_BUY_TEAM_DETAIL = "gbm:group_buy_team_detail:";
        public final static String GROUP_BUY_TEAM_DETAIL_LOCK = "gbm:group_buy_team_detail_lock:";
    }
}
