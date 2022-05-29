package com.example.searchImages.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.hardware.display.DisplayManagerCompat
import com.example.searchImages.App
import com.example.searchImages.R
import com.gyf.immersionbar.ImmersionBar

open class BaseActivity : AppCompatActivity() {

    lateinit var cxt: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBarColor(R.color.purple_700)
        cxt = this
        setBarColor(android.R.color.holo_blue_dark)
        screenValue()
    }

    private fun setBarColor(@ColorRes barColor: Int) {
        ImmersionBar.with(this)
            .reset()
            .fitsSystemWindows(true)
            .barColor(barColor)
            .autoDarkModeEnable(true)
            .init()
    }

    private fun screenValue() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val defaultDisplay =
                DisplayManagerCompat.getInstance(this).getDisplay(Display.DEFAULT_DISPLAY)
            val displayContext = createDisplayContext(defaultDisplay!!)

            App.screenWidth = displayContext.resources.displayMetrics.widthPixels
            App.screenHeight = displayContext.resources.displayMetrics.heightPixels

        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(displayMetrics)

            App.screenHeight = displayMetrics.heightPixels
            App.screenWidth = displayMetrics.widthPixels
        }
    }
}