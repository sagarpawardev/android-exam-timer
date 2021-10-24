package dev.sagar.examtimer.history.adapter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public interface BaseServiceAdapter {
    default Long getLongDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        return localDateTime.toEpochSecond(ZoneOffset.UTC);
    }

    default Duration getDuration(Long timeInMillis){
        if(timeInMillis==null){
            return Duration.ZERO;
        }

        return Duration.ofMillis(timeInMillis);
    }

    default LocalDateTime getLocalDateTime(Long timeInMillis){
        if(timeInMillis==null){
            return null;
        }

        return LocalDateTime.ofEpochSecond(timeInMillis, 0, ZoneOffset.UTC);
    }
}
