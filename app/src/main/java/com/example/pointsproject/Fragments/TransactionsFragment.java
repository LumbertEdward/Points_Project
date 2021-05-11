package com.example.pointsproject.Fragments;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pointsproject.Adapters.NotificationsAdapter;
import com.example.pointsproject.Adapters.TransactionsAdapter;
import com.example.pointsproject.Constants.ConstantsClass;
import com.example.pointsproject.Interfaces.GeneralInterface;
import com.example.pointsproject.Interfaces.TransactionsInterface;
import com.example.pointsproject.Model.PreferencesHelper;
import com.example.pointsproject.Model.Transactions;
import com.example.pointsproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class TransactionsFragment extends Fragment {
    private ImageView back;
    private RecyclerView recyclerView;
    private TextView warn;
    private LinearLayoutManager linearLayoutManager;
    private TransactionsAdapter transactionsAdapter;
    private PreferencesHelper preferencesHelper;
    private TransactionsInterface transactionsInterface;
    private GeneralInterface generalInterface;

    private ArrayList<Transactions> transactionsArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesHelper = new PreferencesHelper(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);
        back = (ImageView) view.findViewById(R.id.backTrans);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerTrans);
        warn = (TextView) view.findViewById(R.id.txtError);
        linearLayoutManager = new LinearLayoutManager(getContext());
        transactionsAdapter = new TransactionsAdapter(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(transactionsAdapter);
        warn.setVisibility(View.VISIBLE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generalInterface.onBackPressed();
            }
        });

        getTransactions();
        return view;
    }

    private void getTransactions() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsClass.USERS_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        
        transactionsInterface = retrofit.create(TransactionsInterface.class);
        Call<String> stringCall = transactionsInterface.getTransactions(preferencesHelper.getEmail());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        warn.setVisibility(View.GONE);
                        setData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Error is " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setData(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.getString("status").equals("true")){
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Transactions transactions = new Transactions();
                    transactions.setMessage(jsonObject1.getString("MESSAGE"));
                    transactions.setDate(jsonObject1.getString("DATE"));
                    transactions.setEmail(jsonObject1.getString("EMAIL"));
                    transactionsArrayList.add(transactions);
                }
                transactionsAdapter.addAll(transactionsArrayList);
            }

        }
        catch (JSONException e){

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        generalInterface = (GeneralInterface) context;
    }
}