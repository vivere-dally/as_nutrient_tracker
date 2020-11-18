package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.login_fragment.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.R
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.TAG
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication.Credentials

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        onActivityCreatedSetup()
        login.setOnClickListener {
            viewModel.login(
                Credentials(
                    Credentials.SimpleCredentials(
                        null,
                        username.text.toString(),
                        password.text.toString()
                    )
                )
            )
        }
    }

    private fun onActivityCreatedSetup() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.action.executing.observe(viewLifecycleOwner, {
            Log.v(TAG, "onActivityCreatedSetup - executing")
            loading.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.action.actionError.observe(viewLifecycleOwner, {
            Log.v(TAG, "onActivityCreatedSetup - actionError")
            if (it != null) {
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.action.completed.observe(viewLifecycleOwner, { completed ->
            Log.v(TAG, "onActivityCreatedSetup - completed")
            if (completed && viewModel.user != null) {
                val user = viewModel.user
                user?.isLoggedIn?.observe(viewLifecycleOwner, {
                    if (it) {
                        Log.v(
                            TAG,
                            "onActivityCreatedSetup - user is logged - navigate to meal list"
                        )

                        val bundle = bundleOf("userId" to user.id)
                        findNavController().navigate(R.id.action_LoginFragment_to_MealListFragment, bundle)
                    }
                })
            }
        })
    }
}