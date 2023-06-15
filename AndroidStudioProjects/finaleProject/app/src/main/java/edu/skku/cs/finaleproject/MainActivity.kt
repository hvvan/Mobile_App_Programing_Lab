package edu.skku.cs.finaleproject

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var button: Button
    private lateinit var responseTextView: TextView

    val openApiURL = "http://aiopen.etri.re.kr:8000/LegalQA"
    val accessKey = "39806840-7d85-410e-a538-3660d97deded" // 발급받은 API Key
    val client = OkHttpClient()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.Search_edt)
        button = findViewById(R.id.Search_button)


        val searchInfo_button = findViewById<Button>(R.id.button_searchinfo)


        searchInfo_button.setBackgroundColor(Color.BLACK)

        button.setOnClickListener{
            val question = editText.text.toString()
            val mediaType = "application/json; charset=utf-8".toMediaType()
            if(question.isNotEmpty()){
                val request = HashMap<String, Any>()
                val argument = HashMap<String, String>()

                argument["question"] = question

                request["argument"] = argument

                val requestBody = Gson().toJson(request).toRequestBody(mediaType)

                val httpRequest = Request.Builder()
                    .url(openApiURL)
                    .addHeader("Content-Type","application/json; charset=UTF-8")
                    .addHeader("Authorization",accessKey)
                    .post(requestBody)
                    .build()

                client.newCall(httpRequest).enqueue(object: Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.use{
                            if (!response.isSuccessful) throw IOException("Unexpected code $response")
                            val responseBody = response.body!!.string()
                            Log.d("reponseBody","${responseBody}")
                            val jsonResponse = JSONObject(responseBody)
                            val returnObject = jsonResponse.getJSONObject("return_object")
                            val legalInfoObject = returnObject.getJSONObject("LegalInfo")
                            val legalQAArray = legalInfoObject.getJSONArray("AnswerInfo")

                            val typeToken = object : TypeToken<List<LegalQA>>() {}.type
                            val legalQAResult: List<LegalQA> = Gson().fromJson(legalQAArray.toString(), typeToken)
//                            Log.d("data","${legalQAResult[0].answer.toString()}")
                            if(legalQAArray.length()>0){
                                CoroutineScope(Dispatchers.Main).launch {
                                    val listAdapter = ListViewAdapter(this@MainActivity, legalQAResult)
                                    var listView = findViewById<ListView>(R.id.listView)
                                    listView.adapter = listAdapter
                                }

                            }
                            else{
                                CoroutineScope(Dispatchers.Main).launch{
                                    Toast.makeText(this@MainActivity, "Please enter a question", Toast.LENGTH_SHORT).show()
                                    editText.setText("")
                                }

                            }



                        }
                    }

                })

            }else{
                Toast.makeText(this, "Please enter a question", Toast.LENGTH_SHORT).show()
            }
        }


        searchInfo_button.setOnClickListener {
            val intent = Intent(this,StoreSearchInfo::class.java)
            startActivity(intent)
        }
    }

//    private fun makeRequest(question: String) {
//        val request = HashMap<String, Any>()
//        val argument = HashMap<String, String>()
//
//        argument["question"] = question
//
//        request["argument"] = argument
//
////        GlobalScope.launch(Dispatchers.IO) {
////            var responseCode: Int? = null
////            var responseBody: String? = null
////            try {
////                val url = URL(openApiURL)
////                val con = url.openConnection() as HttpURLConnection
////                con.requestMethod = "POST"
////                con.doOutput = true
////                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
////                con.setRequestProperty("Authorization", accessKey)
////
////                val wr = DataOutputStream(con.outputStream)
////                wr.write(Gson(request).toByteArray(Charsets.UTF_8))
////                wr.flush()
////                wr.close()
////
////                responseCode = con.responseCode
////                val inputStream: InputStream = con.inputStream
////                val buffer = ByteArray(inputStream.available())
////                val bytesRead = inputStream.read(buffer)
////                responseBody = String(buffer)
////
////                runOnUiThread {
////                    // Extract the legal QA result JSON from the response body
////                    val resultJson = extractLegalQAResult(responseBody)
////                    responseTextView.text = resultJson
////                }
////            } catch (e: MalformedURLException) {
////                e.printStackTrace()
////            } catch (e: IOException) {
////                e.printStackTrace()
////            }
////        }
//    }

//    private fun extractLegalQAResult(responseBody: String?): String {
//        var legalQAResult = ""
//
//        try {
//            val jsonResponse = JSONObject(responseBody)
//            val returnObject = jsonResponse.getJSONObject("return_object")
//            legalQAResult = returnObject.toString()
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//        return legalQAResult
//    }

}