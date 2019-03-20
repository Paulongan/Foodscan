package fr.isen.foodscan

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        IdButtonScan.setOnClickListener {
            val intent = Intent(this, ButtonScanActivity::class.java)
            startActivity(intent)

        }
        IdMesScan.setOnClickListener {
            val intent = Intent(this, ListScanActivity::class.java)
            startActivity(intent)
        }



    }
}
