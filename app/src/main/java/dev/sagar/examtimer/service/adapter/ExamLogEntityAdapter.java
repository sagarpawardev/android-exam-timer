package dev.sagar.examtimer.service.adapter;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import dev.sagar.examtimer.db.entity.ExamLogEntity;
import dev.sagar.examtimer.db.entity.QuestionLogEntity;
import dev.sagar.examtimer.pojo.ExamLog;

public class ExamLogEntityAdapter implements BaseServiceAdapter{

    private static volatile ExamLogEntityAdapter instance;

    public static ExamLogEntityAdapter getInstance() {
        if (instance == null) {
            synchronized (ExamLogEntityAdapter.class) {
                if (instance == null) {
                    instance = new ExamLogEntityAdapter();
                }
            }
        }
        return instance;
    }

    public ExamLogEntity getEntity(ExamLog examLog) {
        ExamLogEntity entity = new ExamLogEntity();

        if (StringUtils.isNotBlank(examLog.getId())) {
            entity.setId(Long.parseLong(examLog.getId()));
        }

        entity.setStartTime(getLongDateTime(examLog.getStartDateTime()));
        entity.setEndTime(getLongDateTime(examLog.getEndDateTime()));
        entity.setQuestionAttempted(examLog.getQuestionsAttempted());
        entity.setActiveTime(examLog.getActiveTime().toMillis());
        return entity;
    }

    public ExamLog get(ExamLogEntity entity){
        ExamLog examLog = new ExamLog();
        examLog.setId(String.valueOf(entity.getId()));
        examLog.setStartDateTime(getLocalDateTime(entity.getStartTime()));
        examLog.setEndDateTime(getLocalDateTime(entity.getEndTime()));
        examLog.setQuestionsAttempted(entity.getQuestionAttempted());
        examLog.setActiveTime(getDuration(entity.getActiveTime()));

        List<QuestionLogEntity> questionLogEntities = entity.getQuestionLogEntities();
        List<ExamLog.QuestionLog> questionLogs = QuestionLogEntityAdapter.getInstance().getAll(questionLogEntities);
        examLog.setQuestionLogList(questionLogs);
        return examLog;
    }

    public List<ExamLog> getAll(List<ExamLogEntity> entities){
        if(entities == null)
            return Collections.emptyList();

        return entities.stream()
                .map(this::get)
                .collect(Collectors.toList());
    }
}
