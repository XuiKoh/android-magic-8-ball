package my.cy.android_magic_8_ball;

/**
 * Created by cylim on 21/03/2016.
 */

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class MagicEightBallModel extends Object {

    private static String[] initialResponseArray = {"My sources say no", "It is certain"};
    private ArrayList<String> responseArray;

    public String getResponse(Integer index) {
        return responseArray.get(index);
    }

    public MagicEightBallModel(){
        responseArray = new ArrayList<String>();
        Collections.addAll(responseArray, initialResponseArray);
    }

    public MagicEightBallModel(String[] extraResponseArray){
        responseArray = new ArrayList<String>();
        Collections.addAll(responseArray, initialResponseArray);
        Collections.addAll(responseArray, extraResponseArray);
    }

    public Integer responseToQuestion(String question) {
        Log.i("Question", question);
        int random = (int )(Math.random() * responseArray.size());
        Log.i("Answer", responseArray.get(random));


        return random;
    }



    public String toString() {
        String description = "";
        for (int i=0; i < responseArray.size(); i++){
            description = description + " " + responseArray.get(i);
        }

        return description;
    }
}
