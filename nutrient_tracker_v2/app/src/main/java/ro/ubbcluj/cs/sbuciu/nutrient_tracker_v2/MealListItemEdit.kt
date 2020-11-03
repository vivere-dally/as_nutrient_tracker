package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MealListItemEdit : Fragment() {

    companion object {
        fun newInstance() = MealListItemEdit()
    }

    private lateinit var viewModel: MealListItemEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.meal_list_item_edit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MealListItemEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

}