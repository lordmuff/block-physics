package com.bloodnbonesgaming.blockphysics.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class RemoveMethodAdapter extends ClassVisitor
{
	private final String mName;
	private final String mDesc;

	public RemoveMethodAdapter(final int api, final ClassVisitor cv, final String mName, final String mDesc)
	{
		super(api, cv);
		this.mName = mName;
		this.mDesc = mDesc;
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions)
	{
		ASMPlugin.log.info("visitMethod Called for method: " + name + " " + desc);
		if (name.equals(this.mName) && desc.equals(this.mDesc))
		{
			ASMPlugin.log.info("Returning null for method: " + name + " " + desc);
			// do not delegate to next visitor -> this removes the method
			return null;
		}
		return this.cv.visitMethod(access, name, desc, signature, exceptions);
	}
}
