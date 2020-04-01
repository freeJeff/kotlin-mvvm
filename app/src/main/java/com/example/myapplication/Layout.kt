package com.example.myapplication

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun LifecycleOwner.textView(controller: TextViewController.() -> Unit): TextView {
    val textView = TextView(this as Context)
    val textViewController = TextViewController(this, textView)
    controller.invoke(textViewController)
    return textView
}


class TextViewController(val lifecycleOwner: LifecycleOwner, val textView: TextView) {

    inner class Property<T>(var set: (value: T?) -> Unit)

    var lp = textView.layoutParams

    init {
        lp = lp?:ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textView.layoutParams = lp
    }

    var textProp: String? = null
        set(value) {
            textView.text = value ?: ""
            field = value
        }

    @IdRes
    var backgroundResProp: Int = 0
        set(value) {
            textView.setBackgroundResource(value)
            field = value
        }

    var widthProp: Int = ViewGroup.LayoutParams.MATCH_PARENT
        set(value) {
            lp.width = value
            field = value
        }



    var text = Property<String?> { textProp = it }
    var backgroundRes = Property<Int> { backgroundResProp = it ?: 0 }
    var width = Property<Int> { }

    @Suppress("UNCHECKED_CAST")
    fun <T, M, V : LiveData<T?>> Property<M>.bind(data: V, binder: ((value: T?) -> M?)? = null) {
        data.observe(lifecycleOwner, Observer {
            if (binder == null) {
                try {
                    this.set.invoke(data.value as M?)
                } catch (e: Exception) {
                    throw Exception("Type of LiveData's value is not capable with view property Type")
                }
            } else {
                this.set.invoke(binder.invoke(data.value))
            }
        })
    }
}

