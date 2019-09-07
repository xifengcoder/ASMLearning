package com.yxf.asmplugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by xifeng.yang on 2019/8/12.
 */
public class RemoveNopClassAdapter extends ClassVisitor {

    public RemoveNopClassAdapter(ClassVisitor cv) {
        super(Opcodes.ASM4, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        MethodVisitor mv;
        mv = cv.visitMethod(access, name, desc, signature, exceptions);

//        //仅移除除了Constructors之外的methods中的NOPs.
//        if (mv != null && !name.equals("<init>")) {
//            mv = new RemoveNopAdapter(mv);
//        }

        MethodVisitor mv1, mv2;
        mv1 = cv.visitMethod(access, name, desc, signature, exceptions);
        mv2 = cv.visitMethod(access, "_" + name, desc, signature, exceptions);
        //return new MultiMethodAdapter(mv1, mv2);

        return mv;
    }
}
