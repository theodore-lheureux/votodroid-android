package lheureux.votodroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import lheureux.votodroid.R;
import lheureux.votodroid.databinding.ActivityVoteBinding;
import lheureux.votodroid.models.Question;
import lheureux.votodroid.models.Vote;
import lheureux.votodroid.services.QuestionService;
import lheureux.votodroid.services.VoteService;


public class VoteActivity extends AppCompatActivity {
    private ActivityVoteBinding binding;
    private VoteService voteService;
    private QuestionService questionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        voteService = VoteService.getInstance(this);
        questionService = QuestionService.getInstance(this);

        binding = ActivityVoteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Long questionId = getIntent().getLongExtra("questionId", 0);
        Question question = questionService.get(questionId);
        TextView tvQuestion = findViewById(R.id.tvQuestion);
        EditText voterName = findViewById(R.id.editTextTextPersonName);

        tvQuestion.setText(question.text);

        for (Question q : questionService.all()) {
            System.out.println(q.questionId);
        }

        binding.submit.setOnClickListener(view1 -> {
            try {
                RatingBar rating = findViewById(R.id.rating);
                float ratingValue = rating.getRating();
                Vote vote = new Vote(ratingValue, questionId, voterName.getText().toString());

                voteService.insert(vote);
                Intent intent= new Intent();
                setResult(RESULT_OK, intent);
                finish();

            } catch (Exception e) {
                voterName.setError(e.getMessage());
            }

        });

    }
}