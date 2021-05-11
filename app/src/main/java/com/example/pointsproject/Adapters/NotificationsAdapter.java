package com.example.pointsproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pointsproject.Model.NotificationModel;
import com.example.pointsproject.R;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NotificationModel> notificationModelArrayList;
    private ArrayList<NotificationModel> notificationModelArrayListFiltered;
    private Context context;

    public static final int FADE_DURATION = 1000;

    public NotificationsAdapter(Context context) {
        this.context = context;
        notificationModelArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.history_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.txt.setText(notificationModelArrayList.get(position).getNotificationNot());
        myViewHolder.date.setText(notificationModelArrayList.get(position).getDateNot());
        setScaleAnimation(myViewHolder.itemView);
    }

    @Override
    public int getItemCount() {
        return notificationModelArrayList.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView txt;
        private TextView date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.info);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }

    public void addAll(ArrayList<NotificationModel> notificationModels){
        for (NotificationModel notificationModel : notificationModels){
            notificationModelArrayList.add(notificationModel);
        }
        notificationModelArrayListFiltered = new ArrayList<>(notificationModels);
    }

    public void clearAll(ArrayList<NotificationModel> notificationModels){
        notificationModelArrayList.clear();
        addAll(notificationModels);
        notifyDataSetChanged();
    }

    private void setScaleAnimation(View v){
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(FADE_DURATION);
        v.startAnimation(scaleAnimation);
    }


}
