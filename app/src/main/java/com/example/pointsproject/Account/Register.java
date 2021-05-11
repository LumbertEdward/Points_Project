package com.example.pointsproject.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pointsproject.Constants.ConstantsClass;
import com.example.pointsproject.Interfaces.RegistrationInterface;
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

public class Register extends AppCompatActivity {
    private ImageView back;
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText confirm;
    private EditText phone;
    private TextView login;
    private Button reg;
    private PreferencesHelper preferencesHelper;
    private RegistrationInterface registrationInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_700));
        }

        //init
        back = (ImageView) findViewById(R.id.backRegister);
        email = (EditText) findViewById(R.id.email);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.secondName);
        password = (EditText) findViewById(R.id.passwordReg);
        confirm = (EditText) findViewById(R.id.confirmPassword);
        phone = (EditText) findViewById(R.id.phone);
        login = (TextView) findViewById(R.id.loginReg);
        reg = (Button) findViewById(R.id.btnRegister);

        preferencesHelper = new PreferencesHelper(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String first = firstName.getText().toString().trim();
        String last = lastName.getText().toString().trim();
        String userMail = email.getText().toString().trim();
        String userPass = password.getText().toString().trim();
        String userConfirm = confirm.getText().toString().trim();
        String userPhone = phone.getText().toString().trim();

        if (!userPass.equals(userConfirm)){
            confirm.setError("Passwords do not match");
        }
        else if(TextUtils.isEmpty(first)){
            firstName.setError("First Name Required");
        }
        else if(TextUtils.isEmpty(last)){
            lastName.setError("Last Name Required");
        }
        else if(TextUtils.isEmpty(userMail)){
            email.setError("Email Required");
        }
        else if(TextUtils.isEmpty(userPass)){
            password.setError("Password Required");
        }
        else if(TextUtils.isEmpty(userConfirm)){
            confirm.setError("Confirm Password");
        }
        else if(TextUtils.isEmpty(userPhone)){
            phone.setError("Phone Number Required");
        }
        else {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantsClass.USERS_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();

            registrationInterface = retrofit.create(RegistrationInterface.class);
            Call<String> call = registrationInterface.sendData(first, last, userMail, userPass, userPhone);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()){
                        if (response.body() != null){
                            String resp = response.body().toString();
                            sendResponse(resp);
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(Register.this, "Response Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

    private void sendResponse(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.optString("status").equals("true")){
                saveData(body);
                startActivity(new Intent(Register.this, MainActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
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
        if (preferencesHelper.getLoggedIn()){
            startActivity(new Intent(Register.this, MainActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        }
        super.onResume();
    }
}