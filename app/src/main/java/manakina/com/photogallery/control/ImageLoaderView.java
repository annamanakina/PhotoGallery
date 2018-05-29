package manakina.com.photogallery.control;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Anna on 27.05.2018.
 */

public class ImageLoaderView extends LinearLayout {

    private ImageView imageView;
    private ProgressBar progressBar;
    private Context context;

    public ImageLoaderView(Context context) {
        super(context);
        init(context, null);
    }

    public ImageLoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ImageLoaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(21)
    public ImageLoaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        this.context = context;
        setGravity(Gravity.CENTER);
        imageView = new ImageView(context);
        LinearLayout.LayoutParams params = new LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        progressBar = new ProgressBar(context);
        progressBar.setVisibility(GONE);

        addView(imageView);
        addView(progressBar);
    }


    public ImageView getImageView() {
        return imageView;
    }

    public void setImageURL(String url) {
        loadingImage(true);


        Picasso.with(context)
                .load(url)
                //.error(R.drawable.brian_up_close)
                .into(imageView, new Callback(){
                    @Override
                    public void onSuccess() {
                        loadingImage(false);
                    }

                    @Override
                    public void onError() {
                         loadingImage(false);
                    }
                });
    }

    public void loadingImage(boolean loading) {
        if (loading) {
            imageView.setVisibility(GONE);
            progressBar.setVisibility(VISIBLE);
        } else {
            progressBar.setVisibility(GONE);
            imageView.setVisibility(VISIBLE);
        }
    }

}
