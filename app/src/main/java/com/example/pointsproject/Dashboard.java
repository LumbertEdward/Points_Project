package com.example.pointsproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.example.pointsproject.Interfaces.GeneralInterface;

public class Dashboard extends Fragment {
    private CardView cardPoints;
    private CardView cardHist;
    private CardView cardProf;
    private CardView cardTans;
    private RelativeLayout search;

    //interfaces
    private GeneralInterface generalInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        cardHist = (CardView) view.findViewById(R.id.cardHistory);
        cardPoints = (CardView) view.findViewById(R.id.cardPoints);
        cardProf = (CardView) view.findViewById(R.id.cardProfile);
        cardTans = (CardView) view.findViewById(R.id.cardTransactions);
        search = (RelativeLayout) view.findViewById(R.id.relSearch);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
        Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
        cardPoints.setAnimation(animation);
        cardTans.setAnimation(animation2);
         cardProf.setAnimation(animation);
         cardHist.setAnimation(animation2);

         search.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 generalInterface.goToSearch();
             }
         });

         cardPoints.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 generalInterface.geToPoints();
             }
         });

         cardHist.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 generalInterface.goToHistory();
             }
         });

         cardProf.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 generalInterface.goToProfile();
             }
         });

         cardTans.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 generalInterface.goToTransaction();
             }
         });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        generalInterface = (GeneralInterface) context;
    }
}