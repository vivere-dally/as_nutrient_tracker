package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.meal_list_item.view.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.MealListItemEdit
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.R
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.Food
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.Meal
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.utils.TAG
import java.io.Serializable

class MealListAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<MealListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateAgo: TextView = view.mli_text_view_date_ago
        val date: TextView = view.mli_text_view_date
        val comment: TextView = view.mli_text_view_comment
        val food: TextView = view.mli_text_view_food
    }

    var meals = emptyList<Meal>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var foods = emptyList<Food>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onMealClick: View.OnClickListener

    init {
        onMealClick = View.OnClickListener {
            val meal = it.tag as Meal
            fragment.findNavController()
                .navigate(R.id.action_MealListFragment_to_MealListItemEditFragment, Bundle().apply {
                    putString(MealListItemEdit.MEAL_ID, meal.id.toString())
                    val serializableFoods = ArrayList(foods)
                    putSerializable(MealListItemEdit.FOODS, serializableFoods)
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val meal = meals[position]
//        holder.dateAgo.text = MomentJS.fromNow(meal.mealDate)
        holder.dateAgo.text = meal.mealDate
        holder.date.text = meal.mealDate
        holder.comment.text = meal.comment
        if (foods.isEmpty()) {
            holder.food.text = holder.food.context.getString(R.string.adapter_mla_foods_are_loading)
        } else {
            holder.food.text = meal.foodIds?.map { foodId ->
                val results = foods
                    .filter { it.id == foodId }
                    .map { it.name }
                if (results.isNotEmpty()) {
                    results.first()
                } else {
                    ""
                }
            }?.joinToString()
        }
    }

    override fun getItemCount() = meals.size
}