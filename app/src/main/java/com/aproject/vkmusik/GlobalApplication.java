package com.aproject.vkmusik;

import android.app.Application;
import android.util.Log;

import com.vk.sdk.VKSdk;


public class GlobalApplication extends Application{
    private final static String TAG = "Log_GlobalActivity";

    public String token = "";
    public Integer userId;

    public void setToken(String tkn) { token = tkn; }
    public void setUserId(Integer usrId) { userId = usrId; }
    public String getToken()
    {
        return token;
    }
    public Integer getUserId() { return userId; }

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
        Log.d(TAG, "VKSdk initialize");
    }
}
