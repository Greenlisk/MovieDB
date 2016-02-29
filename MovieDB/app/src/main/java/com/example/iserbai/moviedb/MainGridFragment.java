package com.example.iserbai.moviedb;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by iserbai on 29.02.16.
 */
public class MainGridFragment extends Fragment {
    private final String LOG_TAG = "MainGridFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle onSave) {
        View rootView = inflater.inflate(R.layout.fragment_grid, viewGroup);
        GridView gridView = (GridView)rootView.findViewById(R.id.posters_grid);
        if (gridView == null) {
            Log.v(LOG_TAG, "NULL GRIDVIEW!!!!");}
        gridView.setAdapter(new ImageAdapter(getActivity()));
        return rootView;
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private Integer[] imagesArray;

        public ImageAdapter(Context context) {
            this.context = context;
            imagesArray = new Integer[20];
            for (int i = 0; i < imagesArray.length; ++i) {
                imagesArray[i] = R.drawable.stars;
            }
        }

        public int getCount() {
            return imagesArray.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(getActivity());
            } else {
                imageView = (ImageView)convertView;
            }
            imageView.setImageResource(imagesArray[position]);
            return imageView;
        }
    }
}
