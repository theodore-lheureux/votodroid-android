package lheureux.votodroid.services;

import android.content.Context;

import java.util.Arrays;
import java.util.List;

import lheureux.votodroid.database.AppDatabase;
import lheureux.votodroid.exceptions.InvalidQuestion;
import lheureux.votodroid.models.Question;

public final class QuestionService {

    private static final QuestionService instance = new QuestionService();

    private AppDatabase db;

    public static synchronized QuestionService getInstance(Context context) {
        instance.db = AppDatabase.getInstance(context);
        return instance;
    }

    public void insert(Question question) throws InvalidQuestion {
        // Validation
        if (question.text == null || question.text.trim().length() == 0) throw new InvalidQuestion("Question vide");
        if (question.text.trim().length() < 5) throw new InvalidQuestion("Question trop courte");
        if (question.text.trim().length() > 255) throw new InvalidQuestion("Question trop longue");
        if (question.questionId != null) throw new InvalidQuestion("Id non nul. La BD doit le gérer");

        // Doublon du texte de la question
        for (Question q : all()){
            if (q.text.equalsIgnoreCase(question.text)){
                    throw new InvalidQuestion("Question existante");
            }
        }

        // Ajout
        question.questionId = db.questionDao().insert(question);
    }
    
    public List<Question> all() {
        return Arrays.asList(db.questionDao().getAllOrdered());
    }
	
	public void deleteAll(){
        db.questionDao().deleteAll();
	}

    public Question get(Long questionId) {
        return db.questionDao().get(questionId);
    }

    public int count() {
        return db.questionDao().count();
    }

}
