package com.example.demo07

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class MoviesFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.movie_frag, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getMovies()
    }

    interface GetMovies {
        @GET("movies.today?")
        fun getMessageByGet(@Query("key")key:String, @Query("cityid")cityid:Int): Call<Movie>
    }

    fun getMovies() {
        Thread {
            val retrofit = Retrofit.Builder().baseUrl("http://v.juhe.cn/movie/")
                .addConverterFactory(
                    GsonConverterFactory.create()
                ).build()
            val Movie = retrofit.create(GetMovies::class.java)
            Movie.getMessageByGet("be160a45904290daa2a2eab5ae4247c5", 3)
                .enqueue(object : Callback<Movie> {
                    override fun onResponse(
                        call: Call<Movie>,
                        response: Response<Movie>
                    ) {
                        val msg = response.body()
                        if (msg != null) {
                            val moviesList = msg.result

                            getActivity()?.runOnUiThread {
                                val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
                                movies_recycle.layoutManager = layoutManager
                                movies_recycle.layoutManager  = GridLayoutManager(activity, 2)
                                val adapter = MoviesAdapter(moviesList)
                                movies_recycle.adapter = adapter

                            }
                        }
                    }

                    override fun onFailure(call: Call<Movie>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }.start()
    }
    inner class MoviesAdapter(val movieslist: List<Movie.ResultBean>) :
        RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val movie_name: TextView = view.findViewById(R.id.movie_name)
            val image: ImageView = view.findViewById(R.id.movie_image)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
            val holder = ViewHolder(view)
            holder.itemView.setOnClickListener {
                val movies = movieslist[holder.adapterPosition]
            }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val movie = movieslist[position]
            holder.movie_name.text=movie.movieName
            getimage.showImage(movie.pic_url,holder.image)
        }

        override fun getItemCount() = movieslist.size
    }
}