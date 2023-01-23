package com.theodorelheureux.votodroid_android.services;

import android.content.Context;

public final class VoteService {

    private static final VoteService instance = new VoteService();

    public static synchronized VoteService getInstance(Context context) {
        return instance;
    }

//    public void insert(Vote vote) throws InvalidVote {
//        if (vote.value < 0 || vote.value > 5) throw new InvalidVote("Valeur invalide");
//        if (vote.voteId != null) throw new InvalidVote("Id non nul. La BD doit le gérer");
//        if (vote.voterName == null || vote.voterName.trim().length() < 4) throw new InvalidVote("Nom trop court. Minimum 4 caractères");
//
//        for (Vote v : allForQuestion(vote.questionId)){
//            if (v.voterName.equalsIgnoreCase(vote.voterName)){
//                throw new InvalidVote("Vous avez deja vote pour cette question");
//            }
//        }
//
//        vote.voteId = db.voteDao().insert(vote);
//    }
//
//    public void deleteAll(){
//        db.voteDao().deleteAll();
//    }
//
//    public int count() {
//        return db.voteDao().count();
//    }
//
//    public double voteAvgForQuestion(Question question) {
//        return db.voteDao().getAverageVoteForQuestion(question.questionId);
//    }
//
//    public double voteSDForQuestion(Question question) {
//        Vote[] votes = db.voteDao().getVotesForQuestion(question.questionId);
//
//        double sum = 0.0, standardDeviation = 0.0;
//        for (Vote vote : votes) {
//            sum += vote.value;
//        }
//        double mean = sum / votes.length;
//
//        for(Vote v: votes) {
//            standardDeviation += Math.pow(v.value - mean, 2);
//        }
//
//        return Math.sqrt(standardDeviation/votes.length);
//    }
//
//    public int[] voteCountForQuestion(Question question) {
//        Vote[] votes = db.voteDao().getVotesForQuestion(question.questionId);
//        int[] count = new int[6];
//        for (Vote vote : votes) {
//            count[(int) vote.value]++;
//        }
//        return count;
//    }
//
//    public void deleteAllForQuestion(Question question) {
//        db.voteDao().deleteAllForQuestion(question.questionId);
//    }
//
//    public List<Vote> allForQuestion(Long questionId) {
//        return Arrays.asList(db.voteDao().getVotesForQuestion(questionId));
//    }
//
//    public List<Vote> allForQuestion(Question question){
//    	return Arrays.asList(db.voteDao().getVotesForQuestion(question.questionId));
//    }

}
