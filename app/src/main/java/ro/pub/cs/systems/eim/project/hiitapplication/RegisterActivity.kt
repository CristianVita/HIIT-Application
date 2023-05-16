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

class RegisterActivity : AppCompatActivity() {
    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText
    private lateinit var registerBtn: Button
    private lateinit var goToLoginActivityTV: TextView

    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        emailET = findViewById(R.id.actRegister_editTextEmail)
        passwordET = findViewById(R.id.actRegister_editTextPassword)
        registerBtn = findViewById(R.id.actRegister_registerButton)
        goToLoginActivityTV = findViewById(R.id.actRegister_goToLoginTextView)

        registerBtn.setOnClickListener {
            // Perform registration action here
            register()
        }

        goToLoginActivityTV.setOnClickListener {
            // Open login activity here
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun register() {
        // Retrieve entered information
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

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration success
                    Toast.makeText(baseContext, "Registration successful", Toast.LENGTH_SHORT,).show()
                    Log.d("RegisterActivity", "createUserWithEmail:success")
                } else {
                    // Registration failed
                    Toast.makeText(baseContext, "Authentication failed", Toast.LENGTH_SHORT,).show()
                    Log.w("RegisterActivity", "createUserWithEmail:failure", task.exception)
                }
            }
    }
}