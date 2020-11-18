package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.meallist

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.meal_list_item.view.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.R
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.Moment
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.TAG
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.Meal
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.mealedit.MealEditFragment
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class MealListAdapter(
    private val fragment: Fragment,
    private val userId: Long
) : RecyclerView.Adapter<MealListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateAgo: TextView = view.mli_text_view_date_ago
        val date: TextView = view.mli_text_view_date
        val comment: TextView = view.mli_text_view_comment
        val foods: TextView = view.mli_text_view_foods
    }

    var meals = emptyList<Meal>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onMealClick: View.OnClickListener

    init {
        onMealClick = View.OnClickListener {
            val meal = it.tag as Meal
            Log.v(TAG, meal.toString())
            Log.v(TAG, "onMealClick")
            fragment.findNavController()
                .navigate(R.id.action_MealListFragment_to_MealEditFragment, Bundle().apply {
                    putString(MealEditFragment.MEAL_ID, meal.id.toString())
                    putString("userId", userId.toString())
                })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.v(TAG, "onCreateViewHolder")
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.meal_list_item, parent, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val meal = meals[position]
        holder.comment.text = meal.comment
        holder.date.text = Moment.toDateString(meal.date!!)
        holder.dateAgo.text = Moment.fromNow(meal.dateEpoch!!)
        holder.foods.text = meal.foods
        holder.itemView.tag = meal
        holder.itemView.setOnClickListener(onMealClick)
    }

    override fun getItemCount() = meals.size
}