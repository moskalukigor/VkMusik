package com.aproject.vkmusik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by igor on 13.03.16.
 */
public class CustomListViewAdapter extends BaseAdapter {

    Context context;
    String[] names;
    String[] artists;
    String[] durations;
    String[] srcAudio;
    private static LayoutInflater inflater=null;

    private boolean isPlaySong = false;

    AudioPlay ap;


    public CustomListViewAdapter(MainActivity mainActivity, String[] audioNameList, String[] audioArtistList, String[] audioDurationList, String[] audioSrc) {
        context = mainActivity;
        names = audioNameList;
        artists = audioArtistList;
        durations = audioDurationList;
        srcAudio = audioSrc;

        context = mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return names.length;
    }
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView name;
        TextView artist;
        TextView duration;
        ImageButton srcSong;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder=new Holder();

        View rowView;
        rowView = inflater.inflate(R.layout.music_list, null);

        holder.name=(TextView) rowView.findViewById(R.id.txtName);
        holder.artist=(TextView) rowView.findViewById(R.id.txtArtist);
        holder.duration =(TextView) rowView.findViewById(R.id.txtDuration);
        holder.srcSong=(ImageButton) rowView.findViewById(R.id.imgBtnDownload);

        holder.name.setText(names[position]);
        holder.artist.setText(artists[position]);
        holder.duration.setText(durations[position]);
        holder.srcSong.setTag(srcAudio[position]);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {

                    if (isPlaySong == true) {
                        ap.StopSong();
                        isPlaySong = false;
                    }

                    ap = new AudioPlay(names[position], artists[position], durations[position], srcAudio[position]);
                    ap.PlaySong();
                    isPlaySong = true;

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(context, "You Clicked " + names[position], Toast.LENGTH_LONG).show();
            }
        });

        return rowView;
    }
}
