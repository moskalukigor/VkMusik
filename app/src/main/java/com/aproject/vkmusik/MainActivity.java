package com.aproject.vkmusik;


import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = "Log_MainActivity";
    private static Button btnStart;
    private static ListView listAudio;
    private static TextView txtNamePanel;
    private static TextView txtArtistPanel;
    public static SeekBar seekBarSong;
    private static ImageView imgViewPlayPause;
    private static ImageView imgViewLogoSong;


    private static List<String> nameList = new ArrayList<String>();
    private static List<String> artistList = new ArrayList<String>();
    private static List<String> durationList = new ArrayList<String>();
    private static List<String> srcAudioList = new ArrayList<String>();


    private SlidingUpPanelLayout mLayout;


    public AudioPlay ap;
    public boolean isPlaying;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnStart = (Button)findViewById(R.id.btnStart);
        listAudio = (ListView)findViewById(R.id.listAudio);
        txtNamePanel = (TextView) findViewById(R.id.nameSlide);
        txtArtistPanel = (TextView) findViewById(R.id.artistSlide);
        seekBarSong = (SeekBar) findViewById(R.id.seekBarSong);
        imgViewPlayPause = (ImageView) findViewById(R.id.imgViewPlayPause);
        imgViewLogoSong  = (ImageView) findViewById(R.id.imgViewLogoSong);

        imgViewPlayPause.setImageResource(R.mipmap.play);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });


    }

    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnStart:
                    btnStart.setVisibility(View.INVISIBLE);
                    getAudio();
                break;
            case R.id.imgViewPlayPause:
                isPlaying = ap.playPauseSong();
                if (isPlaying)
                    imgViewPlayPause.setImageResource(R.mipmap.pause);
                else
                    imgViewPlayPause.setImageResource(R.mipmap.play);
                Log.d(TAG, ap.getDuration() + "");
                Log.d(TAG, "imgViewPlayPause_click");

                break;
        }
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

        View v = getLayoutInflater().inflate(R.layout.activity_main, null);
        ap = new AudioPlay(this,v);



        listAudio.setAdapter(new CustomListViewAdapter(this,v, nameListStr, artistListStr, durationListStr, srcAudioListStr));
        listAudio.setVisibility(View.VISIBLE);
        //listAudio.setBackgroundColor(Color.MAGENTA);
        btnStart.setVisibility(View.INVISIBLE);
    }

    public void getAudio() {
        Integer ownerId = ((GlobalApplication) this.getApplication()).getUserId();

        VKRequest request = VKApi.audio().get(VKParameters.from(VKApiConst.OWNER_ID, ownerId, VKApiConst.COUNT, 5000));
        request.attempts = 10;


        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                double dur;
                int duri;
                for (int i = 0; i < ((VKList<VKApiAudio>) response.parsedModel).size(); i++) {
                    VKApiAudio vkApiAudio = ((VKList<VKApiAudio>) response.parsedModel).get(i);

                    nameList.add(i, vkApiAudio.title);
                    artistList.add(i, vkApiAudio.artist);
                    dur = vkApiAudio.duration;
                    dur = dur * 1000;
                    duri = (int) Math.round(dur);
                    dur = (double) duri / 1000;
                    durationList.add(i, dur + "");
                    srcAudioList.add(i, vkApiAudio.url);


                }
                fillListView(nameList, artistList, durationList, srcAudioList);
            }
        });
    }

    public void playSong(String nameR, String artistR, String durationR, String urlR)
    {
        try {
            ap.playSong(nameR,artistR,durationR,urlR);
            imgViewPlayPause.setImageResource(R.mipmap.pause);

        } catch (IOException e) {
            e.printStackTrace();
        }

        txtNamePanel.setText(nameR);
        txtArtistPanel.setText(artistR);

        seekBarSong.setMax(Integer.getInteger(durationR));
        ap.run();
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_audio) {

        } else if (id == R.id.nav_downloaded) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
