package my.cy.android_magic_8_ball;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

        //Set up relative layout
        RelativeLayout rLayout = new RelativeLayout(this);
        rLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));

        //Set up editText
        RelativeLayout.LayoutParams questionEditTextParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        questionEditTextParams.setMargins(20,20,20,20);
        questionEditTextParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        questionEditTextParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        questionEditText = new EditText(this);
        questionEditText.setHint("Ask a question...");
        questionEditText.setSingleLine(true);
        questionEditText.setLayoutParams(questionEditTextParams);

        // Setup for imageView
        RelativeLayout.LayoutParams imageBallParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        imageBallParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        imageBall = new ImageView(this);
        imageBall.setLayoutParams(imageBallParams);

        // Setup for TextView
        RelativeLayout.LayoutParams answerTextViewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        answerTextViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        answerTextView = new TextView(this);
        answerTextView.setTextColor(Color.WHITE);
        answerTextView.setLayoutParams(answerTextViewParams);

        //Setup shake button
        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        buttonParams.height = 150;
        buttonParams.setMargins(0,20,0,0);
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        shakeButton = new Button(this);
        shakeButton.setText("SHAKE");
        shakeButton.setLayoutParams(buttonParams);

        // Add to view
        rLayout.addView(questionEditText);
        rLayout.addView(imageBall);
        rLayout.addView(answerTextView);
        rLayout.addView(shakeButton);

        setContentView(rLayout);

        String[] responses = {"Very doubtful", "You may rely on it", getEmojiByUnicode(0x1F44D)};
        ball = new MagicEightBallModel(responses);

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
