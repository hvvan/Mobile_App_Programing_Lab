package edu.skku.cs.finaleproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.skku.cs.finaleproject.ListViewAdapter.Companion.EXT_ANSWER
import edu.skku.cs.finaleproject.ListViewAdapter.Companion.EXT_CLAUSE
import edu.skku.cs.finaleproject.ListViewAdapter.Companion.EXT_SOURCE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.net.URLEncoder
import kotlin.coroutines.CoroutineContext

class GetDetailActivity : AppCompatActivity() {
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_detail)

        val answer = intent.getStringExtra(EXT_ANSWER)
        val clause = intent.getStringExtra(EXT_CLAUSE)
        val source = intent.getStringExtra(EXT_SOURCE)

        val answer_textview = findViewById<TextView>(R.id.Ans_intent)
        val detail_textview = findViewById<TextView>(R.id.Detail_intent_tv)
        val source_textview = findViewById<TextView>(R.id.Source_intent_tv)

        answer_textview.text = answer
        detail_textview.text = clause
        source_textview.text = source

        val Internet_btn = findViewById<Button>(R.id.InternetButton)
        val SendData_btn = findViewById<Button>(R.id.SendDataButton)

        Internet_btn.setBackgroundColor(Color.BLACK)
        SendData_btn.setBackgroundColor(Color.BLACK)

        Internet_btn.setOnClickListener {
            val searchTerm = source
            val encodedSearchTerm = URLEncoder.encode(searchTerm,"UTF-8")
            val searchUrl = "https://www.google.com/search?q=$encodedSearchTerm"
            Log.d("SearchUrl",searchUrl)

            val client = OkHttpClient()
            CoroutineScope(Dispatchers.IO).launch{
                try{
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))
                    startActivity(intent)
                }catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        initDatabase()
        SendData_btn.setOnClickListener {
            val databaseReference1 = firebaseDatabase.getReference("searchinfo")

//            databaseReference1.child()

            val SearchInfo = SearchInfo(source, answer)

            val user = Firebase.auth.currentUser
            val userId = user?.uid
//            database.child("users").child(userId.toString()).setValue(SearchInfo)
            databaseReference1.child(userId.toString()).push().setValue(SearchInfo)

            Toast.makeText(this, "검색 결과를 저장하였습니다.",Toast.LENGTH_SHORT).show()



        }





    }

    fun initDatabase(){
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("searchinfo")
    }
}