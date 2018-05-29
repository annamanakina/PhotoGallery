package manakina.com.photogallery;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import manakina.com.photogallery.helpers.ThumbnailDownloader;
import manakina.com.photogallery.helpers.Utils;

public class DisplayImagesActivity extends AppCompatActivity {
    public static final String PREF_COLUMN_AMOUNT = "columnAmount";

    private GridView mGridView;
    private ArrayList<GalleryItem> mItems;
    private ThumbnailDownloader mThumbnailThread;
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

        //это убрать
        mThumbnailThread = new ThumbnailDownloader(new Handler());
        mThumbnailThread.start();
       // Log.i("TAG", "DisplayImagesActivity onCreate  ");
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
            mGridView.setAdapter(new GalleryItemAdapter(this, mThumbnailThread, mItems, imageWidth/columnAmount));
        } else {
            TextView textViewError = (TextView) findViewById(R.id.textViewError);
            textViewError.setVisibility(View.VISIBLE);
            mGridView.setAdapter(null);
            mGridView.setVisibility(View.GONE);
            //Log.i("TAG", "else");
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
                        //new FlickrFetchr().fetchItems();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryItem> items) {
            mItems = items;
            /*if (items.size() > 0) {
                String resultId = items.get(0).getId();
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putString(FlickrFetchr.PREF_LAST_RESULT_ID, resultId)
                        .commit();
            }*/

            setupAdapter();
            mProgressBar.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        mThumbnailThread.clearQueue();
        //Log.i("TAG", "DisplayImagesActivity onStop  ");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailThread.quit();
       // Log.i("TAG", "DisplayImagesActivity onDestroy  ");
    }


    @Override
    protected void onStart() {
        super.onStart();
      //  Log.i("TAG", "DisplayImagesActivity onStart  ");
    }

    @Override
    protected void onResume() {
        super.onResume();
       // Log.i("TAG", "DisplayImagesActivity onResume  ");
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
