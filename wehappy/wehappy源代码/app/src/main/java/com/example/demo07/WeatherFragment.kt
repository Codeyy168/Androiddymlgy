package com.example.demo07

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.weather_frag.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.URLEncoder

class WeatherFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weather_frag, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        search.setOnClickListener {
            println("right")
            getweather()
        }
  //      getweather("%E8%8B%8F%E5%B7%9E")
    }

    interface Getweather {
        @GET("query?")
        fun getMessageByGet(@Query("city") city: String, @Query("key") key: String): Call<Weather>
    }

    fun getweather() {
        Thread {
            val city =  input.getText().toString()
            println(city)
            val retrofit = Retrofit.Builder().baseUrl("https://apis.juhe.cn/simpleWeather/")
                .addConverterFactory(
                    GsonConverterFactory.create()
                ).build()
            val Weather = retrofit.create(Getweather::class.java)
            Weather.getMessageByGet(city, "9a05d63d1ab98477072306a653741afd")
                .enqueue(object : Callback<Weather> {
                    override fun onResponse(
                        call: Call<Weather>,
                        response: Response<Weather>
                    ) {
                        val msg = response.body()
                        println(msg)
                        if (msg != null) {
                            val future_List = msg.result.future
                            val current=msg.result.realtime
                            temp.text=current.temperature
                            local.text=city
                            getActivity()?.runOnUiThread {
                                val layoutManager = LinearLayoutManager(activity)
                                future.layoutManager = layoutManager
                                val adapter = WeatherAdapter(future_List)
                                future.adapter = adapter

                            }
                        }
                    }

                    override fun onFailure(call: Call<Weather>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }.start()
    }
    inner class WeatherAdapter(val future_List: List<Weather.ResultBean.FutureBean>) :
        RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val date: TextView = view.findViewById(R.id.wether_date)
            val temperature: TextView = view.findViewById(R.id.temperature)
            val weather: TextView = view.findViewById(R.id.weather)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.future_item,
                parent,
                false
            )
            val holder = ViewHolder(view)
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val weather = future_List[position]
            holder.date.text=weather.date
            holder.temperature.text=weather.temperature
            holder.weather.text=weather.weather
        }

        override fun getItemCount() = future_List.size
    }
}