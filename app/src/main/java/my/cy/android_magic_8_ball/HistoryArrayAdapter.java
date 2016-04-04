package my.cy.android_magic_8_ball;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cylim on 4/04/2016.
 */
public class HistoryArrayAdapter extends ArrayAdapter<QuestionResponseModel> {

    // View lookup cache
    private static class ViewHolder {
        TextView txtViewQuestion;
        TextView txtViewAnswer;
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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.txtViewQuestion.setText(history.getQuestion());
        viewHolder.txtViewAnswer.setText(history.getAnswer());
        // Return the completed view to render on screen
        return convertView;
    }

}
