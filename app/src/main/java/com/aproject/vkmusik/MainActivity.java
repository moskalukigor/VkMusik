package com.aproject.vkmusik;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static String TAG = "Log_MainActivity";
    private static Button btnStart;
    private static ListView listAudio;


    private static List<String> nameList = new ArrayList<String>();
    private static List<String> artistList = new ArrayList<String>();
    private static List<String> durationList = new ArrayList<String>();
    private static List<String> srcAudioList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent auth = new Intent(this, AuthActivity.class);
        startActivity(auth);



        btnStart = (Button)findViewById(R.id.btnStart);
        listAudio = (ListView)findViewById(R.id.listAudio);

        //listAudio.setVisibility(View.INVISIBLE);


    }


    public void onClick(View view)
    {
        btnStart.setActivated(false);
        getAudio();
    }

    public void fillListView(List<String> names,List<String> artists,List<String> durations,List<String> srcs)
    {
        String[] nameListStr = new String[ names.size() ];
        String[] artistListStr = new String[ artists.size() ];
        String[] durationListStr = new String[ durations.size() ];
        String[] srcAudioListStr = new String[ srcs.size() ];
        names.toArray(nameListStr);
        artists.toArray(artistListStr);
        durations.toArray(durationListStr);
        srcs.toArray(srcAudioListStr);

        listAudio.setAdapter(new CustomListViewAdapter(this, nameListStr, artistListStr, durationListStr, srcAudioListStr));
        listAudio.setVisibility(View.VISIBLE);
        //listAudio.setBackgroundColor(Color.MAGENTA);
        btnStart.setVisibility(View.INVISIBLE);
    }

    public void getAudio()
    {
        Integer ownerId = ((GlobalApplication) this.getApplication()).getUserId();

        VKRequest request = VKApi.audio().get(VKParameters.from(VKApiConst.OWNER_ID, ownerId,VKApiConst.COUNT,5000));
        request.attempts = 10;


        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                for(int i = 0;i<((VKList<VKApiAudio>)response.parsedModel).size();i++){
                    VKApiAudio vkApiAudio = ((VKList<VKApiAudio>)response.parsedModel).get(i);

                    nameList.add(i,vkApiAudio.title);
                    artistList.add(i, vkApiAudio.artist);
                    durationList.add(i,vkApiAudio.duration+"");
                    srcAudioList.add(i,vkApiAudio.url);


                }
                fillListView(nameList,artistList,durationList,srcAudioList);
            }
        });

}



}
