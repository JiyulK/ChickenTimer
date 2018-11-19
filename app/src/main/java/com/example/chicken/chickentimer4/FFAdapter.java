package com.example.chicken.chickentimer4;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    //  ArrayList<Timer> timer;

    public FFAdapter(Context context, int resource, List<FF> objects) {
        super(context, resource, objects);
        this.context = context;
        this.mFF = objects;

        //  timer = new ArrayList<>();
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row, null);
        }
        final FF ff = mFF.get(position);
        // 알람음 초기화
        initAlarm(position);

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
                // 알람 종료
                stopAlarm(position);

                //   registedList.get(position).setActive(false);
                registedList.remove(position);
                arrayAdapter.notifyDataSetChanged();
                for (int i = position; i < registedList.size(); i++) {
                    int count = 0;
                    boolean chk = registedList.get(i).isActive;
                    if (chk) {
                        Toast.makeText(getContext(), "ACTIVE", Toast.LENGTH_SHORT).show();
                        Log.i(" ACTIVE", "ACTIVE");
                        for (int j = 0; j < registedList.get(i).getTime().size(); j++) {
                            if (registedList.get(i).getTime().get(j) == registedList.get(i).getTime_p().get(j)) {
                                count++;
                            }
                            final int finalJ = j;
                            final int finalI = i;
                            ((MainActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBarArrayList[finalJ].setProgress(100 / registedList.get(finalI).getTime().get(finalJ) * registedList.get(finalI).getTime_p().get(finalJ));
                                }
                            });
                        }
                        final int finalI1 = i;
                        final int finalCount = count;
                        TimerTask tt = new TimerTask() {
                            @Override
                            public void run() {
                                timerStart(finalI1, finalCount, progressBarArrayList);
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(tt, 0);
                    } else {
                        Toast.makeText(getContext(), "NOT ACTIVE", Toast.LENGTH_SHORT).show();
                        Log.i("POSITION", "position" + position);
                        Log.i("NOT ACTIVE", "NOT");
                        for (int j = 0; j < registedList.get(i).getTime().size(); j++) {
                            final int finalJ = j;
                            final int finalI = i;
                            ((MainActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBarArrayList[finalJ].setProgress(100 / registedList.get(finalI).getTime().get(finalJ) * registedList.get(finalI).getTime_p().get(finalJ));
                                }
                            });
                            //progressBarArrayList[j].setProgress(100 / mFF.get(i).getTime().get(j) * mFF.get(i).getTime_p().get(j));
                        }
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }
        });
        stbtn = v.findViewById(R.id.startBtn);
        stbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 알람 종료
                stopAlarm(position);


                //Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
//                Log.i("POSITION VALUE", String.valueOf(position) );
                //Toast.makeText(getContext(), "POSITION : "+ position, Toast.LENGTH_SHORT).show();
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
                                //timerStart(mFF.get(position) , count[0], progressBarArrayList[count[0]]);
                                timerStart(position, count[0], progressBarArrayList);
                                chkTimer[0] = false;
                                count[0]++;
                            }
                        }
                        //mTime.setProgress(100 / ff.getTime().get(0) * ff.getTime_p().get(0));
                        //fTime.setProgress(100 / ff.getTime().get(1) * ff.getTime_p().get(1));
                    }
                };

//                timer.add(new Timer());
//                timer.get(timer.size() - 1).schedule(tt, 0, 1000 * num);
                Timer timer = new Timer();
                timer.schedule(tt, 0);
            }
        });
        return v;
    }

    private void timerStart(final int position, final int index, final ProgressBar[] time) {
        //Toast.makeText(getContext(), "POSITION : "+ position, Toast.LENGTH_SHORT).show();
        //변수로 받아와. 이름을
        String name;
        name = registedList.get(position).getName();//옛날 이름

        // 알람 설정
//        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.alertsound);
//        mediaPlayer.setLooping(true);

        Log.i("POSITION", "position" + position);
        while (true) {
            try {
                Thread.sleep(1000);
                registedList.get(position).setActive(true);
                //이 포지션에 해당하는 이름이랑, 내가 옛날에 받아온 이름이랑 같은지 매번 체크
                String now;
                now = registedList.get(position).getName();
                Log.i("menuname1", name + " " + position);
                Log.i("menuname2", now + " " + position);
                if (name == now) {
                    registedList.get(position).getTime_p().set(index, registedList.get(position).getTime_p().get(index) + 1);
                    ((MainActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Log.i("left", String.valueOf(registedList.get(position).getTime().get(index)));
                            //Log.i("right", String.valueOf(registedList.get(position).getTime_p().get(index)));
                            time[index].setProgress(100 / registedList.get(position).getTime().get(index) * registedList.get(position).getTime_p().get(index));
                            Log.i("checktimer", String.valueOf(time[index].getProgress() + "번호" + index));
                        }
                    });
                } else {//같지 않다면, 이것은 삭제된것이다. 그니깐 이 행동을 종료해야돼
                    Log.i("POSITION DIE", "DIE");

                    // 알람 종료
                    stopAlarm(position);
                    break;
                }
                boolean chk = registedList.get(position).isActive;
                if (chk) {
                    Log.i("TimerTaskRun", "OK");
                } else {
                    Log.i("TimerTaskRun", "OMG");
                    break;
                }
                if (registedList.get(position).getTime().get(index) == registedList.get(position).getTime_p().get(index)) {
                    Log.i("BREAK POINT", "HERE");
                    registedList.get(position).setActive(false);

                    // 알람 시작
                    startAlarm(position);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initAlarm(int position) {
        if (registedList.get(position).mediaPlayer == null) {
            registedList.get(position).mediaPlayer = MediaPlayer.create(getContext(), R.raw.alertsound);
            registedList.get(position).mediaPlayer.setLooping(true); // 반복재생 설정
        }
    }

    private void stopAlarm(int position) {
        if (registedList.get(position).mediaPlayer.isPlaying()) {
            registedList.get(position).mediaPlayer.pause();
        }
    }

    private void startAlarm(int position) {
        if (!registedList.get(position).mediaPlayer.isPlaying()) {
            registedList.get(position).mediaPlayer.start();
        }
    }

}