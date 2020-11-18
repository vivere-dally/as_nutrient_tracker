package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.mealedit

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.meal_edit_fragment.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.R
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.TAG
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.Meal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

class MealEditFragment : Fragment() {

    companion object {
        const val MEAL_ID = "MEAL_ID"
        fun newInstance() = MealEditFragment()
    }

    private lateinit var viewModel: MealEditViewModel
    private lateinit var viewModelFactory: MealEditViewModelFactory
    private var mealId: Long? = null
    private var meal: Meal? = null
    private var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
        arguments?.let {
            if (it.containsKey(MEAL_ID)) {
                mealId = it.getString(MEAL_ID)?.toLong()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.meal_edit_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        userId = this.arguments?.get("userId").toString().toLong()
        onActivityCreatedSetup()
        mef_fab_save.setOnClickListener {
            constructMeal()
            val m = meal
            if (m != null) {
                viewModel.saveOrUpdateMeal(m)
            }
        }

        mef_fab_delete.setOnClickListener {
            val mi = mealId
            if (mi != null) {
                viewModel.deleteMealById(mi)
            }
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun onActivityCreatedSetup() {
        viewModelFactory =
            this.activity?.application?.let { MealEditViewModelFactory(it, userId) }!!
        viewModel = viewModelFactory.create(MealEditViewModel::class.java)

        viewModel.action.executing.observe(viewLifecycleOwner, {
            Log.v(TAG, "onActivityCreatedSetup - executing")
            mef_progress_bar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.action.actionError.observe(viewLifecycleOwner, {
            Log.v(TAG, "onActivityCreatedSetup - actionError")
            if (it != null) {
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.action.completed.observe(viewLifecycleOwner, {
            Log.v(TAG, "onActivityCreatedSetup - completed")
            if (it) {
                Log.v(TAG, "onActivityCreatedSetup - completed - navigate back")
                findNavController().popBackStack()
            }
        })

        // Set number picker limits
        mef_number_picker_year.minValue = 1920
        mef_number_picker_year.maxValue = 2120
        mef_number_picker_year.wrapSelectorWheel = true
        mef_number_picker_month.minValue = 1
        mef_number_picker_month.maxValue = 12
        mef_number_picker_month.wrapSelectorWheel = true
        mef_number_picker_day.minValue = 1
        mef_number_picker_day.maxValue = 31
        mef_number_picker_day.wrapSelectorWheel = true
        mef_number_picker_hour.minValue = 0
        mef_number_picker_hour.maxValue = 23
        mef_number_picker_hour.wrapSelectorWheel = true
        mef_number_picker_minute.minValue = 0
        mef_number_picker_minute.maxValue = 59
        mef_number_picker_minute.wrapSelectorWheel = true

        val mi = mealId
        if (mi == null) {
            meal = null
            deconstructMeal(meal)
        } else {
            viewModel.getMealById(mi).observe(viewLifecycleOwner, {
                Log.v(TAG, "onActivityCreatedSetup - getMealById")
                if (it != null) {
                    meal = it
                    deconstructMeal(meal)
                }
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun constructMeal() {
        val now = LocalDateTime.now()
        val date = LocalDateTime.of(
            mef_number_picker_year.value,
            mef_number_picker_month.value,
            mef_number_picker_day.value,
            mef_number_picker_hour.value,
            mef_number_picker_minute.value,
            now.second,
            now.nano
        )

        meal = Meal(
            id = mealId,
            comment = mef_edit_text_comment.text.toString(),
            date = date.toString(),
            dateEpoch = null,
            eaten = mef_switch_eaten.isChecked,
            foods = mef_edit_text_foods.text.toString(),
            price = mef_edit_text_price.text.toString().toFloat(),
            userId = null
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun deconstructMeal(nullableMeal: Meal?) {
        Log.v(TAG, "deconstructMeal")

        if (nullableMeal != null) {
            mef_edit_text_comment.setText(nullableMeal.comment)
            mef_edit_text_foods.setText(nullableMeal.foods)
            mef_edit_text_price.setText(nullableMeal.price.toString())
            mef_switch_eaten.isChecked = nullableMeal.eaten!!

            val mealDate = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(nullableMeal.dateEpoch!!),
                ZoneId.systemDefault()
            )

            mef_number_picker_year.value = mealDate.year
            mef_number_picker_month.value = mealDate.monthValue
            mef_number_picker_day.value = mealDate.dayOfMonth
            mef_number_picker_hour.value = mealDate.hour
            mef_number_picker_minute.value = mealDate.minute
        } else {
            val calendar = Calendar.getInstance()
            mef_number_picker_year.value = calendar.get(Calendar.YEAR)
            mef_number_picker_month.value = calendar.get(Calendar.MONTH) + 1
            mef_number_picker_day.value = calendar.get(Calendar.DAY_OF_MONTH)
            mef_number_picker_hour.value = calendar.get(Calendar.HOUR_OF_DAY)
            mef_number_picker_minute.value = calendar.get(Calendar.MINUTE)
        }
    }
}