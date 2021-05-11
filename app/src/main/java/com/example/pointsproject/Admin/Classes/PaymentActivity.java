package com.example.pointsproject.Admin.Classes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pointsproject.Admin.Clients.DarajaApiClient;
import com.example.pointsproject.Admin.Clients.Utils;
import com.example.pointsproject.Admin.Fragments.PaymentFragment;
import com.example.pointsproject.Admin.Interfaces.RedeemedPaymentInterface;
import com.example.pointsproject.Admin.ModelsMpesa.AccessTokens;
import com.example.pointsproject.Admin.ModelsMpesa.STKPush;
import com.example.pointsproject.Constants.ConstantsClass;
import com.example.pointsproject.Model.Redeem;
import com.example.pointsproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PaymentActivity extends AppCompatActivity {
    private ImageView back;
    private EditText phone;
    private EditText amount;
    private Button pay;
    private ProgressBar progressBar;

    private RedeemedPaymentInterface redeemedPaymentInterface;
    private PreferenceHelperAdmin preferenceHelperAdmin;
    private DarajaApiClient darajaApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_700));
        }

        back = (ImageView) findViewById(R.id.backPay);
        phone = (EditText) findViewById(R.id.editPhone);
        amount = (EditText) findViewById(R.id.editAmount);
        pay = (Button) findViewById(R.id.btnPayment);
        progressBar = (ProgressBar) findViewById(R.id.progressPayment);
        progressBar.setVisibility(View.GONE);

        preferenceHelperAdmin = new PreferenceHelperAdmin(this);
        darajaApiClient = new DarajaApiClient();

        darajaApiClient.setIsDebug(true);

        getAccessToken();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                payUser();
            }
        });

        getData();
    }

    private void getAccessToken() {
        darajaApiClient.setGetAccessToken(true);
        darajaApiClient.stkPushService().getAccessTokens().enqueue(new Callback<AccessTokens>() {
            @Override
            public void onResponse(Call<AccessTokens> call, Response<AccessTokens> response) {
                if (response.isSuccessful()){
                    darajaApiClient.setAuthToken(response.body().accessToken);
                }
            }

            @Override
            public void onFailure(Call<AccessTokens> call, Throwable t) {

            }
        });
    }

    private void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsClass.USERS_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        redeemedPaymentInterface = retrofit.create(RedeemedPaymentInterface.class);
        Call<String> stringCall = redeemedPaymentInterface.getDetails(getIntent().getStringExtra("Email"));
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
                Toast.makeText(PaymentActivity.this, "Error is " + t.getMessage(), Toast.LENGTH_LONG).show();
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
                    phone.setText(jsonObject1.getString("PHONE"));
                    int total = getIntent().getIntExtra("Points", 0) * 500;
                    amount.setText(String.valueOf(total));
                }
            }

        } catch (JSONException e){

        }
    }

    private void payUser() {
        String phoneNo = phone.getText().toString().trim();
        String amountTot = amount.getText().toString();
        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                ConstantsClass.BUSINESS_SHORT_CODE,
                Utils.getPassword(ConstantsClass.BUSINESS_SHORT_CODE, ConstantsClass.PASSKEY, timestamp),
                timestamp,
                ConstantsClass.TRANSACTION_TYPE,
                String.valueOf(amountTot),
                Utils.sanitizePhoneNumber(phoneNo),
                ConstantsClass.PARTYB,
                Utils.sanitizePhoneNumber(phoneNo),
                ConstantsClass.CALLBACKURL,
                "Bakes Test",
                "Testing"
        );

        darajaApiClient.setGetAccessToken(false);
        darajaApiClient.stkPushService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(Call<STKPush> call, Response<STKPush> response) {
                if (response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(PaymentActivity.this, "Post submitted to API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<STKPush> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

            }
        });

    }
}