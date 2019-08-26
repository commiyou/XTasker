package com.example.myapplication

import android.content.Context
import android.view.View
import android.os.Build
import android.content.pm.PackageManager
import android.annotation.TargetApi
import android.content.Intent
import android.os.Handler
import android.provider.Settings
import android.os.Bundle
import android.content.SharedPreferences
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import android.app.ActivityOptions
import android.hardware.display.DisplayManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemService




class DisplayInfo internal constructor(var width: Int, var height: Int, internal var density: Int)



class Util {
    // From android.app.ActivityManager.StackId
    private val FULLSCREEN_WORKSPACE_STACK_ID = 1
    private val FREEFORM_WORKSPACE_STACK_ID = 2

    // From android.app.WindowConfiguration
    private val WINDOWING_MODE_FULLSCREEN = 1
    private val WINDOWING_MODE_FREEFORM = 5

    fun canEnableFreeform(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun hasFreeformSupport(context: Context): Boolean {
        return canEnableFreeform() && (context.packageManager.hasSystemFeature(PackageManager.FEATURE_FREEFORM_WINDOW_MANAGEMENT)
                || Settings.Global.getInt(
            context.contentResolver,
            "enable_freeform_support",
            0
        ) !== 0
                || Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1 && Settings.Global.getInt(
            context.contentResolver,
            "force_resizable_activities",
            0
        ) !== 0)
    }

    fun launchApp(
        context: Context,
        intent: Intent,
        view: View
    ) {
        launchApp(context, Runnable { continueLaunchingApp(context, intent, view) })


    }

    fun launchApp(context: Context, runnable: Runnable) {
        Handler().postDelayed(runnable, 0);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun continueLaunchingApp(
        context: Context,
        intent: Intent,
        view: View
    ) {
        //intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        //intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        val bundle = getActivityOptionsBundle(context, view)
        context.startActivity(intent, bundle)
    }


    fun getActivityOptionsBundle(context: Context, view: View): Bundle {
        val dm = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val display = dm.getDisplay(Display.DEFAULT_DISPLAY)

        val width1 = display.width / 8
        val width2 = display.width - width1
        val height1 = display.height / 8
        val height2 = display.height - height1
        return getActivityOptions(context, view).setLaunchBounds(
            Rect(
                width1,
                height1,
                width2,
                height2
            )
        ).toBundle()
    }


    @TargetApi(Build.VERSION_CODES.N)
    private fun getActivityOptions(
        context: Context,
        view: View?
    ): ActivityOptions {
        val options: ActivityOptions
        if (view != null)
            options = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.width, view.height)
        else
            options = ActivityOptions.makeBasic()


        var stackId = getFreeformWindowModeId()


        try {
            val method = ActivityOptions::class.java.getMethod(
                getWindowingModeMethodName(),
                Int::class.javaPrimitiveType
            )
            method.invoke(options, stackId)
        } catch (e: Exception) { /* Gracefully fail */
        }

        return options
    }
    private fun getFullscreenWindowModeId(): Int {
        return if (getCurrentApiVersion() >= 28.0f)
            WINDOWING_MODE_FULLSCREEN
        else
            FULLSCREEN_WORKSPACE_STACK_ID
    }

    private fun getFreeformWindowModeId(): Int {
        return if (getCurrentApiVersion() >= 28.0f)
            WINDOWING_MODE_FREEFORM
        else
            FREEFORM_WORKSPACE_STACK_ID
    }

    private fun getWindowingModeMethodName(): String {
        return if (getCurrentApiVersion() >= 28.0f)
            "setLaunchWindowingMode"
        else
            "setLaunchStackId"
    }
    private fun getCurrentApiVersion(): Float {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            java.lang.Float.valueOf(Build.VERSION.SDK_INT.toString() + "." + Build.VERSION.PREVIEW_SDK_INT)
        else
            Build.VERSION.SDK_INT.toFloat()
    }
}