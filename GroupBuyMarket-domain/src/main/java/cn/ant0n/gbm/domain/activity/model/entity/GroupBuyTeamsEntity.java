package cn.ant0n.gbm.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyTeamsEntity {
    private String userId;
    private Integer remainCount;
    private String teamId;
    private Date startTime;
    private Date endTime;
    private String validTimeCountdown;

    public static String differenceDateTime2Str(Date validStartTime, Date validEndTime) {
        if (validStartTime == null || validEndTime == null) {
            return "无效的时间";
        }

        long diffInMilliseconds = validEndTime.getTime() - validStartTime.getTime();

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
