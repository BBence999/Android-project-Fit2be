package hu.bme.aut.android.fit2be.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import hu.bme.aut.android.fit2be.activities.MuscleGroupActivity
import hu.bme.aut.android.fit2be.R

data class MuscleGroupAdapter(val muscleList:List<MuscleGroupActivity.MuscleDataSet>, var activity: Activity) : BaseAdapter(){

    override fun getItem(position: Int): Any {
        return muscleList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return muscleList.size
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(activity, R.layout.element_muscle_grid,null)

        val tvLang = view.findViewById(R.id.tv_lang) as TextView
        val imgLang = view.findViewById<ImageView>(R.id.im_lang)

        tvLang.text= muscleList[position].muscleName
        val muscleImg= muscleList[position].photoPath
        imgLang.setImageResource(muscleImg)
        return view
    }

}