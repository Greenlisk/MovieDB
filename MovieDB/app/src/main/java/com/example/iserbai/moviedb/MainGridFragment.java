package com.example.iserbai.moviedb;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by iserbai on 29.02.16.
 */
public class MainGridFragment extends Fragment implements GridView.OnItemClickListener {
    private final String LOG_TAG = "MainGridFragment";
    private MoviesExtractor extractor;
    private int pages = 0;
    ImageAdapter adapter;
    View rootView;
    private boolean sortPop = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle onSave) {
        rootView = inflater.inflate(R.layout.fragment_grid, viewGroup);
        setHasOptionsMenu(true);
        GridView gridView = (GridView)rootView.findViewById(R.id.posters_grid);
        adapter = new ImageAdapter(getActivity());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        extractor = new MoviesExtractor(MoviesExtractor.POPULAR);
        new FetchPage().execute();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_sort, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_popular: {
                if (sortPop) {
                    break;
                }
                extractor.clear();
                extractor.setSortOrder(MoviesExtractor.POPULAR);
                adapter.clear();
                pages = 0;
                new FetchPage().execute();
                break;
            }
            case R.id.sort_top_rated: {
                if (!sortPop) {
                    break;
                }
                extractor.clear();
                extractor.setSortOrder(MoviesExtractor.RATED);
                adapter.clear();
                pages = 0;
                new FetchPage().execute();
                break;
            }

        }
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            new FetchMovie().execute(extractor.moviesList.get(position).id);
        } else {
            Intent intent = new Intent(getActivity(), DescriptionActivity.class);
            intent.putExtra("movie_id", extractor.moviesList.get(position).id);
            startActivity(intent);
        }
        Log.v(LOG_TAG, "Position: " + position);
    }

    private class FetchMovie extends AsyncTask<Long, Void, Movie> {
        @Override
        public Movie doInBackground(Long... id) {
            return MoviesExtractor.getMovie(id[0]);
        }

        @Override
        public void onPostExecute(Movie result) {
            TextView overview = (TextView)getActivity().findViewById(R.id.overview);
            overview.setText(result.overview);
            ImageView poster = (ImageView)getActivity().findViewById(R.id.poster_for_description);
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w342/" + result.posterPath).into(poster);
            TextView title = (TextView)getActivity().findViewById(R.id.title_view);
            title.setText(result.originalTitle);
        }
    }

    private class FetchPage extends AsyncTask<Integer, Void, Boolean> {
        @Override
        public Boolean doInBackground(Integer... none) {
            if (!extractor.getPage(++pages)){
                Log.e(LOG_TAG, "Didn't fetch!!!!");
                return false;
            }
            return true;
        }

        @Override
        public void onPostExecute(Boolean res) {
            if (!res) {
                return;
            }
            for(int i = 0; i < 20; ++i) {
                adapter.add(extractor.moviesList.get((pages-1)*10 + i).posterPath);
            }
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<String> posters;



        public ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            posters = new ArrayList<>(20);

        }

        public void add(String poster) {
            posters.add(poster);
            notifyDataSetChanged();
        }

        public void clear() {
            posters.clear();
            notifyDataSetChanged();
        }

        public int getCount() {
            return posters.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            View view;
            if (convertView == null) {
                view = inflater.inflate(R.layout.grid_tem, parent, false);
            } else {
                view = convertView;
            }
            imageView = (ImageView) view.findViewById(R.id.image_cell);

            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + posters.get(position)).into(imageView);
            return imageView;
        }
    }
}
