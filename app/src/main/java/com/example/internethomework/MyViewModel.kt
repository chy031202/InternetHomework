package com.example.internethomework

import android.content.Context
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class Item(val adress: String)

class MyViewModel : ViewModel() {
    val itemListData = MutableLiveData<ArrayList<Item>>()
    val items = ArrayList<Item>()

    val itemClickEvent = MutableLiveData<Int>()

    fun addItem(item: Item){
        items.add(item)
        itemListData.value = items
    }


    fun refreshRetrofit(userName: String) {
        val baseURL = "https://api.github.com/"
        val api: RestApi = with(Retrofit.Builder()) {
            baseUrl(baseURL)
            addConverterFactory(GsonConverterFactory.create())
            build()
        }.create(RestApi::class.java)

        viewModelScope.launch{
            try {
                val repos = api.listReposCall(userName)
                repos.enqueue(object : Callback<List<Repo>> {
                    override fun onResponse(p0: Call<List<Repo>>, p1: Response<List<Repo>>) {
                        if (p1.isSuccessful) {
                            val repos = p1.body()
                            val items = ArrayList<Item>()
                            val str = StringBuilder().apply {
                                repos?.forEach{
                                    append(it.name)
                                    addItem(Item(it.name))
                                    /*append(" - ")
                                    append(it.owner.login)*/
                                    //append("\n")
                                }
                            }.toString()


                            println(str)


                        }
                    }

                    override fun onFailure(p0: Call<List<Repo>>, p1: Throwable) {
                        "데이터 가져오기 실패"
                    }

                })
            } catch (e: Exception) {
                /*responseBy.value = "retrofit : Error"
                response.value = "Failed to connect to the server ${e.message}"*/
            }
        }


    }

}


