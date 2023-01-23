package com.theodorelheureux.votodroid_android.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.theodorelheureux.votodroid_android.GetAvgForQuestionQuery
import com.theodorelheureux.votodroid_android.GetStatsForQuestionQuery
import com.theodorelheureux.votodroid_android.databinding.ActivityResultBinding
import com.theodorelheureux.votodroid_android.graphql.GQLClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultActivity : AppCompatActivity() {
    var chart: BarChart? = null
    private var binding: ActivityResultBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(
            layoutInflater
        )
        val view: View = binding!!.root
        setContentView(view)
        val questionId = intent.getStringExtra("questionId")!!
        val questionText = intent.getStringExtra("questionText")!!

        binding!!.tvQuestion.text = questionText

        CoroutineScope(Dispatchers.IO).launch {
            val avg = GQLClient.instance.query(GetAvgForQuestionQuery(questionId))
                .execute().data!!.votes.getAvgForQuestion
            withContext(Dispatchers.Main) {
                binding!!.tvAverageValue.text = avg
            }
        }

        chart = binding!!.chart
        var chart = chart!!

        /* Settings for the graph - Change me if you want*/
        chart.setMaxVisibleValueCount(6)
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)
        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.labelCount = 7
        xAxis.valueFormatter = DefaultAxisValueFormatter(0)
        val leftAxis = chart.axisLeft
        leftAxis.setLabelCount(8, false)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.granularity = 1f
        leftAxis.axisMinimum = 0f
        leftAxis.valueFormatter = DefaultAxisValueFormatter(0)
        chart.description.isEnabled = false
        chart.axisRight.isEnabled = false

        chart.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            var count = GQLClient.instance.query(GetStatsForQuestionQuery(questionId))
                .execute().data!!.votes.getStatsForQuestion

            withContext(Dispatchers.Main) {
                /* Data and function call to bind the data to the graph */
                val dataGraph: HashMap<Int, Int> = object : HashMap<Int, Int>() {
                    init {
                        put(0, count[0])
                        put(1, count[1])
                        put(2, count[2])
                        put(3, count[3])
                        put(4, count[4])
                        put(5, count[5])
                    }
                }
                setData(dataGraph)
                chart.requestLayout()
                chart.visibility = View.VISIBLE
                binding!!.resultProgressBar.visibility = View.GONE
                val intent = Intent()
                setResult(RESULT_OK, intent)
            }
        }
    }

    /**
     * methode fournie par le prof pour séparer
     * - la configuration dans le onCreate
     * - l'ajout des données dans le setDate
     * @param datas
     */
    private fun setData(datas: Map<Int, Int>) {
        val values: MutableList<BarEntry> = ArrayList()
        /* Every bar entry is a bar in the graphic */
        for ((key1, value) in datas) {
            values.add(BarEntry(key1.toFloat(), value.toFloat()))
        }
        val set1: BarDataSet
        if (chart!!.data != null &&
            chart!!.data.dataSetCount > 0
        ) {
            set1 = chart!!.data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            chart!!.data.notifyDataChanged()
            chart!!.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, "Values")
            set1.setDrawIcons(false)
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.barWidth = .9f
            chart!!.data = data
        }
    }
}