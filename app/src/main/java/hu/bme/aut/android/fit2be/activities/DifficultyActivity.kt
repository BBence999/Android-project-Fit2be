package hu.bme.aut.android.fit2be.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.fit2be.R
import hu.bme.aut.android.fit2be.databinding.ActivityDifficultiesBinding
import kotlin.concurrent.thread

class DifficultyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDifficultiesBinding

    companion object {
        const val TYPE_BEG = 1
        const val TYPE_INT = 2
        const val TYPE_ADV = 3
        const val TYPE_CUS = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDifficultiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBeg.setOnClickListener {
            val intent = Intent(this, ExercisesListActivity::class.java)
            intent.putExtra(ExercisesListActivity.KEY_DIFFICULT_TYPE, TYPE_BEG)
            startActivity(intent)
            this.finish()
        }
        binding.btnInt.setOnClickListener {
            val intent = Intent(this, ExercisesListActivity::class.java)
            intent.putExtra(ExercisesListActivity.KEY_DIFFICULT_TYPE, TYPE_INT)
            startActivity(intent)
            this.finish()
        }
        binding.btnAdv.setOnClickListener {
            val intent = Intent(this, ExercisesListActivity::class.java)
            intent.putExtra(ExercisesListActivity.KEY_DIFFICULT_TYPE, TYPE_ADV)
            startActivity(intent)
            this.finish()
        }
        binding.btnCus.setOnClickListener {
            val intent = Intent(this, ExercisesListActivity::class.java)
            intent.putExtra(ExercisesListActivity.KEY_DIFFICULT_TYPE, TYPE_CUS)
            startActivity(intent)
            this.finish()
        }
        binding.btnInfo.setOnClickListener()
        {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.difficulty_info_title))
            builder.setMessage(
                getString(R.string.difficulty_info_message)
            )
            builder.setPositiveButton(R.string.button_ok) { _, _ -> }
            builder.show()
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }


}
