package ro.pub.cs.systems.eim.project.hiitapplication.fragment

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.google.gson.Gson
import ro.pub.cs.systems.eim.project.hiitapplication.R
import ro.pub.cs.systems.eim.project.hiitapplication.dto.HiitExercise

private const val EXERCISES_KEY = "exerciseList"

class HiitStartedExerciseFragment : Fragment() {
    private var remainingTimeTV: TextView? = null
    private var endHiitFL: FrameLayout? = null
    private var currentExerciseTitleTV: TextView? = null
    private var exerciseCounterTV: TextView? = null

    private var exercises: ArrayList<HiitExercise>? = null
    private var currentExerciseIndex: Int = 0

    private var listener: Listener? = null

    interface Listener {
        fun onHiitExerciseCanceled()
        fun onHiitSessionFinished()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement HiitStartedExerciseFragment.Listener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            exercises = it.getString(EXERCISES_KEY)?.let { it1 -> Gson().fromJson(it1, Array<HiitExercise>::class.java).toCollection(ArrayList()) }
        }

        Log.e("HiitStartedExerciseFragment", exercises.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hiit_started_exercise, container, false)

        remainingTimeTV = view.findViewById(R.id.fragHiitStartedExercise_remainingTimeText)
        endHiitFL = view.findViewById(R.id.fragHiitStartedExercise_cancelExerciseButton)
        currentExerciseTitleTV = view.findViewById(R.id.fragHiitStartedExercise_currentExerciseTitle)
        exerciseCounterTV = view.findViewById(R.id.fragHiitStartedExercise_exerciseCounter)

        endHiitFL?.setOnClickListener {
            listener?.onHiitExerciseCanceled()
        }

        setExercise(0)

        return view
    }

    private fun setExercise(exerciseIndex: Int) {
        currentExerciseIndex = exerciseIndex
        exerciseCounterTV?.text = "Exercise ${currentExerciseIndex + 1} of ${exercises?.size}"
        remainingTimeTV?.text = exercises?.get(currentExerciseIndex)?.duration.toString()
        currentExerciseTitleTV?.text = exercises?.get(currentExerciseIndex)?.name

        val countDownTimer = object : CountDownTimer((exercises?.get(currentExerciseIndex)?.duration?.toLong()!! + 1) * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeTV?.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                if (currentExerciseIndex < exercises?.size!! - 1) {
                    setBreak()
                } else {
                    setHiitFinished()
                }
            }
        }

        countDownTimer.start()
    }

    private fun setBreak() {
        exerciseCounterTV?.text = "Take a break"
        remainingTimeTV?.text = "15"
        currentExerciseTitleTV?.text = "Break"

        val countDownTimer = object : CountDownTimer(16 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeTV?.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                if (currentExerciseIndex < exercises?.size!! - 1) {
                    setExercise(currentExerciseIndex + 1)
                } else {
                    listener?.onHiitExerciseCanceled()
                }
            }
        }

        countDownTimer.start()
    }

    private fun setHiitFinished() {
        exerciseCounterTV?.text = "Finished"
        remainingTimeTV?.text = "Done"
        currentExerciseTitleTV?.text = "Congratulations for finishing this HIIT Session!"
        endHiitFL?.visibility = View.INVISIBLE

        remainingTimeTV?.setOnClickListener {
            listener?.onHiitSessionFinished()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(exercises: ArrayList<HiitExercise>) =
            HiitStartedExerciseFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val exercisesString = gson.toJson(exercises)

                    putString(EXERCISES_KEY, exercisesString)
                }
            }
    }
}