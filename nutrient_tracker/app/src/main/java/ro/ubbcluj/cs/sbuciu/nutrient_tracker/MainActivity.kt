package ro.ubbcluj.cs.sbuciu.nutrient_tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Entity
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Nutrient
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.utils.TAG

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.activity_main_toolbar))
        Log.i(TAG, "onCreate")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_main_action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}