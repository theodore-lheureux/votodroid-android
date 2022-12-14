package lheureux.votodroid;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import lheureux.votodroid.exceptions.InvalidQuestion;
import lheureux.votodroid.exceptions.InvalidVote;
import lheureux.votodroid.models.Question;
import lheureux.votodroid.models.Vote;
import lheureux.votodroid.services.QuestionService;
import lheureux.votodroid.services.VoteService;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServiceTests {

    private QuestionService questionService;
    private VoteService voteService;

    // S'exécute avant chacun des tests. Crée une BD en mémoire
    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        questionService = QuestionService.getInstance(context);
        voteService = VoteService.getInstance(context);
    }

    @Test(expected = InvalidQuestion.class)
    public void B_ajoutQuestionKOVide() throws InvalidQuestion {
        Question question = new Question();
        question.text = "";
        questionService.insert(question);

        Assert.fail("Exception InvalidQuestion non lancée");
    }

    @Test(expected = InvalidQuestion.class)
    public void C_ajoutQuestionKOCourte() throws InvalidQuestion {
        Question question = new Question();
        question.text = "aa";
        questionService.insert(question);

        Assert.fail("Exception InvalidQuestion non lancée");
    }

    @Test(expected = InvalidQuestion.class)
    public void D_ajoutQuestionKOLongue() throws InvalidQuestion {
        Question question = new Question();
        for (int i = 0 ; i < 256 ; i ++) question.text += "aa";
        questionService.insert(question);

        Assert.fail("Exception InvalidQuestion non lancée");
    }

    @Test(expected = InvalidQuestion.class)
    public void E_ajoutQuestionKOIDFixe() throws InvalidQuestion {
        Question question = new Question();
        question.text
                = "aaaaaaaaaaaaaaaa";
        question.questionId = 5L;
        questionService.insert(question);

        Assert.fail("Exception InvalidQuestion non lancée");
    }

    @Test
    public void FA_deleteAllQuestions() {
        questionService.deleteAll();
        Assert.assertEquals(0, questionService.all().size());
    }

    @Test
    public void F_ajoutQuestionOK() throws InvalidQuestion {
        Question question = new Question();
        question.text
                = "Aimes-tu les brownies au chocolat?";
        questionService.insert(question);

        Assert.assertNotNull(question.questionId);
    }


    @Test(expected = InvalidQuestion.class)
    public void G_ajoutQuestionKOExiste() throws InvalidQuestion {
        Question question = new Question();
        Question question2 = new Question();

        question.text
                = "Aimes-tu les brownies au chocolat?";
        question2.text
                = "Aimes-tu les BROWNIES au chocolAT?";

        questionService.insert(question);
        questionService.insert(question2);

        //TODO Ce test va fail tant que vous n'implémenterez pas toutesLesQuestions() dans ServiceImplementation
        Assert.fail("Exception InvalidQuestion non lancée");
    }

    @Test
    public void H_allQuestions() throws InvalidQuestion {
        questionService.deleteAll();

        Question question = new Question();
        question.text = "Aimes-tu les brownies au chocolat?";
        questionService.insert(question);

        Assert.assertEquals(1, questionService.all().size());
    }

    @Test
    public void I_getQuestion() throws InvalidQuestion {
        questionService.deleteAll();

        Question question = new Question();
        question.text = "Aimes-tu les brownies au chocolat?";
        questionService.insert(question);

        Question question2 = new Question();
        question2.text = "Aimes-tu les brownies au chocolat 2?";
        questionService.insert(question2);

        Assert.assertTrue(question2.text.equals(questionService.get(question2.questionId).text));
    }

    @Test
    public void J_countQuestions() throws InvalidQuestion {
        questionService.deleteAll();

        Question question = new Question();
        question.text = "Aimes-tu les brownies au chocolat?";
        questionService.insert(question);

        Question question2 = new Question();
        question2.text = "Aimes-tu les brownies au chocolat 2?";
        questionService.insert(question2);

        Assert.assertEquals(2, questionService.count());
    }

    // test all methods of VoteService
    @Test(expected = InvalidVote.class)
    public void K_insertVoteInvalidName() throws InvalidQuestion, InvalidVote {
        questionService.deleteAll();
        voteService.deleteAll();

        Question question = new Question();
        question.text = "Aimes-tu les brownies au chocolat?";
        questionService.insert(question);

        Vote vote = new Vote();
        vote.questionId = question.questionId;
        vote.voterName = "a";
        vote.value = 1;
        voteService.insert(vote);

        Assert.fail();
    }

    @Test(expected = InvalidVote.class)
    public void L_insertVoteInvalidValue() throws InvalidQuestion, InvalidVote {
        questionService.deleteAll();
        voteService.deleteAll();

        Question question = new Question();
        question.text = "Aimes-tu les brownies au chocolat?";
        questionService.insert(question);

        Vote vote = new Vote();
        vote.questionId = question.questionId;
        vote.voterName = "aaaa";
        vote.value = -1;
        voteService.insert(vote);

        Assert.fail();
    }

    @Test(expected = InvalidVote.class)
    public void M_insertVoteInvalidValue2() throws InvalidQuestion, InvalidVote {
        questionService.deleteAll();
        voteService.deleteAll();

        Question question = new Question();
        question.text = "Aimes-tu les brownies au chocolat?";
        questionService.insert(question);

        Vote vote = new Vote();
        vote.questionId = question.questionId;
        vote.voterName = "aaaa";
        vote.value = 6;
        voteService.insert(vote);

        Assert.fail();
    }

    @Test(expected = InvalidVote.class)
    public void N_insertVoteInvalidName2() throws InvalidQuestion, InvalidVote {
        questionService.deleteAll();
        voteService.deleteAll();

        Question question = new Question();
        question.text = "Aimes-tu les brownies au chocolat?";
        questionService.insert(question);

        Vote vote = new Vote();
        vote.questionId = question.questionId;
        vote.voterName = "aaa             ";
        vote.value = 3;
        voteService.insert(vote);

        Assert.fail();
    }

    @Test(expected = SQLiteConstraintException.class)
    public void O_insertVoteInvalidQuestionId() throws InvalidQuestion, InvalidVote {
        questionService.deleteAll();
        voteService.deleteAll();

        Question question = new Question();
        question.text = "Aimes-tu les brownies au chocolat?";
        questionService.insert(question);

        Vote vote = new Vote();
        vote.questionId = -1L;
        vote.voterName = "aaaa";
        vote.value = 3;
        voteService.insert(vote);

        Assert.fail();
    }

    @Test(expected = InvalidVote.class)
    public void P_insertVoteInvalidId() throws InvalidQuestion, InvalidVote {
        questionService.deleteAll();
        voteService.deleteAll();

        Question question = new Question();
        question.text = "Aimes-tu les brownies au chocolat?";
        questionService.insert(question);

        Vote vote = new Vote();
        vote.voteId = 1L;
        vote.questionId = question.questionId;
        vote.voterName = "aaaa";
        vote.value = 3;
        voteService.insert(vote);

        Assert.fail();
    }

    @Test
    public void Q_insertVoteOk() throws InvalidQuestion, InvalidVote {
        questionService.deleteAll();
        voteService.deleteAll();

        Question question = new Question();
        question.text = "Aimes-tu les brownies au chocolat?";
        questionService.insert(question);

        Vote vote = new Vote();
        vote.questionId = question.questionId;
        vote.voterName = "aaaa";
        vote.value = 3;
        voteService.insert(vote);

        Assert.assertEquals(1, voteService.count());
    }

    @Test
    public void R_moyenneVote() throws InvalidQuestion, InvalidVote {
        questionService.deleteAll();
        voteService.deleteAll();

        Question question = new Question();
        question.text = "Aimes-tu les brownies au chocolat?";
        questionService.insert(question);

        Vote vote = new Vote();
        vote.questionId = question.questionId;
        vote.voterName = "aaaa1";
        vote.value = 3;
        voteService.insert(vote);

        Vote vote1 = new Vote();
        vote1.questionId = question.questionId;
        vote1.voterName = "aaaaa2";
        vote1.value = 3;
        voteService.insert(vote1);

        Vote vote2 = new Vote();
        vote2.questionId = question.questionId;
        vote2.voterName = "aaaa3";
        vote2.value = 4;
        voteService.insert(vote2);

        Vote vote3 = new Vote();
        vote3.questionId = question.questionId;
        vote3.voterName = "aaaa4";
        vote3.value = 4;
        voteService.insert(vote3);

        Assert.assertEquals(3.5, voteService.voteAvgForQuestion(question), 0.01);
    }

    @Test
    public void S_deleteAllVotes() throws InvalidQuestion, InvalidVote {
        questionService.deleteAll();
        voteService.deleteAll();

        Question question = new Question();
        question.text = "Aimes-tu les brownies au chocolat?";
        questionService.insert(question);

        Vote vote = new Vote();
        vote.questionId = question.questionId;
        vote.voterName = "aaaa1";
        vote.value = 3;
        voteService.insert(vote);

        Vote vote1 = new Vote();
        vote1.questionId = question.questionId;
        vote1.voterName = "aaaaa2";
        vote1.value = 3;
        voteService.insert(vote1);

        Vote vote2 = new Vote();
        vote2.questionId = question.questionId;
        vote2.voterName = "aaaa3";
        vote2.value = 4;
        voteService.insert(vote2);

        Vote vote3 = new Vote();
        vote3.questionId = question.questionId;
        vote3.voterName = "aaaa4";
        vote3.value = 4;
        voteService.insert(vote3);

        Assert.assertEquals(4, voteService.count());
        voteService.deleteAll();
        Assert.assertEquals(0, voteService.count());
    }

    
}
