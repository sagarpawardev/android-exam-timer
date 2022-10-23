package dev.sagar.examtimer.converter;

import java.util.ArrayList;
import java.util.List;

import dev.sagar.examtimer.dto.QuestionStateDto;
import dev.sagar.examtimer.utils.CountUpTimer;

public class TimerQuestionStateConverter {
    public static List<QuestionStateDto> toQuestionState(CountUpTimer[] timers){
        List<QuestionStateDto> questionStateList = new ArrayList<>(timers.length);
        for(CountUpTimer timer: timers){
            QuestionStateDto state = new QuestionStateDto();
            long timeSpent = timer.getSpentTime();
            state.setTimeSpent(timeSpent);
            state.setRunning(timer.isRunning);
            state.setVisited(timeSpent != 0);
            questionStateList.add(state);
        }

        return questionStateList;
    }
}
