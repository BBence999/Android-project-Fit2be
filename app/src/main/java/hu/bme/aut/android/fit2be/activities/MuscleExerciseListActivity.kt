package hu.bme.aut.android.fit2be.activities
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.fit2be.R
import hu.bme.aut.android.fit2be.adapters.MuscleListAdapter
import hu.bme.aut.android.fit2be.data.ExerciseItem
import hu.bme.aut.android.fit2be.data.ExerciseListDatabase
import hu.bme.aut.android.fit2be.databinding.ActivityMuscleExerciselistBinding
import kotlin.concurrent.thread

class MuscleExerciseListActivity : AppCompatActivity(), MuscleListAdapter.MuscleExerciseItemClickListener {

    private lateinit var adapter: MuscleListAdapter
    private lateinit var binding: ActivityMuscleExerciselistBinding
    private lateinit var database: ExerciseListDatabase

    override fun onCreate(savedInstanceState: Bundle?)
    {

        val muscleName = intent.getStringExtra("muscleName")

        super.onCreate(savedInstanceState)

        binding = ActivityMuscleExerciselistBinding.inflate(layoutInflater)

        setContentView(binding.root)

        database = ExerciseListDatabase.getDatabase(applicationContext)

        initRecyclerView(muscleName!!)
    }

    private fun initRecyclerView(muscleName: String) {
        adapter = MuscleListAdapter(this)
        binding.recycView.layoutManager = LinearLayoutManager(this@MuscleExerciseListActivity)
        binding.recycView.adapter = adapter
        loadItemsInBackground(muscleName)
    }

    private fun loadItemsInBackground(muscleName: String) {
        thread {
            val items = database.exerciseItemDAO().getExercisesByMuscleGroup(muscleName)
                        runOnUiThread {
                binding.recycView.startLayoutAnimation()
                adapter.load(items)
            }
        }
    }

    override fun onItemAdd(item: ExerciseItem) {
        thread {
           database.exerciseItemDAO().setMyPlan(item.id)
            runOnUiThread {
                Toast.makeText(this@MuscleExerciseListActivity, item.exercise_name +" " +
                        getString(R.string.new_muscle_exercise), Toast.LENGTH_SHORT).show()
              adapter.remove(item)
            }
        }
    }
}