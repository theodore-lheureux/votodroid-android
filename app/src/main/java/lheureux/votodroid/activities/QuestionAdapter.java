package lheureux.votodroid.activities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lheureux.votodroid.R;
import lheureux.votodroid.interfaces.onClickInterface;
import lheureux.votodroid.models.Question;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {
    public List<Question> list;
    Context context;
    lheureux.votodroid.interfaces.onClickInterface onClickInterface;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvQuestion;
        public LinearLayout questionContainer;
        public ImageButton btnResult;

        public MyViewHolder(LinearLayout v) {
            super(v);
            tvQuestion = v.findViewById(R.id.tvQuestion);
            questionContainer = v;
            btnResult = v.findViewById(R.id.btnResultButton);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public QuestionAdapter(Context context, List<Question> list, onClickInterface onClickInterface) {
        this.context = context;
        this.list = list;
        this.onClickInterface = onClickInterface;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public QuestionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        Log.i("DEBOGAGE", "appel a onCreateViewHolder");
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Question questionCourante = list.get(position);
        holder.tvQuestion.setText(questionCourante.text);
        holder.questionContainer.setOnClickListener(view -> {
            onClickInterface.setClick(0, questionCourante.questionId);
        });
        holder.btnResult.setOnClickListener(view -> {
            onClickInterface.setClick(1, questionCourante.questionId);
        });
        Log.i("DEBOGAGE", "appel a onBindViewHolder " + position);
    }

    // renvoie la taille de la liste
    @Override
    public int getItemCount() {
        return list.size();
    }

}
