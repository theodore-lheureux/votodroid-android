package com.theodorelheureux.votodroid_android

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo3.api.Optional
import com.theodorelheureux.votodroid_android.activities.*
import com.theodorelheureux.votodroid_android.databinding.ActivityMainBinding
import com.theodorelheureux.votodroid_android.graphql.GQLClient
import com.theodorelheureux.votodroid_android.interfaces.onClickInterface
import com.theodorelheureux.votodroid_android.services.UserService
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var questionAdapter: QuestionAdapter? = null
    private var onClickInterface: onClickInterface = object : onClickInterface {
        override fun setClick(clickedId: Int, questionId: String, questionText: String) {
            when (clickedId) {
                0 -> {
                    val intent = Intent(this@MainActivity, VoteActivity::class.java)
                    intent.putExtra("questionId", questionId)
                    intent.putExtra("questionText", questionText)
                    startActivity(intent)
                }
                1 -> {
                    val intent = Intent(this@MainActivity, ResultActivity::class.java)
                    intent.putExtra("questionId", questionId)
                    intent.putExtra("questionText", questionText)
                    startActivity(intent)
                }
                else -> {
                    return
                }
            }
        }
    }

    private var cursor: String? = null
    private var isLoading = false
    private var hasMore = false
    private val pageSize = 10

    var activityLauncher =
        registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                remplirRecycler()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        val view: View = binding!!.root
        val swipeRefresh = binding!!.swiperefresh
        setContentView(view)

        val user = runBlocking { UserService.getUser() }

        if (user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            activityLauncher.launch(intent)
        }

        binding!!.addButton.setOnClickListener { view1 ->
            val i = Intent(this@MainActivity, AddItemActivity::class.java)
            activityLauncher.launch(i)
        }
        binding!!.mainProgressBar.visibility = View.GONE
        initRecycler()
        remplirRecycler()
        initScrollListener()

        swipeRefresh.setOnRefreshListener {
            remplirRecycler()
            swipeRefresh.isRefreshing = false
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                runBlocking { UserService.logout() }
                val intent = Intent(this, LoginActivity::class.java)
                activityLauncher.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun remplirRecycler() {
        questionAdapter!!.list.clear()
        cursor = null
        hasMore = false
        isLoading = false
        loadMore()
    }

    private fun initRecycler() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)

        // use a linear layout manager
        val layoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = layoutManager

        // specify an adapter (see also next example)
        questionAdapter = QuestionAdapter(ArrayList(), onClickInterface)
        recyclerView.adapter = questionAdapter
    }

    private fun initScrollListener() {
        binding!!.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?

                if (!isLoading && hasMore) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == questionAdapter!!.list.size - 1) {
                        isLoading = true
                        binding!!.mainProgressBar.visibility = View.VISIBLE
                        loadMore()
                    }
                }
            }
        })
    }

    private fun loadMore() {
        CoroutineScope(Dispatchers.IO).launch {
            var query =
                GQLClient.instance.query(GetPaginatedQuery(pageSize, Optional.present(cursor)))
                    .execute()

            if (query.data?.questions?.getPaginated?.questions == null) {
                return@launch
            }

            withContext(Dispatchers.Main) {
                var questions = query.data!!.questions.getPaginated.questions!!

                if (questions.size == pageSize) {
                    hasMore = true
                    cursor = questions[questions.size - 1].id.toString()
                    questions = questions.subList(0, questions.size - 2)
                } else {
                    hasMore = false
                }

                questionAdapter!!.list.addAll(questions)
                questionAdapter!!.notifyDataSetChanged()

                binding!!.mainProgressBar.visibility = View.GONE
                isLoading = false
            }
        }
    }
}