package ro.pub.cs.systems.eim.project.hiitapplication.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ro.pub.cs.systems.eim.project.hiitapplication.R
import ro.pub.cs.systems.eim.project.hiitapplication.adapter.HiitAdapter
import ro.pub.cs.systems.eim.project.hiitapplication.data.dto.HiitExercise
import ro.pub.cs.systems.eim.project.hiitapplication.repository.HiitRepository
import java.net.SocketTimeoutException

private const val DIFF_KEY = "difficulty"

class HiitExercisesSelectFragment : Fragment() {
    private var difficulty: String? = null

    private lateinit var hiitRepository: HiitRepository
    /* This is the list of exercises that will be displayed in the ListView */
    private var hiitExercises: ArrayList<HiitExercise> = ArrayList()
    /* This is the ListView that will display the exercises */
    private var hiitExercisesListLV : ListView? = null
    /* This is the TextView that will display the message */
    private var hiitExercisesTitleTV: TextView? = null
    /* These are the buttons that will be used to start the HIIT and to change the difficulty */
    private var hiitStartBtn: FrameLayout? = null
    private var hiitChangeDiffBtn: FrameLayout? = null

    /* This dictionary will be used to map the difficulty to the number of exercises that will be displayed */
    private val difficultyToExercisesCount = mapOf(
        "beginner" to 3,
        "intermediate" to 5,
        "advanced" to 7
    )

    private var listener: Listener? = null

    interface Listener {
        fun onStartHiit(exercises: ArrayList<HiitExercise>)
        fun onChangeDifficulty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.i("HiitActivity", "onCreateView() called")

        val view =  inflater.inflate(R.layout.fragment_hiit_exercises_select, container, false) as View

        hiitExercisesTitleTV = view.findViewById(R.id.fragHiitExecSel_exercises_title)
        hiitExercisesTitleTV?.text = "Please select ${difficultyToExercisesCount[difficulty]} exercises:"

        hiitStartBtn = view.findViewById(R.id.fragHiitExecSel_start_button)
        hiitChangeDiffBtn = view.findViewById(R.id.fragHiitExecSel_change_difficulty_button)

        hiitStartBtn?.setOnClickListener {
            /* If there are less than the required number of exercises selected, then the user cannot start the HIIT */
            if (hiitExercises.filter { it.checked == true }.size < difficultyToExercisesCount[difficulty]!!) {
                Log.e("HiitActivity", "You must select ${difficultyToExercisesCount[difficulty]} exercises")
                Toast.makeText(requireContext(), "You must select ${difficultyToExercisesCount[difficulty]} exercises", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            /* Start the HIIT using the selected exercises */
            listener?.onStartHiit(hiitExercises.filter { it.checked == true } as ArrayList<HiitExercise>)
        }

        hiitChangeDiffBtn?.setOnClickListener {
            listener?.onChangeDifficulty()
        }

        hiitExercisesListLV = view.findViewById(R.id.fragHiitExecSel_exercises_list)

        /* Create the adapter and set it to the ListView */
        val hiitAdapter = HiitAdapter(requireContext(), hiitExercises)
        hiitAdapter.setOnItemClickListener(object : HiitAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                /* If there are more than the required number of exercises selected, then the user cannot select more */
                if (hiitExercises.filter { it.checked == true }.size >= difficultyToExercisesCount[difficulty]!! && !hiitExercises[position].checked!!) {
                    Toast.makeText(requireContext(), "You can select only ${difficultyToExercisesCount[difficulty]} exercises", Toast.LENGTH_SHORT).show()
                    return
                }

                /* Toggle the checked state of the exercise */
                hiitExercises[position].checked = !hiitExercises[position].checked!!
                hiitAdapter.notifyDataSetChanged()
            }
        })
        hiitExercisesListLV?.adapter = hiitAdapter

        setExercises(difficulty)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("HiitActivity", "onCreate() called")

        super.onCreate(savedInstanceState)
        arguments?.let {
            difficulty = it.getString(DIFF_KEY)
        }

        hiitRepository = HiitRepository(requireContext())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HiitExercisesSelectFragment.Listener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement HiitExercisesSelectFragment.Listener")
        }
    }

    private fun setExercises(difficulty: String?) {
        Log.i("HiitActivity", "setExercises() called with difficulty: $difficulty")

        Log.e("HiitActivity", hiitExercises.toString())

        /* Get the exercises from the repository using a coroutine */
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                /* Get the exercises from the repository based on the difficulty */
                if (difficulty.equals("beginner"))
                    hiitExercises.addAll(hiitRepository.getBeginnerExercises())
                else if (difficulty.equals("intermediate"))
                    hiitExercises.addAll(hiitRepository.getIntermediateExercises())
                else if (difficulty.equals("advanced"))
                    hiitExercises.addAll(hiitRepository.getAdvancedExercises())

                /* Notify the adapter that the data has changed */
                withContext(Dispatchers.Main) {
                    Log.e("HiitActivity", hiitExercises.toString())
                    (hiitExercisesListLV?.adapter as HiitAdapter).notifyDataSetChanged()
                }
            } catch (e: SocketTimeoutException) {
                Log.e("HiitActivity", e.message.toString())

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "The server is not responding", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /* this is used to initialize the fragment and its arguments (passed as a bundle) */
    companion object {
        @JvmStatic
        fun newInstance(difficulty: String) =
            HiitExercisesSelectFragment().apply {
                arguments = Bundle().apply {
                    putString(DIFF_KEY, difficulty)
                }
            }
    }
}