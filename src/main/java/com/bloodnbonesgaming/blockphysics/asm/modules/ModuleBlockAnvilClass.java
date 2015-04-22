package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleBlockAnvilClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.block.BlockAnvil"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformBlockAnvilClass";
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
		
		if (transformedName.equals("net.minecraft.block.BlockAnvil"))
		{
			ModInfo.Log.info("Transforming class: " + transformedName);
			
			//"func_149829_a", "(Lnet/minecraft/entity/item/EntityFallingBlock;)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149829_a", "(Lnet/minecraft/entity/item/EntityFallingBlock;)V");
            if (methodNode != null)
            {
            	transformFunc_149829_a(methodNode);
            }
            else
                throw new RuntimeException("Could not find func_149829_a method in " + transformedName);
            //"func_149828_a", "(Lnet/minecraft/world/World;IIII)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149828_a", "(Lnet/minecraft/world/World;IIII)V");
            if (methodNode != null)
            {
            	transformFunc_149828_a(methodNode);
            }
            else
            	throw new RuntimeException("Could not find func_149828_a method in " + transformedName);
            
            return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}
	
	public void transformFunc_149829_a(MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (start == null || end == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockAnvil.func_149829_a");
		
		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
	}
	
	public void transformFunc_149828_a(MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (start == null || end == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockAnvil.func_149828_a");

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
	}
}