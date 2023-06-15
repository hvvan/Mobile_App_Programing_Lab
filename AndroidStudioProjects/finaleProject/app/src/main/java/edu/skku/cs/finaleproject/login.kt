package edu.skku.cs.finaleproject

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)
        val email_login_btn = findViewById<Button>(R.id.email_login_button)
        email_login_btn.setBackgroundColor(Color.BLACK)
        email_login_btn.setTextColor(Color.WHITE)
        val email_edt = findViewById<EditText>(R.id.email_edt)
        val password_edt = findViewById<EditText>(R.id.password_edt)
        auth = Firebase.auth

        email_login_btn.setOnClickListener{
            if(email_edt.text.toString().isNullOrEmpty() || password_edt.text.toString().isNullOrEmpty()){
                Toast.makeText(this,"이메일 혹은 비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show()
                email_edt.setText("")
                password_edt.setText("")
            }else{
                signIn(email_edt.text.toString(), password_edt.text.toString())

            }
        }
        val btn_registration = findViewById<Button>(R.id.registration_button)
        btn_registration.setBackgroundColor(Color.WHITE)
        btn_registration.setTextColor(Color.BLACK)
        btn_registration.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        val intentMain = Intent(this, MainActivity::class.java)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("로그인", "성공")
                    val user = auth.currentUser
//                        updateUI(user)
                    startActivity(intentMain)
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "정확한 아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    Log.d("로그인", "실패")
//                        updateUI(null)
                }
            }

    }


}