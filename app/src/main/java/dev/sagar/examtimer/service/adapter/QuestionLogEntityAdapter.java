package dev.sagar.examtimer.service.adapter;

import androidx.annotation.NonNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import dev.sagar.examtimer.db.entity.ExamLogEntity;
import dev.sagar.examtimer.db.entity.QuestionLogEntity;
import dev.sagar.examtimer.pojo.ExamLog;

public class QuestionLogEntityAdapter implements BaseServiceAdapter {

    private static volatile QuestionLogEntityAdapter instance;

    public static QuestionLogEntityAdapter getInstance() {
        if (instance == null) {
            synchronized (QuestionLogEntityAdapter.class) {
                if (instance == null) {
                    instance = new QuestionLogEntityAdapter();
                }
            }
        }
        return instance;
    }

    public List<QuestionLogEntity> getEntities(@NonNull ExamLog examLog, @NonNull ExamLogEntity examEntity) {
        List<QuestionLogEntity> qLogEntities = Collections.emptyList();
        if (examEntity.getId() == null) {
            throw new IllegalArgumentException("exam log id required to create question log entity");
        }

        if (examLog.getQuestionLogList() != null) {
            qLogEntities = new ArrayList<>(examLog.getQuestionLogList().size());
            for (ExamLog.QuestionLog qLog : examLog.getQuestionLogList()) {
                QuestionLogEntity qEntity = new QuestionLogEntity();
                Duration duration = qLog.getDuration();
                qEntity.setDuration(duration.toMillis());
                qEntity.setIndex(qLog.getIndex());
                qEntity.setExamLogId(examEntity.getId());
                qEntity.setExamLogEntity(examEntity);
                qLogEntities.add(qEntity);
            }
        }
        return qLogEntities;
    }

    public ExamLog.QuestionLog get(QuestionLogEntity entity) {
        ExamLog.QuestionLog qLog = new ExamLog.QuestionLog();
        qLog.setIndex(entity.getIndex());
        qLog.setDuration(getDuration(entity.getDuration()));
        return qLog;
    }

    public List<ExamLog.QuestionLog> getAll(List<QuestionLogEntity> entities) {
        if (entities == null)
            return Collections.emptyList();

        return entities.stream()
                .map(this::get)
                .collect(Collectors.toList());
    }
}
