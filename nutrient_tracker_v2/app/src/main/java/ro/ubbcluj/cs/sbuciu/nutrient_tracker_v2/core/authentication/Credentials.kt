package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.BaseEntity

@Entity(tableName = "credentials")
class Credentials(

    @PrimaryKey
    @ColumnInfo(name = "id")
    override var id: Long?,

    @ColumnInfo(name = "NAME", defaultValue = "LOGGED_USER")
    var NAME: String? = "LOGGED_USER",

    @ColumnInfo(name = "username")
    var username: String?,

    @ColumnInfo(name = "password")
    var password: String?,

    @ColumnInfo(name = "token")
    var token: String?
) : BaseEntity<Long> {
    class SimpleCredentials(
        var id: Long?,
        var username: String?,
        var password: String?,
    )

    @Ignore
    val mutableIsLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

    @Ignore
    val isLoggedIn: LiveData<Boolean> = mutableIsLoggedIn

    fun toSimpleCredentials(): SimpleCredentials {
        return SimpleCredentials(id, username, password)
    }

    constructor(credentials: SimpleCredentials) : this(
        credentials.id,
        "LOGGED_USER",
        credentials.username,
        credentials.password,
        null
    )
}