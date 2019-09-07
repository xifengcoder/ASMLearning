package com.yxf.asmplugin

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.gradle.api.Project

public class MyInjects {
    //初始化类池
    private final static ClassPool pool = ClassPool.getDefault();

    public static void inject(String path, Project project) {
        //参数path路径是：/Users/Han/Development/ASMLearning/app/build/intermediates/javac/debug/compileDebugJavaWithJavac/classes

        //将当前路径加入类池,不然找不到这个类
        pool.appendClassPath(path);

        //project.android.bootClasspath[0].toString()是/Users/Han/Library/Android/sdk/platforms/android-28/android.jar
        //project.android.bootClasspath 加入android.jar，不然找不到android相关的所有类
        pool.appendClassPath(project.android.bootClasspath[0].toString());

        //引入android.os.Bundle包，因为onCreate方法参数有Bundle
        pool.importPackage("android.os.Bundle");

        File dir = new File(path);
        if (dir.isDirectory()) {
            //遍历文件夹
            dir.eachFileRecurse { File file ->
                if (file.getName().equals("MainActivity.class")) {

                    //获取MainActivity.class
                    CtClass ctClass = pool.getCtClass("com.yxf.asm.MainActivity");
                    println("ctClass = " + ctClass)
                    //解冻
                    if (ctClass.isFrozen()) {
                        ctClass.defrost()
                    }

                    CtMethod ctMethod = ctClass.getDeclaredMethod("onCreate") //获取到OnCreate方法
                    println("方法名 = " + ctMethod)

                    String insetBeforeStr = """
        android.widget.Toast.makeText(this,"我是被插入的Toast代码~!!",android.widget.Toast.LENGTH_SHORT).show();
"""
                    //在方法开头插入代码
                    ctMethod.insertBefore(insetBeforeStr);
                    ctClass.writeFile(path)
                    ctClass.detach()
                }
            }
        }

    }
}