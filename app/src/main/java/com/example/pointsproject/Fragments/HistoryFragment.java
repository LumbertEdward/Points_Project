package com.example.pointsproject.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pointsproject.Adapters.NotificationsAdapter;
import com.example.pointsproject.Constants.ConstantsClass;
import com.example.pointsproject.Interfaces.GeneralInterface;
import com.example.pointsproject.Interfaces.NotificationInterface;
import com.example.pointsproject.Model.NotificationAll;
import com.example.pointsproject.Model.NotificationModel;
import com.example.pointsproject.Model.PreferencesHelper;
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


public class HistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageView back;
    private ImageView filter;
    private LinearLayoutManager linearLayoutManager;
    private NotificationsAdapter notificationsAdapter;

    private NotificationInterface notificationInterface;
    private PreferencesHelper preferencesHelper;
    private GeneralInterface generalInterface;

    private ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesHelper = new PreferencesHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerNotifications);
        back = (ImageView) view.findViewById(R.id.backHist);
        filter = (ImageView) view.findViewById(R.id.filterHist);
        linearLayoutManager = new LinearLayoutManager(getContext());
        notificationsAdapter = new NotificationsAdapter(getContext());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generalInterface.onBackPressed();
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        notificationInterface = retrofit.create(NotificationInterface.class);
        Call<String> notificationAllCall = notificationInterface.readNotifications(preferencesHelper.getEmail());
        notificationAllCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        passData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void passData(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.getString("status").equals("true")){
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    NotificationModel notificationModel = new NotificationModel();
                    notificationModel.setEmailNot(jsonObject1.getString("EMAIL"));
                    notificationModel.setDateNot(jsonObject1.getString("DATE"));
                    notificationModel.setNotificationNot(jsonObject1.getString("NOTIFICATIONS"));
                    notificationModelArrayList.add(notificationModel);
                }

                setAdapter();
            }

        }
        catch (JSONException e){

        }

    }

    private void setAdapter() {
        notificationsAdapter.addAll(notificationModelArrayList);
        recyclerView.setAdapter(notificationsAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        generalInterface = (GeneralInterface) context;
    }
}