package com.example.chicken.chickentimer4;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.chicken.chickentimer4.MainActivity.arrayAdapter;
import static com.example.chicken.chickentimer4.MainActivity.registedList;

public class FFAdapter extends ArrayAdapter<FF> {
    List<FF> mFF;
    Context context;
    Button stbtn;
    Button stopbtn;
    int num = 1;

    public FFAdapter(Context context, int resource, List<FF> objects) {
        super(context, resource, objects);
        this.context = context;
        this.mFF = objects;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row, null);
        }
        final FF ff = mFF.get(position);

        final ProgressBar mTime;
        final ProgressBar fTime;

        mTime = v.findViewById(R.id.Mtime);
        fTime = v.findViewById(R.id.Ftime);
        mTime.setMax(100);
        fTime.setMax(100);

        final ProgressBar[] progressBarArrayList = new ProgressBar[]{mTime, fTime};
        Log.i("test1", position + " " + mFF.get(position).getName() + "");
        Log.i("test1", position + " " + ff.getName() + "");

        if (ff != null) {
            TextView fName = v.findViewById(R.id.Fname);
            fName.setText(ff.getName().toString());
        }
        stopbtn = v.findViewById(R.id.stopBtn);
        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registedList.remove(position);
                arrayAdapter.notifyDataSetChanged();
            }
        });
        stbtn = v.findViewById(R.id.startBtn);
        stbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
                final boolean[] chkTimer = new boolean[1];
                chkTimer[0] = true;
                final int[] count = {0};
                TimerTask tt = new TimerTask() {
                    @Override
                    public void run() {
                        //Log.i("TimerTaskRun", "Run");
                        for (int i = 0; i < ff.getTime().size(); i++) {
                            if (ff.getTime().get(i) == ff.getTime_p().get(i)) {
                                count[0]++;
                            }
                        }
                        if (chkTimer[0]) {
                            Log.i("TIMERRUNFORFUNCTION", String.valueOf(count[0]));
                            if (count[0] < ff.getTime().size()) {
                                timerStart(ff, count[0], progressBarArrayList[count[0]]);
                                chkTimer[0] = false;
                                count[0]++;
                            }
                        }
                        //mTime.setProgress(100 / ff.getTime().get(0) * ff.getTime_p().get(0));
                        //fTime.setProgress(100 / ff.getTime().get(1) * ff.getTime_p().get(1));
                    }
                };
                Timer timer = new Timer();
                timer.schedule(tt, 0, 1000 * num);
            }
        });
        return v;
    }

    private void timerStart(final FF ff, final int index, final ProgressBar time) {
        while (true) {
            try {
                Thread.sleep(1000);
                Log.i("TimerTaskRun", "Run");
                ff.getTime_p().set(index, ff.getTime_p().get(index) + 1);
                ((MainActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("left", String.valueOf(ff.getTime().get(index)));
                        Log.i("right", String.valueOf(ff.getTime_p().get(index)));
                        time.setProgress(100 / ff.getTime().get(index) * ff.getTime_p().get(index));
                    }
                });
                if (ff.getTime().get(index) == ff.getTime_p().get(index)) {
                    Log.i("BREAK POINT", "HERE");
                    ff.setActive(false);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
