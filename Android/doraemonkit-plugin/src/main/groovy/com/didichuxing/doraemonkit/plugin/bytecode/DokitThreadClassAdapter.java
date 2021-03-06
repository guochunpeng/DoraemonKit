package com.didichuxing.doraemonkit.plugin.bytecode;

import com.didichuxing.doraemonkit.plugin.DokitExtUtil;
import com.didichuxing.doraemonkit.plugin.bytecode.method.comm.ThreadLogAdapter;
import com.didichuxing.doraemonkit.plugin.bytecode.method.thread.ThreadMethodAdapter;
import com.didichuxing.doraemonkit.plugin.bytecode.method.urlconnection.UrlConnectionMethodAdapter;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
/**
 * ================================================
 * 作    者：maple
 * 版    本：1.0
 * 创建日期：2020-04-28
 * 描    述：类访问器
 * 修订历史：
 * ================================================
 */
public class DokitThreadClassAdapter extends ClassVisitor {
    /**
     * 当前类型
     */
    private String className;

    /**
     * @param cv cv 传进来的是 ClassWriter
     */
    public DokitThreadClassAdapter(final ClassVisitor cv) {
        super(Opcodes.ASM7, cv);

    }

    @Override
    public void visit(int version, int access, String className, String signature, String superName, String[] interfaces) {
        super.visit(version, access, className, signature, superName, interfaces);
        this.className = className;
    }


    /**
     * Visits a method of the class. This method <i>must</i> return a new {@link MethodVisitor}
     * instance (or {@literal null}) each time it is called, i.e., it should not return a previously
     * returned visitor.
     *
     * @param access     the method's access flags (see {@link Opcodes}). This parameter also indicates if
     *                   the method is synthetic and/or deprecated.
     * @param methodName the method's name.
     * @param desc       the method's descriptor (see {@link Type}).
     * @param signature  the method's signature. May be {@literal null} if the method parameters,
     *                   return type and exceptions do not use generic types.
     * @param exceptions the internal names of the method's exception classes (see {@link
     *                   Type#getInternalName()}). May be {@literal null}.
     * @return an object to visit the byte code of the method, or {@literal null} if this class
     * visitor is not interested in visiting the code of this method.
     */
    @Override
    public MethodVisitor visitMethod(int access, String methodName, String desc, String signature, String[] exceptions) {
        //从传进来的ClassWriter中读取MethodVisitor
        MethodVisitor mv = cv.visitMethod(access, methodName, desc, signature, exceptions);
        //开关被关闭 不插入代码
        if (!DokitExtUtil.getInstance().isDokitPluginSwitch()) {
            return mv;
        }
        //过滤所有类中当前方法中所有的字节码
        return mv == null ? null : new ThreadMethodAdapter(className, methodName, access, desc, mv);

    }
}
