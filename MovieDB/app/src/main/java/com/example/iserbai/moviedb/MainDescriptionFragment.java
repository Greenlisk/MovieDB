package com.example.iserbai.moviedb;

import android.app.Fragment;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by iserbai on 29.02.16.
 */
public class MainDescriptionFragment extends Fragment {
    private final String LOG_TAG = "DescriptionPragment";
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle onSave) {
        rootView = inflater.inflate(R.layout.fragment_description, viewGroup);
        Intent intent = getActivity().getIntent();

        if (intent.getAction() != Intent.ACTION_MAIN) {
            Log.v(LOG_TAG, "Intent: " + intent.getExtras().getLong("movie_id"));
            Log.v(LOG_TAG, "Intent: " + intent.getAction());
            new FetchMovie().execute(intent.getExtras().getLong("movie_id"));
        }
        return rootView;
    }

    private class FetchMovie extends AsyncTask<Long, Void, Movie> {
        @Override
        public Movie doInBackground(Long... id) {
            return MoviesExtractor.getMovie(id[0]);
        }

        @Override
        public void onPostExecute(Movie result) {
            TextView overview = (TextView)rootView.findViewById(R.id.overview);
            overview.setText(result.overview);
            ImageView poster = (ImageView)rootView.findViewById(R.id.poster_for_description);
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w342/" + result.posterPath).into(poster);
            TextView title = (TextView)rootView.findViewById(R.id.title_view);
            title.setText(result.originalTitle);
        }
    }
}
