package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.meal_list_fragment.*
import kotlinx.android.synthetic.main.meal_list_item_edit_fragment.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.Food
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.Meal
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.utils.TAG
import java.util.*

class MealListItemEdit : Fragment() {

    companion object {
        const val MEAL_ID = "MEAL_ID"
        const val FOODS = "FOODS"
        fun newInstance() = MealListItemEdit()
    }

    private lateinit var viewModel: MealListItemEditViewModel
    private var mealId: Long? = null
    private var foods: List<Food> = emptyList()
    private var actualFoods: MutableList<Food> = mutableListOf()
    private var meal: Meal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
        arguments?.let {
            if (it.containsKey(MEAL_ID)) {
                mealId = it.getString(MEAL_ID)?.toLong()
            }

            if (it.containsKey(FOODS)) {
                foods = it.getSerializable(FOODS) as List<Food>
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.meal_list_item_edit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onActivityCreatedSetup()
        mlief_fab_save.setOnClickListener {
            val m = meal
            if (m != null) {
                fieldsToMeal()
                viewModel.saveOrUpdateMeal(m)
            }
        }

        mlief_fab_delete.setOnClickListener {
            val id = mealId
            if (id != null) {
                viewModel.deleteMealById(id)
            }
        }
    }

    private fun onActivityCreatedSetup() {
        viewModel = ViewModelProviders.of(this).get(MealListItemEditViewModel::class.java)
        // Observe executing
        viewModel.operationStateManager.executing.observe(viewLifecycleOwner, {
            Log.v(TAG, "onActivityCreatedSetup - operationStateManager.executing - update")
            mlief_progress_bar.visibility = if (it) View.VISIBLE else View.GONE
        })

        // Observe errors
        viewModel.operationStateManager.failure.observe(viewLifecycleOwner, {
            Log.v(TAG, "onActivityCreatedSetup - operationStateManager.failure - update")
            if (it != null) {
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.operationStateManager.completed.observe(viewLifecycleOwner, {
            Log.v(TAG, "onActivityCreatedSetup - operationStateManager.completed - update")
            if (it) {
                Log.v(
                    TAG,
                    "onActivityCreatedSetup - operationStateManager.completed - navigate back"
                )
                findNavController().popBackStack()
            }
        })

        val id = mealId
        if (id == null) {
            meal = Meal(null, null, null, null)
        } else {
            viewModel.getMealById(id).observe(viewLifecycleOwner, {
                Log.v(TAG, "onActivityCreatedSetup - updated meal")
                if (it != null) {
                    meal = it
                    mealToFields()
                }
            })
        }
    }

    private fun mealToFields() {
        mlief_edit_text_comment.setText(meal?.comment)
        val adapter: ArrayAdapter<Food> =
            ArrayAdapter<Food>(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                foods
            )
        mlief_spinner_foods.adapter = adapter
        mlief_spinner_foods.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                actualFoods.add(foods[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

    }

    private fun fieldsToMeal() {

    }
}