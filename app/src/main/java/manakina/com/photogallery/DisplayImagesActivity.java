package manakina.com.photogallery;


import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import manakina.com.photogallery.adapters.GalleryItemAdapter;
import manakina.com.photogallery.helpers.FlickrFetchr;
import manakina.com.photogallery.model.GalleryItem;
import manakina.com.photogallery.helpers.Utils;

public class DisplayImagesActivity extends AppCompatActivity {
    public static final String PREF_COLUMN_AMOUNT = "columnAmount";

    private GridView mGridView;
    private ArrayList<GalleryItem> mItems;
    private String mQuery;
    private int columnAmount;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        mGridView = (GridView) findViewById(R.id.gridview);

        mQuery = getIntent().getStringExtra(FetchActivity.EXTRA_SEARCH_QUERY);
        columnAmount = getIntent().getIntExtra(FetchActivity.EXTRA_COLUMN_AMOUNT, -1);

        updateItems();
    }

    public void updateItems() {
        new FetchItemsTask().execute();
    }

    void setupAdapter() {
        if ( mGridView == null) return;

        if (mItems != null && (mItems.size() != 0)) {
            mGridView.setNumColumns(columnAmount);
            int imageWidth = Utils.getWindowWidth(this);
            mGridView.setColumnWidth(((int) (imageWidth*0.8))/columnAmount);
            mGridView.setAdapter(new GalleryItemAdapter(this, mItems, imageWidth/columnAmount));
        } else {
            TextView textViewError = (TextView) findViewById(R.id.textViewError);
            textViewError.setVisibility(View.VISIBLE);
            mGridView.setAdapter(null);
            mGridView.setVisibility(View.GONE);
        }
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,ArrayList<GalleryItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<GalleryItem> doInBackground(Void... params) {

            /*String query = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .getString(FlickrFetchr.PREF_SEARCH_QUERY, null);*/
            if (mQuery != null) {
                Log.i("TAG", "mQuery != null");
                return new FlickrFetchr().search(mQuery);

            } else {
                Log.i("TAG", "mQuery == null");
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryItem> items) {
            mItems = items;
            setupAdapter();
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.i("TAG", "DisplayImagesActivity onPause  ");
       /* PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(FlickrFetchr.PREF_SEARCH_QUERY, mQuery)
                .putInt(DisplayImagesActivity.PREF_COLUMN_AMOUNT, mColumnAmount)
                .commit();*/
    }


}
