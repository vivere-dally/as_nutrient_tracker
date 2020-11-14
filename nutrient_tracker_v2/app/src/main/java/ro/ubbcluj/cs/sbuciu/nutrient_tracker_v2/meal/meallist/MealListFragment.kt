package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.meallist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.meal_list_fragment.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.R
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.TAG

class MealListFragment : Fragment() {

    companion object {
        fun newInstance() = MealListFragment()
    }

    private lateinit var viewModel: MealListViewModel
    private lateinit var adapter: MealListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.meal_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mlf_fab.setOnClickListener {
            Log.v(TAG, "onActivityCreated - newMeal")
            findNavController().navigate(R.id.action_MealListFragment_to_MealEditFragment)
        }

        onActivityCreatedSetup()
    }

    private fun onActivityCreatedSetup() {
        adapter = MealListAdapter(this)
        viewModel = ViewModelProviders.of(this).get(MealListViewModel::class.java)
        mlf_recycler_view.adapter = adapter

        viewModel.meals.observe(viewLifecycleOwner, { meals ->
            Log.v(TAG, "onActivityCreatedSetup - meals")
            adapter.meals = meals.sortedByDescending { it.dateEpoch }
        })

        viewModel.action.executing.observe(viewLifecycleOwner, {
            Log.v(TAG, "onActivityCreatedSetup - executing")
            mlf_progress_bar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.action.actionError.observe(viewLifecycleOwner, {
            Log.v(TAG, "onActivityCreatedSetup - actionError")
            if (it != null) {
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.refresh()
    }
}