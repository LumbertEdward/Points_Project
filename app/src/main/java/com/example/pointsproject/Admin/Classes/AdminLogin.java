package com.example.pointsproject.Admin.Classes;

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

import com.example.pointsproject.Account.Login;
import com.example.pointsproject.Account.Register;
import com.example.pointsproject.Admin.Interfaces.AdminLoginInterface;
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
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AdminLogin extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button login;
    private TextView goToReg;
    private PreferenceHelperAdmin preferencesHelper;

    private AdminLoginInterface adminLoginInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_700));
        }

        //init
        email = (EditText) findViewById(R.id.emailAdminLogin);
        password = (EditText) findViewById(R.id.passwordAdminLogin);
        login = (Button) findViewById(R.id.btnAdminLogin);
        goToReg = (TextView) findViewById(R.id.adminBack);
        preferencesHelper = new PreferenceHelperAdmin(this);

        goToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLogin.this, Login.class));
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

            adminLoginInterface = retrofit.create(AdminLoginInterface.class);
            Call<String> call = adminLoginInterface.sendData(userEmail, userPassword);
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
                        Toast.makeText(AdminLogin.this, "unresponsive", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(AdminLogin.this, "Response Error", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void sendResponse(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.getString("status").equals("true")){
                startActivity(new Intent(AdminLogin.this, AdminActivity.class));
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
                    preferencesHelper.setFirstName(jsonObject1.getString("NAME"));
                }

            }

        }
        catch (JSONException e){
            Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}