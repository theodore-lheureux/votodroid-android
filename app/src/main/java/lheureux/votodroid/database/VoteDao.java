package lheureux.votodroid.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import lheureux.votodroid.models.Vote;

@Dao
public interface VoteDao {
    @Insert
    Long insert(Vote v);

    @Query("SELECT * FROM Vote")
    Vote[] getAll();

    @Query("SELECT COUNT(*) FROM Vote")
    int count();

    @Query("DELETE FROM Vote")
    void deleteAll();

    @Query("DELETE FROM Vote WHERE questionId = :questionId")
    void deleteAllForQuestion(Long questionId);

    @Query("SELECT * FROM Vote WHERE questionId = :questionId")
    Vote[] getVotesForQuestion(Long questionId);

    @Query("SELECT COUNT(*) FROM Vote WHERE questionId = :questionId AND value = 1")
    int getVoteCountForQuestion(Long questionId);

    @Query("SELECT AVG(value) FROM Vote WHERE questionId = :questionId")
    double getAverageVoteForQuestion(Long questionId);

}
