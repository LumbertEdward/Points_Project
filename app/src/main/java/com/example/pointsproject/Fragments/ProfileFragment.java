package com.example.pointsproject.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pointsproject.Constants.ConstantsClass;
import com.example.pointsproject.Interfaces.GeneralInterface;
import com.example.pointsproject.Interfaces.ReadUserInterface;
import com.example.pointsproject.Interfaces.UpdatePointsInterface;
import com.example.pointsproject.Interfaces.UpdateUserInterface;
import com.example.pointsproject.Model.PreferencesHelper;
import com.example.pointsproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ProfileFragment extends Fragment {
    private ImageView back;
    private EditText firstName;
    private EditText lastName;
    private EditText phone;
    private Button btn;
    private ProgressBar progressBar;

    private GeneralInterface generalInterface;
    private UpdateUserInterface updateUserInterface;
    private PreferencesHelper preferencesHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesHelper = new PreferencesHelper(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        back = (ImageView) view.findViewById(R.id.backProf);
        firstName = (EditText) view.findViewById(R.id.editFirst);
        lastName = (EditText) view.findViewById(R.id.editLast);
        phone = (EditText) view.findViewById(R.id.editPhone);
        btn = (Button) view.findViewById(R.id.btnSave);
        progressBar = (ProgressBar) view.findViewById(R.id.progressProfile);
        progressBar.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               generalInterface.onBackPressed();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
        return view;
    }

    private void saveData() {
        String first_name = firstName.getText().toString().trim();
        String last_name = lastName.getText().toString().trim();
        String phone_number = phone.getText().toString().trim();

        if (TextUtils.isEmpty(first_name)){
            firstName.setError("Enter First Name");
        }
        else if (TextUtils.isEmpty(last_name)){
            lastName.setError("Enter Last Name");
        }
        else if (TextUtils.isEmpty(phone_number)){
            phone.setError("Enter Phone");
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl(ConstantsClass.USERS_BASE_URL)
                    .build();

            updateUserInterface = retrofit.create(UpdateUserInterface.class);
            Call<String> stringCall = updateUserInterface.updateUser(first_name, last_name, phone_number, preferencesHelper.getEmail());
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()){
                        if (response.body() != null){
                            progressBar.setVisibility(View.GONE);
                            setData(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Error is " + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

    private void setData(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.getString("status").equals("true")){
                generalInterface.goToProfile();
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