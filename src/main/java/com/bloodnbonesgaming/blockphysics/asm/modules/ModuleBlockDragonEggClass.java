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

public class ModuleBlockDragonEggClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.block.BlockDragonEgg"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformBlockDragonEggClass";
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
		
		if (transformedName.equals("net.minecraft.block.BlockDragonEgg"))
		{
			ModInfo.Log.info("Transforming class: " + transformedName);
			
			//"onNeighborBlockChange", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149695_a", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;)V");
            if (methodNode != null)
            {
            	transformOnNeighborBlockChange(methodNode);
            }
            else
                throw new RuntimeException("Could not find onNeighborBlockChange method in " + transformedName);
            //"updateTick", "(Lnet/minecraft/world/World;IIILjava/util/Random;)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149674_a", "(Lnet/minecraft/world/World;IIILjava/util/Random;)V");
            if (methodNode != null)
            {
            	transformUpdateTick(methodNode);
            }
            else
            	throw new RuntimeException("Could not find updateTick method in " + transformedName);
            //"func_150018_e", "(Lnet/minecraft/world/World;III)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_150018_e", "(Lnet/minecraft/world/World;III)V");
            if (methodNode != null)
            {
            	transformFunc_150018_e(methodNode);
            }
            else
            	throw new RuntimeException("Could not find func_150018_e method in " + transformedName);
            //"func_150019_m", "(Lnet/minecraft/world/World;III)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_150019_m", "(Lnet/minecraft/world/World;III)V");
            if (methodNode != null)
            {
            	transformFunc_150019_m(methodNode);
            }
            else
            	throw new RuntimeException("Could not find func_150019_m method in " + transformedName);
            
            return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}
	
	public void transformOnNeighborBlockChange(MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (start == null || end == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockDragonEgg.onNeighborBlockChange");
		
		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
		
		//BlockPhysics.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, Block.blockRegistry.getNameForObject(this));
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockDragonEgg.onNeighborBlockChange");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new VarInsnNode(ILOAD, 2));
		toInject.add(new VarInsnNode(ILOAD, 3));
		toInject.add(new VarInsnNode(ILOAD, 4));
		toInject.add(new FieldInsnNode(GETSTATIC, "net/minecraft/block/Block", "field_149771_c", "Lnet/minecraft/util/RegistryNamespaced;"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/util/RegistryNamespaced", "func_148750_c", "(Ljava/lang/Object;)Ljava/lang/String;", false));
		toInject.add(new MethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "onNeighborBlockChange", "(Lnet/minecraft/world/World;IIILjava/lang/String;)V", false));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformUpdateTick(MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (start == null || end == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockDragonEgg.updateTick");

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
	}
	
	public void transformFunc_150018_e(MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, RETURN);

		if (start == null || end == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockDragonEgg.func_150018_e");

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
	}
	
	public void transformFunc_150019_m(MethodNode method)
	{
		//p_150019_1_.setBlockToAir(p_150019_2_, p_150019_3_, p_150019_4_);
		AbstractInsnNode target = ASMHelper.find(method.instructions, new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/World", "func_147468_f", "(III)Z", false));
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockDragonEgg.func_150019_m");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new VarInsnNode(ILOAD, 6));
		toInject.add(new VarInsnNode(ILOAD, 8));
		toInject.add(new VarInsnNode(ILOAD, 7));
		toInject.add(new MethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "notifyMove", "(Lnet/minecraft/world/World;III)V", false));
		
		method.instructions.insert(target, toInject);
	}
}