package my.cy.android_magic_8_ball;


import java.io.Serializable;

/**
 * Created by cylim on 4/04/2016.
 */

// Lab 4.1
public class QuestionResponseModel extends Object implements Serializable {
    private String question;
    private String answer;

    public QuestionResponseModel(String question, String answer){
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return "QuestionResponseModel{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
