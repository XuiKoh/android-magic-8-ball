package my.cy.android_magic_8_ball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by cylim on 4/04/2016.
 */
public class HistoryArrayAdapter extends ArrayAdapter<QuestionResponseModel> {

    // View lookup cache
    private static class ViewHolder {
        TextView txtViewQuestion;
        TextView txtViewAnswer;
        ImageView userImage;
    }

    public HistoryArrayAdapter(Context context, ArrayList<QuestionResponseModel> historyList) {
        super(context, R.layout.item_history, historyList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        QuestionResponseModel history = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_history, parent, false);
            viewHolder.txtViewQuestion = (TextView) convertView.findViewById(R.id.txtViewQuestion);
            viewHolder.txtViewAnswer = (TextView) convertView.findViewById(R.id.txtViewAnswer);
            viewHolder.userImage  = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        new ImageDownloadTask(viewHolder.userImage).execute(history.getImage());

        viewHolder.txtViewQuestion.setText(history.getQuestion());
        viewHolder.txtViewAnswer.setText(history.getAnswer());
        // Return the completed view to render on screen
        return convertView;
    }

    private class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloadTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                Log.i("Image", params[0]);
                bitmap = downloadBitmap(params[0]);
            } catch(Exception e){
                Log.v("Image",e.getLocalizedMessage());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        }
    }
    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            if(urlConnection != null) {

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
                inputStream.close();
            }
        } catch (Exception e) {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

        }
        return null;
    }
}
