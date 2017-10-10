package com.example.notifier;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by akshat on 8/10/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private String refreshedToken;
    @Override
    public void onTokenRefresh() {
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
    }

    public String getRefreshedToken() {
        return refreshedToken;
    }
}
