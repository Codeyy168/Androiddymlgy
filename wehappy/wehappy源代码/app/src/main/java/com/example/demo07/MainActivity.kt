package com.example.demo07
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    val response = StringBuilder()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
       but1.setOnClickListener{
            replaceFragment(NewsFragment())
        }
        but2.setOnClickListener{
           replaceFragment(MoviesFragment())
        }
        but3.setOnClickListener{
            replaceFragment(WeatherFragment())
        }
        but4.setOnClickListener{
           replaceFragment(MyFragment())
        }
        replaceFragment(NewsFragment())

    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager=supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.ly_content,fragment)
        transaction.commit()
    }


    }


















