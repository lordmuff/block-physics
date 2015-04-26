package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;
import squeek.asmhelper.com.bloodnbonesgaming.lib.ObfHelper;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.insn.RedirectedFieldInsnNode;
import com.bnbgaming.lib.core.insn.RedirectedMethodInsnNode;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

public class ModuleTileEntityPistonClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
				"net.minecraft.tileentity.TileEntityPiston"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformTileEntityPistonClass";
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

		if (transformedName.equals("net.minecraft.tileentity.TileEntityPiston"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);

			//"<init>", "(Lnet/minecraft/block/Block;IIZZ)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/block/Block;IIZZ)V");
			if (methodNode != null)
			{
				this.transformInit(methodNode);
			} else {
				throw new RuntimeException("Could not find <init> method in " + transformedName);
			}

			//"clearPistonTileEntity", "()V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "clearPistonTileEntity" : "func_145866_f", "()V");
			if (methodNode != null)
			{
				this.transformClearPistonTileEntity(methodNode);
			} else {
				throw new RuntimeException("Could not find clearPistonTileEntity method in " + transformedName);
			}

			//"updateEntity", "()V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "updateEntity" : "func_145845_h", "()V");
			if (methodNode != null)
			{
				this.transformUpdateEntity(methodNode);
			} else {
				throw new RuntimeException("Could not find updateEntity method in " + transformedName);
			}

			//"readFromNBT", "(Lnet/minecraft/nbt/NBTTagCompound;)V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "readFromNBT" : "func_145839_a", "(Lnet/minecraft/nbt/NBTTagCompound;)V");
			if (methodNode != null)
			{
				this.transformReadFromNBT(methodNode);
			} else {
				throw new RuntimeException("Could not find readFromNBT method in " + transformedName);
			}

			//"writeToNBT", "(Lnet/minecraft/nbt/NBTTagCompound;)V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "writeToNBT" : "func_145841_b", "(Lnet/minecraft/nbt/NBTTagCompound;)V");
			if (methodNode != null)
			{
				this.transformWriteToNBT(methodNode);
			} else {
				throw new RuntimeException("Could not find writeToNBT method in " + transformedName);
			}

			return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}

	public void transformInit(final MethodNode method)
	{
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.ACONST_NULL));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/tileentity/TileEntityPiston", "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", this));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.ICONST_0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/tileentity/TileEntityPiston", "bpmeta", "I", this));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformClearPistonTileEntity(final MethodNode method)
	{
		final AbstractInsnNode target = ASMHelper.find(method.instructions, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", !ObfHelper.isObfuscated() ? "notifyBlockOfNeighborChange" : "func_147460_e", "(IIILnet/minecraft/block/Block;)V", false));

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "worldObj" : "field_145850_b", "Lnet/minecraft/world/World;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "xCoord" : "field_145851_c", "I", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));;
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "yCoord" : "field_145848_d", "I", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "zCoord" : "field_145849_e", "I", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "bpmeta", "I", this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "setBlockBPdata", "(Lnet/minecraft/world/World;IIII)Z", false, this));
		toInject.add(new InsnNode(Opcodes.POP));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", this));
		final LabelNode label1 = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.IFNULL, label1));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "storedBlock" : "field_145869_a", "Lnet/minecraft/block/Block;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "worldObj" : "field_145850_b", "Lnet/minecraft/world/World;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "storedMetadata" : "field_145876_i", "I", this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/block/Block", "createTileEntity", "(Lnet/minecraft/world/World;I)Lnet/minecraft/tileentity/TileEntity;", false, this));
		toInject.add(new VarInsnNode(Opcodes.ASTORE, 1));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/tileentity/TileEntity", !ObfHelper.isObfuscated() ? "readFromNBT" : "func_145839_a", "(Lnet/minecraft/nbt/NBTTagCompound;)V", false, this));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "worldObj" : "field_145850_b", "Lnet/minecraft/world/World;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "xCoord" : "field_145851_c", "I", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));;
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "yCoord" : "field_145848_d", "I", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "zCoord" : "field_145849_e", "I", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", !ObfHelper.isObfuscated() ? "setTileEntity" : "func_147455_a", "(IIILnet/minecraft/tileentity/TileEntity;)V", false, this));
		toInject.add(label1);

		method.instructions.insert(target, toInject);
	}

	public void transformUpdateEntity(final MethodNode method)
	{
		final AbstractInsnNode target = ASMHelper.find(method.instructions, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", !ObfHelper.isObfuscated() ? "notifyBlockOfNeighborChange" : "func_147460_e", "(IIILnet/minecraft/block/Block;)V", false));

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "worldObj" : "field_145850_b", "Lnet/minecraft/world/World;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "xCoord" : "field_145851_c", "I", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));;
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "yCoord" : "field_145848_d", "I", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "zCoord" : "field_145849_e", "I", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "bpmeta", "I", this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "setBlockBPdata", "(Lnet/minecraft/world/World;IIII)Z", false, this));
		toInject.add(new InsnNode(Opcodes.POP));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", this));
		final LabelNode label1 = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.IFNULL, label1));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "storedBlock" : "field_145869_a", "Lnet/minecraft/block/Block;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "worldObj" : "field_145850_b", "Lnet/minecraft/world/World;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "storedMetadata" : "field_145876_i", "I", this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/block/Block", "createTileEntity", "(Lnet/minecraft/world/World;I)Lnet/minecraft/tileentity/TileEntity;", false, this));
		toInject.add(new VarInsnNode(Opcodes.ASTORE, 1));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/tileentity/TileEntity", !ObfHelper.isObfuscated() ? "readFromNBT" : "func_145839_a", "(Lnet/minecraft/nbt/NBTTagCompound;)V", false, this));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "worldObj" : "field_145850_b", "Lnet/minecraft/world/World;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "xCoord" : "field_145851_c", "I", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));;
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "yCoord" : "field_145848_d", "I", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", !ObfHelper.isObfuscated() ? "zCoord" : "field_145849_e", "I", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", !ObfHelper.isObfuscated() ? "setTileEntity" : "func_147455_a", "(IIILnet/minecraft/tileentity/TileEntity;)V", false, this));
		toInject.add(label1);

		method.instructions.insert(target, toInject);
	}

	public void transformReadFromNBT(final MethodNode method)
	{
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new LdcInsnNode("BPData"));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", !ObfHelper.isObfuscated() ? "hasKey" : "func_74764_b", "(Ljava/lang/String;)Z", false, this));
		final LabelNode label1 = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.IFEQ, label1));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new LdcInsnNode("BPData"));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", !ObfHelper.isObfuscated() ? "getByte" : "func_74771_c", "(Ljava/lang/String;)B", false, this));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/tileentity/TileEntityPiston", "bpmeta", "I", this));
		toInject.add(label1);

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new LdcInsnNode("TileEntityData"));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", !ObfHelper.isObfuscated() ? "hasKey" : "func_74764_b", "(Ljava/lang/String;)Z", false, this));
		final LabelNode label2 = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.IFEQ, label2));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new LdcInsnNode("TileEntityData"));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", !ObfHelper.isObfuscated() ? "getCompoundTag" : "func_74775_l", "(Ljava/lang/String;)Lnet/minecraft/nbt/NBTTagCompound;", false, this));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/tileentity/TileEntityPiston", "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", this));
		toInject.add(label2);

		method.instructions.insertBefore(target, toInject);
	}

	public void transformWriteToNBT(final MethodNode method)
	{
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new LdcInsnNode("BPData"));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "bpmeta", "I", this));
		toInject.add(new InsnNode(Opcodes.I2B));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", !ObfHelper.isObfuscated() ? "setByte" : "func_74774_a", "(Ljava/lang/String;B)V", false, this));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", this));
		final LabelNode label1 = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.IFNULL, label1));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new LdcInsnNode("TileEntityData"));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", !ObfHelper.isObfuscated() ? "setTag" : "func_74782_a", "(Ljava/lang/String;Lnet/minecraft/nbt/NBTBase;)V", false, this));
		toInject.add(label1);

		method.instructions.insertBefore(target, toInject);
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry registry) {
		registry.registerFieldAddition("net/minecraft/tileentity/TileEntityPiston", new FieldNode(Opcodes.ACC_PUBLIC, "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", null, null));
		registry.registerFieldAddition("net/minecraft/tileentity/TileEntityPiston", new FieldNode(Opcodes.ACC_PUBLIC, "bpmeta", "I", null, null));
	}
}