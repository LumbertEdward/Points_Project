package com.example.pointsproject.Fragments;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pointsproject.Constants.ConstantsClass;
import com.example.pointsproject.Interfaces.NewNotificationInterface;
import com.example.pointsproject.Interfaces.NotificationInterface;
import com.example.pointsproject.Interfaces.UpdatePointsInterface;
import com.example.pointsproject.Interfaces.ViewPointsInterface;
import com.example.pointsproject.MainActivity;
import com.example.pointsproject.Model.PreferencesHelper;
import com.example.pointsproject.R;
import com.google.gson.JsonArray;

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

public class PointsFragment extends Fragment {
    private TextView points;
    private TextView amount;
    private EditText enterPoints;
    private TextView viewEquivalent;
    private Button redeem;
    private ProgressBar progressBar;

    private ViewPointsInterface viewPointsInterface;
    private UpdatePointsInterface updatePointsInterface;
    private PreferencesHelper preferencesHelper;
    private NewNotificationInterface newNotificationInterface;

    private int myPoints = 0;
    private String notPoints = " ";
    private String notAmount = " ";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesHelper = new PreferencesHelper(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_points, container, false);
        points = (TextView) view.findViewById(R.id.totalPoints);
        amount = (TextView) view.findViewById(R.id.totalAmount);
        enterPoints = (EditText) view.findViewById(R.id.pointsRedeeming);
        viewEquivalent = (TextView) view.findViewById(R.id.equivAmount);
        redeem = (Button) view.findViewById(R.id.btnRedeem);
        progressBar = (ProgressBar) view.findViewById(R.id.progressPoints);
        progressBar.setVisibility(View.GONE);

        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOutput();
            }
        });
        //get Points
        getPoints();
        enterPoints();
        return view;
    }

    private void enterPoints() {
        enterPoints.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String x = String.valueOf(charSequence);
                int out = Integer.parseInt(x);
                if (String.valueOf(out) != null){
                    viewEquivalent.setText("Ksh " + String.valueOf(out * 500));
                }
                else {

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void getPoints() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsClass.USERS_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        viewPointsInterface = retrofit.create(ViewPointsInterface.class);
        Call<String> stringCall = viewPointsInterface.viewPoints(preferencesHelper.getEmail());
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
                    String newPoints = jsonObject1.getString("TOTALPOINTS");
                    points.setText(newPoints);
                    myPoints = Integer.parseInt(newPoints);
                    int total = Integer.parseInt(newPoints) * 500;
                    amount.setText("Ksh " + String.valueOf(total));
                }

            }
        }
        catch (JSONException e){

        }
    }

    private void showOutput() {
        String selectPoints = enterPoints.getText().toString().trim();
        if (TextUtils.isEmpty(selectPoints)){
            enterPoints.setError("Specify Points to Redeem");
        }
        else if (myPoints >= Integer.parseInt(selectPoints)){
            int toSend = myPoints - Integer.parseInt(selectPoints);
            notPoints = selectPoints;
            notAmount = String.valueOf(Integer.parseInt(selectPoints) * 500);
            progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantsClass.USERS_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();

            updatePointsInterface = retrofit.create(UpdatePointsInterface.class);
            Call<String> call = updatePointsInterface.updatePoints(preferencesHelper.getEmail(), toSend, Integer.parseInt(selectPoints));
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            progressBar.setVisibility(View.GONE);
                            //enterPoints.setText(" ");
                            updatePoints(response.body());
                            sendNotification();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    //enterPoints.setText(" ");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                    View v = layoutInflater.inflate(R.layout.failure_item, null);
                    Button btn = (Button) v.findViewById(R.id.btnEror);
                    builder.setView(v);
                    AlertDialog alertDialog = builder.create();
                    if (alertDialog != null) {
                        alertDialog.getWindow().getAttributes().windowAnimations = R.style.slidingDialog;
                    }
                    alertDialog.show();
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    //Toast.makeText(getContext(), "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        else {
            Toast.makeText(getContext(), "Check Points", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendNotification() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(ConstantsClass.USERS_BASE_URL)
                .build();

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String notDate = simpleDateFormat.format(date);
        newNotificationInterface = retrofit.create(NewNotificationInterface.class);
        String sedNot = "You have redeemed " + notPoints + " points, " + notAmount + " shillings will be depositied to your mpesa account shortly";
        Call<String> stringCall = newNotificationInterface.sendNotification(preferencesHelper.getEmail(), sedNot, notDate);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (response.body() != null) {
                        getNotification(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }

    private void getNotification(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.getString("status").equals("true")){
                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            }

        }
        catch (JSONException e){

        }
    }

    private void updatePoints(String body) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.success_item, null);
        Button success = (Button) v.findViewById(R.id.btnOk);
        builder.setView(v);
        AlertDialog alertDialog = builder.create();
        if (alertDialog != null){
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.slidingDialog;
        }
        alertDialog.show();
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                setNotification();
            }
        });

        try {
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.getString("status").equals("true")){
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String nwPoints = jsonObject1.getString("TOTALPOINTS");
                    points.setText(nwPoints);

                }
            }

        }
        catch (JSONException e){

        }
    }

    private void setNotification() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

        NotificationCompat.Builder builder1 = new NotificationCompat.Builder(getContext(), ConstantsClass.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_logout_24)
                .setContentTitle("Points")
                .setContentText("You have redeemed " + notPoints + " points, " + notAmount + " shillings will be depositied to your mpesa account shortly")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
        notificationManagerCompat.notify(ConstantsClass.NOTIFICATION_ID, builder1.build());
    }
}