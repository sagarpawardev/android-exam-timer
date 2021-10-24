package dev.sagar.examtimer.history;

import static dev.sagar.examtimer.Constants.PROP_MOCK_SERVICES;

import android.app.Activity;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import dev.sagar.examtimer.ExamTimerApplication;
import dev.sagar.examtimer.db.entity.ExamLogEntity;
import dev.sagar.examtimer.db.entity.ExamLogEntityDao;
import dev.sagar.examtimer.db.entity.QuestionLogEntity;
import dev.sagar.examtimer.db.entity.QuestionLogEntityDao;
import dev.sagar.examtimer.history.adapter.ExamLogEntityAdapter;
import dev.sagar.examtimer.history.adapter.QuestionLogEntityAdapter;
import dev.sagar.examtimer.pojo.ExamLog;

public class ExamLogService {
    private final Activity activity;
    private ExamLogService(Activity activity){
        this.activity = activity;
    }

    public static ExamLogService getInstance(Activity activity) {
        ExamLogService instance;
        Properties properties = new Properties();
        try {
            InputStream inputStream = activity.getBaseContext().getAssets().open("config.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean mockService = Boolean.parseBoolean(properties.getProperty(PROP_MOCK_SERVICES, "false"));
        if (mockService) {
            instance = new MockExamLogService(activity);
        } else {
            instance = new ExamLogService(activity);
        }

        return instance;
    }

    public List<ExamLog> getLogList(){
        ExamTimerApplication application = (ExamTimerApplication) this.activity.getApplication();
        ExamLogEntityDao examDao = application.getDaoSession().getExamLogEntityDao();
        List<ExamLogEntity> examLogEntities = examDao.loadAll();
        return ExamLogEntityAdapter.getInstance().getAll(examLogEntities);
    }

    public ExamLog getExamLog(@NonNull String id) {
        if(StringUtils.isBlank(id)){
            throw new IllegalArgumentException("id is required for this operation: "+id);
        }

        ExamTimerApplication application = (ExamTimerApplication) this.activity.getApplication();
        ExamLogEntityDao examDao = application.getDaoSession().getExamLogEntityDao();
        ExamLogEntity entity = examDao.load(Long.parseLong(id));
        return ExamLogEntityAdapter.getInstance().get(entity);
    }

    public ExamLog createExamLog(ExamLog examLog){
        // Save Exam
        ExamLogEntity examEntity = ExamLogEntityAdapter.getInstance().getEntity(examLog);
        ExamTimerApplication application = (ExamTimerApplication) this.activity.getApplication();
        ExamLogEntityDao examDao = application.getDaoSession().getExamLogEntityDao();
        long examId = examDao.insert(examEntity);
        examEntity.setId(examId);
        examLog.setId(String.valueOf(examId));

        // Save Question
        QuestionLogEntityDao questionDao = application.getDaoSession().getQuestionLogEntityDao();
        List<QuestionLogEntity> qLogEntities = QuestionLogEntityAdapter.getInstance().getEntities(examLog, examEntity);
        questionDao.insertInTx(qLogEntities);
        return examLog;
    }

    private static class MockExamLogService extends ExamLogService{
        private MockExamLogService(Activity activity) {
            super(activity);
        }

        @Override
        public List<ExamLog> getLogList() {
            List<ExamLog> examLogs = new ArrayList<>(10);
            for(int i=0; i<10; i++){
                ExamLog log = new ExamLog();
                log.setStartDateTime(LocalDateTime.now());
                log.setEndDateTime(LocalDateTime.now().plusHours(1));
                log.setActiveTime(Duration.ofHours(i).plusMinutes(i).plusSeconds(i*10));
                log.setQuestionsAttempted(100-i);
                examLogs.add(log);
            }

            return examLogs;
        }

        @Override
        public ExamLog getExamLog(@NonNull String id) {
            int totalQuestions = 40;

            ExamLog log = new ExamLog();
            log.setStartDateTime(LocalDateTime.now());
            log.setEndDateTime(LocalDateTime.now().plusHours(5));
            log.setQuestionsAttempted(totalQuestions);
            log.setId(id);
            log.setActiveTime(Duration.between(log.getStartDateTime(), log.getEndDateTime()));
            List<ExamLog.QuestionLog> questions = new ArrayList<>();
            Random random = new Random();
            for(int i=0; i<totalQuestions; i++){
                ExamLog.QuestionLog questionLog = new ExamLog.QuestionLog();
                questionLog.setDuration(Duration.ofSeconds(Math.abs(random.nextInt(500))));
                questionLog.setIndex(i);
                questions.add(questionLog);
            }
            log.setQuestionLogList(questions);

            return log;
        }

        @Override
        public ExamLog createExamLog(ExamLog examLog) {
            examLog.setId("123");
            return examLog;
        }
    }
}
