package hu.bme.aut.android.fit2be.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import hu.bme.aut.android.fit2be.R
import hu.bme.aut.android.fit2be.data.ExerciseListDatabase
import hu.bme.aut.android.fit2be.databinding.ActivityMainBinding
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    companion object {
        const val TYPE_MON = 1
        const val TYPE_TUE = 2
        const val TYPE_WED = 3
        const val TYPE_THU = 4
        const val TYPE_FRI = 5
        const val TYPE_SAT = 6
        const val TYPE_SUN = 7
    }

    private lateinit var database: ExerciseListDatabase
    private lateinit var binding: ActivityMainBinding
    private var haveCurrentPlan: Boolean = false

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(800)
        Locale.ENGLISH
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = ExerciseListDatabase.getDatabase(applicationContext)
        thread {
            haveCurrentPlan = database.exerciseItemDAO().getMyPlan().isNotEmpty()
        }
        val currentDay: String = LocalDate.now().dayOfWeek.name
        binding.txtDay.text = " $currentDay"

        when (currentDay) {
            "MONDAY"-> binding.btnMday.setBackgroundResource(R.drawable.design_button_current_day)
            "TUESDAY" -> binding.btnTuday.setBackgroundResource(R.drawable.design_button_current_day)
            "WEDNESDAY" -> binding.btnWday.setBackgroundResource(R.drawable.design_button_current_day)
            "THURSDAY" -> binding.btnThday.setBackgroundResource(R.drawable.design_button_current_day)
            "FRIDAY" -> binding.btnFday.setBackgroundResource(R.drawable.design_button_current_day)
            "SATURDAY" -> binding.btnSatday.setBackgroundResource(R.drawable.design_button_current_day)
            "SUNDAY" -> binding.btnSunday.setBackgroundResource(R.drawable.design_button_current_day)
        }

        binding.btnNewPlan.setOnClickListener {
            if (haveCurrentPlan) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.new_plan_toast_msg),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                startActivity(Intent(this, DifficultyActivity::class.java))
                this.finish()
            }
        }
        binding.btnDelPlan.setOnClickListener {
            if (!haveCurrentPlan)
                Toast.makeText(
                    applicationContext,
                    getString(R.string.delete_failed_plan_toast_msg),
                    Toast.LENGTH_LONG
                ).show()
            else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.delete_plan_info_title))
                builder.setMessage(getString(R.string.delete_plan_info_question))
                builder.setPositiveButton(R.string.info_button_yes) { _, _ ->
                    thread {
                        database.exerciseItemDAO().resetMyPlan()
                    }
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.delete_success_plan_toast_message),
                        Toast.LENGTH_LONG
                    ).show()
                    haveCurrentPlan = false
                }
                builder.setNegativeButton(R.string.info_button_cancel) { _, _ -> }
                builder.show()

            }
        }
        binding.btnInfo.setOnClickListener()
        {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.fit2be_info_title))
            builder.setMessage(
                getString(R.string.fit2be_info_message)
            )
            builder.setPositiveButton(R.string.button_ok) { _, _ -> }
            builder.show()
        }

        binding.btnMday.setOnClickListener()
        {
            if (!haveCurrentPlan) {
                noWorkoutPlanDialog()
            } else {
                val intent = Intent(this, WorkoutActivity::class.java)
                intent.putExtra(WorkoutActivity.KEY_DIFFICULT_TYPE, TYPE_MON)
                startActivity(intent)
            }
        }
        binding.btnTuday.setOnClickListener()
        {
            if (!haveCurrentPlan) {
                noWorkoutPlanDialog()
            } else {
                val intent = Intent(this, WorkoutActivity::class.java)
                intent.putExtra(WorkoutActivity.KEY_DIFFICULT_TYPE, TYPE_TUE)
                startActivity(intent)
            }
        }
        binding.btnWday.setOnClickListener()
        {
            restDayDialog()
        }
        binding.btnThday.setOnClickListener()
        {
            if (!haveCurrentPlan) {
                noWorkoutPlanDialog()
            } else {
                val intent = Intent(this, WorkoutActivity::class.java)
                intent.putExtra(WorkoutActivity.KEY_DIFFICULT_TYPE, TYPE_THU)
                startActivity(intent)
            }
        }
        binding.btnFday.setOnClickListener()
        {
            if (!haveCurrentPlan) {
                noWorkoutPlanDialog()
            } else {
                val intent = Intent(this, WorkoutActivity::class.java)
                intent.putExtra(WorkoutActivity.KEY_DIFFICULT_TYPE, TYPE_FRI)
                startActivity(intent)
            }
        }
        binding.btnSatday.setOnClickListener()
        {
            restDayDialog()
        }
        binding.btnSunday.setOnClickListener()
        {
            restDayDialog()
        }

    }

    private fun restDayDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.rest_day_info_title))
        builder.setMessage(
            getString(R.string.rest_day_info_message)
        )
        builder.setPositiveButton(R.string.button_ok) { _, _ -> }
        builder.show()

    }

    private fun noWorkoutPlanDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.no_workout_plan_info_title))
        builder.setMessage(
            getString(R.string.no_workout_plan_info_message)
        )
        builder.setPositiveButton(R.string.button_ok) { _, _ -> }
        builder.show()

    }

    override fun onBackPressed() {
        thread {
            database.close()
        }
        this.finish()
    }

    override fun onRestart() {
        super.onRestart()
        thread {
            haveCurrentPlan = database.exerciseItemDAO().getMyPlan().isNotEmpty()
        }
    }


}