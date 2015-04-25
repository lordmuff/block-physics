package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.insn.RedirectedFieldInsnNode;
import com.bnbgaming.lib.core.insn.RedirectedMethodInsnNode;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

public class ModuleWorldClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
				"net.minecraft.world.World"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformWorldClass";
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

		if (transformedName.equals("net.minecraft.world.World"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);

			//"<init>", "(Lnet/minecraft/world/storage/ISaveHandler;Ljava/lang/String;Lnet/minecraft/world/WorldProvider;Lnet/minecraft/world/WorldSettings;Lnet/minecraft/profiler/Profiler;)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/storage/ISaveHandler;Ljava/lang/String;Lnet/minecraft/world/WorldProvider;Lnet/minecraft/world/WorldSettings;Lnet/minecraft/profiler/Profiler;)V");
			if (methodNode != null)
			{
				this.transformInit1(methodNode);
			} else {
				ASMPlugin.log.debug("Could not find <init>1 method in " + transformedName + ". This must be a dedicated server.");
			}

			//"<init>", "(Lnet/minecraft/world/storage/ISaveHandler;Ljava/lang/String;Lnet/minecraft/world/WorldSettings;Lnet/minecraft/world/WorldProvider;Lnet/minecraft/profiler/Profiler;)V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/storage/ISaveHandler;Ljava/lang/String;Lnet/minecraft/world/WorldSettings;Lnet/minecraft/world/WorldProvider;Lnet/minecraft/profiler/Profiler;)V");
			if (methodNode != null)
			{
				this.transformInit2(methodNode);
			} else {
				throw new RuntimeException("Could not find <init>2 method in " + transformedName);
			}

			//"newExplosion", "(Lnet/minecraft/entity/Entity;DDDFZZ)Lnet/minecraft/world/Explosion;"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_72885_a", "(Lnet/minecraft/entity/Entity;DDDFZZ)Lnet/minecraft/world/Explosion;");
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

	public void transformInit1(final MethodNode method)
	{
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in World.<init> 1");
		}

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new TypeInsnNode(Opcodes.NEW, ModInfo.MAIN_PACKACE + "/blockphysics/BTickList"));
		toInject.add(new InsnNode(Opcodes.DUP));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESPECIAL, ModInfo.MAIN_PACKACE + "/blockphysics/BTickList", "<init>", "()V", false, this));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/World", "moveTickList", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/BTickList;", this));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new TypeInsnNode(Opcodes.NEW, "java/util/HashSet"));
		toInject.add(new InsnNode(Opcodes.DUP));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESPECIAL, "java/util/HashSet", "<init>", "()V", false, this));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/World", "pistonMoveBlocks", "Ljava/util/HashSet;", this));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new TypeInsnNode(Opcodes.NEW, ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue"));
		toInject.add(new InsnNode(Opcodes.DUP));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESPECIAL, ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue", "<init>", "()V", false, this));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/World", "explosionQueue", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue;", this));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformInit2(final MethodNode method)
	{
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in World.<init> 2");
		}

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new TypeInsnNode(Opcodes.NEW, ModInfo.MAIN_PACKACE + "/blockphysics/BTickList"));
		toInject.add(new InsnNode(Opcodes.DUP));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESPECIAL, ModInfo.MAIN_PACKACE + "/blockphysics/BTickList", "<init>", "()V", false, this));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/World", "moveTickList", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/BTickList;", this));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new TypeInsnNode(Opcodes.NEW, "java/util/HashSet"));
		toInject.add(new InsnNode(Opcodes.DUP));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESPECIAL, "java/util/HashSet", "<init>", "()V", false, this));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/World", "pistonMoveBlocks", "Ljava/util/HashSet;", this));

		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new TypeInsnNode(Opcodes.NEW, ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue"));
		toInject.add(new InsnNode(Opcodes.DUP));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESPECIAL, ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue", "<init>", "()V", false, this));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/World", "explosionQueue", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue;", this));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformNewExplosion(final MethodNode method)
	{
		final InsnList toRemove = new InsnList();
		toRemove.add(new VarInsnNode(Opcodes.ALOAD, 11));
		toRemove.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/Explosion", "func_77278_a", "()V", false));

		final AbstractInsnNode start = ASMHelper.find(method.instructions, toRemove);
		final AbstractInsnNode end = ASMHelper.move(start, 7);


		if (start == null || end == null) {
			throw new RuntimeException("Unexpected instruction pattern in World.NewExplosion");
		}

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);

		final AbstractInsnNode target2 = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.ARETURN);

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/World", "explosionQueue", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 11));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue", "add", "(Lnet/minecraft/world/Explosion;)V", false, this));

		method.instructions.insertBefore(target2, toInject);
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry registry) {
		registry.registerFieldAddition("net/minecraft/world/World", new FieldNode(Opcodes.ACC_PUBLIC, "moveTickList", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/BTickList;", null, null));
		registry.registerFieldAddition("net/minecraft/world/World", new FieldNode(Opcodes.ACC_PUBLIC, "pistonMoveBlocks", "Ljava/util/HashSet;", null, null));
		registry.registerFieldAddition("net/minecraft/world/World", new FieldNode(Opcodes.ACC_PUBLIC, "explosionQueue", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue;", null, null));
	}
}