package com.example.pointsproject.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pointsproject.Admin.Classes.AdminActivity;
import com.example.pointsproject.Admin.Classes.AdminLogin;
import com.example.pointsproject.Constants.ConstantsClass;
import com.example.pointsproject.Interfaces.LoginInterface;
import com.example.pointsproject.MainActivity;
import com.example.pointsproject.Model.PreferencesHelper;
import com.example.pointsproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Login extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button login;
    private TextView goToReg;
    private TextView admin;
    private PreferencesHelper preferencesHelper;

    private LoginInterface loginInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_700));
        }

        //init
        email = (EditText) findViewById(R.id.emailLogin);
        password = (EditText) findViewById(R.id.passwordLogin);
        login = (Button) findViewById(R.id.btnLogin);
        goToReg = (TextView) findViewById(R.id.registerText);
        admin = (TextView) findViewById(R.id.adminText);
        preferencesHelper = new PreferencesHelper(this);

        goToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, AdminLogin.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
            }
        });

    }

    private void checkData() {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(userEmail)){
            email.setError("Email Requires");
        }
        else if(TextUtils.isEmpty(userPassword)){
            password.setError("Password Required");
        }
        else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantsClass.USERS_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();

            loginInterface = retrofit.create(LoginInterface.class);
            Call<String> call = loginInterface.sendData(userEmail, userPassword);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()){
                        if (response.body() != null){
                            String resp = response.body().toString();
                            sendResponse(resp);
                        }
                    }
                    else {
                        Toast.makeText(Login.this, "unresponsive", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(Login.this, "Response Error", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void sendResponse(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.getString("status").equals("true")){
                startActivity(new Intent(Login.this, MainActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
                saveData(body);

            }
        }
        catch (JSONException e){
            Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    private void saveData(String body) {
        preferencesHelper.setLoggedIn(true);
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.getString("status").equals("true")){
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    preferencesHelper.setEmail(jsonObject1.getString("EMAIL"));
                    preferencesHelper.setFirstName(jsonObject1.getString("FIRSTNAME"));
                }

            }

        }
        catch (JSONException e){
            Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferencesHelper.getLoggedIn()){
            startActivity(new Intent(Login.this, MainActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        }
    }
}