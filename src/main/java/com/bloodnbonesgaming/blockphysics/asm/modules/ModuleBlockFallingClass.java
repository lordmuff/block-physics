package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.insn.RedirectedFieldInsnNode;
import com.bnbgaming.lib.core.insn.RedirectedMethodInsnNode;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleBlockFallingClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.block.BlockFalling"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformBlockFallingClass";
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
		
		if (transformedName.equals("net.minecraft.block.BlockFalling"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);
			
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
            
            //"func_149830_m", "(Lnet/minecraft/world/World;III)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149830_m", "(Lnet/minecraft/world/World;III)V");
            if (methodNode != null)
            {
            	transformFunc_149830_m(methodNode);
            }
            else
            	throw new RuntimeException("Could not find func_149830_m method in " + transformedName);
            
            //"func_149831_e", "(Lnet/minecraft/world/World;III)Z"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149831_e", "(Lnet/minecraft/world/World;III)Z");
            if (methodNode != null)
            {
            	transformFunc_149831_e(methodNode);
            }
            else
            	throw new RuntimeException("Could not find func_149831_e method in " + transformedName);
            
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
			throw new RuntimeException("Unexpected instruction pattern in BlockFalling.onNeighborBlockChange");
		
		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
		
		//BlockPhysics.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, Block.blockRegistry.getNameForObject(this));
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockFalling.onNeighborBlockChange");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new VarInsnNode(ILOAD, 2));
		toInject.add(new VarInsnNode(ILOAD, 3));
		toInject.add(new VarInsnNode(ILOAD, 4));
		toInject.add(new RedirectedFieldInsnNode(GETSTATIC, "net/minecraft/block/Block", "field_149771_c", "Lnet/minecraft/util/RegistryNamespaced;", this));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/util/RegistryNamespaced", "func_148750_c", "(Ljava/lang/Object;)Ljava/lang/String;", false, this));
		toInject.add(new RedirectedMethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "onNeighborBlockChange", "(Lnet/minecraft/world/World;IIILjava/lang/String;)V", false, this));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformUpdateTick(MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (start == null || end == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockFalling.updateTick");

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
	}
	
	public void transformFunc_149830_m(MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, RETURN);

		if (start == null || end == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockFalling.func_149830_m");

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
	}
	
	public void transformFunc_149831_e(MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, IRETURN);

		if (start == null || end == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockFalling.func_149831_e");

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
		
		//return BlockPhysics.canMoveTo(p_149831_0_, p_149831_1_, p_149831_2_, p_149831_3_, 0);
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, IRETURN);
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new VarInsnNode(ILOAD, 1));
		toInject.add(new VarInsnNode(ILOAD, 2));
		toInject.add(new VarInsnNode(ILOAD, 3));
		toInject.add(new InsnNode(ICONST_0));
		toInject.add(new RedirectedMethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "canMoveTo", "(Lnet/minecraft/world/World;IIII)Z", false, this));
		
		method.instructions.insertBefore(target, toInject);
	}

	@Override
	public void registerAdditions(ASMAdditionRegistry arg0) {}
}