package dev.sagar.examtimer.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "exam_log")
public class ExamLogEntity {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "start_time")
    private Long startTime;

    @Property(nameInDb = "end_time")
    private Long endTime;

    @Property(nameInDb = "questions_attempted")
    private int questionAttempted;

    @Property(nameInDb = "active_time")
    private Long activeTime;

    @ToMany(referencedJoinProperty = "examLogId")
    @OrderBy("index ASC")
    private List<QuestionLogEntity> questionLogEntities;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 86256232)
    private transient ExamLogEntityDao myDao;

    @Generated(hash = 733858350)
    public ExamLogEntity(Long id, Long startTime, Long endTime, int questionAttempted,
            Long activeTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.questionAttempted = questionAttempted;
        this.activeTime = activeTime;
    }

    @Generated(hash = 985324999)
    public ExamLogEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public int getQuestionAttempted() {
        return this.questionAttempted;
    }

    public void setQuestionAttempted(int questionAttempted) {
        this.questionAttempted = questionAttempted;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1139965189)
    public List<QuestionLogEntity> getQuestionLogEntities() {
        if (questionLogEntities == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            QuestionLogEntityDao targetDao = daoSession.getQuestionLogEntityDao();
            List<QuestionLogEntity> questionLogEntitiesNew = targetDao
                    ._queryExamLogEntity_QuestionLogEntities(id);
            synchronized (this) {
                if (questionLogEntities == null) {
                    questionLogEntities = questionLogEntitiesNew;
                }
            }
        }
        return questionLogEntities;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1301353504)
    public synchronized void resetQuestionLogEntities() {
        questionLogEntities = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public Long getActiveTime() {
        return this.activeTime;
    }

    public void setActiveTime(Long activeTime) {
        this.activeTime = activeTime;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1376183889)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getExamLogEntityDao() : null;
    }



}
