package dev.sagar.examtimer.strategy;

import android.app.Activity;

import java.time.LocalDate;

import dev.sagar.examtimer.R;
import dev.sagar.examtimer.service.ExamLogService;
import dev.sagar.examtimer.utils.SharedPrefUtil;

public interface ReviewStrategy {

    default boolean show(Activity activity){
        int CUTOFF_DAYS = 5, MAX_ASK_COUNT = 3;
        long examCount = ExamLogService.getInstance(activity).getCount();
        SharedPrefUtil prefUtil = SharedPrefUtil.newInstance(activity);
        LocalDate lastAskedDate = prefUtil.getDate(R.string.pref_review_last_ask_date);
        Integer askCount = prefUtil.getInt(R.string.pref_review_ask_count);
        LocalDate cutOffDate = LocalDate.now().minusDays(CUTOFF_DAYS);

        return (examCount>1 && lastAskedDate.isBefore(cutOffDate) && askCount<MAX_ASK_COUNT);

    }
}
