package com.example.chicken.chickentimer4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    static int MAX; // 등록가능한 최대 타이머 개수

    Spinner spinner1; // 편의점을 선택하는 스피너
    Spinner spinner2; // ff 제품을 선택하는 스피너
    HashMap<String, HashMap<String, ArrayList<Integer>>> c; // ff 제품에 대한 정보를 저장하고 있는 변수


    static ListView listView; // 타이머를 표시해줄 리스트뷰
    // 타이머를 표시해줄 리스트뷰
    static ArrayList<FF> registedList; // 현재 등록된 타이머 정보들
    ArrayList<FF> queue;
    String conName; // 타이머 등록시 FF클래스 객체 생성자에 넘겨줄 편의점 이름 변수
    String FFName; // 타이머 등록시 FF클래스 객체 생성자에 넘겨줄 FF 제품 이름 변수
    ArrayList<Integer> times; // 타이머 등록시 FF클래스 객체 생성자에 넘겨줄 n가지 시간 변수
    static FFAdapter arrayAdapter; // 타이머 등록시 FF클래스 객체 생성자에 넘겨줄 listView의 어댑터
    QueueAdapter queueadapter; // 대기큐 어뎁터
    LinearLayoutManager layoutManager; // RecyclerView 설정을 수평으로 만들어 주기 위함
    RecyclerView recyclerView; // 대기큐를 출력하는데 사용하는 RecyclerView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data_init();
        view_init();
        setMax(); // 타이머 최대치 설정
    }

    private void setMax() {
        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("조리기 개수 입력");
        builder.setView(edittext);
        builder.setPositiveButton("확정",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "타이머 최대 개수 : " + edittext.getText().toString(), Toast.LENGTH_LONG).show();
                        MAX = Integer.parseInt(edittext.getText().toString());
                    }
                });
        builder.show();
    }

    private void data_init() {

        c = new HashMap<>();
        c.put("미니스톱", new HashMap<String, ArrayList<Integer>>());
        c.put("세븐일레븐", new HashMap<String, ArrayList<Integer>>());
        c.put("GS25", new HashMap<String, ArrayList<Integer>>());
        c.put("CU", new HashMap<String, ArrayList<Integer>>());

        c.get("미니스톱").put("mf0", new ArrayList<Integer>(Arrays.asList(3, 7)));
        c.get("미니스톱").put("mf1", new ArrayList<Integer>(Arrays.asList(3, 7)));
        c.get("미니스톱").put("mf2", new ArrayList<Integer>(Arrays.asList(3, 7)));
        c.get("미니스톱").put("mf3", new ArrayList<Integer>(Arrays.asList(3, 7)));

        c.get("세븐일레븐").put("sf0", new ArrayList<Integer>(Arrays.asList(3, 7)));
        c.get("세븐일레븐").put("sf1", new ArrayList<Integer>(Arrays.asList(3, 7)));
        c.get("세븐일레븐").put("sf2", new ArrayList<Integer>(Arrays.asList(3, 7)));
        c.get("세븐일레븐").put("sf3", new ArrayList<Integer>(Arrays.asList(3, 7)));

        c.get("GS25").put("gf0", new ArrayList<Integer>(Arrays.asList(3, 7)));
        c.get("GS25").put("gf1", new ArrayList<Integer>(Arrays.asList(3, 7)));
        c.get("GS25").put("gf2", new ArrayList<Integer>(Arrays.asList(3, 7)));
        c.get("GS25").put("gf3", new ArrayList<Integer>(Arrays.asList(3, 7)));

        c.get("CU").put("cf0", new ArrayList<Integer>(Arrays.asList(3, 7)));
        c.get("CU").put("cf1", new ArrayList<Integer>(Arrays.asList(3, 7)));
        c.get("CU").put("cf2", new ArrayList<Integer>(Arrays.asList(3, 7)));
        c.get("CU").put("cf3", new ArrayList<Integer>(Arrays.asList(3, 7)));


        //Log.i("test", c.get("미니스톱").keySet().toArray().length + "");
    }

    private void view_init() {
        // 레이아웃 연결해주기
        times = new ArrayList<>(); // 전자레인지, 튀김기 시간
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        listView = findViewById(R.id.ListView1);
        registedList = new ArrayList<FF>();
        arrayAdapter = new FFAdapter(this, R.layout.row, registedList);
        listView.setAdapter(arrayAdapter);

        // 대기큐 설정
        queue = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        queueadapter = new QueueAdapter(queue);
        recyclerView.setAdapter(queueadapter);

        // 스피너 연결해주기
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, c.keySet().toArray());
        spinner1.setAdapter(arrayAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                conName = spinner1.getItemAtPosition(position).toString(); // 선택됨 편의점 이름
                ArrayAdapter arrayAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, c.get(conName).keySet().toArray());
                spinner2.setAdapter(arrayAdapter2);
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        FFName = spinner2.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        FFName = spinner2.getItemAtPosition(0).toString();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ArrayAdapter arrayAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, c.get(spinner1.getItemAtPosition(0).toString()).keySet().toArray());
                spinner2.setAdapter(arrayAdapter2);
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        FFName = spinner2.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        FFName = spinner2.getItemAtPosition(0).toString();
                    }
                });

            }
        });

    }

    public void onButton1Clicked(View view) {
//        times[0] = c.get(conName).get(FFName)[0];
//        times[1] = c.get(conName).get(FFName)[1];
        times = c.get(conName).get(FFName);
        ArrayList<Integer> time_p = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            time_p.add(0);
        }

        Log.i("test", FFName + "");
        FF ff = new FF(FFName, times, time_p, false);

        if (isMax(registedList, MAX)) { // 타이머가 가득 등록됐다면
            registOnQueue(ff); // 대기큐에 등록하기
            Toast.makeText(getApplicationContext(), "최대 타이머 개수 : " + MAX, Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkOverlap(registedList, ff)) { // 중복 된다면
            registOnQueue(ff); // 대기큐에 등록하기
            Toast.makeText(getApplicationContext(), "중복된 타이머 : ", Toast.LENGTH_SHORT).show();
            return;
        }

        registOnRegistedList(ff); // 타이머로 등록
    }

    public void onQueueBtnClicked(View view) {
        while (true) {
            Log.i("queuesize", queue.size() + "");
            if (queue.size() > 0 && !isMax(registedList, MAX)) { // 대기큐에 한개라도 들어있고 타이머 최대치가 아니면

                if (checkOverlap(registedList, queue.get(0))) { // 중복된다면
                    queue.remove(0);
                } else { // 중복이 안된다면
                    registOnRegistedList(queue.get(0)); // 타이머 등록
                    queue.remove(0);
                    // queueadapter = new QueueAdapter(queue);
                    queueadapter.notifyDataSetChanged();
                    return;
                }
            } else {
                queueadapter.notifyDataSetChanged();
                return;
            }
        }
    }

    private boolean isMax(ArrayList<FF> list, int max) { // 최대 타이머 개수 확인
        if (list.size() == max) return true;
        else return false;
    }

    private boolean checkOverlap(ArrayList<FF> list, FF ff) { // 중복 확인 함수
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName() == ff.getName()) { // 중복됨
                return true;
            }
        }
        return false; // 중복 안됨
    }

    private void registOnRegistedList(FF ff) { // 타이머로 등록
        registedList.add(ff);
        // arrayAdapter = new FFAdapter(this, R.layout.row, registedList);
        arrayAdapter.notifyDataSetChanged();
    }

    private void registOnQueue(FF ff) { // 데이큐에 등록
        queue.add(ff);
        // queueadapter = new QueueAdapter(queue);
        queueadapter.notifyDataSetChanged();
    }


}
