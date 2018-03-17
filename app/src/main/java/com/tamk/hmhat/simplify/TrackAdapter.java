package com.tamk.hmhat.simplify;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hmhat on 17.3.2018.
 */

public class TrackAdapter extends ArrayAdapter<Track> {

    private Context context;
    private int resource;
    private List<Track> data = null;

    public  TrackAdapter(Context context, int resource, List<Track> data) {
        super(context, resource, data);
        this.resource = resource;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        TrackHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new TrackHolder();
            holder.trackName = row.findViewById(R.id.song_name);
            holder.artistName = row.findViewById(R.id.performer);

            row.setTag(holder);
        } else {
            holder = (TrackHolder) row.getTag();
        }

        Track track = data.get(position);
        holder.trackName.setText(track.getName());
        holder.artistName.setText(track.getArtists()[0].getName());

        return row;
    }

    class TrackHolder {
        TextView trackName;
        TextView artistName;
    }

}
