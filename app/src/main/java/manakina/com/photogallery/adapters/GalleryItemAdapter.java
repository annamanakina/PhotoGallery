package manakina.com.photogallery.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;

import manakina.com.photogallery.R;
import manakina.com.photogallery.control.ImageLoaderView;
import manakina.com.photogallery.model.GalleryItem;


public class GalleryItemAdapter extends ArrayAdapter<GalleryItem> {
    private LayoutInflater layoutInflater;
    private int imageWidth;
    private Context context;
    private ArrayList<GalleryItem> items;


    public GalleryItemAdapter(Context context, ArrayList<GalleryItem> items, int width) {
        super(context, 0, items);
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.items = items;
        imageWidth = width;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.gallery_item, parent, false);
        }

        GalleryItem item = getItem(position);
        final ImageLoaderView imageView = (ImageLoaderView) convertView
                .findViewById(R.id.item_imageView);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                ((int) (imageWidth*0.95)), ((int) (imageWidth*0.95))));
        if (item != null) {
            imageView.setImageURL(item.getUrl());
        }
        return convertView;
    }

}
