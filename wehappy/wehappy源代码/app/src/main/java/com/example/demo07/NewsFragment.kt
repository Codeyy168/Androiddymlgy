package com.example.demo07

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.movie_frag.*
import kotlinx.android.synthetic.main.news_frag.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class NewsFragment: Fragment() {
    val response = StringBuilder()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.news_frag, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getNews()
    }

    interface GetNews {
        @GET("index?")
        fun getMessageByGet(@Query("type")type:String, @Query("key")key:String): Call<News>
    }
    fun getNews() {
        Thread {
            val retrofit = Retrofit.Builder().baseUrl("http://v.juhe.cn/toutiao/")
                .addConverterFactory(
                    GsonConverterFactory.create()
                ).build()
            val News = retrofit.create(GetNews::class.java)
            News.getMessageByGet("top", "154cabcccee4a2b654fef022d9c75c6d")
                .enqueue(object : Callback<News> {
                    override fun onResponse(
                        call: Call<News>,
                        response: Response<News>
                    ) {
                        val msg = response.body()
                        if (msg != null) {
                            val newsList = msg.result.data
                            getActivity()?.runOnUiThread {
                                val layoutManager= LinearLayoutManager(activity)
                                news_recycle.layoutManager = GridLayoutManager(activity, 3)
                                news_recycle.layoutManager = layoutManager
                                val adapter = NewsAdapter(newsList)
                                news_recycle.adapter = adapter
                            }
                        }
                    }

                    override fun onFailure(call: Call<News>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }.start()
    }
    inner class NewsAdapter(val newsList: List<News.ResultBean.DataBean>) :
        RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title:TextView= view.findViewById(R.id.title)
            val image: ImageView = view.findViewById(R.id.image)
            val date: TextView = view.findViewById(R.id.date)
            val authorname: TextView = view.findViewById(R.id.authorname)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
            val holder = ViewHolder(view)
            holder.itemView.setOnClickListener {
                val news = newsList[holder.adapterPosition]
                val intent=Intent(activity, NewsContentActivity::class.java)
                intent.putExtra("url", news.url)
                startActivity(intent)
            }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val news = newsList[position]
            holder.title.text=news.title
            getimage.showImage(news.thumbnail_pic_s,holder.image)
            holder.date.text = news.date
            holder.authorname.text = news.author_name
        }

        override fun getItemCount() = newsList.size
    }
}