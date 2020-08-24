package dev.sagar.examtimer.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

public class TimeAsynctask extends AsyncTask<Void, Long, Long>{

    private Context context;
    private TextView view;
    boolean isPaused = false;

    public TimeAsynctask(Context context, TextView view){
        this.context = context;
        this.view  = view;
    }

    public void stopTimer(){
        isPaused = true;
    }



    @Override
    protected Long doInBackground(Void... voids) {
        return 0l;
    }

    @Override
    protected void onPostExecute(Long aLong) {
        super.onPostExecute(aLong);
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
    }

    public interface Callback{
        void setTime();
    }
}
