package com.example.demo07

data class Weather(val reason: String,
                     val result: ResultBean,
                     val error_code: Int) {

    data class ResultBean(val city: String,
                          val realtime: RealtimeBean,
                          val future: List<FutureBean>) {

        data class RealtimeBean(val temperature: String,
                                val humidity: String,
                                val info: String,
                                val wid: String,
                                val direct: String,
                                val power: String,
                                val aqi: String)

        data class FutureBean(val date: String,
                              val temperature: String,
                              val weather: String,
                              val wid: WidBean,
                              val direct: String) {

            data class WidBean(val day: String,
                               val night: String)
        }
    }
}