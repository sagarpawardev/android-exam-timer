package dev.sagar.examtimer.utils;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.time.LocalDate;

public class SharedPrefUtil {
    private final Activity activity;
    private final SharedPreferences sharedPref;

    private SharedPrefUtil(Activity activity, SharedPreferences sharedPref) {
        this.activity = activity;
        this.sharedPref = sharedPref;
    }

    @NonNull
    public String get(@StringRes int key){
        return sharedPref.getString(activity.getString(key), EMPTY);
    }

    @NonNull
    public Integer getInt(@StringRes int key){
        return sharedPref.getInt(activity.getString(key), 0);
    }

    @NonNull
    public LocalDate getDate(@StringRes int key){
        long localEpochDay = LocalDate.MIN.toEpochDay();
        long epochDay= sharedPref.getLong(activity.getString(key), localEpochDay);
        return LocalDate.ofEpochDay(epochDay);
    }

    public void putDate(@StringRes int key, @NonNull LocalDate value){
        SharedPreferences.Editor editor = sharedPref.edit();
        long epochDay = value.toEpochDay();
        editor.putLong(activity.getString(key), epochDay);
        editor.apply();
    }

    public void putInt(@StringRes int key, @NonNull Integer value){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(activity.getString(key), value);
        editor.apply();
    }

    @NonNull
    public static SharedPrefUtil newInstance(Activity activity){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return new SharedPrefUtil(activity, sharedPref);
    }
}
