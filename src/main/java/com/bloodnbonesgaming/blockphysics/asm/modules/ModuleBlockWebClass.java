package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleBlockWebClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.block.BlockWeb"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformBlockWebClass";
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
		
		if (transformedName.equals("net.minecraft.block.BlockWeb"))
		{
			ModInfo.Log.info("Transforming class: " + transformedName);
			
			//"onEntityCollidedWithBlock", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V"
            MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149670_a", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V");
            if (methodNode != null)
            {
            	trasnformOnEntityCollidedWithBlock(methodNode);
            }
            else
            	throw new RuntimeException("Could not find onEntityCollidedWithBlock method in " + transformedName);
            
            return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}
	
	public void trasnformOnEntityCollidedWithBlock(MethodNode method)
	{
		//BlockPhysics.onEntityCollidedWithBlock(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_, Block.blockRegistry.getNameForObject(this), p_149670_5_);
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockWeb.onEntityCollidedWithBlock");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new VarInsnNode(ILOAD, 2));
		toInject.add(new VarInsnNode(ILOAD, 3));
		toInject.add(new VarInsnNode(ILOAD, 4));
		toInject.add(new FieldInsnNode(GETSTATIC, "net/minecraft/block/Block", "field_149771_c", "Lnet/minecraft/util/RegistryNamespaced;"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/util/RegistryNamespaced", "func_148750_c", "(Ljava/lang/Object;)Ljava/lang/String;", false));
		toInject.add(new VarInsnNode(ALOAD, 5));
		toInject.add(new MethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "onEntityCollidedWithBlock", "(Lnet/minecraft/world/World;IIILjava/lang/String;Lnet/minecraft/entity/Entity;)V", false));
		
		method.instructions.insertBefore(target, toInject);
	}
}