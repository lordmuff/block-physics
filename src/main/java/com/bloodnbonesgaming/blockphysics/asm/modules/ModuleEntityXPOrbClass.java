package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleEntityXPOrbClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.entity.item.EntityXPOrb"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformEntityXPOrbClass";
	}

	@Override
	public boolean canBeDisabled()
	{
		return false;
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		return bytes;
	}
	
	public void createSetInWeb(ClassNode classNode)
	{
		MethodVisitor methodVisitor = classNode.visitMethod(ACC_PUBLIC, "func_70110_aj", "()V", null, null);
		methodVisitor.visitCode();
		methodVisitor.visitInsn(RETURN);
		methodVisitor.visitMaxs(0, 1);
		methodVisitor.visitEnd();
	}

	@Override
	public void registerAdditions(ASMAdditionRegistry registry) {
		ClassNode classNode = new ClassNode();
		
		createSetInWeb(classNode);
		
		registry.registerMethodAddition("net.minecraft.entity.item.EntityXPOrb", ASMHelper.findMethodNodeOfClass(classNode, "func_70110_aj", "()V"));
	}
}