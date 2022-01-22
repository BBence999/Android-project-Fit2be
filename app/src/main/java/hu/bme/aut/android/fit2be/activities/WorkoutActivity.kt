package hu.bme.aut.android.fit2be.activities

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.fit2be.adapters.ExerciseListAdapter
import hu.bme.aut.android.fit2be.adapters.WorkoutListAdapter
import hu.bme.aut.android.fit2be.data.ExerciseItem
import hu.bme.aut.android.fit2be.data.ExerciseListDatabase
import hu.bme.aut.android.fit2be.databinding.ActivityWorkoutBinding
import kotlin.concurrent.thread
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import android.content.res.Configuration
import android.os.*
import android.text.InputType
import android.widget.EditText
import androidx.annotation.RequiresApi
import hu.bme.aut.android.fit2be.R


class WorkoutActivity : AppCompatActivity(), WorkoutListAdapter.WorkoutExerciseItemClickListener {

    private lateinit var adapter: WorkoutListAdapter
    private lateinit var binding: ActivityWorkoutBinding
    private lateinit var database: ExerciseListDatabase
    private var timer: CountDownTimer? = null
    private var workoutTimeInMillisSec: Long = 90 * 60 * 1000
    private lateinit var workoutExercises: List<ExerciseItem>
    private var workoutExercisesLeft: Int = 0

    companion object {
        const val KEY_DIFFICULT_TYPE = "KEY_DIFFICULT_TYPE"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        val dayType = this.intent.getIntExtra(KEY_DIFFICULT_TYPE, -1)

        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = ExerciseListDatabase.getDatabase(applicationContext)

        initRecyclerView(dayType)

        binding.btnTimer.setOnClickListener {
            if (binding.btnTimer.text.equals("Start")) {
                binding.btnTimer.text = "Stop"
                binding.btnTimer.setBackgroundResource(R.drawable.stop_btn)
                startTimer()
            } else {
                binding.btnTimer.text = "Start"
                stopTimer()
                binding.btnTimer.setBackgroundResource(R.drawable.start_btn)
            }
        }

        binding.btnSetTimer.setOnClickListener {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            val alert = AlertDialog.Builder(this)
            alert.setTitle(R.string.set_timer_title)
            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_NUMBER
            input.setRawInputType(Configuration.KEYBOARD_12KEY)
            alert.setView(input)
            alert.setPositiveButton(R.string.set_timer_btn_set) { _, _ ->
                if (input.text.toString() != "") {
                    workoutTimeInMillisSec = input.text.toString().toLong() * 60 * 1000
                    val totalSecs = workoutTimeInMillisSec / 1000
                    binding.txtElapsedTime.text = String.format(
                        "%02d:%02d:%02d",
                        totalSecs / 3600,
                        ((totalSecs % 3600) / 60),
                        totalSecs % 60
                    )
                    binding.btnTimer.text = "Start"
                    stopTimer()
                    binding.btnTimer.setBackgroundResource(R.drawable.start_btn)
                } else
                    Toast.makeText(
                        this@WorkoutActivity, getString(R.string.set_timer_invalid), Toast.LENGTH_LONG
                    ).show()
            }
            alert.setNegativeButton(R.string.info_button_cancel) { _, _ -> }
            alert.show()
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(workoutTimeInMillisSec, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val totalSecs = millisUntilFinished / 1000
                workoutTimeInMillisSec = millisUntilFinished
                binding.txtElapsedTime.text = String.format(
                    "%02d:%02d:%02d",
                    totalSecs / 3600,
                    ((totalSecs % 3600) / 60),
                    totalSecs % 60
                )
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                Toast.makeText(
                    this@WorkoutActivity,
                    getString(R.string.workout_time_out_message),
                    Toast.LENGTH_LONG
                ).show()
                cancel()
                binding.btnTimer.text = "Start"
                binding.btnTimer.setBackgroundResource(R.drawable.start_btn)

            }
        }.start()
    }

    private fun stopTimer() {
        timer?.cancel()
    }

    private fun initRecyclerView(dayType: Int) {
        adapter = WorkoutListAdapter(this)
        binding.recycView.layoutManager = LinearLayoutManager(this)
        binding.recycView.adapter = adapter
        loadItemsInBackground(dayType)
    }

    @SuppressLint("SetTextI18n")
    private fun loadItemsInBackground(diffType: Int) {

        thread {
            workoutExercises = when (diffType) {
                MainActivity.TYPE_MON -> database.exerciseItemDAO().getMondayExercises()
                MainActivity.TYPE_TUE -> database.exerciseItemDAO().getTuesdayExercises()
                MainActivity.TYPE_THU -> database.exerciseItemDAO().getThursdayExercises()
                MainActivity.TYPE_FRI -> database.exerciseItemDAO().getFridayExercises()
                else -> emptyList()
            }
            runOnUiThread {
                binding.recycView.startLayoutAnimation()
                adapter.load(workoutExercises)
                binding.txtExercisesDone.text =
                    workoutExercisesLeft.toString() + "/" + workoutExercises.size.toString()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onItemDone(item: ExerciseItem) {
        workoutExercisesLeft++
        binding.txtExercisesDone.text =
            workoutExercisesLeft.toString() + "/" + workoutExercises.size.toString()
        if (workoutExercisesLeft == workoutExercises.size) {
            stopTimer()
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.workout_done_title))
            builder.setMessage(
                getString(R.string.workout_done_message)
            )
            builder.setPositiveButton(getString(R.string.button_great)) { _, _ ->
                binding.btnTimer.text = "Start"
                binding.btnTimer.setBackgroundResource(R.drawable.start_btn)
                this.finish()
            }
            builder.show()
        }
        adapter.remove(item)
    }
}