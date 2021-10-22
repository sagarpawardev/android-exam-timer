package dev.sagar.examtimer.history;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.sagar.examtimer.pojo.ExamLog;

public class ExamLogService {
    private static ExamLogService instance = null;

    private ExamLogService(){}

    public static ExamLogService getInstance() {
        if (instance == null) {
            synchronized(ExamLogService.class) {
                instance = new MockExamLogService(); //TODO Actual Implementation
            }
        }
        return instance;
    }

    public List<ExamLog> getLogList(){
        return Collections.emptyList(); //TODO implementation here
    }

    private static class MockExamLogService extends ExamLogService{
        @Override
        public List<ExamLog> getLogList() {
            List<ExamLog> examLogs = new ArrayList<>(10);
            for(int i=0; i<10; i++){
                ExamLog log = new ExamLog();
                log.setStartDateTime(LocalDateTime.now());
                log.setEndDateTime(LocalDateTime.now().plusHours(1));
                log.setTimeTaken(Duration.ofHours(i).plusMinutes(i).plusSeconds(i*10));
                log.setQuestionsAttempted(100-i);
                examLogs.add(log);
            }

            return examLogs;
        }
    }
}
