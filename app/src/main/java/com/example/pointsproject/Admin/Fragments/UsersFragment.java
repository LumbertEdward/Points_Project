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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pointsproject.Admin.Adapters.UsersAdapter;
import com.example.pointsproject.Admin.Classes.PreferenceHelperAdmin;
import com.example.pointsproject.Admin.Interfaces.AdminInterfaces;
import com.example.pointsproject.Admin.Interfaces.AllUsersInterface;
import com.example.pointsproject.Constants.ConstantsClass;
import com.example.pointsproject.Model.Users;
import com.example.pointsproject.Model.UsersAll;
import com.example.pointsproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UsersFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageView filter;
    private TextView warn;
    private RelativeLayout search;
    private TextView logOut;
    private LinearLayoutManager linearLayoutManager;
    private UsersAdapter usersAdapter;
    private AllUsersInterface allUsersInterface;

    private ArrayList<Users> usersArrayList = new ArrayList<>();
    private AdminInterfaces adminInterfaces;

    private PreferenceHelperAdmin preferenceHelperAdmin;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceHelperAdmin = new PreferenceHelperAdmin(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerAdminUsers);
        filter = (ImageView) view.findViewById(R.id.filter);
        warn = (TextView) view.findViewById(R.id.txtWarnAdmin);
        search = (RelativeLayout) view.findViewById(R.id.searchAdmin);
        logOut = (TextView) view.findViewById(R.id.adminOut);
        linearLayoutManager = new LinearLayoutManager(getContext());
        usersAdapter = new UsersAdapter(getContext());
        recyclerView.setAdapter(usersAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        warn.setVisibility(View.VISIBLE);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminInterfaces.logOut();
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

        allUsersInterface = retrofit.create(AllUsersInterface.class);
        Call<String> stringCall = allUsersInterface.getAllUsers(preferenceHelperAdmin.getEmail());
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
                Toast.makeText(getContext(), "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Users users = new Users();
                    users.setEmail(jsonObject1.getString("EMAIL"));
                    users.setFirstName(jsonObject1.getString("FIRSTNAME"));
                    users.setLastName(jsonObject1.getString("LASTNAME"));
                    users.setReferredBy(jsonObject1.getString("REFERREDBY"));
                    usersArrayList.add(users);
                }
                usersAdapter.adAll(usersArrayList);
            }
        }
        catch (JSONException e){

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        adminInterfaces = (AdminInterfaces) context;
    }
}