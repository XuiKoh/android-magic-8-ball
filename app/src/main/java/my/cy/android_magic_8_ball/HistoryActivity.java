package my.cy.android_magic_8_ball;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by cylim on 4/04/2016.
 */
public class HistoryActivity extends AppCompatActivity {

    ArrayList<QuestionResponseModel> historyList = new ArrayList<>();
    private HistoryArrayAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_history);

        // Construct the data source
        //historyList = (ArrayList<QuestionResponseModel>) getIntent().getSerializableExtra("historyList");
        //Log.i("History", historyList.toString());
        // Create the adapter to convert the array to views
        //HistoryArrayAdapter adapter = new HistoryArrayAdapter(this, historyList);
        // Attach the adapter to a ListView
        listView = (ListView) findViewById(R.id.list_history);
        //listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new GetAllEntryTask().execute();
        } else {
            Log.v("info","network connection unaavailable");
            //textView.setText("No network connection available.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void populateListView(){
        adapter = new HistoryArrayAdapter(this, historyList);
        listView.setAdapter(adapter);
        listView.refreshDrawableState();
    }


    private class GetAllEntryTask extends AsyncTask<String, Integer,
                ArrayList<QuestionResponseModel>> {
        private String getURL = "http://li859-75.members.linode.com/retrieveAllEntries.php";

        protected ArrayList<QuestionResponseModel> getAllEntry() throws IOException {
            InputStream is = null;
            try {
                URL url = new URL(getURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000); /*milliseconds*/
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int response = connection.getResponseCode(); //If 200, continue 
                if (response == 200) {
                    is = connection.getInputStream();
                    JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
                    try {
                        historyList = new ArrayList<QuestionResponseModel>();
                        reader.beginArray();
                        while (reader.hasNext()) {
                            String question = null;
                            String answer = null;
                            String image = null;
                            reader.beginObject();
                            while (reader.hasNext()) {
                                String currentkey = reader.nextName();
                                if (currentkey.equals("question")) {
                                    question = reader.nextString();
                                } else if (currentkey.equals("answer")) {
                                    answer = reader.nextString();
                                } else if (currentkey.equals("imageURL")) {
                                    image = reader.nextString();
                                } else {
                                    reader.skipValue();
                                }
                            }
                            reader.endObject();

                            historyList.add(new QuestionResponseModel(question, answer, image));
                        }
                        reader.endArray();
                        reader.close();

                        return historyList;
                    } catch (IOException e) {
                        //exception
                    }

                }
            }finally{
                if (is != null) {
                    is.close();
                }
            }
            return null;

        }

        protected ArrayList<QuestionResponseModel> doInBackground(String... urls){
            try {
                return getAllEntry();
            } catch (IOException e) {
                return null;
            }
        }

        protected void onPostExecute(ArrayList<QuestionResponseModel> result){
            populateListView();
        }
    }
}
