package com.example.pointsproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pointsproject.Model.Transactions;
import com.example.pointsproject.R;

import java.util.ArrayList;

public class TransactionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Transactions> transactions;
    private ArrayList<Transactions> transactionsArrayListFiltered;

    public TransactionsAdapter(Context context) {
        this.context = context;
        transactions = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.transaction_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.message.setText(transactions.get(position).getMessage());
        myViewHolder.date.setText(transactions.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<Transactions> transactionsFiltered = new ArrayList<>();
                if (charSequence == null || charSequence.length() == 0){
                    transactionsFiltered.addAll(transactionsArrayListFiltered);
                }
                else {
                    String value = charSequence.toString().toLowerCase().trim();
                    for (Transactions transactions1 : transactionsArrayListFiltered){
                        if (transactions1.getMessage().toString().toLowerCase().contains(value)){
                            transactionsFiltered.add(transactions1);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = transactionsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                transactions.clear();
                transactions.addAll((ArrayList) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView message;
        private TextView date;
        private CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.trans);
            date = (TextView) itemView.findViewById(R.id.dateTrans);
            cardView = (CardView) itemView.findViewById(R.id.cardTrans);
        }
    }

    public void addAll(ArrayList<Transactions> transactionsArrayList){
        for (Transactions transactions1 : transactionsArrayList){
            transactions.add(transactions1);
            notifyDataSetChanged();
        }
        transactionsArrayListFiltered = new ArrayList<>(transactionsArrayList);
        notifyDataSetChanged();
    }

}
