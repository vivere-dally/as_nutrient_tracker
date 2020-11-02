package ro.ubbcluj.cs.sbuciu.nutrient_tracker.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.StackView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.meal_list_item.view.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.R
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.config.Environment
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Meal
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.utils.TAG
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

class MealListAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<MealListAdapter.ViewHolder>() {
    var meals = emptyList<Meal>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private var formatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern(Environment.DATE_FORMAT_API, Locale.getDefault())

//    private var onMealClick: View.OnClickListener

    init {
//        onMealClick = View.OnClickListener { view ->
//            val meal = view.tag as Meal
//            fragment.findNavController().navigate(R.id.fragment_meal_list)
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.meal_list_item, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val meal = meals[position]

        val timeTV = holder.mealView[0] as TextView
        timeTV.text = DateUtils.getRelativeTimeSpanString(
            LocalDateTime
                .parse(meal.mealDate, formatter)
                .atZone(ZoneId.of("Europe/Bucharest"))
                .toInstant()
                .toEpochMilli(),
            System.currentTimeMillis(),
            DateUtils.SECOND_IN_MILLIS
        )

        val commentTV = holder.mealView[1] as TextView
        commentTV.text = meal.comment

        val foodTV = holder.mealView[2] as TextView
        foodTV.text = "da"
    }

    override fun getItemCount() = meals.count()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mealView: StackView = view.meal_list_item_sv
    }
}