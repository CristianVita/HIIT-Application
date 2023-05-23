package ro.pub.cs.systems.eim.project.hiitapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ro.pub.cs.systems.eim.project.hiitapplication.adapter.WorkoutHistoryAdapter
import ro.pub.cs.systems.eim.project.hiitapplication.data.dto.WorkoutSession
import ro.pub.cs.systems.eim.project.hiitapplication.repository.WorkoutHistoryRepository

class WorkoutHistoryActivity : AppCompatActivity() {
    private lateinit var workoutHistory: ArrayList<WorkoutSession>
    private lateinit var workoutHistoryLV: ListView
    private lateinit var workoutHistoryRepository: WorkoutHistoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_history)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!

        workoutHistoryRepository = WorkoutHistoryRepository(user.uid)

        lifecycleScope.launch(Dispatchers.IO) {
            workoutHistory = workoutHistoryRepository.getWorkoutSessions() as ArrayList<WorkoutSession>
        }.invokeOnCompletion {
            runOnUiThread {
                workoutHistoryLV = findViewById(R.id.actWorkoutHistory_historyList)
                workoutHistoryLV.adapter = WorkoutHistoryAdapter(this, workoutHistory)
            }
        }
    }
}