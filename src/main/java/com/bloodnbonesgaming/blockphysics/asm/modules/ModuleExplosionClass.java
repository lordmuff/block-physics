package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
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

public class ModuleExplosionClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
				"net.minecraft.world.Explosion"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformExplosionClass";
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

		if (transformedName.equals("net.minecraft.world.Explosion"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);

			//"<init>", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;DDDF)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;DDDF)V");
			if (methodNode != null)
			{
				this.transformInit(methodNode);
			} else {
				throw new RuntimeException("Could not find <init> method in " + transformedName);
			}

			//"doExplosionA", "()V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "doExplosionA" : "func_77278_a", "()V");
			if (methodNode != null)
			{
				this.transformDoExplosionA(methodNode);
			} else {
				throw new RuntimeException("Could not find doExplosionA method in " + transformedName);
			}

			//"doExplosionB", "(Z)V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "doExplosionB" : "func_77279_a", "(Z)V");
			if (methodNode != null)
			{
				this.transformDoExplosionB(methodNode);
			} else {
				throw new RuntimeException("Could not find doExplosionB method in " + transformedName);
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
		toInject.add(new InsnNode(Opcodes.ICONST_0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/Explosion", "impact", "Z", this));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformDoExplosionA(final MethodNode method)
	{
		method.instructions.clear();
		method.localVariables.clear();


		//BlockPhysics.doExplosionA(this.worldObj, this);
		//AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/Explosion", !ObfHelper.isObfuscated() ? "worldObj" : "field_77287_j", "Lnet/minecraft/world/World;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "doExplosionA", "(Lnet/minecraft/world/World;Lnet/minecraft/world/Explosion;)V", false, this));
		toInject.add(new InsnNode(Opcodes.RETURN));

		method.instructions.add(toInject);


	}

	public void transformDoExplosionB(final MethodNode method)
	{
		method.instructions.clear();
		method.localVariables.clear();

		//BlockPhysics.doExplosionB(this.worldObj, this, p_77279_1_);
		//AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/Explosion", !ObfHelper.isObfuscated() ? "worldObj" : "field_77287_j", "Lnet/minecraft/world/World;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 1));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "doExplosionB", "(Lnet/minecraft/world/World;Lnet/minecraft/world/Explosion;Z)V", false, this));
		toInject.add(new InsnNode(Opcodes.RETURN));

		method.instructions.add(toInject);
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry registry) {
		registry.registerFieldAddition("net/minecraft/world/Explosion", new FieldNode(Opcodes.ACC_PUBLIC, "impact", "Z", null, null));
	}
}