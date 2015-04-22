package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.IClassTransformerModule;

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
		ClassNode classNode = ASMHelper.readClassFromBytes(bytes);
		
		if (transformedName.equals("net.minecraft.entity.item.EntityXPOrb"))
		{
			ModInfo.Log.info("Transforming class: " + transformedName);
			
			createSetInWeb(classNode);
			
			verifyMethodAdded(classNode, "func_70110_aj", "()V");
            
            return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}

	public void verifyMethodAdded(ClassNode classNode, String name, String desc)
	{
		MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, name, desc);
		if (methodNode != null)
		{
			ModInfo.Log.info("Successfully added method: " + methodNode.name + methodNode.desc + " in " + classNode.name);
		} else
			throw new RuntimeException("Could not create method: " + name + desc + " in " + classNode.name);
	}
	
	public void createSetInWeb(ClassNode classNode)
	{
		MethodVisitor methodVisitor = classNode.visitMethod(ACC_PUBLIC, "func_70110_aj", "()V", null, null);
		methodVisitor.visitCode();
		methodVisitor.visitInsn(RETURN);
		methodVisitor.visitMaxs(0, 1);
		methodVisitor.visitEnd();
	}
}