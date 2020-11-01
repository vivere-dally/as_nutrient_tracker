package ro.ubbcluj.cs.sbuciu.nutrient_tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Entity
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Nutrient

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val x: Nutrient = Nutrient("3", "caca", 1.3F)
    }
}