package dev.sagar.examtimer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import dev.sagar.examtimer.R;
import dev.sagar.examtimer.utils.CountUpTimer;

import java.util.HashMap;

public class MyGridAdapter extends BaseAdapter{
    private Context context;
    private int qCount = 0;

    private HashMap<Integer, CountUpTimer> timerMap = new HashMap<>();
    private HashMap<Integer, View> viewMap = new HashMap<>()
;
    public MyGridAdapter(Context context, int count){
        this.context = context;
        this.qCount = count;
    }


    @Override
    public int getCount() {
        return this.qCount;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup viewGroup) {
        Log.i("My Tag", "Inflating: "+position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = viewMap.get(position);
        if(view==null) {
            view = inflater.inflate(R.layout.inner_grid, null);
            viewMap.put(position, view);
        }

        TextView tvQno = view.findViewById(R.id.tv_q_number);
        tvQno.setText(""+(position+1));

        final TextView tvTime = view.findViewById(R.id.tv_time);

        CountUpTimer timer = timerMap.get(position);
        if(timer == null) {
            timer = new CountUpTimer(context, tvTime);
            view.setOnClickListener(new TimerOnClickListerner(timer));
            timerMap.put(position, timer);
        }

        view.setSelected(false);
        return view;
    }

    class TimerOnClickListerner implements View.OnClickListener {
        private CountUpTimer timer;
        private TimerOnClickListerner(CountUpTimer timer){
            this.timer = timer;
        }

        @Override
        public void onClick(View view) {
            Log.i("My Tag", "Clicked");
            if (timer.isRunning) {
                timer.pause();
            } else {
                timer.start();
            }
        }
    }
}
