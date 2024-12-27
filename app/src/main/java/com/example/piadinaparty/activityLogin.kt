package com.example.piadinaparty

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.piadinaparty.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class activityLogin : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener{
            val email = binding.LoginEmail.text.toString()
            val password = binding.LoginPassword.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){task ->
                        if (task.isSuccessful){
                            Toast.makeText(this,"Login avvenuto correttamente!",Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("FRAGMENT_TO_LOAD", "HOME_FRAGMENT")
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this,"Login fallito!",Toast.LENGTH_SHORT).show()

                        }
                    }
            }else{
                Toast.makeText(this,"Inserire Email e Password",Toast.LENGTH_SHORT).show()

            }
        }
        binding.Messaggioschermo1.setOnClickListener{
            startActivity(Intent(this,activityRegistrazione::class.java))
            finish()
        }
    }
}