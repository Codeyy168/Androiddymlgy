package com.example.demo07

data class News(val reason: String,
                val result: ResultBean,
                val error_code: Int) {

    data class ResultBean(val stat: String,
                          val data: List<DataBean>) {

        data class DataBean(val uniquekey: String,
                            val title: String,
                            val date: String,
                            val category: String,
                            val author_name: String,
                            val url: String,
                            val thumbnail_pic_s: String)
    }
}