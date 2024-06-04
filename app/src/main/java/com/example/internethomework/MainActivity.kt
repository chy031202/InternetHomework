package com.example.internethomework

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


data class Owner(val login: String)
data class Repo(val name: String, val owner: Owner, val url: String)


interface RestApi {
    @GET("users/{user}/repos")
    fun listReposCall(@Path("user") user: String): Call<List<Repo>>
}

interface RestApi2 {
    @GET("images/branding/googlelogo/2x/googlelogo_color_272x92dp.png")
    suspend fun getImage() : ResponseBody
}

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MyViewModel>()
    //private val viewModel by viewModels<MyViewModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = MyAdapter(viewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        viewModel.itemListData.observe(this) {
            adapter.notifyDataSetChanged()
        }


        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork
        val actNw = connectivityManager.getNetworkCapabilities(nw)

        val isConnected =  actNw?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)?: false

        if(!isConnected) {
            Snackbar.make(findViewById(R.id.main), "인터넷 연결 필요",Snackbar.LENGTH_SHORT).show()

        }



        findViewById<Button>(R.id.buttonQuery).setOnClickListener{
            val name = findViewById<EditText>(R.id.editUsername).text
            viewModel.refreshRetrofit(name.toString())
            println("버튼 눌림")
        }
    }


    /*private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        println("${actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)}, ${actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)}, " +
                "${actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)}")
        return actNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

    }
*/
}