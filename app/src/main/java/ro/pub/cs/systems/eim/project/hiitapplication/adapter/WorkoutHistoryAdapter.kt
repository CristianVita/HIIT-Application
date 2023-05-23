package ro.pub.cs.systems.eim.project.hiitapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import ro.pub.cs.systems.eim.project.hiitapplication.R
import ro.pub.cs.systems.eim.project.hiitapplication.data.dto.WorkoutSession

class WorkoutHistoryAdapter(context: Context, private val historyArray: ArrayList<WorkoutSession>) :
    ArrayAdapter<WorkoutSession>(context, R.layout.history_list_item, historyArray) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val listItemView = inflater.inflate(R.layout.history_list_item, null)

        val sessionTypeTV = listItemView.findViewById<android.widget.TextView>(R.id.history_list_item_session)
        val sessionDetailsTV = listItemView.findViewById<android.widget.TextView>(R.id.history_list_item_details)
        val sessionDateTV = listItemView.findViewById<android.widget.TextView>(R.id.history_list_item_date)

        val sessionInfo = historyArray[position]
        sessionTypeTV.text = sessionInfo.type
        sessionDetailsTV.text = sessionInfo.details
        sessionDateTV.text = sessionInfo.date

        return listItemView
    }
}