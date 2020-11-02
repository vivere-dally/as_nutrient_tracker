package ro.ubbcluj.cs.sbuciu.nutrient_tracker.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.dao.FoodDao
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.dao.MealDao
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Food
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Meal
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.converter.LongArrayStringConverter

@Database(entities = [Food::class, Meal::class], version = 1, exportSchema = false)
@TypeConverters(LongArrayStringConverter::class)
abstract class NutrientTrackerDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
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
                .build()

            return INSTANCE as NutrientTrackerDatabase
        }

        private class NutrientTrackerDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        database.foodDao().delete()
                        database.mealDao().delete()
                    }
                }
            }
        }
    }
}