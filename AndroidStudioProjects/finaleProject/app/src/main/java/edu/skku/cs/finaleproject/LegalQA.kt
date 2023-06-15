package edu.skku.cs.finaleproject

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.io.IOException
import java.net.URLEncoder

data class LegalQAResult(
    val result: Int,
    val return_object: ReturnObject
)
data class ReturnObject(
    val LegalInfo: LegalInfo
)
data class LegalInfo(
    val LegalQA: List<LegalQA>
)
data class LegalQA(
    val rank: Int? = null,
    val answer: String? = null,
    val source: String? = null,
    val clause: String? = null
)

class ListViewAdapter(val context : Context, val items:List<LegalQA>): BaseAdapter() {
    companion object{
        const val EXT_ANSWER = "extra_key_answer"
        const val EXT_SOURCE = "extra_key_source"
        const val EXT_CLAUSE = "extra_key_clause"
    }



    override fun getCount(): Int {
        return items.size

    }

    override fun getItem(p0: Int): Any {
        return items.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.qa_item, null)

        val rank_tv = view.findViewById<TextView>(R.id.rank)
        val answer_tv = view.findViewById<TextView>(R.id.answer)
        val source_tv = view.findViewById<TextView>(R.id.source)
//        val clause_tv = view.findViewById<TextView>(R.id.clause)
        val source_button = view.findViewById<Button>(R.id.move_to_Link_Button)

        source_button.setBackgroundColor(Color.BLACK)

        rank_tv.text = items.get(p0).rank.toString()
        answer_tv.text = items.get(p0).answer.toString()
        source_tv.text = items.get(p0).source.toString()
//        clause_tv.text = items.get(p0).clause.toString()
        source_button.setOnClickListener {

//            Get First Link
//            val searchTerm = items.get(p0).source.toString()
//            val encodedSearchTerm = URLEncoder.encode(searchTerm,"UTF-8")
//            val searchUrl = "https://www.google.com/search?q=$encodedSearchTerm"
//            Log.d("SearchUrl",searchUrl)

//            val client = OkHttpClient()
            CoroutineScope(Dispatchers.IO).launch{
                try{
                    val intent = Intent(context, GetDetailActivity::class.java).apply{
                        putExtra(EXT_ANSWER, answer_tv.text)
                        putExtra(EXT_CLAUSE, items.get(p0).clause.toString())
                        putExtra(EXT_SOURCE, source_tv.text)
                    }
                    context.startActivity( intent)

//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))
//                    context.startActivity(intent)
                }catch (e: Exception) {
                    e.printStackTrace()
                }
            }




//            val response = client.newCall(request).execute()
//            val responseBody = response.body!!.string()
//
//            val document = Jsoup.parse(responseBody)
//            val firstLinkUrl = document.select("div#search div.g a").first()?.absUrl("href")
//
////            Web Activity
//
//            val intent = Intent(context,WebActivity::class.java).apply {
//                putExtra("url", firstLinkUrl)
//            }
//
//            context.startActivity(intent)



        }

        return view
    }
}
