package hu.bme.aut.android.fit2be.activities

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.fit2be.adapters.MuscleGroupAdapter
import java.util.ArrayList
import hu.bme.aut.android.fit2be.R

import android.view.animation.Animation
import android.view.animation.AnimationUtils


class MuscleGroupActivity : AppCompatActivity() {

    private lateinit var muscleListGridView: GridView
    private var muscleGroupList: ArrayList<MuscleDataSet> = ArrayList<MuscleDataSet>()
    private var adapter: MuscleGroupAdapter? = null

    inner class MuscleDataSet {
        var muscleName: String? = null
        var photoPath: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muscle_group)
        muscleListGridView = findViewById(R.id.list_lang)
        prepareData()
        adapter = MuscleGroupAdapter(muscleGroupList, this)
        val anim: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.griditem_animation)
        muscleListGridView.animation = anim
        anim.start()
        muscleListGridView.adapter = adapter
        muscleListGridView.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, MuscleExerciseListActivity::class.java)
            val muscleName = muscleGroupList[position].muscleName
            intent.putExtra("muscleName", muscleName )
            startActivity(intent)
        }
    }

    private fun prepareData() {

        var tmpMuscleData = MuscleDataSet()

        tmpMuscleData.muscleName = getString(R.string.muscle_chest)
        tmpMuscleData.photoPath = R.drawable.chest
        muscleGroupList.add(tmpMuscleData)

        tmpMuscleData = MuscleDataSet()
        tmpMuscleData.muscleName = getString(R.string.muscle_shoulders)
        tmpMuscleData.photoPath = R.drawable.shoulder
        muscleGroupList.add(tmpMuscleData)

        tmpMuscleData = MuscleDataSet()
        tmpMuscleData.muscleName = getString(R.string.muscle_back)
        tmpMuscleData.photoPath = R.drawable.back
        muscleGroupList.add(tmpMuscleData)

        tmpMuscleData = MuscleDataSet()
        tmpMuscleData.muscleName = getString(R.string.muscle_biceps)
        tmpMuscleData.photoPath = R.drawable.biceps
        muscleGroupList.add(tmpMuscleData)

        tmpMuscleData = MuscleDataSet()
        tmpMuscleData.muscleName = getString(R.string.muscle_triceps)
        tmpMuscleData.photoPath = R.drawable.triceps
        muscleGroupList.add(tmpMuscleData)

        tmpMuscleData = MuscleDataSet()
        tmpMuscleData.muscleName = getString(R.string.muscle_forearm)
        tmpMuscleData.photoPath = R.drawable.forearm
        muscleGroupList.add(tmpMuscleData)

        tmpMuscleData = MuscleDataSet()
        tmpMuscleData.muscleName = getString(R.string.muscle_calves)
        tmpMuscleData.photoPath = R.drawable.calves
        muscleGroupList.add(tmpMuscleData)

        tmpMuscleData = MuscleDataSet()
        tmpMuscleData.muscleName = getString(R.string.muscle_abs)
        tmpMuscleData.photoPath = R.drawable.abs
        muscleGroupList.add(tmpMuscleData)

        tmpMuscleData = MuscleDataSet()
        tmpMuscleData.muscleName = getString(R.string.muscle_thighs)
        tmpMuscleData.photoPath = R.drawable.thighs
        muscleGroupList.add(tmpMuscleData)

    }
}