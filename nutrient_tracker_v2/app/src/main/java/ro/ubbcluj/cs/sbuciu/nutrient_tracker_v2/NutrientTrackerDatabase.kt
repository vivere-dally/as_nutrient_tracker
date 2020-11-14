package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.Meal
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.core.MealDao

@Database(entities = [Meal::class], version = 1, exportSchema = false)
abstract class NutrientTrackerDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        private var INSTANCE: NutrientTrackerDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NutrientTrackerDatabase {
            if (INSTANCE != null) {
                return INSTANCE as NutrientTrackerDatabase
            }

            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    NutrientTrackerDatabase::class.java,
                    "nutrient_tracker_db"
                )
                .addCallback(NutrientTrackerDatabaseCallback(scope))
                .allowMainThreadQueries()
                .build()

            return INSTANCE as NutrientTrackerDatabase
        }

        private class NutrientTrackerDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        database.mealDao().delete()
                    }
                }
            }
        }
    }
}