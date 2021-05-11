package com.example.pointsproject.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pointsproject.Constants.ConstantsClass;
import com.example.pointsproject.Interfaces.GeneralInterface;
import com.example.pointsproject.Interfaces.ReadUserInterface;
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


public class UserProfile extends Fragment {
    private ImageView back;
    private TextView firstName;
    private TextView lastName;
    private TextView phone;
    private Button btn;

    private GeneralInterface generalInterface;
    private ReadUserInterface readUserInterface;
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
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        back = (ImageView) view.findViewById(R.id.backUser);
        firstName = (TextView) view.findViewById(R.id.txtFirst);
        lastName = (TextView) view.findViewById(R.id.txtLast);
        phone = (TextView) view.findViewById(R.id.txtPhone);
        btn = (Button) view.findViewById(R.id.btnEdit);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generalInterface.goToEditProfile();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generalInterface.onBackPressed();
            }
        });
        readData();
        return view;
    }

    private void readData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsClass.USERS_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        readUserInterface = retrofit.create(ReadUserInterface.class);
        Call<String> stringCall = readUserInterface.userDetails(preferencesHelper.getEmail());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        setUserData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Error is " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUserData(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.getString("status").equals("true")){
                Toast.makeText(getContext(), "first : ", Toast.LENGTH_SHORT).show();
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    firstName.setText(jsonObject1.getString("FIRSTNAME"));
                    lastName.setText(jsonObject1.getString("LASTNAME"));
                    phone.setText(jsonObject1.getString("PHONE"));
                }
            }
            else {
                Toast.makeText(getContext(), "Error is : " + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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