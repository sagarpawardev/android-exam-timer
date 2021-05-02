package dev.sagar.examtimer.utils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HtmlContentUtil {

    public String createTimerTable(List<CountUpTimer> timerList){

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        StringBuilder builder = new StringBuilder();
        for(int qNo=1; qNo<=timerList.size(); qNo++){
            CountUpTimer timer = timerList.get(qNo-1);
            String rowTxt = String.format(
                    "Question %s:\t\t%s\n",
                    qNo, format.format(timer.getSpentTime())
            );
            builder.append(rowTxt);
        }

        return builder.toString();
    }
}
