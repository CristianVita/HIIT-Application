package ro.pub.cs.systems.eim.project.hiitapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.widget.ListView
import android.widget.Toast
import android.widget.Toast.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ro.pub.cs.systems.eim.project.hiitapplication.adapter.HiitAdapter
import ro.pub.cs.systems.eim.project.hiitapplication.dto.HiitExercise
import ro.pub.cs.systems.eim.project.hiitapplication.fragment.HiitDifficultySelectFragment
import ro.pub.cs.systems.eim.project.hiitapplication.fragment.HiitExercisesSelectFragment
import ro.pub.cs.systems.eim.project.hiitapplication.fragment.HiitStartedExerciseFragment
import ro.pub.cs.systems.eim.project.hiitapplication.repository.HiitRepository
import java.net.SocketTimeoutException

class HiitActivity : AppCompatActivity(), HiitDifficultySelectFragment.Listener, HiitExercisesSelectFragment.Listener, HiitStartedExerciseFragment.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hiit)

        setFragment(R.id.actHiit_fragment_container, HiitDifficultySelectFragment.newInstance())
    }

    private fun setFragment(containerId: Int, fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(containerId, fragment)
        fragmentTransaction.commit()
    }

    override fun onDifficultySelect(difficulty: String) {
        Log.i("HiitActivity", "onDifficultySelect() called with difficulty: $difficulty")

        // depending on the difficulty, we will set the fragment
        setFragment(R.id.actHiit_fragment_container, HiitExercisesSelectFragment.newInstance(difficulty))
    }

    override fun onStartHiit(exercises: ArrayList<HiitExercise>) {
        Log.e("HiitActivity", "onStartHiit() called with exercises: $exercises")

        setFragment(R.id.actHiit_fragment_container, HiitStartedExerciseFragment.newInstance(exercises))
    }

    override fun onChangeDifficulty() {
        Log.i("HiitActivity", "onChangeDifficulty() called")

        setFragment(R.id.actHiit_fragment_container, HiitDifficultySelectFragment.newInstance())
    }

    override fun onHiitExerciseCanceled() {
        Log.i("HiitActivity", "onHiitExerciseCanceled() called")

        setFragment(R.id.actHiit_fragment_container, HiitDifficultySelectFragment.newInstance())
    }

    override fun onHiitSessionFinished() {
        Log.i("HiitActivity", "onHiitSessionFinished() called")

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}