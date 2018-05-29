package manakina.com.photogallery.helpers;

import android.net.Uri;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import manakina.com.photogallery.model.GalleryItem;


public class FlickrFetchr {
    public static final String TAG = "FlickrFetchr";

    private static final String ENDPOINT = "https://api.flickr.com/services/rest/";
    private static final String API_KEY = "f45a9a9428e728f5a58e797cf4e87586";
    private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";

    //public static final String PREF_SEARCH_QUERY = "searchQuery";

    //additional parameter
    private static final String PARAM_EXTRAS = "extras";
    private static final String METHOD_SEARCH = "flickr.photos.search";
    private static final String PARAM_TEXT = "text";

    //and its value, include a miniature version of the picture, if it is an available
    private static final String EXTRA_SMALL_URL = "url_s";

    //for using an object GalleryItem
    private static final String XML_PHOTO = "photo";


    /*this method get datas from URL and returns them as a array of bytes*/
    byte [] getUrlBytes (String urlSpec) throws IOException {

            URL url = new URL(urlSpec);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out  = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int bytesRead = 0;
            byte [] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            } out.close();


            return out.toByteArray();
        }
        finally {
            connection.disconnect();
        }
    }


    /*convert the result from method getUrlBytes() into String*/
    public String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    //builds URL-address of request and load its content
    //public void fetchItems () {
    public ArrayList <GalleryItem> fetchItems () {

              String url = Uri.parse(ENDPOINT).buildUpon()
                      .appendQueryParameter("method", METHOD_GET_RECENT)
                      .appendQueryParameter("api_key", API_KEY)
                      .appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
                      .build().toString();

        return downloadGalleryItems(url);
    }

    public ArrayList<GalleryItem> downloadGalleryItems(String  url) {
        ArrayList<GalleryItem> items = new ArrayList<GalleryItem>();

        try {
            String xmlString = getUrl(url);
            Log.i(TAG, "Received xml: " + xmlString);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));

            parseItems(items, parser);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (XmlPullParserException xppe) {
            Log.e(TAG, "Failed to parse items", xppe);
        }
        return items;
    }

    public ArrayList<GalleryItem> search(String query) {
        String url = Uri.parse(ENDPOINT).buildUpon()
                .appendQueryParameter("method", METHOD_SEARCH)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
                .appendQueryParameter(PARAM_TEXT, query)
                .build().toString();
        return downloadGalleryItems(url);
    }

    void parseItems(ArrayList<GalleryItem> items, XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.next();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG &&
                    XML_PHOTO.equals(parser.getName())) {
                String id = parser.getAttributeValue(null, "id");
                String caption = parser.getAttributeValue(null, "title");
                String smallUrl = parser.getAttributeValue(null, EXTRA_SMALL_URL);

                GalleryItem item = new GalleryItem();
                item.setId(id);
                item.setCaption(caption);
                item.setUrl(smallUrl);
                items.add(item);
            }

            eventType = parser.next();
        }
    }

}
