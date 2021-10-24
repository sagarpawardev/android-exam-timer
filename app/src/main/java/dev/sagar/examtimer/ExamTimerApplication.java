package dev.sagar.examtimer;

import android.app.Application;

import dev.sagar.examtimer.db.entity.DaoMaster;
import dev.sagar.examtimer.db.entity.DaoSession;

public class ExamTimerApplication extends Application {
    private DaoSession daoSession;
    public static final String EXAM_TIMER_DB = "exam_timer.db";

    @Override
    public void onCreate() {
        super.onCreate();
        daoSession = new DaoMaster(
                new DaoMaster.DevOpenHelper(this, EXAM_TIMER_DB).getWritableDb()
        ).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
