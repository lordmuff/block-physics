package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
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

public class ModuleBlockPistonBaseClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.block.BlockPistonBase"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformBlockPistonBaseClass";
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
		
		if (transformedName.equals("net.minecraft.block.BlockPistonBase"))
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
            
            //"updatePistonState", "(Lnet/minecraft/world/World;III)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_150078_e", "(Lnet/minecraft/world/World;III)V");
            if (methodNode != null)
            {
            	transformUpdatePistonState(methodNode);
            }
            else
            	throw new RuntimeException("Could not find updatePistonState method in " + transformedName);
            
            //"onBlockEventReceived", "(Lnet/minecraft/world/World;IIIII)Z"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149696_a", "(Lnet/minecraft/world/World;IIIII)Z");
            if (methodNode != null)
            {
            	transformOnBlockEventReceived(methodNode);
            }
            else
            	throw new RuntimeException("Could not find onBlockEventReceived method in " + transformedName);
            
            return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}
	
	public void transformOnNeighborBlockChange(MethodNode method)
	{
		//BlockPhysics.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, Block.blockRegistry.getNameForObject(this));
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockPistonBase.onNeighborBlockChange");
		
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
	
	public void transformUpdatePistonState(MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, RETURN);

		if (start == null || end == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockPistonBase.updatePistonState");

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
		
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		//BlockPhysics.updatePistonState(p_150078_1_, p_150078_2_, p_150078_3_, p_150078_4_, this, this.isSticky);
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new VarInsnNode(ILOAD, 2));
		toInject.add(new VarInsnNode(ILOAD, 3));
		toInject.add(new VarInsnNode(ILOAD, 4));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/block/BlockPistonBase", "field_150082_a", "Z", this));
		toInject.add(new RedirectedMethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "updatePistonState", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/BlockPistonBase;Z)V", false, this));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformOnBlockEventReceived(MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, IRETURN);

		if (start == null || end == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockPistonBase.onBlockEventReceived");

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
		
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, IRETURN);
		
		//return BlockPhysics.onBlockPistonEventReceived(p_149696_1_, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_5_, p_149696_6_, this, this.isSticky);
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new VarInsnNode(ILOAD, 2));
		toInject.add(new VarInsnNode(ILOAD, 3));
		toInject.add(new VarInsnNode(ILOAD, 4));
		toInject.add(new VarInsnNode(ILOAD, 5));
		toInject.add(new VarInsnNode(ILOAD, 6));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/block/BlockPistonBase", "field_150082_a", "Z", this));
		toInject.add(new RedirectedMethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "onBlockPistonEventReceived", "(Lnet/minecraft/world/World;IIIIILnet/minecraft/block/BlockPistonBase;Z)Z", false, this));
		
		method.instructions.insertBefore(target, toInject);
	}

	@Override
	public void registerAdditions(ASMAdditionRegistry arg0) {}
}