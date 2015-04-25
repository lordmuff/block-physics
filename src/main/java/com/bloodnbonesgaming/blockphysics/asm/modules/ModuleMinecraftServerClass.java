package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.insn.RedirectedMethodInsnNode;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

public class ModuleMinecraftServerClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
				"net.minecraft.server.MinecraftServer"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformMinecraftServerClass";
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

		if (transformedName.equals("net.minecraft.server.MinecraftServer"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);

			//"run", "()V"
			final MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "run", "()V");
			if (methodNode != null)
			{
				this.transformRun(methodNode);
			} else {
				throw new RuntimeException("Could not find run method in " + transformedName);
			}

			return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}

	public void transformRun(final MethodNode method)
	{
		final InsnList toFind = new InsnList();
		toFind.add(new VarInsnNode(Opcodes.LLOAD, 3));
		toFind.add(new VarInsnNode(Opcodes.LLOAD, 7));
		toFind.add(new InsnNode(Opcodes.LADD));
		toFind.add(new VarInsnNode(Opcodes.LSTORE, 3));

		final AbstractInsnNode target = ASMHelper.find(method.instructions, toFind);

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in MinecraftServer.run");
		}

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.LLOAD, 3));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "setSkipMove", "(J)V", false, this));

		method.instructions.insertBefore(target, toInject);
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0) {}
}