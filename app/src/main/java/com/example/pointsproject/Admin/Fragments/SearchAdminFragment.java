package com.example.pointsproject.Admin.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.pointsproject.Adapters.TransactionsAdapter;
import com.example.pointsproject.Admin.Classes.PreferenceHelperAdmin;
import com.example.pointsproject.Admin.Interfaces.AdminInterfaces;
import com.example.pointsproject.Admin.Interfaces.SearchAdminInterface;
import com.example.pointsproject.Constants.ConstantsClass;
import com.example.pointsproject.Interfaces.GeneralInterface;
import com.example.pointsproject.Interfaces.SearchInterface;
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

public class SearchAdminFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageView back;
    private EditText search;
    private LinearLayoutManager linearLayoutManager;
    private TransactionsAdapter transactionsAdapter;
    private PreferenceHelperAdmin preferenceHelperAdmin;
    private SearchAdminInterface searchAdminInterface;
    private ArrayList<Transactions> transactionsArrayList = new ArrayList<>();
    private AdminInterfaces adminInterfaces;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceHelperAdmin = new PreferenceHelperAdmin(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_admin, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerSearchAdmin);
        back = (ImageView) view.findViewById(R.id.searchBackAdmin);
        search = (EditText) view.findViewById(R.id.editSearchAdmin);
        linearLayoutManager = new LinearLayoutManager(getContext());
        transactionsAdapter = new TransactionsAdapter(getContext());
        recyclerView.setAdapter(transactionsAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminInterfaces.onBackPressed();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                transactionsAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getData();
        return view;
    }

    private void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsClass.USERS_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        searchAdminInterface = retrofit.create(SearchAdminInterface.class);
        Call<String> stringCall = searchAdminInterface.getTransactions(preferenceHelperAdmin.getEmail());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        setData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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

        } catch (JSONException e){
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        adminInterfaces = (AdminInterfaces) context;
    }
}