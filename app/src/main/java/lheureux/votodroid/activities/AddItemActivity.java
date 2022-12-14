package lheureux.votodroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import lheureux.votodroid.R;
import lheureux.votodroid.databinding.ActivityAddItemBinding;
import lheureux.votodroid.models.Question;
import lheureux.votodroid.services.QuestionService;


public class AddItemActivity extends AppCompatActivity {
    private ActivityAddItemBinding binding;
    private QuestionService questionService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        questionService = QuestionService.getInstance(this);

        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        EditText questionText = findViewById(R.id.editTextTextQuestion);

        binding.submit.setOnClickListener(view1 -> {
            Question question = new Question(questionText.getText().toString());
            try {
                questionService.insert(question);
                Intent intent= new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } catch (Exception e) {
                questionText.setError(e.getMessage());
            }
        });

    }
}