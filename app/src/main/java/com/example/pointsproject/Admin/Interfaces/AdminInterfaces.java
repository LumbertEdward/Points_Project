package com.example.pointsproject.Admin.Interfaces;

import com.example.pointsproject.Model.Redeem;
import com.example.pointsproject.Model.Users;

public interface AdminInterfaces {
    void goToDetails(Users users);
    void onBackPressed();
    void logOut();
    void goToHome();
    void sendPaymentDetails(Redeem redeem);
    void goToSearch();
}
