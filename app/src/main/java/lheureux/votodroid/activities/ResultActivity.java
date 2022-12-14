package lheureux.votodroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lheureux.votodroid.R;
import lheureux.votodroid.databinding.ActivityResultBinding;
import lheureux.votodroid.models.Question;
import lheureux.votodroid.services.QuestionService;
import lheureux.votodroid.services.VoteService;


public class ResultActivity extends AppCompatActivity {

    BarChart chart;
    private ActivityResultBinding binding;
    private VoteService voteService;
    private QuestionService questionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        voteService = VoteService.getInstance(this);
        questionService = QuestionService.getInstance(this);

        binding = ActivityResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Question question = questionService.get(getIntent().getLongExtra("questionId", 0));
        TextView tvQuestion = findViewById(R.id.tvQuestion);
        TextView tvAverageValue = findViewById(R.id.tvAverageValue);
        TextView tvStdDeviationValue = findViewById(R.id.tvStdDeviationValue);

        tvQuestion.setText(question.text);
        tvAverageValue.setText(String.format("%.2f", voteService.voteAvgForQuestion(question)));
        tvStdDeviationValue.setText(String.format("%.2f", voteService.voteSDForQuestion(question)));

        chart = findViewById(R.id.chart);

        /* Settings for the graph - Change me if you want*/
        chart.setMaxVisibleValueCount(6);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new DefaultAxisValueFormatter(0));

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setGranularity(1);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setValueFormatter(new DefaultAxisValueFormatter(0));
        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        /* Data and function call to bind the data to the graph */
        Map<Integer, Integer> dataGraph = new HashMap<Integer, Integer>() {{

            int[] count = voteService.voteCountForQuestion(question);

            put(0, count[0]);
            put(1, count[1]);
            put(2, count[2]);
            put(3, count[3]);
            put(4, count[4]);
            put(5, count[5]);

        }};
        setData(dataGraph);

        Intent intent= new Intent();
        setResult(RESULT_OK, intent);
    }

    /**
     * methode fournie par le prof pour séparer
     * - la configuration dans le onCreate
     * - l'ajout des données dans le setDate
     * @param datas
     */
    private void setData(Map<Integer, Integer> datas) {
        List<BarEntry> values = new ArrayList<>();
        /* Every bar entry is a bar in the graphic */
        for (Map.Entry<Integer, Integer> key : datas.entrySet()){
            values.add(new BarEntry(key.getKey() , key.getValue()));
        }

        BarDataSet set1;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Notes");

            set1.setDrawIcons(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(.9f);
            chart.setData(data);
        }
    }
}