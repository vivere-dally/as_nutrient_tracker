package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication.login

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.NutrientTrackerDatabase
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.BaseAction
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.BaseResult
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.TAG
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication.Credentials
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication.core.AuthenticationApi
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication.core.CredentialsRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val credentialsRepository: CredentialsRepository
    var user: Credentials? = null
    val action: BaseAction = BaseAction(
        initExecuting = false,
        initCompleted = false,
        initActionError = null
    )

    init {
        val credentialsDao =
            NutrientTrackerDatabase.getDatabase(application, viewModelScope).credentialsDao()
        credentialsRepository =
            CredentialsRepository(credentialsDao, AuthenticationApi.authenticationApiService)
    }

    fun login(credentials: Credentials) {
        viewModelScope.launch {
            Log.v(TAG, "login - start")
            action.mutableExecuting.value = true
            action.mutableActionError.value = null
            when (val result: BaseResult<Credentials> =
                credentialsRepository.login(credentials.toSimpleCredentials())) {
                is BaseResult.Success -> {
                    Log.d(TAG, "login - success")
                    user = result.data
                    action.mutableCompleted.value = true
                }

                is BaseResult.Error -> {
                    Log.w(TAG, "login - error", result.exception)
                    action.mutableActionError.value = result.exception
                }
            }
        }

        action.mutableExecuting.value = false
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 3
    }
}