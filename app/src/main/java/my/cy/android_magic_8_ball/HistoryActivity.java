package my.cy.android_magic_8_ball;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by cylim on 4/04/2016.
 */
public class HistoryActivity extends AppCompatActivity {

    ArrayList<QuestionResponseModel> historyList = new ArrayList<QuestionResponseModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_history);

        // Construct the data source
        historyList = (ArrayList<QuestionResponseModel>) getIntent().getSerializableExtra("historyList");
        Log.i("History", historyList.toString());
        // Create the adapter to convert the array to views
        HistoryArrayAdapter adapter = new HistoryArrayAdapter(this, historyList);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.list_history);
        listView.setAdapter(adapter);
    }


}
