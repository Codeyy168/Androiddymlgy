package com.example.demo07

data class Movie(val reason: String,
                     val result: List<ResultBean>,
                     val error_code: Int) {

    data class ResultBean(val movieId: String,
                          val movieName: String,
                          val pic_url: String)
}