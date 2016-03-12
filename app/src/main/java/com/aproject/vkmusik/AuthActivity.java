package com.aproject.vkmusik;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

/**
 * Created by igor on 12.03.16.
 */
public class AuthActivity extends Activity{

    private static String token = "";
    private static Integer userId;
    private static String[] sMyScope = new String[]{VKScope.AUDIO, VKScope.NOHTTPS};
    private static String TAG = "Log_authActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "login");
        VKSdk.login(this, sMyScope);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Log.d(TAG, res.accessToken);
                token = res.accessToken;
                userId = Integer.getInteger(res.userId);
            }
            @Override
            public void onError(VKError error) {
                Log.d(TAG, error.errorMessage);
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if(token != "")
        {
            ((GlobalApplication) this.getApplication()).setToken(token);
            ((GlobalApplication) this.getApplication()).setUserId(userId);

        }
        finish();
    }
}
