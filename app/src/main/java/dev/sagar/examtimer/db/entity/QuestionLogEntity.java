package dev.sagar.examtimer.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "question_log")
public class QuestionLogEntity {

    @Property(nameInDb = "duration")
    private Long duration;

    @Property(nameInDb = "index")
    private Integer index;

    private Long examLogId;

    @ToOne(joinProperty = "examLogId")
    private ExamLogEntity examLogEntity;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 159984055)
    private transient QuestionLogEntityDao myDao;

    @Generated(hash = 1634940247)
    private transient Long examLogEntity__resolvedKey;

    @Generated(hash = 838141104)
    public QuestionLogEntity(Long duration, Integer index, Long examLogId) {
        this.duration = duration;
        this.index = index;
        this.examLogId = examLogId;
    }

    @Generated(hash = 1739306633)
    public QuestionLogEntity() {
    }

    public Long getDuration() {
        return this.duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getIndex() {
        return this.index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Long getExamLogId() {
        return this.examLogId;
    }

    public void setExamLogId(Long examLogId) {
        this.examLogId = examLogId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 241420283)
    public ExamLogEntity getExamLogEntity() {
        Long __key = this.examLogId;
        if (examLogEntity__resolvedKey == null
                || !examLogEntity__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ExamLogEntityDao targetDao = daoSession.getExamLogEntityDao();
            ExamLogEntity examLogEntityNew = targetDao.load(__key);
            synchronized (this) {
                examLogEntity = examLogEntityNew;
                examLogEntity__resolvedKey = __key;
            }
        }
        return examLogEntity;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1284615524)
    public void setExamLogEntity(ExamLogEntity examLogEntity) {
        synchronized (this) {
            this.examLogEntity = examLogEntity;
            examLogId = examLogEntity == null ? null : examLogEntity.getId();
            examLogEntity__resolvedKey = examLogId;
        }
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1758379448)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getQuestionLogEntityDao() : null;
    }
}
