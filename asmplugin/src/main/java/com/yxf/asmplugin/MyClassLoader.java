package com.yxf.asmplugin;

import org.objectweb.asm.ClassWriter;

/**
 * Created by xifeng.yang on 2019/8/13.
 */
public class MyClassLoader extends ClassLoader {

    public Class defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        if (name.endsWith("_Stub")) {
            ClassWriter cw = new ClassWriter(0);
            //...
            byte[] b = cw.toByteArray();
            return defineClass(name, b, 0, b.length);
        }

        return super.findClass(name);
    }
}
