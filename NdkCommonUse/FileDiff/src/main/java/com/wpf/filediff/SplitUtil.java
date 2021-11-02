package com.wpf.filediff;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Author: feipeng.wang
 * Time:   2021/11/2
 * Description : This is description.
 */
public class SplitUtil {
    /**
     * 文件分割方法
     *
     * @param srcFilePath 源文件Path
     * @param dstFilePath 分割文件的目标目录
     * @param count       分割个数
     */
    public static void splitFile(String srcFilePath, String dstFilePath, int count) {
        RandomAccessFile raf = null;
        try {
            //获取目标文件 预分配文件所占的空间 在磁盘中创建一个指定大小的文件   r 是只读
            raf = new RandomAccessFile(new File(srcFilePath), "r");
            long length = raf.length();//文件的总长度
            long maxSize = length / count;//文件切片后的长度
            long offSet = 0L;//初始化偏移量
            for (int i = 0; i < count - 1; i++) { //最后一片单独处理
                long begin = offSet;
                long end = (i + 1) * maxSize;
//                offSet = writeFile(file, begin, end, i);
                offSet = getWrite(srcFilePath, dstFilePath, i, begin, end);
            }
            if (length - offSet > 0) {
                getWrite(srcFilePath, dstFilePath, count - 1, offSet, length);
            }
        } catch (FileNotFoundException e) {
            Log.e("TAG", "没有找到文件 srcFilePath:" + srcFilePath);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("TAG", "IOException");
            e.printStackTrace();
        } finally {
            try {
                if (raf != null)
                    raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 指定文件每一份的边界，写入不同文件中
     *
     * @param srcFilePath 源文件
     * @param dstFilePath 目标目录
     * @param index       源文件的顺序标识
     * @param begin       开始指针的位置
     * @param end         结束指针的位置
     * @return long
     */
    public static long getWrite(String srcFilePath, String dstFilePath, int index, long begin,
                                long end) {
        File srcFile = new File(srcFilePath);
        long endPointer = 0L;
        try {
            //申明文件切割后的文件磁盘
            RandomAccessFile in = new RandomAccessFile(new File(srcFilePath), "r");
            //定义一个可读，可写的文件并且后缀名为.tmp的二进制文件
            RandomAccessFile out = new RandomAccessFile(new File(dstFilePath + srcFile.getName()
                    .split("\\.")[0]
                    + "_" + index + ".tmp"), "rw");

            //申明具体每一文件的字节数组
            byte[] b = new byte[1024];
            int n = 0;
            //从指定位置读取文件字节流
            in.seek(begin);
            //判断文件流读取的边界
            while (in.getFilePointer() <= end && (n = in.read(b)) != -1) {
                //从指定每一份文件的范围，写入不同的文件
                out.write(b, 0, n);
            }
            //定义当前读取文件的指针
            endPointer = in.getFilePointer();
            //关闭输入流
            in.close();
            //关闭输出流
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "getWrite Exception");
        }
        return endPointer;
    }

    /**
     * @param srcFile 分割文件目录
     * @param dstFile 目标合并文件绝对路径
     */
    public static void merge(String srcFile, String dstFile) {
        File file = new File(srcFile);
        if (file != null && file.exists() && file.listFiles().length > 0) {
            merge(dstFile, srcFile, file.listFiles().length);
        }
    }

    /**
     * 文件合并
     *
     * @param dstFile   指定合并文件
     * @param tempFile  分割前的目录
     * @param tempCount 文件个数
     */
    private static void merge(String dstFile, String tempFile, int tempCount) {
        RandomAccessFile raf = null;
        try {
            //申明随机读取文件RandomAccessFile
            raf = new RandomAccessFile(new File(dstFile), "rw");
            //开始合并文件，对应切片的二进制文件
            File splitFileDir = new File(tempFile);
            File[] files = splitFileDir.listFiles();
            for (int i = 0; i < tempCount; i++) {
                //读取切片文件
                RandomAccessFile reader = new RandomAccessFile(files[i], "r");
                byte[] b = new byte[1024];
                int n = 0;
                //先读后写
                while ((n = reader.read(b)) != -1) {//读
                    raf.write(b, 0, n);//写
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "merge Exception" + e.getMessage());
        } finally {
            try {
                if (raf != null)
                    raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
