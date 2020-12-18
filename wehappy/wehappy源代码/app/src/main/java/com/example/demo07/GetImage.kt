package com.example.demo07

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.StrictMode
import android.util.Log
import android.util.LruCache
import android.widget.ImageView
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


var getimage = GetImage()
class GetImage {
    private val lruCache: LruCache<String?, ByteArray>
    fun showImage(imageUrl: String?, imageView: ImageView?) {
        var bitmap: Bitmap? = null
        var connection: HttpURLConnection? = null
        var inputStream: InputStream? = null
        bitmap = getBitmapFromMemCache(imageUrl)
        if(bitmap == null) {
            try {
                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)
                val url = URL(imageUrl)
                val byteArray: ByteArray
                connection = url.openConnection() as HttpURLConnection
                connection.setConnectTimeout(5000)
                connection.setRequestMethod("GET")
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream()
                    byteArray = inputStream.readBytes()
                    bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
                    if(bitmap != null){
                        addBitmapToMemory(imageUrl, byteArray)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
                if (inputStream != null) {
                    try {
                        inputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            Log.i(ContentValues.TAG,"showImage: from url")
        } else {
            Log.i(ContentValues.TAG,"showImage: from cache")
        }
        imageView?.setImageBitmap(bitmap)
    }


    // 把Bitmap对象加入到缓存中
    fun addBitmapToMemory(key: String?, byteArray: ByteArray?) {
        lruCache.put(key, byteArray)
        Log.i("add", "hahah")
    }

    // 从缓存中得到Bitmap对象
    fun getBitmapFromMemCache(key: String?): Bitmap? {
        Log.i(ContentValues.TAG, "lrucache size: " + lruCache.size())
        Log.i(ContentValues.TAG, "maxx size: " + lruCache.maxSize())
        Log.i(ContentValues.TAG,key.toString())
        if(lruCache[key] == null) {
            return null
        }
        return BitmapFactory.decodeByteArray(lruCache[key],0,lruCache[key].size)
    }

    // 从缓存中删除指定的Bitmap
    fun removeBitmapFromMemory(key: String?) {
        lruCache.remove(key)
    }

    init {
        val maxMemory = Runtime.getRuntime().maxMemory()
        val cacheSize = (maxMemory / 4).toInt()
        lruCache = object : LruCache<String?, ByteArray>(cacheSize) {
            override fun sizeOf(key: String?, value: ByteArray): Int {
                return value.size
            }
        }
    }
}