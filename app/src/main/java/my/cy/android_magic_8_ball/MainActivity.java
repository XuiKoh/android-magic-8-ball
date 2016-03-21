package my.cy.android_magic_8_ball;

import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText questionEditText;
    ImageView imageBall;
    TextView answerTextView;
    Button shakeButton;

    MagicEightBallModel ball;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String name = "CY Lim";
        Double age = Double.valueOf(22);

        Log.i("1", "CY Lim");
        Log.i("1", String.format("%.2f", age));
        Log.i("1", name);

        String[] responses = {"Very doubtful", "You may rely on it", getEmojiByUnicode(0x1F44D)};
        ball = new MagicEightBallModel(responses);
        System.out.println(ball);

        ball.responseToQuestion("Will I get full marks for this lab");
        ball.responseToQuestion("Will the Cronulla sharks receive a premiership this year");
        ball.responseToQuestion("Will I end up becoming a cat person when I get old");


        // Lab 2
        questionEditText = (EditText) this.findViewById(R.id.questionEditText);
        imageBall = (ImageView) this.findViewById(R.id.imageViewBall);
        answerTextView = (TextView) this.findViewById(R.id.answerTextView);
        shakeButton = (Button) this.findViewById(R.id.shakeButton);

        imageBall.setAlpha(0.0f);

        shakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAnswer();
            }
        });

        questionEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                getAnswer();
                return false;
            }
        });
    }

    private void getAnswer(){
        final TypedArray ballImages = getResources().obtainTypedArray(R.array.ballImages);

        imageBall.setAlpha(0.0f);

        int random = (int)(Math.random() * ballImages.length());
        int drawableID = ballImages.getResourceId(random, -1);
        imageBall.setImageResource(drawableID);

        imageBall.animate().alpha(1.0f).setDuration(1000);

        answerTextView.setText(ball.responseToQuestion(questionEditText.getText().toString()));
    }

    // Java/Android should be able to process emoji, but it can't be show in log or the IDE,
    // mainly because they didn't built in the update charset features for it. .
    private String getEmojiByUnicode(int unicode){
        return String.valueOf(Character.toChars(unicode));
    }
}
