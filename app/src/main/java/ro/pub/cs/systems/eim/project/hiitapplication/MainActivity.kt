package ro.pub.cs.systems.eim.project.hiitapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private var hiitSessionBtn: FrameLayout? = null
    private var runningSessionBtn: FrameLayout? = null
    private var statisticsBtn: FrameLayout? = null
    private var welcomeTV: TextView? = null
    private var logoutBtn: Button? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        if (user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        hiitSessionBtn = findViewById(R.id.actMain_hiitSessionBtn)
        runningSessionBtn = findViewById(R.id.actMain_runningSessionBtn)
        statisticsBtn = findViewById(R.id.actMain_statisticsBtn)
        welcomeTV = findViewById(R.id.actMain_welcomeTextView)
        logoutBtn = findViewById(R.id.actMain_logoutButton)

        hiitSessionBtn?.setOnClickListener {
            val intent = Intent(this, HiitActivity::class.java)
            startActivity(intent)
        }

        runningSessionBtn?.setOnClickListener {
            val intent = Intent(this, RunningActivity::class.java)
            startActivity(intent)
        }

        statisticsBtn?.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }

        welcomeTV?.text = "Welcome, ${user.email}"
        logoutBtn?.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


}

