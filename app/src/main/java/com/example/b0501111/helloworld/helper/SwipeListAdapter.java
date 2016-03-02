package com.example.b0501111.helloworld.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.b0501111.helloworld.R;

import java.util.List;

/**
 * Created by B0501111 on 2016/2/4.
 */
public class SwipeListAdapter extends BaseAdapter{
    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<Movie> mMovieList;
    private String[] bgColors;

    public SwipeListAdapter(Activity activity, List<Movie> movieList){
        this.mActivity = activity;
        this.mMovieList = movieList;
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);
    }


    @Override
    public int getCount() {
        return mMovieList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMovieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mInflater == null)
            mInflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = mInflater.inflate(R.layout.list_row, null);

        TextView serial  = (TextView) convertView.findViewById(R.id.serial);
        TextView title = (TextView) convertView.findViewById(R.id.title);

        serial.setText(String.valueOf(mMovieList.get(position).id));
        title.setText(mMovieList.get(position).title);

        String color = bgColors[position % bgColors.length];
        serial.setBackgroundColor(Color.parseColor(color));

        return convertView;
    }
}
