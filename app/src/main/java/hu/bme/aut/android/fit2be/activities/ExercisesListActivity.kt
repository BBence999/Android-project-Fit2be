package hu.bme.aut.android.fit2be.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.fit2be.R
import hu.bme.aut.android.fit2be.adapters.ExerciseListAdapter
import hu.bme.aut.android.fit2be.data.ExerciseItem
import hu.bme.aut.android.fit2be.data.ExerciseListDatabase
import hu.bme.aut.android.fit2be.databinding.ActivityExerciseslistBinding
import kotlin.concurrent.thread

open class ExercisesListActivity : AppCompatActivity(),
    ExerciseListAdapter.ExerciseItemClickListener {

    private lateinit var adapter: ExerciseListAdapter
    private lateinit var binding: ActivityExerciseslistBinding
    private lateinit var database: ExerciseListDatabase

    companion object {
        const val KEY_DIFFICULT_TYPE = "KEY_DIFFICULT_TYPE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val diffType = this.intent.getIntExtra(KEY_DIFFICULT_TYPE, -1)

        super.onCreate(savedInstanceState)

        binding = ActivityExerciseslistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = ExerciseListDatabase.getDatabase(applicationContext)

        initRecyclerView(diffType)

        binding.btnNew.setOnClickListener {
            startActivity(Intent(this, MuscleGroupActivity::class.java))
        }

        binding.btnSave.setOnClickListener()
        {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.saving_info_title))
            builder.setMessage(getString(R.string.saving_info_question))
            builder.setPositiveButton(getString(R.string.info_button_yes)) { _, _ ->
                Toast.makeText(
                    applicationContext, getString(R.string.saving_info_message)
                            , Toast.LENGTH_LONG
                ).show()
                startActivity(Intent(this, MainActivity::class.java))
                this.finish()
            }
            builder.setNegativeButton(getString(R.string.info_button_cancel)) { _, _ -> }
            builder.show()
        }

        binding.btnInfo.setOnClickListener()
        {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.planner_info_title))
            builder.setMessage(
                getString(R.string.training_info_message)
            )
            builder.setPositiveButton(R.string.button_ok) { _, _ -> }
            builder.show()
        }

    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.unsaved_plan_title)
        builder.setMessage(getString(R.string.unsaved_plan_message))
        builder.setPositiveButton(getString(R.string.info_button_yes)) { _, _ ->
            thread {
                database.exerciseItemDAO().resetMyPlan()
            }
            startActivity(Intent(this, MainActivity::class.java))
        }
        builder.setNegativeButton(R.string.info_button_cancel) { _, _ -> }
        builder.show()
    }

    override fun onRestart() {
        super.onRestart()
        thread {
            val myPlan = database.exerciseItemDAO().getMyPlan()
            runOnUiThread {
                adapter.load(myPlan)
            }
        }

    }

    private fun initRecyclerView(diffType: Int) {
        adapter = ExerciseListAdapter(this)
        binding.recycView.layoutManager = LinearLayoutManager(this@ExercisesListActivity)
        binding.recycView.adapter = adapter
        loadItemsInBackground(diffType)
    }

    private fun loadItemsInBackground(diffType: Int) {

        thread {
            val items = when (diffType) {
                DifficultyActivity.TYPE_BEG -> database.exerciseItemDAO().getBeginnerExercises()
                DifficultyActivity.TYPE_INT -> database.exerciseItemDAO().getIntermediateExercises()
                DifficultyActivity.TYPE_ADV -> database.exerciseItemDAO().getAdvancedExercises()
                else -> null
            }

            items?.forEach {
                database.exerciseItemDAO().setMyPlan(it.id)
            }

            val myPlan = database.exerciseItemDAO().getMyPlan()

            runOnUiThread {
                binding.recycView.startLayoutAnimation()
                adapter.load(myPlan)
            }
        }
    }

    override fun onItemDelete(item: ExerciseItem) {
        thread {
            database.exerciseItemDAO().removeFromMyPlan(item.id)
            runOnUiThread {
                adapter.remove(item)
            }
        }
    }
}