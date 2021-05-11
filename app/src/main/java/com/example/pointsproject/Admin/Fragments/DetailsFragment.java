package com.example.pointsproject.Admin.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pointsproject.Admin.Adapters.UsersAdapter;
import com.example.pointsproject.Admin.Classes.PreferenceHelperAdmin;
import com.example.pointsproject.Admin.Interfaces.AdminInterfaces;
import com.example.pointsproject.Admin.Interfaces.ApprovedReferralInterface;
import com.example.pointsproject.Admin.Interfaces.ReferringUsersInterface;
import com.example.pointsproject.Constants.ConstantsClass;
import com.example.pointsproject.Model.Users;
import com.example.pointsproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DetailsFragment extends Fragment {
    private TextView first;
    private TextView last;
    private TextView email;
    private TextView refer;
    private Spinner spinner;
    private Button save;
    private ImageView back;
    private ProgressBar progressBar;

    private AdminInterfaces adminInterfaces;
    private Bundle bundle;
    private Users users;
    private PreferenceHelperAdmin preferenceHelperAdmin;
    private ApprovedReferralInterface approvedReferralInterface;
    private ReferringUsersInterface referringUsersInterface;
    private ArrayList<Users> usersArrayList = new ArrayList<>();
    String[] options = {"messi@gmail.com"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
        if (bundle != null){
            users = bundle.getParcelable("User");
        }

        preferenceHelperAdmin = new PreferenceHelperAdmin(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        first = (TextView) view.findViewById(R.id.firstUser);
        last = (TextView) view.findViewById(R.id.lastUser);
        email = (TextView) view.findViewById(R.id.emailUser);
        refer = (TextView) view.findViewById(R.id.referUser);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        save = (Button) view.findViewById(R.id.btnRefer);
        back = (ImageView) view.findViewById(R.id.backDet);
        progressBar = (ProgressBar) view.findViewById(R.id.progressAdmin);
        progressBar.setVisibility(View.GONE);


        ArrayAdapter<String> aa = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, options);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //refer.setText(usersArrayList.get(i).getEmail());
                refer.setText(options[i]);
                Toast.makeText(getContext(), spinner.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminInterfaces.onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                saveData();
            }
        });

        setData();
        fillArray();
        setEmails();
        return view;
    }

    private void setEmails() {
        if (!usersArrayList.isEmpty()){
            for (int i = 0; i < usersArrayList.size(); i++){
                options[i] = usersArrayList.get(i).getEmail();
                Toast.makeText(getContext(), "Emails are " + usersArrayList.get(i).getEmail(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fillArray() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(ConstantsClass.USERS_BASE_URL)
                .build();

        referringUsersInterface = retrofit.create(ReferringUsersInterface.class);
        Call<String> stringCall = referringUsersInterface.getAllUsers(preferenceHelperAdmin.getEmail());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("status").equals("true")){
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    Users user = new Users();
                                    user.setEmail(jsonObject1.getString("EMAIL"));
                                    usersArrayList.add(user);
                                }
                            }

                        }
                        catch (JSONException e){

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void setData() {
        first.setText(users.getFirstName());
        last.setText(users.getLastName());
        email.setText(users.getEmail());
    }

    private void saveData() {
        String referrer = refer.getText().toString().trim();

        if (TextUtils.isEmpty(referrer)){
            refer.setError("Select Email");
        }
        else {
            String referred = users.getEmail();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantsClass.USERS_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();

            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String notDate = simpleDateFormat.format(date);

            String notification = "You have received one point for referring " + users.getEmail();

            approvedReferralInterface = retrofit.create(ApprovedReferralInterface.class);
            Call<String> stringCall = approvedReferralInterface.sendReferredUser(referred, referrer, notification, notDate);
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()){
                        if (response.body() != null){
                            progressBar.setVisibility(View.GONE);
                            try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                if (jsonObject.getString("status").equals("true")){
                                    adminInterfaces.goToHome();
                                    Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (JSONException e){

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getContext(), "Error is " + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        adminInterfaces = (AdminInterfaces) context;
    }
}