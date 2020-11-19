package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication.core

import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.BaseResult
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication.Credentials
import java.lang.Exception

class CredentialsRepository(
    private val credentialsDao: CredentialsDao,
    private val authenticationApiService: AuthenticationApi.AuthenticationApiService
) {

    suspend fun login(credentials: Credentials.SimpleCredentials): BaseResult<Credentials> {
        return try {
            val validatedCredentials = authenticationApiService.login(credentials)
            credentialsDao.save(validatedCredentials)
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