package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication.core

import androidx.room.Dao
import androidx.room.Query
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication.Credentials
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.BaseDao

@Dao
abstract class CredentialsDao : BaseDao<Credentials, Long> {

    @Query("SELECT * FROM credentials where username=:username")
    abstract fun get(username: String): Credentials

    @Query("SELECT * FROM credentials where id=:id")
    abstract fun get(id: Long): Credentials

    @Query("SELECT * FROM credentials where NAME='LOGGED_USER'")
    abstract fun getDefault(): Credentials?

    @Query("DELETE FROM credentials")
    abstract suspend fun delete()
}