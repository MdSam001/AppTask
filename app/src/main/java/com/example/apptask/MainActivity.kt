package com.example.apptask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import kotlin.jvm.Throws
import androidx.appcompat.widget.SearchView
import com.android.volley.DefaultRetryPolicy
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var stringRequest: StringRequest
    private val newsArray = ArrayList<News>()
    private val tempArray  = ArrayList<News>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getData()
        recyclerView = findViewById(R.id.recyclerView)

    }

    //To get News Data from API
    private fun getData() {

        //Volley.newRequestQueue(this).cache.clear()
        //val requestQueue : RequestQueue = Volley.newRequestQueue(this)
        stringRequest = object : StringRequest(
            Method.GET,
            "https://newsapi.org/v2/top-headlines?country=in&apiKey=ebadd43d87ab4443b1e00510070d00ed&pageSize=100",
            Response.Listener { response ->


                try {

                    val jo = JSONObject(response)
                    val ja = jo.getJSONArray("articles")
                    //Toast.makeText(this, "Array length is ${ja}", Toast.LENGTH_SHORT).show()
                    Log.d("heyy","$ja")
                    for(i in 0 until  ja.length()){
                        val obj = ja.getJSONObject(i)
                        val name = obj.getJSONObject("source").getString("name")
                        val title = obj.getString("title")
                        val description = obj.getString("description")
                        val urlToImage = obj.getString("urlToImage")
                        val publishedAt = obj.getString("publishedAt")
                        //Toast.makeText(this, "$name\n$title\n$description\n$publishedAt", Toast.LENGTH_SHORT).show()
                        newsArray.add(News(name,title,description,urlToImage, publishedAt))

                    }

                    tempArray.addAll(newsArray)
                    recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                    newsAdapter = NewsAdapter(this,tempArray)
                    recyclerView.adapter = newsAdapter
                    Toast.makeText(this, "${tempArray.size}", Toast.LENGTH_SHORT).show()

                }catch (e : Exception){

                    Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()

                }

            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
            }
        ){


            override fun getBodyContentType(): String {
                return "application/json"
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["User-Agent"] = "Mozilla/5.0"
                return params
            }

        }
        //Volley.newRequestQueue(this).cache.clear()
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            2,
            2.0f
        )
        Volley.newRequestQueue(this).add(stringRequest)



    //getData() Ends
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu,menu)
        val item = menu?.findItem(R.id.search_action_btn)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {

                tempArray.clear()
                val searchtext = newText!!.lowercase(Locale.getDefault())
                if(searchtext.isNotEmpty()){
                    newsArray.forEach {

                        if (it.title.lowercase(Locale.getDefault()).contains(searchtext)){

                            tempArray.add(it)

                        }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                else{

                    tempArray.clear()
                    tempArray.addAll(newsArray)
                    recyclerView.adapter!!.notifyDataSetChanged()
                    //newsAdapter = NewsAdapter(this@MainActivity,tempArray)
                }

                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

//MainActivity Ends
}