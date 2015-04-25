package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.insn.RedirectedMethodInsnNode;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

public class ModuleAnvilChunkLoaderClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
				"net.minecraft.world.chunk.storage.AnvilChunkLoader"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformAnvilChunkLoaderClass";
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

		if (transformedName.equals("net.minecraft.world.chunk.storage.AnvilChunkLoader"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);
			//writeChunkToNBT
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_75820_a", "(Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)V");
			if (methodNode != null)
			{
				this.transformWriteChunkToNBT(methodNode);
			} else {
				throw new RuntimeException("Could not find writeChunkToNBT method in " + transformedName);
			}

			methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_75823_a", "(Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/world/chunk/Chunk;");
			if (methodNode != null)
			{
				this.transformReadChunkFromNBT(methodNode);
			} else {
				throw new RuntimeException("Could not find readChunkFromNBT method in " + transformedName);
			}

			return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}

	public void transformWriteChunkToNBT(final MethodNode method)
	{
		//nbttaglist.appendTag(nbttagcompound1);
		//InsnList toFind = new InsnList();
		//toFind.add(new VarInsnNode(ALOAD, 5));
		//toFind.add(new VarInsnNode(ALOAD, 9));
		final AbstractInsnNode appendTag = ASMHelper.find(method.instructions, new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagList", "func_74742_a", "(Lnet/minecraft/nbt/NBTBase;)V", false, this));
		final VarInsnNode nbtTagCompound = (VarInsnNode) ASMHelper.move(appendTag, -1);
		final AbstractInsnNode target = ASMHelper.move(appendTag, -2);

		final VarInsnNode extendedBlockStorage = (VarInsnNode) ASMHelper.move(ASMHelper.find(method.instructions, new InsnNode(Opcodes.AALOAD)), 1);

		if (appendTag == null || nbtTagCompound == null || target == null) {
			throw new RuntimeException("Unexpected instruction pattern in AnvilChunkLoader.writeChunkToNBT");
		}

		//nbttagcompound1.setByteArray("BPData", extendedblockstorage.getBPdataArray());
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, nbtTagCompound.var));
		toInject.add(new LdcInsnNode("BPData"));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, extendedBlockStorage.var));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "getBPdataArray", "()[B", false, this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74773_a", "(Ljava/lang/String;[B)V", false, this));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformReadChunkFromNBT(final MethodNode method)
	{
		//extendedblockstorage.removeInvalidBlocks();
		final InsnList toFind = new InsnList();
		toFind.add(new VarInsnNode(Opcodes.ALOAD, 13));
		toFind.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "func_76672_e", "()V", false, this));

		final AbstractInsnNode target = ASMHelper.find(method.instructions, toFind);

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in AnvilChunkLoader.readChunkFromNBT");
		}

		//if (nbttagcompound1.hasKey("BPData"))
		//{
		//	extendedblockstorage.setBPdataArray(nbttagcompound1.getByteArray("BPData"));
		//}
		//else
		//{
		//	extendedblockstorage.setBPdataArray(new byte[4096]);
		//}
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 11));
		toInject.add(new LdcInsnNode("BPData"));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74764_b", "(Ljava/lang/String;)Z", false, this));
		final LabelNode label1 = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.IFEQ, label1));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 13));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 11));
		toInject.add(new LdcInsnNode("BPData"));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74770_j", "(Ljava/lang/String;)[B", false, this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "setBPdataArray", "([B)V", false, this));
		final LabelNode label2 = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.GOTO, label2));
		toInject.add(label1);
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 13));
		toInject.add(new IntInsnNode(Opcodes.SIPUSH, 4096));
		toInject.add(new IntInsnNode(Opcodes.NEWARRAY, Opcodes.T_BYTE));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "setBPdataArray", "([B)V", false, this));
		toInject.add(label2);

		method.instructions.insertBefore(target, toInject);
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0) {}
}