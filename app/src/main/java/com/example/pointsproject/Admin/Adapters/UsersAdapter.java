package com.example.pointsproject.Admin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pointsproject.Admin.Interfaces.AdminInterfaces;
import com.example.pointsproject.Model.Users;
import com.example.pointsproject.R;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Users> usersArrayList;
    private AdminInterfaces adminInterfaces;

    public UsersAdapter(Context context) {
        this.context = context;
        usersArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.admin_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.name.setText(usersArrayList.get(position).getFirstName());
        myViewHolder.email.setText(usersArrayList.get(position).getEmail());
        if (usersArrayList.get(position).getReferredBy() == null){
            myViewHolder.mark.setImageResource(R.drawable.ic_baseline_dashboard_24);
        }
        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminInterfaces.goToDetails(usersArrayList.get(position));
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
        return usersArrayList.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView email;
        private ImageView mark;
        private CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameAdmin);
            email = (TextView) itemView.findViewById(R.id.emailAdmin);
            mark = (ImageView) itemView.findViewById(R.id.mark);
            cardView = (CardView) itemView.findViewById(R.id.cardUsers);
        }
    }

    public void adAll(ArrayList<Users> users){
        for (Users users1 : users){
            usersArrayList.add(users1);
            notifyDataSetChanged();
        }
    }
}
