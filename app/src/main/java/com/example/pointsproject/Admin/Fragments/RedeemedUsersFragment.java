package com.example.pointsproject.Admin.Fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.pointsproject.Admin.Adapters.RedeemAdapter;
import com.example.pointsproject.Admin.Classes.PreferenceHelperAdmin;
import com.example.pointsproject.Admin.Interfaces.AdminInterfaces;
import com.example.pointsproject.Admin.Interfaces.RedeemedInterface;
import com.example.pointsproject.Constants.ConstantsClass;
import com.example.pointsproject.Model.Redeem;
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

public class RedeemedUsersFragment extends Fragment {
    private ImageView back;
    private TextView warn;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RedeemAdapter redeemAdapter;
    private PreferenceHelperAdmin preferenceHelperAdmin;
    private RedeemedInterface redeemedInterface;
    private AdminInterfaces adminInterfaces;

    private ArrayList<Redeem> redeemArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceHelperAdmin = new PreferenceHelperAdmin(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_redeemed_users, container, false);
        back = (ImageView) view.findViewById(R.id.backRed);
        warn = (TextView) view.findViewById(R.id.txtRedWarn);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerRedeem);
        linearLayoutManager = new LinearLayoutManager(getContext());
        redeemAdapter = new RedeemAdapter(getContext());
        recyclerView.setAdapter(redeemAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        warn.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminInterfaces.onBackPressed();
            }
        });

        showData();
        return view;
    }

    private void showData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsClass.USERS_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        redeemedInterface = retrofit.create(RedeemedInterface.class);
        Call<String> stringCall = redeemedInterface.getRedeemedUsers(preferenceHelperAdmin.getEmail());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        warn.setVisibility(View.GONE);
                        getData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Error is " + t.getMessage(), Toast.LENGTH_SHORT).show();
                warn.setVisibility(View.GONE);
            }
        });
    }

    private void getData(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.getString("status").equals("true")){
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Redeem redeem = new Redeem();
                    redeem.setEmail(jsonObject1.getString("EMAIL"));
                    redeem.setPoints(jsonObject1.getInt("POINTS"));
                    redeemArrayList.add(redeem);
                }
                redeemAdapter.addAll(redeemArrayList);
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