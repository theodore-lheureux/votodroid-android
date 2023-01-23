package com.theodorelheureux.votodroid_android.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.theodorelheureux.votodroid_android.CreateQuestionMutation
import com.theodorelheureux.votodroid_android.databinding.ActivityAddItemBinding
import com.theodorelheureux.votodroid_android.graphql.GQLClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddItemActivity : AppCompatActivity() {
    private var binding: ActivityAddItemBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(
            layoutInflater
        )
        val view: View = binding!!.root
        setContentView(view)
        val questionText = binding!!.editTextTextQuestion

        binding!!.addItemProgressBar.visibility = View.GONE

        binding!!.submit.setOnClickListener { view1: View? ->

            binding!!.addItemProgressBar.visibility = View.VISIBLE
            binding!!.submit.visibility = View.GONE

            CoroutineScope(Dispatchers.IO).launch {
                var question =
                    GQLClient.instance.mutation(CreateQuestionMutation(questionText.text.toString()))
                        .execute()

                withContext(Dispatchers.Main) {
                    var question = question

                    if (question.data!!.questions.create.errors != null && question.data!!.questions.create.errors!!.isNotEmpty()) {
                        val errors = question.data!!.questions.create.errors!!

                        for (error in errors) {
                            questionText.error = error.message
                        }
                        binding!!.addItemProgressBar.visibility = View.GONE
                        binding!!.submit.visibility = View.VISIBLE
                    } else {
                        val intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }
    }
}