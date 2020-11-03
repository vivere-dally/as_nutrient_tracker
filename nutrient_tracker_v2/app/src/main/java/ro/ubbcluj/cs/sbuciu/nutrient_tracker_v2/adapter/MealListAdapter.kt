package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.meal_list_item.view.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.R
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.Meal
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.utils.TAG

class MealListAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<MealListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mealView: View = view.mli_text_view
    }

    var meals = emptyList<Meal>()
        set(value) {
            field = value
            notifyDataSetChanged()
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
        val mliTextView = holder.mealView as TextView
        mliTextView.text = meal.comment
    }

    override fun getItemCount() = meals.size
}