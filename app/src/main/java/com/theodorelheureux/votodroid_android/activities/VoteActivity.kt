package com.theodorelheureux.votodroid_android.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.theodorelheureux.votodroid_android.CreateVoteMutation
import com.theodorelheureux.votodroid_android.databinding.ActivityVoteBinding
import com.theodorelheureux.votodroid_android.graphql.GQLClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoteActivity : AppCompatActivity() {
    private var binding: ActivityVoteBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoteBinding.inflate(
            layoutInflater
        )
        val view: View = binding!!.root
        setContentView(view)
        binding!!.voteProgressBar.visibility = View.GONE

        val questionId = intent.getStringExtra("questionId")!!
        val questionText = intent.getStringExtra("questionText")!!

        binding!!.tvQuestion.text = questionText

        binding!!.submit.setOnClickListener {
            val value = binding!!.rating.rating.toInt()

            binding!!.submit.visibility = View.GONE
            binding!!.voteProgressBar.visibility = View.VISIBLE

            CoroutineScope(Dispatchers.IO).launch {
                val mutation =
                    GQLClient.instance.mutation(CreateVoteMutation(questionId, value)).execute()

                withContext(Dispatchers.Main) {
                    if (mutation.data?.votes?.create?.errors != null && mutation.data!!.votes.create.errors!!.isNotEmpty()) {
                        binding!!.submit.error = mutation.data!!.votes.create.errors!![0].message
                        binding!!.submit.visibility = View.VISIBLE
                        binding!!.voteProgressBar.visibility = View.GONE
                    } else {
                        val intent = Intent()
                        setResult(RESULT_OK, intent)
                        finish()
                    }

                }
            }

        }
    }
}