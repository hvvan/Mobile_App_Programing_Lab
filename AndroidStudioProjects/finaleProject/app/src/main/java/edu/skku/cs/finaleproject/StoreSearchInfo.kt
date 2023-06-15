package edu.skku.cs.finaleproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class StoreSearchInfo : AppCompatActivity() {

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var adapter: SearchInfoAdapter
    lateinit var searchInfoList :ArrayList<SearchInfo>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_search_info)

        initDatabase()
        val databaseReference2 = firebaseDatabase.getReference("searchinfo")
        val user = Firebase.auth.currentUser
        val userId = user?.uid

        searchInfoList = ArrayList()

        var search_info_lv = findViewById<ListView>(R.id.search_list_item)
        adapter = SearchInfoAdapter(this,searchInfoList)
        search_info_lv.adapter = adapter

        databaseReference2.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = Firebase.auth.currentUser
                val userId = user?.uid
                for(ds in snapshot.children){
                    Log.d("key",ds.key.toString())
                    when{
                        userId.toString().equals(ds.key.toString()) ->{
                            val info = snapshot.child(userId.toString())
                            for(item in info.children){
                                val source : String = item.child("source").value as String
                                val answer : String = item.child("answer").value as String
                                val Sinfo = SearchInfo(source, answer)
                                searchInfoList.add(Sinfo)
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@StoreSearchInfo,"Can't Load Search information",Toast.LENGTH_SHORT).show()
            }

        })

    }


    fun initDatabase(){
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("searchinfo")
    }


}