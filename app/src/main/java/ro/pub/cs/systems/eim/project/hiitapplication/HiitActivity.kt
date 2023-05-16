package ro.pub.cs.systems.eim.project.hiitapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import ro.pub.cs.systems.eim.project.hiitapplication.data.dto.HiitExercise
import ro.pub.cs.systems.eim.project.hiitapplication.fragment.HiitDifficultySelectFragment
import ro.pub.cs.systems.eim.project.hiitapplication.fragment.HiitExercisesSelectFragment
import ro.pub.cs.systems.eim.project.hiitapplication.fragment.HiitStartedExerciseFragment

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