package com.aproject.vkmusik;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





        btnStart = (Button)findViewById(R.id.btnStart);
        listAudio = (ListView)findViewById(R.id.listAudio);

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

    public void getAudio() {
        Integer ownerId = ((GlobalApplication) this.getApplication()).getUserId();

        VKRequest request = VKApi.audio().get(VKParameters.from(VKApiConst.OWNER_ID, ownerId, VKApiConst.COUNT, 5000));
        request.attempts = 10;


        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                for (int i = 0; i < ((VKList<VKApiAudio>) response.parsedModel).size(); i++) {
                    VKApiAudio vkApiAudio = ((VKList<VKApiAudio>) response.parsedModel).get(i);

                    nameList.add(i, vkApiAudio.title);
                    artistList.add(i, vkApiAudio.artist);
                    durationList.add(i, vkApiAudio.duration + "");
                    srcAudioList.add(i, vkApiAudio.url);


                }
                fillListView(nameList, artistList, durationList, srcAudioList);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
