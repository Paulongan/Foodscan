package fr.isen.foodscan

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import android.util.Log


class LoginActivity : AppCompatActivity() {


    companion object {
        val USER_PREF = "user_pref"
    }

    lateinit var pref: SharedPreferences
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FirebaseApp.initializeApp(applicationContext)

        pref = getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
        mAuth = FirebaseAuth.getInstance()

        IdConnectButton.setOnClickListener {
            doLogin()
        }

        IdInscription.setOnClickListener {
            doCreateIn()
        }
    }

    fun doLogin() {
        val email = emailField.text.toString()
        val password = passwordField.text.toString()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Authent reussie", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Echec authent", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun doCreateIn() {
        val email = emailField.text.toString()
        val password = passwordField.text.toString()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Enregistrement reussi", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w("debug", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, "Echec authent", Toast.LENGTH_LONG).show()
                }
            }


    }
}
