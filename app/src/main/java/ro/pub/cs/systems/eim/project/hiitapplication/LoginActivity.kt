package ro.pub.cs.systems.eim.project.hiitapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText
    private lateinit var loginBtn: Button
    private lateinit var registerTV: TextView

    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        emailET = findViewById(R.id.actLogin_editTextEmail)
        passwordET = findViewById(R.id.actLogin_editTextPassword)
        loginBtn = findViewById(R.id.actLogin_loginButton)
        registerTV = findViewById(R.id.actLogin_registerTextView)

        loginBtn.setOnClickListener {
            login()
        }

        registerTV.setOnClickListener {
            // Open registration activity here
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        // Retrieve entered email and password
        val email = emailET.text.toString()
        val password = passwordET.text.toString()

        // Check if email and password are valid (e.g., not empty)
        if (email.isEmpty()) {
            Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.d("LoginActivity", "signInWithEmail:success")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.d("LoginActivity", "signInWithEmail:failure", task.exception)
                    // If sign in fails, display a message to the user
                    Toast.makeText(baseContext, "Authentication failed", Toast.LENGTH_SHORT,).show()
                }
            }
    }
}