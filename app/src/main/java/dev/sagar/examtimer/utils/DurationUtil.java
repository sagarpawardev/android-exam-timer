package dev.sagar.examtimer.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.Duration;

public class DurationUtil {
    private DurationUtil(){}

    public static String getFormattedString(Duration duration){
        if(duration == null)
            return StringUtils.EMPTY;

        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        StringBuilder builder = new StringBuilder();
        if(hours > 0){
            builder.append(hours).append(" hr ");
        }

        if(minutes > 0){
            builder.append(minutes).append(" min ");
        }

        if(seconds > 0){
            builder.append(seconds).append(" sec ");
        }

        if(hours==0 && minutes==0 && seconds==0){
            builder.append("0 sec");
        }

        return builder.toString();
    }
}
