package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
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

public class ModuleWorldServerClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
				"net.minecraft.world.WorldServer"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformWorldServerClass";
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

		if (transformedName.equals("net.minecraft.world.WorldServer"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);

			//"tick", "()V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "tick" : "func_72835_b", "()V");
			if (methodNode != null)
			{
				this.transformTick(methodNode);
			} else {
				throw new RuntimeException("Could not find tick method in " + transformedName);
			}

			//"newExplosion", "(Lnet/minecraft/entity/Entity;DDDFZZ)Lnet/minecraft/world/Explosion;"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "newExplosion" : "func_72885_a", "(Lnet/minecraft/entity/Entity;DDDFZZ)Lnet/minecraft/world/Explosion;");
			if (methodNode != null)
			{
				this.transformNewExplosion(methodNode);
			} else {
				throw new RuntimeException("Could not find newExplosion method in " + transformedName);
			}

			return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}

	public void transformTick(final MethodNode method)
	{
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in WorldServer.tick");
		}

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "tickBlocksRandomMove", "(Lnet/minecraft/world/WorldServer;)V", false, this));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/World", "moveTickList", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/BTickList;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, ModInfo.MAIN_PACKACE + "/blockphysics/BTickList", "tickMoveUpdates", "(Lnet/minecraft/world/World;)V", false, this));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/World", "pistonMoveBlocks", "Ljava/util/HashSet;", this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/HashSet", "clear", "()V", false, this));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/World", "explosionQueue", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue;", this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue", "doNextExplosion", "()V", false, this));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformNewExplosion(final MethodNode method)
	{
		final InsnList toRemove1 = new InsnList();
		toRemove1.add(new VarInsnNode(Opcodes.ALOAD, 11));
		toRemove1.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/Explosion", !ObfHelper.isObfuscated() ? "doExplosionA" : "func_77278_a", "()V", false, this));

		final AbstractInsnNode start1 = ASMHelper.find(method.instructions, toRemove1);
		final AbstractInsnNode end1 = ASMHelper.move(start1, 2);

		if (start1 == null || end1 == null) {
			throw new RuntimeException("Unexpected instruction pattern in WorldServer.tick 1");
		}

		ASMHelper.removeFromInsnListUntil(method.instructions, start1, end1);

		final InsnList toRemove2 = new InsnList();
		toRemove2.add(new VarInsnNode(Opcodes.ALOAD, 11));
		toRemove2.add(new InsnNode(Opcodes.ICONST_0));
		toRemove2.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/Explosion", !ObfHelper.isObfuscated() ? "doExplosionB" : "func_77279_a", "(Z)V", false, this));

		final AbstractInsnNode start2 = ASMHelper.find(method.instructions, toRemove2);
		final AbstractInsnNode end2 = ASMHelper.move(start2, 3);

		if (start2 == null || end2 == null) {
			throw new RuntimeException("Unexpected instruction pattern in WorldServer.tick 2");
		}

		ASMHelper.removeFromInsnListUntil(method.instructions, start2, end2);

		final AbstractInsnNode target = ASMHelper.find(method.instructions, new FrameNode(Opcodes.F_APPEND,1, new Object[] {"net/minecraft/world/Explosion"}, 0, null));

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in WorldServer.tick 3");
		}

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/World", "explosionQueue", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 11));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue", "add", "(Lnet/minecraft/world/Explosion;)V", false, this));

		method.instructions.insert(target, toInject);
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0) {}
}