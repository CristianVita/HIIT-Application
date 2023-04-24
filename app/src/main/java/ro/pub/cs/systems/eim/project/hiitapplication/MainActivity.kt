package ro.pub.cs.systems.eim.project.hiitapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var hiitSessionBtn: FrameLayout? = null
    private var runningSessionBtn: FrameLayout? = null
    private var statisticsBtn: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hiitSessionBtn = findViewById(R.id.actMain_hiitSessionBtn)
        runningSessionBtn = findViewById(R.id.actMain_runningSessionBtn)
        statisticsBtn = findViewById(R.id.actMain_statisticsBtn)

        hiitSessionBtn?.setOnClickListener {
            val intent = Intent(this, HiitActivity::class.java)
            startActivity(intent)
        }

        runningSessionBtn?.setOnClickListener {
            val intent = Intent(this, RunningActivity::class.java)
            startActivity(intent)
        }

        statisticsBtn?.setOnClickListener {
            Toast.makeText(this, "Statistics", Toast.LENGTH_SHORT).show()
        }

    }


}

