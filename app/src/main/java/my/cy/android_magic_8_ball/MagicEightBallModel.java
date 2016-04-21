package my.cy.android_magic_8_ball;

/**
 * Created by cylim on 21/03/2016.
 */

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class MagicEightBallModel extends Object {

    private ArrayList<String> responseArray;

    private Context context;

    public MagicEightBallModel(Context context, String[] extraResponseArray){
        this.context = context;

        responseArray = new ArrayList<>();
        String[] initialResponseArray = {context.getResources().getString(R.string.response0), context.getResources().getString(R.string.response1)};
        Collections.addAll(responseArray, initialResponseArray);
        Collections.addAll(responseArray, extraResponseArray);
    }

    public String getResponse(Integer index) {
        return responseArray.get(index);
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
