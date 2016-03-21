package my.cy.android_magic_8_ball;

/**
 * Created by cylim on 21/03/2016.
 */

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by cylim on 7/03/2016.
 */
public class MagicEightBallModel extends Object {

    private static String[] initialResponseArray = {"My sources say no", "It is certain"};
    private ArrayList<String> responseArray;

    public MagicEightBallModel(){
        responseArray = new ArrayList<String>();
        Collections.addAll(responseArray, initialResponseArray);
    }

    public MagicEightBallModel(String[] extraResponseArray){
        responseArray = new ArrayList<String>();
        Collections.addAll(responseArray, initialResponseArray);
        Collections.addAll(responseArray, extraResponseArray);
    }

    public String responseToQuestion(String question) {
        Log.i("Question", question);
        int random = (int )(Math.random() * responseArray.size());
        Log.i("Answer", responseArray.get(random));

        return responseArray.get(random);
    }

    public String toString() {
        String description = "";
        for (int i=0; i < responseArray.size(); i++){
            description = description + " " + responseArray.get(i);
        }

        return description;
    }
}
