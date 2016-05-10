package my.cy.android_magic_8_ball;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextToSpeech textToSpeech;

    EditText questionEditText;
    ImageView imageBall;
    TextView answerTextView;
    Button shakeButton;

    MagicEightBallModel ball;

    ArrayList<QuestionResponseModel> historyList = new ArrayList<>();
    String fileName = "historyData.bin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lab 1
//        String name = "CY Lim";
//        Double age = Double.valueOf(22);
//
//        Log.i("1", "CY Lim");
//        Log.i("1", String.format("%.2f", age));
//        Log.i("1", name);
//
        String[] responses = {getEmojiByUnicode(0x1F44D)};
        ball = new MagicEightBallModel(this, responses);
//        System.out.println(ball);
//
//        ball.responseToQuestion("Will I get full marks for this lab");
//        ball.responseToQuestion("Will the Cronulla sharks receive a premiership this year");
//        ball.responseToQuestion("Will I end up becoming a cat person when I get old");


        // Lab 2
        questionEditText = (EditText) this.findViewById(R.id.questionEditText);
        imageBall = (ImageView) this.findViewById(R.id.imageViewBall);
        answerTextView = (TextView) this.findViewById(R.id.answerTextView);
        shakeButton = (Button) this.findViewById(R.id.shakeButton);

        imageBall.setAlpha(0.0f);

        // Lab 4.8
        shakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent nextActivity = new Intent(MainActivity.this, HistoryActivity.class);
                    nextActivity.putExtra("historyList", historyList);
                    startActivity(nextActivity);
                }catch(Exception e){
                    Log.v("Error", e.toString());
                }
            }
        });

        questionEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(questionEditText.getWindowToken(), 0);
                getAnswer();
                return false;
            }
        });

        // Lab 4.5
//        historyList = loadSerializedObject(this);
//        Log.i("QuestionResponseModel", historyList.toString());

        // Lab 7
        textToSpeech = new TextToSpeech(this, null);
    }

    private void getAnswer() {
        final TypedArray ballImages = getResources().obtainTypedArray(R.array.ballImages);


        imageBall.setAlpha(0.0f);

        int random = (int) (Math.random() * ballImages.length());
        int drawableID = ballImages.getResourceId(random, -1);
        imageBall.setImageResource(drawableID);

        imageBall.animate().alpha(1.0f).setDuration(1000);



        String questionText = questionEditText.getText().toString();

        Integer responseNum = ball.responseToQuestion(questionText);
        String answerText = ball.getResponse(responseNum);
        answerTextView.setText(answerText);

        // Lab 8, 9
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //Firstly check the network connection:
        if (networkInfo != null && networkInfo.isConnected()) {
            new PostEntry().execute(questionText, answerText);
        } else {
            Log.e("Error", "Networking not available!");
        }

        // Lab 4.3
//        historyList.add(new QuestionResponseModel(questionText, answerText));
//        Log.i("QuestionResponseModel", historyList.toString());

        // Lab 4.4
//        saveObject(this);

        // Lab 6 and 7
        playSound(responseNum);

        ballImages.recycle();
    }

    private void playSound(Integer responseNum){
        if(responseNum > 1){
            // Lab 6
            final TypedArray audio = getResources().obtainTypedArray(R.array.audio);
            int audioID = audio.getResourceId(responseNum, -1);
            MediaPlayer mp = MediaPlayer.create(this, audioID);
            try {
                mp.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            audio.recycle();
        } else {
            // Lab 7
            // Default TTS engine doesn't support Chinese, I added German as a backup language
            textToSpeech.setLanguage(Locale.getDefault());
            textToSpeech.speak(answerTextView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);

        }

    }

    // Lab 4.4
//    public void saveObject(Context context) {
//        try {
//            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
//            objectOutputStream.writeObject(historyList);    // write the class as an 'object'
//            objectOutputStream.flush();                     // flush the stream to insure all of the information was written to 'save_object.bin'
//            objectOutputStream.close();                     // close the stream
//        } catch (Exception ex) {
//            Log.v("Error : ", ex.getMessage());
//            ex.printStackTrace();
//        }
//    }

//    public ArrayList<QuestionResponseModel> loadSerializedObject(Context context) {
//        ArrayList<QuestionResponseModel> historyData = null;
//        try {
//            FileInputStream fileInputStream = context.openFileInput(fileName);
//            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//            historyData = (ArrayList<QuestionResponseModel>) objectInputStream.readObject();
//
//        } catch (Exception ex) {
//            Log.v("Error : ", ex.getMessage());
//            ex.printStackTrace();
//        }
//        return historyData;
//    }

    // Java/Android should be able to process emoji, but it can't be show in log or the IDE,
    // mainly because they didn't built in the update charset features for it. .
    protected String getEmojiByUnicode(int unicode) {
        return String.valueOf(Character.toChars(unicode));
    }

    // Async Task
    class PostEntry extends AsyncTask<String, Void, Boolean> {

        private String postURL = "http://li859-75.members.linode.com/addEntry.php";
        private String user = "cyl851";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... parameter) {
            try {
                String postString = "username="+user+"&question="+parameter[0]+"&answer="+parameter[1];

                URL url = new URL(postURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                connection.setChunkedStreamingMode(0);

                connection.connect();
                OutputStream os = connection.getOutputStream();
                os.write(postString.getBytes("UTF-8"));
                os.close();

                int response = connection.getResponseCode();
                if (response == 200) {
                    Log.i("POST", "Sucess");
                }
                connection.disconnect();
            } catch (IOException e) {
                //exception
                Log.v("exception",e.getLocalizedMessage());

            }

            return true;
        }

        protected void onPostExecute(Boolean result) {

        }
    }
}
