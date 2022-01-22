package hu.bme.aut.android.fit2be.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.fit2be.activities.MuscleExerciseListActivity
import hu.bme.aut.android.fit2be.data.ExerciseItem
import hu.bme.aut.android.fit2be.R
import hu.bme.aut.android.fit2be.databinding.ElementMuscleExerciseBinding

class MuscleListAdapter(private val listener: MuscleExerciseListActivity) : RecyclerView.Adapter<MuscleListAdapter.MuscleExerciseViewHolder>() {

    inner class MuscleExerciseViewHolder(val binding: ElementMuscleExerciseBinding) : RecyclerView.ViewHolder(binding.root)

    private val items = mutableListOf<ExerciseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =

        MuscleExerciseViewHolder(ElementMuscleExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MuscleExerciseViewHolder, position: Int) {

        val exerciseItem = items[position]

        holder.binding.ivIcon.setImageResource(getImageResource(exerciseItem.body_part))
        holder.binding.tvExerciseName.text = exerciseItem.exercise_name
        holder.binding.tvMuscleGroup.text = exerciseItem.muscle_group
        holder.binding.tvSets.text = exerciseItem.sets.toString()
        holder.binding.tvReps.text = exerciseItem.reps.toString()
        holder.binding.tvDiff.text = exerciseItem.difficulty

        holder.binding.ibAddExercise.setOnClickListener {
            listener.onItemAdd(exerciseItem)
        }

    }
    @DrawableRes
    private fun getImageResource(category: ExerciseItem.BodyPart): Int {
        return when (category) {
            ExerciseItem.BodyPart.FULL -> R.drawable.full_bodypart
            ExerciseItem.BodyPart.LOWER -> R.drawable.lower_bodypart
            ExerciseItem.BodyPart.UPPER -> R.drawable.upper_bodypart
        }
    }

    interface MuscleExerciseItemClickListener {
        fun onItemAdd(item: ExerciseItem)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun load(exerciseItems: List<ExerciseItem>) {
        items.clear()
        items.addAll(exerciseItems)
        notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun remove(item: ExerciseItem) {
        items.remove(item)
        notifyDataSetChanged()
    }


}