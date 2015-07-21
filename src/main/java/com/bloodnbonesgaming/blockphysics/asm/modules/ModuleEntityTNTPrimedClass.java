package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
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

public class ModuleEntityTNTPrimedClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
				"net.minecraft.entity.item.EntityTNTPrimed"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformEntityTNTPrimedClass";
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

		if (transformedName.equals("net.minecraft.entity.item.EntityTNTPrimed"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);

			this.changeSuperClass(classNode);

			//"<init>", "(Lnet/minecraft/world/World;)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/World;)V");
			if (methodNode != null)
			{
				this.transformInit1(methodNode);
			} else {
				throw new RuntimeException("Could not find init(World) method in " + transformedName);
			}

			//"canBeCollidedWith", "()Z"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "canBeCollidedWith" : "func_70067_L", "()Z");
			if (methodNode != null)
			{
				this.transformCanBeCollidedWith(methodNode);
			} else {
				throw new RuntimeException("Could not find canBeCollidedWith method in " + transformedName);
			}

			//"onUpdate", "()V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "onUpdate" : "func_70071_h_", "()V");
			if (methodNode != null)
			{
				this.transformOnUpdate(methodNode);
			} else {
				throw new RuntimeException("Could not find onUpdate method in " + transformedName);
			}

			//"explode", "()V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "explode" : "func_70515_d", "()V");
			if (methodNode != null)
			{
				this.transformExplode(methodNode);
			} else {
				throw new RuntimeException("Could not find explode method in " + transformedName);
			}

			//"writeEntityToNBT", "(Lnet/minecraft/nbt/NBTTagCompound;)V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "writeEntityToNBT" : "func_70014_b", "(Lnet/minecraft/nbt/NBTTagCompound;)V");
			if (methodNode != null)
			{
				this.transformWriteEntityToNBT(methodNode);
			} else {
				throw new RuntimeException("Could not find writeEntityToNBT method in " + transformedName);
			}

			//"readEntityFromNBT", "(Lnet/minecraft/nbt/NBTTagCompound;)V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "readEntityFromNBT" : "func_70037_a", "(Lnet/minecraft/nbt/NBTTagCompound;)V");
			if (methodNode != null)
			{
				this.transformReadEntityFromNBT(methodNode);
			} else {
				throw new RuntimeException("Could not find readEntityFromNBT method in " + transformedName);
			}

			return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}

	public void changeSuperClass(final ClassNode classNode)
	{
		classNode.superName = "net/minecraft/entity/item/EntityFallingBlock";
	}

	public void transformInit1(final MethodNode method)
	{
		//Fix super call
		final AbstractInsnNode toReplace = ASMHelper.find(method.instructions, new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/Entity", "<init>", "(Lnet/minecraft/world/World;)V", false));
		final AbstractInsnNode replacement = new RedirectedMethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/item/EntityFallingBlock", "<init>", "(Lnet/minecraft/world/World;)V", false, this);

		if (toReplace == null) {
			throw new RuntimeException("Unexpected instruction pattern in EntityTNTPrimed.init 1");
		}

		method.instructions.set(toReplace, replacement);
	}

	public void transformInit2(final MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		final AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		final AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (start == null || end == null) {
			throw new RuntimeException("Unexpected instruction pattern in EntityTNTPrimed.<init>(World,D,D,D,EntityLivingBase)");
		}

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);

		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		final InsnList toInject = new InsnList();
		//super(p_i1730_1_, p_i1730_2_, p_i1730_4_, p_i1730_6_, Blocks.tnt, 0);
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new VarInsnNode(Opcodes.DLOAD, 2));
		toInject.add(new VarInsnNode(Opcodes.DLOAD, 4));
		toInject.add(new VarInsnNode(Opcodes.DLOAD, 6));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Blocks", !ObfHelper.isObfuscated() ? "tnt" : "field_150335_W", "Lnet/minecraft/block/Block;", this));
		toInject.add(new InsnNode(Opcodes.ICONST_0));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/item/EntityFallingBlock", "<init>", "(Lnet/minecraft/world/World;DDDLnet/minecraft/block/Block;I)V", false, this));

		//this.fuse = 80;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new IntInsnNode(Opcodes.BIPUSH, 80));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityTNTPrimed", !ObfHelper.isObfuscated() ? "fuse" : "field_70516_a", "I", this));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformCanBeCollidedWith(final MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		final AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		final AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.IRETURN);

		if (start == null || end == null) {
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.canBeCollidedWith");
		}

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);

		//return false;
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.IRETURN);

		final InsnList toInject = new InsnList();
		toInject.add(new InsnNode(Opcodes.ICONST_0));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformOnUpdate(final MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		final AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		final AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (start == null || end == null) {
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.onUpdate");
		}

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);

		//BlockPhysics.fallingSandUpdate(this.worldObj, this);
		final AbstractInsnNode target = ASMHelper.findFirstInstruction(method);

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "worldObj" : "field_70170_p", "Lnet/minecraft/world/World;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "fallingSandUpdate", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/item/EntityFallingBlock;)V", false, this));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformExplode(final MethodNode method)
	{
		//Set access to public
		method.access = Opcodes.ACC_PUBLIC;

		//Replace all ACONST_NULL with ALOAD, 0 - Fixes a superclass change error I imagine
		AbstractInsnNode toReplace = ASMHelper.find(method.instructions, new InsnNode(Opcodes.ACONST_NULL));
		final AbstractInsnNode replacement = new VarInsnNode(Opcodes.ALOAD, 0);

		while (toReplace != null)
		{
			method.instructions.set(toReplace, replacement);
			toReplace = ASMHelper.find(method.instructions, new InsnNode(Opcodes.ACONST_NULL));
		}
	}

	public void transformWriteEntityToNBT(final MethodNode method)
	{
		//super.writeEntityToNBT(p_70014_1_);
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "writeEntityToNBT" : "func_70014_b", "(Lnet/minecraft/nbt/NBTTagCompound;)V", false, this));

		method.instructions.insertBefore(target,  toInject);
	}

	public void transformReadEntityFromNBT(final MethodNode method)
	{
		//super.readEntityFromNBT(p_70037_1_);
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "readEntityFromNBT" : "func_70037_a", "(Lnet/minecraft/nbt/NBTTagCompound;)V", false, this));

		method.instructions.insertBefore(target,  toInject);
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0) {}
}