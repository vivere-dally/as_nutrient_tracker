package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.BaseResult
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.RetrofitConfig
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication.Credentials
import java.lang.Exception

class CredentialsRepository(
    private val credentialsDao: CredentialsDao,
    private val authenticationApiService: AuthenticationApi.AuthenticationApiService
) {

    fun defaultLogin(): BaseResult<Credentials> {
        val storedCredentials = credentialsDao.getDefault()
        return if (storedCredentials != null) {
            storedCredentials.mutableIsLoggedIn.value = true
            RetrofitConfig.tokenInterceptor.credentials = storedCredentials
            BaseResult.Success(storedCredentials)
        } else {
            BaseResult.Error(Exception("No credentials stored!"))
        }
    }

    suspend fun login(credentials: Credentials.SimpleCredentials): BaseResult<Credentials> {
        return try {
            val validatedCredentials = authenticationApiService.login(credentials)
            validatedCredentials.isLoggedIn.observeForever {
                if (it) {
                    CoroutineScope(Dispatchers.Main).launch {
                        credentialsDao.save(validatedCredentials)
                    }
                }
            }

            BaseResult.Success(validatedCredentials)
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun logout(): BaseResult<Boolean> {
        return try {
            authenticationApiService.logout()
            credentialsDao.delete()
            BaseResult.Success(true)
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }
}