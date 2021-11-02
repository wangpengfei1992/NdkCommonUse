package com.wpf.filediff

object NdkSplit {
    init {
        System.loadLibrary("hello_c_test")
    }
    /*
    * 文件切割
    * */
    external fun splitFile(srcFilePath:String,desFilePath:String,number:Int)
    external fun mergeFile(srcFilePath:String,desFilePath:String,number:Int)
}