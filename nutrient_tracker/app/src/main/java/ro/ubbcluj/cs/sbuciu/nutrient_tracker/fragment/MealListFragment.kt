package ro.ubbcluj.cs.sbuciu.nutrient_tracker.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_meal_list.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.R
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.adapter.MealListAdapter
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.utils.TAG
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.viewmodel.MealListViewModel

class MealListFragment : Fragment() {
    companion object {
        const val ITEM_ID = "ITEM_ID"
    }

    private lateinit var mealListViewModel: MealListViewModel
    private lateinit var mealListAdapter: MealListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_meal_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setup()
    }

    private fun setup() {
        mealListAdapter = MealListAdapter(this)
        meal_list.adapter = mealListAdapter
        mealListViewModel = ViewModelProvider(this).get(MealListViewModel::class.java)
        mealListViewModel.meals.observe(viewLifecycleOwner, {
            Log.v(TAG, "meals - observe")
            mealListAdapter.meals = it
        })

        mealListViewModel.operationStateManager.executing.observe(viewLifecycleOwner, {
            Log.v(TAG, "executing - observe")
            progress_bar.visibility = if (it) View.VISIBLE else View.GONE
        })

        mealListViewModel.operationStateManager.failure.observe(viewLifecycleOwner, {
            Log.v(TAG, "failure - observe")
            if (it != null) {
                Log.i(TAG, "failure - payload")
                val message = "Exception occurred ${it.message}."
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()

            }
        })

        mealListViewModel.refresh()
    }
}