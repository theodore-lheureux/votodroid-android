package lheureux.votodroid.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import lheureux.votodroid.models.Question;

@Dao
public interface QuestionDao {
    @Insert
    Long insert(Question v);

    @Query("SELECT * FROM Question")
    Question[] getAll();

    @Query("DELETE FROM Question")
    void deleteAll();

    @Query("SELECT * FROM Question ORDER BY (SELECT COUNT(*) FROM Vote WHERE questionId = Question.questionId) DESC, text")
    Question[] getAllOrdered();

    @Query("SELECT * FROM Question WHERE questionId = :questionId")
    Question get(Long questionId);

    @Query("SELECT COUNT(*) FROM Question")
    int count();

}
