package com.wpf.ndkcommonuse

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wpf.filediff.NdkSplit
import com.wpf.filediff.SplitUtil
import kotlin.concurrent.thread


/*
*
* 记录Ndk常用的一些例子
* */
class MainActivity : AppCompatActivity() {
    private val SD_DIR = "/storage/emulated/0/wpf"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun split(View: View?) {
        thread {
            Log.e("wpf","start")
            val srcFile: String = SD_DIR.toString() + "/" + "123456789.zip"
            val dstFile: String = SD_DIR.toString() + "/split/" + "a_%d"
//            NdkSplit.splitFile(srcFile, dstFile, 4)  //14s
            SplitUtil.splitFile(srcFile,dstFile,4)  //6s
            Log.e("wpf","end")
        }
    }

    fun merge(view: View?) {
        val srcFile: String = SD_DIR.toString() + "/" + "a.mp4"
        val dstFile: String = SD_DIR.toString() + "/" + "a_%d.mp4"
        NdkSplit.mergeFile(srcFile, dstFile, 4)
    }
}