package ro.pub.cs.systems.eim.project.hiitapplication.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import ro.pub.cs.systems.eim.project.hiitapplication.R
import ro.pub.cs.systems.eim.project.hiitapplication.dto.HiitExercise

class HiitAdapter(context: Context, private val hiitExercisesArray: ArrayList<HiitExercise>) :
    ArrayAdapter<HiitExercise>(context, R.layout.hiit_list_item, hiitExercisesArray) {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val listItemView = inflater.inflate(R.layout.hiit_list_item, null)

        val exerciseName = listItemView.findViewById<android.widget.TextView>(R.id.hiit_list_item_exercise_name)
        val exerciseDuration = listItemView.findViewById<android.widget.TextView>(R.id.hiit_list_item_exercise_duration)
        val exerciseCheckbox = listItemView.findViewById<android.widget.CheckBox>(R.id.hiit_list_item_exercise_checkbox)

        val exerciseData = hiitExercisesArray[position]
        exerciseName.text = exerciseData.name
        exerciseDuration.text = exerciseData.duration.toString() + " s"
        exerciseCheckbox.isChecked = exerciseData.checked!!
        exerciseCheckbox.isClickable = false

        listItemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }

        return listItemView
    }
}