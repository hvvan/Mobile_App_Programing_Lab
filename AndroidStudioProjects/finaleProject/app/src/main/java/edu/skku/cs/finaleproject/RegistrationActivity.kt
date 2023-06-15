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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class RegistrationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        auth = Firebase.auth
        database = Firebase.database.reference
        val register_id_edt = findViewById<EditText>(R.id.email_register_edt)
        val register_pw_edt = findViewById<EditText>(R.id.password_register_edt)
        val register_name_edt = findViewById<EditText>(R.id.name_register_edt)
        val register_btn = findViewById<Button>(R.id.registration_button)
        register_btn.setBackgroundColor(Color.DKGRAY)

        val intent = Intent(this, MainActivity::class.java)

        register_btn.setOnClickListener{

            val register_email = register_id_edt.text
            val register_pw = register_pw_edt.text
            val register_name = register_name_edt.text
            if(register_name_edt.text.toString().isNullOrEmpty() || register_id_edt.text.toString().isNullOrEmpty() || register_pw_edt.text.toString().isNullOrEmpty()){
                Toast.makeText(this, "아이디와 비밀번호를 제대로 입력해주세요.", Toast.LENGTH_SHORT).show()
                Log.d("Email", "${register_id_edt.text.toString()}, ${register_pw_edt.text.toString()}")
            }
            else{
                auth.createUserWithEmailAndPassword(register_email.toString(),register_pw.toString())
                    .addOnCompleteListener(this){task->
                        if(task.isSuccessful){
                            val user = Firebase.auth.currentUser
                            val userId = user?.uid
                            val userIdst = userId.toString()
                            val new_user = User(register_email.toString(),register_name.toString(),userIdst)
                            database.child("users").child(userId.toString()).setValue(new_user)
                            Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this, "등록에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }

                    }
            }
        }





    }
}