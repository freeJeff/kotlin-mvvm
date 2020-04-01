package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this)[MyViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(contentView())
        viewModel.loadText()
    }

    private fun contentView() =
        textView {
            text.bind(viewModel.textLiveData)
            backgroundRes.bind(viewModel.textLiveData) {
                if (it == "Red") R.color.colorAccent else 0
            }
        }

}


class MyViewModel : ViewModel() {

    val textLiveData = MutableLiveData<String?>()


    fun loadText() {
        Handler().postDelayed(Runnable {
            textLiveData.value = "Red"
        }, 2000);
    }

    override fun onCleared() {
        super.onCleared()
    }
}
