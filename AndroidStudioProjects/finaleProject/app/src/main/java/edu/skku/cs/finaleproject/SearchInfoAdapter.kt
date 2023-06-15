package edu.skku.cs.finaleproject

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URLEncoder

class SearchInfoAdapter(val context : Context, val items:List<SearchInfo>): BaseAdapter(){
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.search_info_item, null)

        val search_answer = view.findViewById<TextView>(R.id.search_answer)
        val search_source = view.findViewById<TextView>(R.id.search_source)
        val move_link_btn = view.findViewById<Button>(R.id.mv_link_btn)
        move_link_btn.setBackgroundColor(Color.BLACK)

        search_answer.text = items.get(position).answer.toString()
        search_source.text = items.get(position).source.toString()

        move_link_btn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try{
                    val searchTerm = items.get(position).source.toString()
                    val encodedSearchTerm = URLEncoder.encode(searchTerm,"UTF-8")
                    val searchUrl = "https://www.google.com/search?q=$encodedSearchTerm"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))
                    context.startActivity(intent)
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }




        return view
    }

}