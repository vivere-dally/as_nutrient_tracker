package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.meallist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.meal_list_fragment.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.R
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.TAG

class MealListFragment : Fragment() {

    companion object {
        fun newInstance() = MealListFragment()
    }

    private lateinit var viewModel: MealListViewModel
    private lateinit var viewModelFactory: MealListViewModelFactory
    private lateinit var adapter: MealListAdapter
    private var userId: Long = -1

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
            val bundle = bundleOf("userId" to userId)
            findNavController().navigate(R.id.action_MealListFragment_to_MealEditFragment, bundle)
        }

        mlf_fab_logout.setOnClickListener {
            Log.v(TAG, "onActivityCreated - logout")
            viewModel.logout()
        }

        userId = this.arguments?.get("userId").toString().toLong()
        onActivityCreatedSetup()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun onActivityCreatedSetup() {
        adapter = MealListAdapter(this, userId)
        viewModelFactory =
            this.activity?.application?.let { MealListViewModelFactory(it, userId) }!!
        viewModel = viewModelFactory.create(MealListViewModel::class.java)
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

        viewModel.action.completed.observe(viewLifecycleOwner, {
            Log.v(TAG, "onActivityCreatedSetup - completed")
            if (it) {
                Log.v(TAG, "onActivityCreatedSetup - completed - navigate back")
                findNavController().navigate(R.id.action_MealListFragment_to_LoginFragment)
            }
        })

        viewModel.refresh()
    }
}