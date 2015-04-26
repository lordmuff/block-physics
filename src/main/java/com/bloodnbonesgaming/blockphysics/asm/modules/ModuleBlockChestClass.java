package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;
import squeek.asmhelper.com.bloodnbonesgaming.lib.ObfHelper;

import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.insn.RedirectedMethodInsnNode;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

public class ModuleBlockChestClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
				"net.minecraft.block.BlockChest"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformBlockChestClass";
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

		if (transformedName.equals("net.minecraft.block.BlockChest"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);

			//"onBlockAdded", "(Lnet/minecraft/world/World;III)V"
			final MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "onBlockAdded" : "func_149726_b", "(Lnet/minecraft/world/World;III)V");
			if (methodNode != null)
			{
				this.transformOnBlockAdded(methodNode);
			} else {
				throw new RuntimeException("Could not find onBlockAdded method in " + transformedName);
			}


			return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}

	public void transformOnBlockAdded(final MethodNode method)
	{
		final InsnList toFind = new InsnList();
		toFind.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toFind.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toFind.add(new VarInsnNode(Opcodes.ILOAD, 2));
		toFind.add(new VarInsnNode(Opcodes.ILOAD, 3));
		toFind.add(new VarInsnNode(Opcodes.ILOAD, 4));
		toFind.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/block/BlockChest", "func_149954_e", "(Lnet/minecraft/world/World;III)V", false, this));

		final AbstractInsnNode start = ASMHelper.find(method.instructions, toFind);
		final AbstractInsnNode end = ASMHelper.move(start, 6);

		if (start == null || end == null) {
			throw new RuntimeException("Unexpected instruction pattern in BlockChest.onBlockAdded");
		}

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0) {}
}