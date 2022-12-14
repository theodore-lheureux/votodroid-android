package lheureux.votodroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lheureux.votodroid.activities.AddItemActivity;
import lheureux.votodroid.activities.QuestionAdapter;
import lheureux.votodroid.activities.ResultActivity;
import lheureux.votodroid.activities.VoteActivity;
import lheureux.votodroid.databinding.ActivityMainBinding;
import lheureux.votodroid.exceptions.InvalidQuestion;
import lheureux.votodroid.exceptions.InvalidVote;
import lheureux.votodroid.models.Question;
import lheureux.votodroid.models.Vote;
import lheureux.votodroid.services.QuestionService;
import lheureux.votodroid.services.VoteService;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private QuestionAdapter questionAdapter;
    private QuestionService questionService;
    private VoteService voteService;

    private lheureux.votodroid.interfaces.onClickInterface onClickInterface;

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            remplirRecycler();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        questionService = QuestionService.getInstance(this);
        voteService = VoteService.getInstance(this);

        binding.addButton.setOnClickListener(view1 -> {
            Intent i = new Intent(MainActivity.this, AddItemActivity.class);
            activityLauncher.launch(i);
        });

        onClickInterface = ((clickedId, questionId) -> {
            switch ((int) clickedId) {
                case 0:
                    Intent i = new Intent(MainActivity.this, VoteActivity.class);
                    i.putExtra("questionId", questionId);
                    activityLauncher.launch(i);
                    break;
                case 1:
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra("questionId", questionId);
                    activityLauncher.launch(intent);
                    break;
                default:
                    break;
            }
        });

        initRecycler();
        remplirRecycler();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete_questions:
                questionService.deleteAll();
                remplirRecycler();
                return true;
            case R.id.menu_delete_votes:
                voteService.deleteAll();
                remplirRecycler();
                return true;
            case R.id.menu_add_questions:
                if (voteService.count() > 0 && questionService.count() > 0)
                    return true;
                try {
                    questionService.insert(new Question("Que pensez-vous de Java ?"));
                    questionService.insert(new Question("Que pensez-vous de Kotlin ?"));
                    questionService.insert(new Question("Que pensez-vous de Python ?"));
                    questionService.insert(new Question("Que pensez-vous de C ?"));
                    questionService.insert(new Question("Que pensez-vous de C++ ?"));
                    questionService.insert(new Question("Que pensez-vous de C# ?"));
                    questionService.insert(new Question("Que pensez-vous de JavaScript ?"));
                    questionService.insert(new Question("Que pensez-vous de PHP ?"));
                    questionService.insert(new Question("Que pensez-vous de Ruby ?"));
                    questionService.insert(new Question("Que pensez-vous de Swift ?"));
                    questionService.insert(new Question("Que pensez-vous de Go ?"));
                    questionService.insert(new Question("Que pensez-vous de Rust ?"));
                    questionService.insert(new Question("Que pensez-vous de Dart ?"));
                    questionService.insert(new Question("Que pensez-vous de Scala ?"));
                    questionService.insert(new Question("Que pensez-vous de Groovy ?"));
                    questionService.insert(new Question("Que pensez-vous de Perl ?"));
                    questionService.insert(new Question("Que pensez-vous de Haskell ?"));
                    questionService.insert(new Question("Que pensez-vous de Lisp ?"));
                    questionService.insert(new Question("Que pensez-vous de Prolog ?"));
                    questionService.insert(new Question("Que pensez-vous de Erlang ?"));
                } catch (InvalidQuestion invalidQuestion) {
                    invalidQuestion.printStackTrace();
                }

                for (Question question : questionService.all()) {
                    int numberOfVotes = (int) (Math.random() * 50) + 50;

                    for (int i = 0; i < numberOfVotes; i++) {
                        try {
                            voteService.insert(new Vote((int) (Math.random() * 6), question.questionId, "user" + i));
                        } catch (InvalidVote e) {
                            e.printStackTrace();
                        }
                    }
                }
                remplirRecycler();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void remplirRecycler() {
        questionAdapter.list.clear();
        questionAdapter.list.addAll(questionService.all());
        questionAdapter.notifyDataSetChanged();
    }

    private void initRecycler(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        questionAdapter = new QuestionAdapter(this, new ArrayList<Question>(), onClickInterface);
        recyclerView.setAdapter(questionAdapter);
    }

}