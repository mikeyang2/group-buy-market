package cn.ant0n.gbm.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductMarketResponseDTO implements Serializable {

    private List<Team> groupBuyTeams;
    private TeamStatistic teamStatistic;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Team implements Serializable {
        private String userId;
        private Integer remainCount;
        private String teamId;
        private Date startTime;
        private Date endTime;
        private String validTimeCountdown;

        public static String differenceDateTime2Str(Date nowTime, Date validEndTime) {
            if (nowTime == null || validEndTime == null) {
                return "无效的时间";
            }

            long diffInMilliseconds = validEndTime.getTime() - nowTime.getTime();

            if (diffInMilliseconds < 0) {
                return "已结束";
            }

            long seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMilliseconds) % 60;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMilliseconds) % 60;
            long hours = TimeUnit.MILLISECONDS.toHours(diffInMilliseconds) % 24;
            long days = TimeUnit.MILLISECONDS.toDays(diffInMilliseconds);

            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TeamStatistic implements Serializable{
        private BigDecimal deductionPrice;
        private BigDecimal originalPrice;
        private BigDecimal payPrice;
        private Integer participateCount;
        private Integer completeGroupCount;
        private Integer inProgressGroupCount;
    }
}
