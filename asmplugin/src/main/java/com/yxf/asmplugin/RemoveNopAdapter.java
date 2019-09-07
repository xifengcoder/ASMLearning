package com.yxf.asmplugin;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


/**
 * Created by xifeng.yang on 2019/8/11.
 */
public class RemoveNopAdapter extends MethodVisitor {

    public RemoveNopAdapter(MethodVisitor mv) {
        super(Opcodes.ASM4, mv);
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode != Opcodes.NOP) {
            mv.visitInsn(opcode);
        }
    }
}
