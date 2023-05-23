package ro.pub.cs.systems.eim.project.hiitapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout

class StatisticsActivity : AppCompatActivity() {
    private lateinit var workoutHistoryBtn: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        workoutHistoryBtn = findViewById(R.id.actStatistics_historyBtn)
        workoutHistoryBtn.setOnClickListener {
            val intent = Intent(this, WorkoutHistoryActivity::class.java)
            startActivity(intent)
        }
    }
}