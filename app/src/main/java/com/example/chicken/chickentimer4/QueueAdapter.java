package com.example.chicken.chickentimer4;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.ViewHolder> {
    List<FF> mFF;

    public QueueAdapter(List<FF> data) {
        mFF = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.queue_row, viewGroup, false);  // card_layout 만들었으니깐 이제 ViewHolder 생성자에서 card_layout의 위젯들을 연결시켜주자
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull QueueAdapter.ViewHolder viewHolder, int i) {
        viewHolder.f_name.setText(mFF.get(i).getName());
    }


    @Override
    public int getItemCount() {
        return mFF.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView f_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            f_name = itemView.findViewById(R.id.Fname);
        }
    }
}
