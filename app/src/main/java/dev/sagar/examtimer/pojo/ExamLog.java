package dev.sagar.examtimer.pojo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class ExamLog {
    private String id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Duration activeTime;
    private int questionsAttempted;
    private List<QuestionLog> questionLogList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Duration getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Duration activeTime) {
        this.activeTime = activeTime;
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

    public static class QuestionLog{
        private int index;
        private Duration duration;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public Duration getDuration() {
            return duration;
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }
    }

    public static class Factory{
        private Factory(){}
        public static final int DUMMY = 0;

        public static ExamLog getInstance(int type){
            ExamLog examLog = null;
            if(type == DUMMY ){
                examLog = new ExamLog();
                examLog.setQuestionLogList(Collections.emptyList());
                examLog.setActiveTime(Duration.ofSeconds(0));
                examLog.setStartDateTime(LocalDateTime.now());
                examLog.setEndDateTime(LocalDateTime.now());
                examLog.setQuestionsAttempted(0);
            }
            return examLog;
        }
    }
}
