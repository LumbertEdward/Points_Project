package com.example.pointsproject.Admin.Fragments;

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
import android.widget.Toast;

import com.example.pointsproject.Admin.Classes.PreferenceHelperAdmin;
import com.example.pointsproject.Admin.Clients.DarajaApiClient;
import com.example.pointsproject.Admin.Clients.Utils;
import com.example.pointsproject.Admin.Interfaces.AdminInterfaces;
import com.example.pointsproject.Admin.Interfaces.DeletePayedUserInterface;
import com.example.pointsproject.Admin.Interfaces.PostAdminTransactions;
import com.example.pointsproject.Admin.Interfaces.PostTransactionInterface;
import com.example.pointsproject.Admin.Interfaces.RedeemedPaymentInterface;
import com.example.pointsproject.Admin.ModelsMpesa.AccessTokens;
import com.example.pointsproject.Admin.ModelsMpesa.STKPush;
import com.example.pointsproject.Constants.ConstantsClass;
import com.example.pointsproject.Model.Redeem;
import com.example.pointsproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PaymentFragment extends Fragment {

    private ImageView back;
    private EditText phone;
    private EditText amount;
    private Bundle bundle;
    private Redeem redeem;
    private Button pay;
    private ProgressBar progressBar;

    private AdminInterfaces adminInterfaces;
    private RedeemedPaymentInterface redeemedPaymentInterface;
    private PreferenceHelperAdmin preferenceHelperAdmin;
    private DarajaApiClient darajaApiClient;
    private PostTransactionInterface postTransactionInterface;
    private PostAdminTransactions postAdminTransactions;
    private DeletePayedUserInterface deletePayedUserInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
        if (bundle != null){
            redeem = bundle.getParcelable("Redeem");
        }

        preferenceHelperAdmin = new PreferenceHelperAdmin(getContext());
        darajaApiClient = new DarajaApiClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        back = (ImageView) view.findViewById(R.id.backPay);
        phone = (EditText) view.findViewById(R.id.editPhone);
        amount = (EditText) view.findViewById(R.id.editAmount);
        pay = (Button) view.findViewById(R.id.btnPayment);
        progressBar = (ProgressBar) view.findViewById(R.id.progressPayment);
        progressBar.setVisibility(View.GONE);


        darajaApiClient.setIsDebug(true);

        getAccessToken();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminInterfaces.onBackPressed();
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
        return view;
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
        Call<String> stringCall = redeemedPaymentInterface.getDetails(redeem.getEmail());
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
                Toast.makeText(getContext(), "Error is " + t.getMessage(), Toast.LENGTH_LONG).show();
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
                    int total = redeem.getPoints() * 500;
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
                    Toast.makeText(getContext(), "Post submitted to API", Toast.LENGTH_SHORT).show();
                    sendUserTransactions();
                }
            }

            @Override
            public void onFailure(Call<STKPush> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    private void sendUserTransactions() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsClass.USERS_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        String trans = String.valueOf(redeem.getPoints() * 500);
        String transaction = trans + " sent to your Mpesa acccount.";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String transDate = simpleDateFormat.format(date);
        postTransactionInterface = retrofit.create(PostTransactionInterface.class);
        Call<String> stringCall = postTransactionInterface.sendTransaction(redeem.getEmail(), transaction, transDate);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        Toast.makeText(getContext(), "Sent", Toast.LENGTH_SHORT).show();
                        sendToAdminTransactions();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Error is " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendToAdminTransactions() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsClass.USERS_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        String trans = String.valueOf(redeem.getPoints() * 500);
        String transaction = trans + " sent to  Mpesa acccount of " + redeem.getEmail();
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String transDate = simpleDateFormat.format(date);
        postAdminTransactions = retrofit.create(PostAdminTransactions.class);
        Call<String> stringCall = postAdminTransactions.sendTransaction(preferenceHelperAdmin.getEmail(), transaction, transDate);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        Toast.makeText(getContext(), "Sent to admin", Toast.LENGTH_SHORT).show();
                        deleteUser();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Admin Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void deleteUser() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsClass.USERS_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        deletePayedUserInterface = retrofit.create(DeletePayedUserInterface.class);
        Call<String> stringCall = deletePayedUserInterface.deleteUser(redeem.getEmail());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        adminInterfaces.goToHome();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        adminInterfaces = (AdminInterfaces) context;
    }
}