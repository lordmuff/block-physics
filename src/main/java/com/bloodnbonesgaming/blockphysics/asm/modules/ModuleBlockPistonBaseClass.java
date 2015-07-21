package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;
import squeek.asmhelper.com.bloodnbonesgaming.lib.ObfHelper;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bloodnbonesgaming.lib.core.ASMAdditionRegistry;
import com.bloodnbonesgaming.lib.core.insn.RedirectedFieldInsnNode;
import com.bloodnbonesgaming.lib.core.insn.RedirectedMethodInsnNode;
import com.bloodnbonesgaming.lib.core.module.IClassTransformerModule;

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
	public byte[] transform(final String name, final String transformedName, final byte[] bytes)
	{
		final ClassNode classNode = ASMHelper.readClassFromBytes(bytes);

		if (transformedName.equals("net.minecraft.block.BlockPistonBase"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);

			//"onNeighborBlockChange", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "onNeighborBlockChange" : "func_149695_a", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;)V");
			if (methodNode != null)
			{
				this.transformOnNeighborBlockChange(methodNode);
			} else {
				throw new RuntimeException("Could not find onNeighborBlockChange method in " + transformedName);
			}

			//"updatePistonState", "(Lnet/minecraft/world/World;III)V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "updatePistonState" : "func_150078_e", "(Lnet/minecraft/world/World;III)V");
			if (methodNode != null)
			{
				this.transformUpdatePistonState(methodNode);
			} else {
				throw new RuntimeException("Could not find updatePistonState method in " + transformedName);
			}

			//"onBlockEventReceived", "(Lnet/minecraft/world/World;IIIII)Z"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "onBlockEventReceived" : "func_149696_a", "(Lnet/minecraft/world/World;IIIII)Z");
			if (methodNode != null)
			{
				this.transformOnBlockEventReceived(methodNode);
			} else {
				throw new RuntimeException("Could not find onBlockEventReceived method in " + transformedName);
			}

			return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}

	public void transformOnNeighborBlockChange(final MethodNode method)
	{
		//BlockPhysics.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, Block.blockRegistry.getNameForObject(this));
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in BlockPistonBase.onNeighborBlockChange");
		}

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 2));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/block/Block", !ObfHelper.isObfuscated() ? "blockRegistry" : "field_149771_c", "Lnet/minecraft/util/RegistryNamespaced;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/RegistryNamespaced", !ObfHelper.isObfuscated() ? "getNameForObject" : "func_148750_c", "(Ljava/lang/Object;)Ljava/lang/String;", false, this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "onNeighborBlockChange", "(Lnet/minecraft/world/World;IIILjava/lang/String;)V", false, this));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformUpdatePistonState(final MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		final AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		final AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (start == null || end == null) {
			throw new RuntimeException("Unexpected instruction pattern in BlockPistonBase.updatePistonState");
		}

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);

		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		//BlockPhysics.updatePistonState(p_150078_1_, p_150078_2_, p_150078_3_, p_150078_4_, this, this.isSticky);
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 2));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/block/BlockPistonBase", !ObfHelper.isObfuscated() ? "isSticky" : "field_150082_a", "Z", this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "updatePistonState", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/BlockPistonBase;Z)V", false, this));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformOnBlockEventReceived(final MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		final AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		final AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.IRETURN);

		if (start == null || end == null) {
			throw new RuntimeException("Unexpected instruction pattern in BlockPistonBase.onBlockEventReceived");
		}

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);

		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.IRETURN);

		//return BlockPhysics.onBlockPistonEventReceived(p_149696_1_, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_5_, p_149696_6_, this, this.isSticky);
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 2));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 5));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 6));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/block/BlockPistonBase", !ObfHelper.isObfuscated() ? "isSticky" : "field_150082_a", "Z", this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "onBlockPistonEventReceived", "(Lnet/minecraft/world/World;IIIIILnet/minecraft/block/BlockPistonBase;Z)Z", false, this));

		method.instructions.insertBefore(target, toInject);
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0) {}
}