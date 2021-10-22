package dev.sagar.examtimer.pojo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class ExamLog {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Duration timeTaken;
    private int questionsAttempted;
    private List<QuestionLog> questionLogList;

    public Duration getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Duration timeTaken) {
        this.timeTaken = timeTaken;
    }

    public int getQuestionsAttempted() {
        return questionsAttempted;
    }

    public void setQuestionsAttempted(int questionsAttempted) {
        this.questionsAttempted = questionsAttempted;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public List<QuestionLog> getQuestionLogList() {
        return questionLogList;
    }

    public void setQuestionLogList(List<QuestionLog> questionLogList) {
        this.questionLogList = questionLogList;
    }

    static class QuestionLog{
        private int index;
        private int timeTaken;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getTimeTaken() {
            return timeTaken;
        }

        public void setTimeTaken(int timeTaken) {
            this.timeTaken = timeTaken;
        }
    }
}
