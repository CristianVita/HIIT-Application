package ro.pub.cs.systems.eim.project.hiitapplication.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import ro.pub.cs.systems.eim.project.hiitapplication.R

class HiitDifficultySelectFragment : Fragment() {
    private var chooseBeginnerBtn: FrameLayout? = null
    private var chooseIntermediateBtn: FrameLayout? = null
    private var chooseAdvancedBtn: FrameLayout? = null

    // listener for the fragments buttons
    private var listener: Listener? = null

    // listener interface for the fragments buttons
    interface Listener {
        fun onDifficultySelect(difficulty: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_hiit_difficulty_select, container, false)

        chooseBeginnerBtn = view.findViewById(R.id.fragHiitDiffSel_beginner_button)
        chooseIntermediateBtn = view.findViewById(R.id.fragHiitDiffSel_intermediate_button)
        chooseAdvancedBtn = view.findViewById(R.id.fragHiitDiffSel_advanced_button)

        chooseBeginnerBtn?.setOnClickListener {
            listener?.onDifficultySelect("beginner")
        }

        chooseIntermediateBtn?.setOnClickListener {
            listener?.onDifficultySelect("intermediate")
        }

        chooseAdvancedBtn?.setOnClickListener {
            listener?.onDifficultySelect("advanced")
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement HiitDifficultySelectFragment.Listener")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HiitDifficultySelectFragment()
    }
}