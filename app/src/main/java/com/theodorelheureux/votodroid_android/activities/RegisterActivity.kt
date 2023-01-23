package com.theodorelheureux.votodroid_android.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.theodorelheureux.votodroid_android.RegisterUserMutation
import com.theodorelheureux.votodroid_android.databinding.ActivityRegisterBinding
import com.theodorelheureux.votodroid_android.graphql.GQLClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private var binding: ActivityRegisterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(
            layoutInflater
        )
        var binding = binding!!
        val view: View = binding.root
        setContentView(view)

        binding.registerProgressBar.visibility = View.GONE

        binding.editTextPassword.setOnEditorActionListener { _, actionId, _ ->
            binding.registerButton.performClick()
            true
        }

        binding.loginButton.setOnClickListener { finish() }

        binding.registerButton.setOnClickListener {
            binding.registerProgressBar.visibility = View.VISIBLE
            binding.registerButton.visibility = View.GONE

            val email = binding.editTextEmailAddress.text.toString()
            val username = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                var response =
                    GQLClient.instance.mutation(RegisterUserMutation(username, email, password))
                        .execute()

                withContext(Dispatchers.Main) {
                    var response = response

                    if (response.data!!.users.register.errors != null && response.data!!.users.register.errors!!.isNotEmpty()) {
                        val errors = response.data!!.users.register.errors!!

                        for (error in errors) {
                            when (error.field) {
                                "username" -> {
                                    binding.editTextUsername.error = error.message
                                }
                                "email" -> {
                                    binding.editTextEmailAddress.error = error.message
                                }
                                "password" -> {
                                    binding.editTextPassword.error = error.message
                                }
                            }
                        }
                        binding.registerProgressBar.visibility = View.GONE
                        binding.registerButton.visibility = View.VISIBLE
                    } else {
                        val user = response.data!!.users.register.user
                        if (user != null) {
                            finish()
                        }
                    }
                }

            }
        }
    }
}