package dev.sagar.examtimer.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class ExamStateDto implements Serializable {
    private List<QuestionStateDto> questionStateList;
    private LocalDateTime startTime = LocalDateTime.now();

    public List<QuestionStateDto> getQuestionStateList(){
        return this.questionStateList;
    }

    public void setQuestionStateList(List<QuestionStateDto> questionStateList) {
        this.questionStateList = questionStateList;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
