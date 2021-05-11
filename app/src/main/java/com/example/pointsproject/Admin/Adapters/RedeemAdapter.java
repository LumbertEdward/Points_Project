package com.example.pointsproject.Admin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pointsproject.Admin.Interfaces.AdminInterfaces;
import com.example.pointsproject.Model.Redeem;
import com.example.pointsproject.R;

import java.util.ArrayList;

public class RedeemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Redeem> redeemArrayList;
    private Context context;
    private AdminInterfaces adminInterfaces;

    public RedeemAdapter(Context context) {
        this.context = context;
        redeemArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.redeem_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.email.setText(redeemArrayList.get(position).getEmail());
        myViewHolder.points.setText(redeemArrayList.get(position).getPoints() + "Points");
        myViewHolder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminInterfaces.sendPaymentDetails(redeemArrayList.get(position));
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        adminInterfaces = (AdminInterfaces) context;
    }

    @Override
    public int getItemCount() {
        return redeemArrayList.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView email;
        private TextView points;
        private Button pay;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            email = (TextView) itemView.findViewById(R.id.txtEmailRed);
            points = (TextView) itemView.findViewById(R.id.txtPointsRed);
            pay = (Button) itemView.findViewById(R.id.btnRed);
        }
    }

    public void addAll(ArrayList<Redeem> redeemArrayList1){
        for (Redeem redeem : redeemArrayList1){
            redeemArrayList.add(redeem);
            notifyDataSetChanged();
        }
    }
}
