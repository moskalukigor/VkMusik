package com.aproject.vkmusik;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Created by igor on 13.03.16.
 */
public class AudioPlay implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private String name;
    private String artist;
    private String duration;
    private String url;

    MediaPlayer mediaPlayer;
    AudioManager am;

    final String LOG_TAG = "AudioPlay";

    public AudioPlay(String name, String artist, String duration, String url)
    {
        this.name = name;
        this.artist = artist;
        this.duration = duration;
        this.url = url;
    }

    public void PlaySong() throws IOException {
        releaseMP();

        Log.d(LOG_TAG, "start Stream");
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Log.d(LOG_TAG, "prepareAsync");
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.prepareAsync();

        if (mediaPlayer == null)
            return;

        mediaPlayer.setOnCompletionListener(this);
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


    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(LOG_TAG, "onCompletion");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(LOG_TAG, "onPrepared");
        mp.start();
    }

    public void StopSong()
    {
        mediaPlayer.stop();
    }

}
