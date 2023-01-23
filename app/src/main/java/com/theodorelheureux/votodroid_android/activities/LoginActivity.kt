package com.theodorelheureux.votodroid_android.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.theodorelheureux.votodroid_android.LoginMutation
import com.theodorelheureux.votodroid_android.databinding.ActivityLoginBinding
import com.theodorelheureux.votodroid_android.graphql.GQLClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private var binding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(
            layoutInflater
        )
        var binding = binding!!
        val view: View = binding.root
        setContentView(view)

        binding.loginProgressBar.visibility = View.GONE

        binding.editTextTextPassword.setOnEditorActionListener { _, actionId, _ ->
            binding.loginButton.performClick()
            true
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            binding.loginProgressBar.visibility = View.VISIBLE
            binding.loginButton.visibility = View.GONE

            val usernameOrEmail = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                var response =
                    GQLClient.instance.mutation(LoginMutation(usernameOrEmail, password)).execute()

                withContext(Dispatchers.Main) {
                    var response = response

                    if (response.data!!.users.login.errors != null && response.data!!.users.login.errors!!.isNotEmpty()) {
                        val errors = response.data!!.users.login.errors!!

                        for (error in errors) {
                            when (error.field) {
                                "usernameOrEmail" -> {
                                    binding.editTextTextEmailAddress.error = error.message
                                }
                                "password" -> {
                                    binding.editTextTextPassword.error = error.message
                                }
                            }
                        }
                        binding.loginProgressBar.visibility = View.GONE
                        binding.loginButton.visibility = View.VISIBLE
                    } else {
                        val user = response.data!!.users.login.user
                        if (user != null) {
                            finish()
                        }
                    }
                }

            }
        }
    }

    override fun onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true)
    }
}