package manakina.com.photogallery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class FetchActivity extends AppCompatActivity {

    public static final String EXTRA_SEARCH_QUERY = "com.manakina.fetchpic.search_query";
    public static final String EXTRA_COLUMN_AMOUNT = "com.manakina.fetchpic.column_amount";

    private TextView mColumnAmountView, mStartSearchView;
    private EditText mSearchInput;
    private SeekBar mSeekBar;

    private String mQuery;
    private int mColumnAmount;
    private int inputType;


    class DoneOnEditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);

        //получить из сохранения
        /*String query = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(FlickrFetchr.PREF_SEARCH_QUERY, null);*/


        //условие если картинок нет вообще, то отобразить какой-то текст или тоаст
        mSearchInput = (EditText) findViewById(R.id.search_input);
        mSearchInput.setOnEditorActionListener(new DoneOnEditorActionListener());
        mSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               // mQuery = mSearchInput.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSeekBar = (SeekBar) findViewById(R.id.columns_option_seekbar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mColumnAmountView.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mColumnAmountView = (TextView) findViewById(R.id.columns_amount_textView);
        mColumnAmountView.setText(String.valueOf(mSeekBar.getProgress()));
        //mColumnAmount = mSeekBar.getProgress();
        mStartSearchView = (TextView) findViewById(R.id.search_textView);
        mStartSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mSearchInput.setText("leaf");
                if(!mSearchInput.getText().toString().equals("") && mSeekBar.getProgress() != 0) {
                    //Toast.makeText(FetchActivity.this, "go search", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(FetchActivity.this, DisplayImagesActivity.class);
                    intent.putExtra(EXTRA_SEARCH_QUERY, mSearchInput.getText().toString());
                    intent.putExtra(EXTRA_COLUMN_AMOUNT, mSeekBar.getProgress());
                    startActivity(intent);
                }

            }
        });
        //Log.i("TAG", "onCreate  " + query);
    }

   /* @Override
    protected void onStart() {
        super.onStart();
        Log.i("TAG", "onStart  ");
        //тут восстанавливать из PreferenceManager
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG", "onResume  ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TAG", "onPause  ");
        //это пока не надо
        /*PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(FlickrFetchr.PREF_SEARCH_QUERY, mQuery)
                .putInt(DisplayImagesActivity.PREF_COLUMN_AMOUNT, mColumnAmount)
                .commit();
    }*/


   /* @Override
    protected void onStop() {
        super.onStop();
        Log.i("TAG", "onStop  ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TAG", "onDestroy  ");
    }*/
}
