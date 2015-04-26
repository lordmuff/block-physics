package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;
import squeek.asmhelper.com.bloodnbonesgaming.lib.ObfHelper;

import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

public class ModuleEntityBoatClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
				"net.minecraft.entity.item.EntityBoat"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformEntityBoatClass";
	}

	@Override
	public boolean canBeDisabled()
	{
		return false;
	}

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] bytes)
	{
		return bytes;
	}

	public void createSetInWeb(final ClassNode classNode)
	{
		final MethodVisitor methodVisitor = classNode.visitMethod(Opcodes.ACC_PUBLIC, !ObfHelper.isObfuscated() ? "setInWeb" : "func_70110_aj", "()V", null, null);
		methodVisitor.visitCode();
		methodVisitor.visitInsn(Opcodes.RETURN);
		methodVisitor.visitMaxs(0, 1);
		methodVisitor.visitEnd();
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry registry) {
		final ClassNode classNode = new ClassNode();

		this.createSetInWeb(classNode);

		//registry.registerMethodAddition("net/minecraft/entity/item/EntityBoat", ASMHelper.findMethodNodeOfClass(classNode, "func_70110_aj", "()V"));
	}
}