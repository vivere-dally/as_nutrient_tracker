package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import kotlinx.android.synthetic.main.meal_list_fragment.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.adapter.MealListAdapter
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.utils.TAG

class MealList : Fragment() {

    companion object {
        fun newInstance() = MealList()
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
        onActivityCreatedSetup()
    }

    private fun onActivityCreatedSetup() {
        adapter = MealListAdapter(this)
        viewModel = ViewModelProviders.of(this).get(MealListViewModel::class.java)
        mlf_recycler_view.adapter = adapter

        // Observe meals
        viewModel.meals.observe(viewLifecycleOwner, {
            Log.v(TAG, "onActivityCreatedSetup - meals - update")
            adapter.meals = it
        })

        // Observe executing
        viewModel.operationStateManager.executing.observe(viewLifecycleOwner, {
            Log.v(TAG, "onActivityCreatedSetup - operationStateManager.executing - update")
            mlf_progress_bar.visibility = if (it) View.VISIBLE else View.GONE
        })

        // Observe errors
        viewModel.operationStateManager.failure.observe(viewLifecycleOwner, {
            Log.v(TAG, "onActivityCreatedSetup - operationStateManager.failure - update")
            if (it != null) {
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })

        // Refresh
        viewModel.refresh()
    }
}