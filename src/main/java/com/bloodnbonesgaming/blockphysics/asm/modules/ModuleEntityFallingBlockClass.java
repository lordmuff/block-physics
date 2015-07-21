package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;
import squeek.asmhelper.com.bloodnbonesgaming.lib.ObfHelper;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bloodnbonesgaming.lib.core.ASMAdditionRegistry;
import com.bloodnbonesgaming.lib.core.insn.RedirectedFieldInsnNode;
import com.bloodnbonesgaming.lib.core.insn.RedirectedMethodInsnNode;
import com.bloodnbonesgaming.lib.core.insn.RedirectedMethodVisitor;
import com.bloodnbonesgaming.lib.core.module.IClassTransformerModule;

public class ModuleEntityFallingBlockClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
				"net.minecraft.entity.item.EntityFallingBlock"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformEntityFallingBlockClass";
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

		if (transformedName.equals("net.minecraft.entity.item.EntityFallingBlock"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);

			//"<init>", "(Lnet/minecraft/world/World;)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/World;)V");
			if (methodNode != null)
			{
				this.transformInit1(methodNode);
			} else {
				throw new RuntimeException("Could not find init(World) method in " + transformedName);
			}

			//"<init>", "(Lnet/minecraft/world/World;DDDLnet/minecraft/block/Block;I)V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/World;DDDLnet/minecraft/block/Block;I)V");
			if (methodNode != null)
			{
				this.transformInit2(methodNode);
			} else {
				throw new RuntimeException("Could not find init(World,D,D,D,Block,I) method in " + transformedName);
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

			//"fall", "(F)V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "fall" : "func_70069_a", "(F)V");
			if (methodNode != null)
			{
				this.transformFall(methodNode);
			} else {
				throw new RuntimeException("Could not find fall method in " + transformedName);
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

			//"canRenderOnFire", "()Z"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "canRenderOnFire" : "func_90999_ad", "()Z");
			if (methodNode != null)
			{
				this.transformCanRenderOnFire(methodNode);
			} else {
				ASMPlugin.log.debug("Could not find canRenderOnFire method in " + transformedName + ". This must be a dedicated server.");
			}

			return ASMHelper.writeClassToBytesNoDeobfSkipFrames(classNode);
		}
		return bytes;
	}

	public void createGetBoundingBox(final ClassNode classNode)
	{
		final MethodVisitor methodVisitor = classNode.visitMethod(Opcodes.ACC_PUBLIC, !ObfHelper.isObfuscated() ? "getBoundingBox" : "func_70046_E", "()Lnet/minecraft/util/AxisAlignedBB;", null, null);
		final RedirectedMethodVisitor rMethodVisitor = new RedirectedMethodVisitor(methodVisitor);
		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
		rMethodVisitor.visitRedirectedFieldInsn(Opcodes.GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "boundingBox" : "field_70121_D", "Lnet/minecraft/util/AxisAlignedBB;", this);
		methodVisitor.visitInsn(Opcodes.ARETURN);
		methodVisitor.visitMaxs(1, 1);
		methodVisitor.visitEnd();
	}

	public void createSetInWeb(final ClassNode classNode)
	{
		final MethodVisitor methodVisitor = classNode.visitMethod(Opcodes.ACC_PUBLIC, !ObfHelper.isObfuscated() ? "setInWeb" : "func_70110_aj", "()V", null, null);
		methodVisitor.visitCode();
		methodVisitor.visitInsn(Opcodes.RETURN);
		methodVisitor.visitMaxs(0, 1);
		methodVisitor.visitEnd();
	}

	public void createMoveEntity(final ClassNode classNode)
	{
		final MethodVisitor methodVisitor = classNode.visitMethod(Opcodes.ACC_PUBLIC, !ObfHelper.isObfuscated() ? "moveEntity" : "func_70091_d", "(DDD)V", null, null);
		final RedirectedMethodVisitor rMethodVisitor = new RedirectedMethodVisitor(methodVisitor);
		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
		rMethodVisitor.visitRedirectedFieldInsn(Opcodes.GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "worldObj" : "field_70170_p", "Lnet/minecraft/world/World;", this);
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
		methodVisitor.visitVarInsn(Opcodes.DLOAD, 1);
		methodVisitor.visitVarInsn(Opcodes.DLOAD, 3);
		methodVisitor.visitVarInsn(Opcodes.DLOAD, 5);
		rMethodVisitor.visitRedirectedMethodInsn(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "moveEntity", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/item/EntityFallingBlock;DDD)V", false, this);
		methodVisitor.visitInsn(Opcodes.RETURN);
		methodVisitor.visitMaxs(8, 7);
		methodVisitor.visitEnd();
	}

	public void transformInit1(final MethodNode method)
	{
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		final InsnList toInject = new InsnList();
		//this.preventEntitySpawning = true;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.ICONST_1));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "preventEntitySpawning" : "field_70156_m", "Z", this));

		//this.setSize(0.996f, 0.996f);
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new LdcInsnNode(new Float("0.996")));
		toInject.add(new LdcInsnNode(new Float("0.996")));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "setSize" : "func_70105_a", "(FF)V", false, this));

		//this.yOffset = this.height/2.0f;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "height" : "field_70131_O", "F", this));
		toInject.add(new InsnNode(Opcodes.FCONST_2));
		toInject.add(new InsnNode(Opcodes.FDIV));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "yOffset" : "field_70129_M", "F", this));

		//this.motionX = 0;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.DCONST_0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "motionX" : "field_70159_w", "D", this));

		//this.motionY = 0;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.DCONST_0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "motionY" : "field_70181_x", "D", this));

		//this.motionZ = 0;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.DCONST_0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "motionZ" : "field_70179_y", "D", this));

		//this.accelerationX = 0;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.DCONST_0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationX", "D", this));

		//this.accelerationY = -0.024525;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new LdcInsnNode(new Double("-0.024525")));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationY", "D", this));

		//this.accelerationZ = 0;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.DCONST_0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationZ", "D", this));

		//this.slideDir = 0;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.ICONST_0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "slideDir", "B", this));

		//this.noClip = true;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.ICONST_1));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "noClip" : "field_70145_X", "Z", this));

		//this.entityCollisionReduction = 0.8F;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new LdcInsnNode(new Float("0.8")));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "entityCollisionReduction" : "field_70144_Y", "F", this));

		//this.dead = 4;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.ICONST_4));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "dead", "B", this));

		//this.bpdata = 0;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.ICONST_0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "bpdata", "I", this));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformInit2(final MethodNode method)
	{
		AbstractInsnNode toReplace = ASMHelper.find(method.instructions, new LdcInsnNode(new Float("0.98")));
		while (toReplace != null)
		{
			final LdcInsnNode replacement = new LdcInsnNode(new Float("0.996"));

			method.instructions.set(toReplace, replacement);

			toReplace = ASMHelper.find(method.instructions, new LdcInsnNode(new Float("0.98")));
		}

		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		final InsnList toInject = new InsnList();

		//this.accelerationX = 0;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.DCONST_0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationX", "D", this));

		//this.accelerationY = -0.024525;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new LdcInsnNode(new Double("-0.024525")));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationY", "D", this));

		//this.accelerationZ = 0;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.DCONST_0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationZ", "D", this));

		//this.slideDir = 0;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.ICONST_0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "slideDir", "B", this));

		//this.noClip = true;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.ICONST_1));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "noClip" : "field_70145_X", "Z", this));

		//this.entityCollisionReduction = 0.8F;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new LdcInsnNode(new Float("0.8")));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "entityCollisionReduction" : "field_70144_Y", "F", this));

		//this.dead = 4;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.ICONST_4));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "dead", "B", this));

		//this.bpdata = 0;
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.ICONST_0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "bpdata", "I", this));

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

	public void transformFall(final MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		final AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		final AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (start == null || end == null) {
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.fall");
		}

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
	}

	public void transformWriteEntityToNBT(final MethodNode method)
	{
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		//p_70014_1_.setTag("Acceleration", newDoubleNBTList(new double[] {this.accelerationX, this.accelerationY, this.accelerationZ}));
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new LdcInsnNode("Acceleration"));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.ICONST_3));
		toInject.add(new IntInsnNode(Opcodes.NEWARRAY, Opcodes.T_DOUBLE));
		toInject.add(new InsnNode(Opcodes.DUP));
		toInject.add(new InsnNode(Opcodes.ICONST_0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationX", "D", this));
		toInject.add(new InsnNode(Opcodes.DASTORE));
		toInject.add(new InsnNode(Opcodes.DUP));
		toInject.add(new InsnNode(Opcodes.ICONST_1));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationY", "D", this));
		toInject.add(new InsnNode(Opcodes.DASTORE));
		toInject.add(new InsnNode(Opcodes.DUP));
		toInject.add(new InsnNode(Opcodes.ICONST_2));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationZ", "D", this));
		toInject.add(new InsnNode(Opcodes.DASTORE));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "newDoubleNBTList" : "func_70087_a", "([D)Lnet/minecraft/nbt/NBTTagList;", false, this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", !ObfHelper.isObfuscated() ? "setTag" : "func_74782_a", "(Ljava/lang/String;Lnet/minecraft/nbt/NBTBase;)V", false, this));

		//p_70014_1_.setByte("BPData", (byte)this.bpdata);
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new LdcInsnNode("BPData"));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", "bpdata", "I", this));
		toInject.add(new InsnNode(Opcodes.I2B));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", !ObfHelper.isObfuscated() ? "setByte" : "func_74774_a", "(Ljava/lang/String;B)V", false, this));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformReadEntityFromNBT(final MethodNode method)
	{
		//Remove "TileID" calls
		final AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		final AbstractInsnNode end = ASMHelper.move(start, 25);

		if (start == null || end == null) {
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.readEntityFromNBT 1");
		}

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);

		//this.field_145811_e = BlockPhysics.readFallingSandID(p_70037_1_);
		final AbstractInsnNode target = ASMHelper.findFirstInstruction(method);

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.readEntityFromNBT 2");
		}

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "readFallingSandID", "(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/block/Block;", false, this));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_145811_e", "Lnet/minecraft/block/Block;", this));

		method.instructions.insertBefore(target, toInject);

		//if (p_70037_1_.hasKey("Acceleration"))
		//{
		//	NBTTagList acceleration = p_70037_1_.getTagList("Acceleration", Constants.NBT.TAG_LIST);
		// 	this.accelerationX = acceleration.func_150309_d(0);
		// 	this.accelerationY = acceleration.func_150309_d(1);
		// 	this.accelerationZ = acceleration.func_150309_d(2);
		//}
		//else
		//{
		//	this.accelerationX = 9.0;
		//	this.accelerationY = 0.0;
		//	this.accelerationZ = 0.0;
		//}
		//if (p_70037_1_.hasKey("BPData"))
		//{
		//	this.bpdata = p_70037_1_.getByte("BPData");
		//}
		//else
		//{
		//	this.bpdata = 0;
		//}
		//}
		final AbstractInsnNode target2 = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (target2 == null) {
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.readEntityFromNBT 3");
		}

		final InsnList toInject2 = new InsnList();
		toInject2.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject2.add(new LdcInsnNode("Acceleration"));
		toInject2.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", !ObfHelper.isObfuscated() ? "hasKey" : "func_74764_b", "(Ljava/lang/String;)Z", false, this));
		final LabelNode label1 = new LabelNode();
		toInject2.add(new JumpInsnNode(Opcodes.IFEQ, label1));

		toInject2.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject2.add(new LdcInsnNode("Acceleration"));
		toInject2.add(new IntInsnNode(Opcodes.BIPUSH, 9));
		toInject2.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", !ObfHelper.isObfuscated() ? "getTagList" : "func_150295_c", "(Ljava/lang/String;I)Lnet/minecraft/nbt/NBTTagList;", false, this));
		toInject2.add(new VarInsnNode(Opcodes.ASTORE, 2));

		toInject2.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject2.add(new VarInsnNode(Opcodes.ALOAD, 2));
		toInject2.add(new InsnNode(Opcodes.ICONST_0));
		toInject2.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagList", "func_150309_d", "(I)D", false, this));
		toInject2.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationX", "D", this));

		toInject2.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject2.add(new VarInsnNode(Opcodes.ALOAD, 2));
		toInject2.add(new InsnNode(Opcodes.ICONST_1));
		toInject2.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagList", "func_150309_d", "(I)D", false, this));
		toInject2.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationY", "D", this));

		toInject2.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject2.add(new VarInsnNode(Opcodes.ALOAD, 2));
		toInject2.add(new InsnNode(Opcodes.ICONST_2));
		toInject2.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagList", "func_150309_d", "(I)D", false, this));
		toInject2.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationZ", "D", this));

		final LabelNode label2 = new LabelNode();
		toInject2.add(new JumpInsnNode(Opcodes.GOTO, label2));
		toInject2.add(label1);

		toInject2.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject2.add(new InsnNode(Opcodes.DCONST_0));
		toInject2.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationX", "D", this));


		toInject2.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject2.add(new InsnNode(Opcodes.DCONST_0));
		toInject2.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationY", "D", this));

		toInject2.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject2.add(new InsnNode(Opcodes.DCONST_0));
		toInject2.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationZ", "D", this));
		toInject2.add(label2);

		toInject2.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject2.add(new LdcInsnNode("BPData"));
		toInject2.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", !ObfHelper.isObfuscated() ? "hasKey" : "func_74764_b", "(Ljava/lang/String;)Z", false, this));
		final LabelNode label3 = new LabelNode();
		toInject2.add(new JumpInsnNode(Opcodes.IFEQ, label3));

		toInject2.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject2.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject2.add(new LdcInsnNode("BPData"));
		toInject2.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", !ObfHelper.isObfuscated() ? "getByte" : "func_74771_c", "(Ljava/lang/String;)B", false, this));
		toInject2.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "bpdata", "I", this));

		final LabelNode label4 = new LabelNode();
		toInject2.add(new JumpInsnNode(Opcodes.GOTO, label4));
		toInject2.add(label3);

		toInject2.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject2.add(new InsnNode(Opcodes.ICONST_0));
		toInject2.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "bpdata", "I", this));
		toInject2.add(label4);

		method.instructions.insertBefore(target2, toInject2);
	}

	public void transformCanRenderOnFire(final MethodNode method)
	{
		//Remove return
		final AbstractInsnNode toRemove = ASMHelper.find(method.instructions, new InsnNode(Opcodes.ICONST_0));

		if (toRemove == null) {
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.canRenderOnFire 1");
		}

		method.instructions.remove(toRemove);

		//return this.isInWater();
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.IRETURN);

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.canRenderOnFire 2");
		}

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/item/EntityFallingBlock", !ObfHelper.isObfuscated() ? "isInWater" : "func_70090_H", "()Z", false, this));

		method.instructions.insertBefore(target, toInject);
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry registry) {
		registry.registerFieldAddition("net/minecraft/entity/item/EntityFallingBlock", new FieldNode(Opcodes.ACC_PUBLIC, "slideDir", "B", null, null));
		registry.registerFieldAddition("net/minecraft/entity/item/EntityFallingBlock", new FieldNode(Opcodes.ACC_PUBLIC, "accelerationX", "D", null, null));
		registry.registerFieldAddition("net/minecraft/entity/item/EntityFallingBlock", new FieldNode(Opcodes.ACC_PUBLIC, "accelerationY", "D", null, null));
		registry.registerFieldAddition("net/minecraft/entity/item/EntityFallingBlock", new FieldNode(Opcodes.ACC_PUBLIC, "accelerationZ", "D", null, null));
		registry.registerFieldAddition("net/minecraft/entity/item/EntityFallingBlock", new FieldNode(Opcodes.ACC_PUBLIC, "bpdata", "I", null, null));
		registry.registerFieldAddition("net/minecraft/entity/item/EntityFallingBlock", new FieldNode(Opcodes.ACC_PUBLIC, "media", "Ljava/lang/String;", null, null));
		registry.registerFieldAddition("net/minecraft/entity/item/EntityFallingBlock", new FieldNode(Opcodes.ACC_PUBLIC, "dead", "B", null, null));

		final ClassNode classNode = new ClassNode();

		this.createGetBoundingBox(classNode);
		this.createSetInWeb(classNode);
		this.createMoveEntity(classNode);

		registry.registerMethodAddition("net/minecraft/entity/item/EntityFallingBlock", ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "getBoundingBox" : "func_70046_E", "()Lnet/minecraft/util/AxisAlignedBB;"));
		registry.registerMethodAddition("net/minecraft/entity/item/EntityFallingBlock", ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "setInWeb" : "func_70110_aj", "()V"));
		registry.registerMethodAddition("net/minecraft/entity/item/EntityFallingBlock", ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "moveEntity" : "func_70091_d", "(DDD)V"));
	}
}