package com.aproject.vkmusik;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import java.io.IOException;

/**
 * Created by igor on 13.03.16.
 */
public class AudioPlay implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, Runnable, SeekBar.OnSeekBarChangeListener {

    private String name;
    private String artist;
    private String duration;
    private String url;

    protected View view;
    protected Context context;

    private SeekBar sBar;


    MediaPlayer mediaPlayer;
    AudioManager am;


    final String TAG = "AudioPlay";

    public AudioPlay(MainActivity context,View view)
    {
        this.context = context;
        this.view =  view;
    }

    public void playSong(String nameR, String artistR, String durationR, String urlR) throws IOException {


        name = nameR;
        artist = artistR;
        duration = durationR;
        url = urlR;

        releaseMP();

        Log.d(TAG, "start Stream");
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Log.d(TAG, "prepareAsync");
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.prepareAsync();

        if (mediaPlayer == null)
            return;

        mediaPlayer.setOnCompletionListener(this);

        sBar = (SeekBar)view.findViewById(R.id.seekBarSong);


    }

    private void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean playPauseSong()
    {
        if(mediaPlayer == null)
            return false;
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            return mediaPlayer.isPlaying();
        }
        else{
            mediaPlayer.start();
            return mediaPlayer.isPlaying();
        }
    }




    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "onPrepared");
        mp.start();
    }

    public String getDuration() {
        return "getDuration: " + mediaPlayer.getDuration() + " getCurrentPosition: " + mediaPlayer.getCurrentPosition();
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mediaPlayer.seekTo(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void run() {
        int currentPosition = mediaPlayer.getCurrentPosition();
        int total = mediaPlayer.getDuration();

        while (mediaPlayer != null && currentPosition < total) {
            try {
                Thread.sleep(1000);
                currentPosition = mediaPlayer.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }
            MainActivity.seekBarSong.setProgress(currentPosition);
        }
    }
}
