package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.insn.RedirectedFieldInsnNode;
import com.bnbgaming.lib.core.insn.RedirectedMethodInsnNode;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

public class ModuleBlockFarmlandClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
				"net.minecraft.block.BlockFarmland"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformBlockFarmlandClass";
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

		if (transformedName.equals("net.minecraft.block.BlockFarmland"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);

			//"onFallenUpon", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;F)V"
			final MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149746_a", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;F)V");
			if (methodNode != null)
			{
				this.transformOnFallenUpon(methodNode);
			} else {
				throw new RuntimeException("Could not find onFallenUpon method in " + transformedName);
			}

			return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}

	public void transformOnFallenUpon(final MethodNode method)
	{
		final AbstractInsnNode target = ASMHelper.findFirstInstructionWithOpcode(method, Opcodes.RETURN);

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in BlockFalling.onFallenUpon");
		}

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 2));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/block/Block", "field_149771_c", "Lnet/minecraft/util/RegistryNamespaced;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/RegistryNamespaced", "func_148750_c", "(Ljava/lang/Object;)Ljava/lang/String;", false, this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "onNeighborBlockChange", "(Lnet/minecraft/world/World;IIILjava/lang/String;)V", false, this));

		method.instructions.insertBefore(target, toInject);

		final AbstractInsnNode target2 = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (target2 == null) {
			throw new RuntimeException("Unexpected instruction pattern in BlockFalling.onFallenUpon");
		}

		method.instructions.insertBefore(target2, toInject);
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0) {}
}