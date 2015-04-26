package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
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

public class ModuleBlockClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
				"net.minecraft.block.Block"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformBlockClass";
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

		if (transformedName.equals("net.minecraft.block.Block"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);

			//"onBlockDestroyedByPlayer", "(Lnet/minecraft/world/World;IIII)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "onBlockDestroyedByPlayer" : "func_149664_b", "(Lnet/minecraft/world/World;IIII)V");
			if (methodNode != null)
			{
				this.transformOnBlockDestroyedByPlayer(methodNode);
			} else {
				throw new RuntimeException("Could not find onBlockDestroyedByPlayer method in " + transformedName);
			}
			//"onNeighborBlockChange", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;)V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "onNeighborBlockChange" : "func_149695_a", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;)V");
			if (methodNode != null)
			{
				this.transformOnNeighborBlockChange(methodNode);
			} else {
				throw new RuntimeException("Could not find onNeighborBlockChange method in " + transformedName);
			}
			//"onEntityWalking", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "onEntityWalking" : "func_149724_b", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V");
			if (methodNode != null)
			{
				this.transformOnEntityWalking(methodNode);
			} else {
				throw new RuntimeException("Could not find onEntityWalking method in " + transformedName);
			}
			//"onEntityCollidedWithBlock", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "onEntityCollidedWithBlock" : "func_149670_a", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V");
			if (methodNode != null)
			{
				this.trasnformOnEntityCollidedWithBlock(methodNode);
			} else {
				throw new RuntimeException("Could not find onEntityCollidedWithBlock method in " + transformedName);
			}
			//"onPostBlockPlaced", "(Lnet/minecraft/world/World;IIII)V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "onPostBlockPlaced" : "func_149714_e", "(Lnet/minecraft/world/World;IIII)V");
			if (methodNode != null)
			{
				this.transformOnPostBlockPlaced(methodNode);
			} else {
				throw new RuntimeException("Could not find onPostBlockPlaced method in " + transformedName);
			}
			//"onFallenUpon", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;F)V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "onFallenUpon" : "func_149746_a", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;F)V");
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

	public void transformOnBlockDestroyedByPlayer(final MethodNode method)
	{
		//BlockPhysics.onBlockDestroyedByPlayer(p_149664_1_, p_149664_2_, p_149664_3_, p_149664_4_, p_149664_5_, Block.blockRegistry.getNameForObject(this));
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in Block.onBlockDestroyedByPlayer");
		}

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 2));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 5));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/block/Block", !ObfHelper.isObfuscated() ? "blockRegistry" : "field_149771_c", "Lnet/minecraft/util/RegistryNamespaced;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/RegistryNamespaced", !ObfHelper.isObfuscated() ? "getNameForObject" : "func_148750_c", "(Ljava/lang/Object;)Ljava/lang/String;", false, this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "onBlockDestroyedByPlayer", "(Lnet/minecraft/world/World;IIIILjava/lang/String;)V", false, this));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformOnNeighborBlockChange(final MethodNode method)
	{
		//BlockPhysics.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, Block.blockRegistry.getNameForObject(this));
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in Block.onNeighborBlockChange");
		}

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 2));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/block/Block", !ObfHelper.isObfuscated() ? "blockRegistry" : "field_149771_c", "Lnet/minecraft/util/RegistryNamespaced;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/RegistryNamespaced", !ObfHelper.isObfuscated() ? "getNameForObject" : "func_148750_c", "(Ljava/lang/Object;)Ljava/lang/String;", false, this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "onNeighborBlockChange", "(Lnet/minecraft/world/World;IIILjava/lang/String;)V", false, this));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformOnEntityWalking(final MethodNode method)
	{
		//BlockPhysics.onNeighborBlockChange(p_149724_1_, p_149724_2_, p_149724_3_, p_149724_4_, Block.blockRegistry.getNameForObject(this));
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in Block.onEntityWalking");
		}

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 2));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/block/Block", !ObfHelper.isObfuscated() ? "blockRegistry" : "field_149771_c", "Lnet/minecraft/util/RegistryNamespaced;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/RegistryNamespaced", !ObfHelper.isObfuscated() ? "getNameForObject" : "func_148750_c", "(Ljava/lang/Object;)Ljava/lang/String;", false, this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "onNeighborBlockChange", "(Lnet/minecraft/world/World;IIILjava/lang/String;)V", false, this));

		method.instructions.insertBefore(target, toInject);
	}

	public void trasnformOnEntityCollidedWithBlock(final MethodNode method)
	{
		//BlockPhysics.onEntityCollidedWithBlock(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_, Block.blockRegistry.getNameForObject(this), p_149670_5_);
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in Block.onEntityCollidedWithBlock");
		}

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 2));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/block/Block", !ObfHelper.isObfuscated() ? "blockRegistry" : "field_149771_c", "Lnet/minecraft/util/RegistryNamespaced;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/RegistryNamespaced", !ObfHelper.isObfuscated() ? "getNameForObject" : "func_148750_c", "(Ljava/lang/Object;)Ljava/lang/String;", false, this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 5));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "onEntityCollidedWithBlock", "(Lnet/minecraft/world/World;IIILjava/lang/String;Lnet/minecraft/entity/Entity;)V", false, this));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformOnPostBlockPlaced(final MethodNode method)
	{
		//BlockPhysics.onPostBlockPlaced(p_149714_1_, p_149714_2_, p_149714_3_, p_149714_4_, Block.blockRegistry.getNameForObject(this), p_149714_5_);
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in Block.onPostBlockPlaced");
		}

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 2));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/block/Block", !ObfHelper.isObfuscated() ? "blockRegistry" : "field_149771_c", "Lnet/minecraft/util/RegistryNamespaced;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/RegistryNamespaced", !ObfHelper.isObfuscated() ? "getNameForObject" : "func_148750_c", "(Ljava/lang/Object;)Ljava/lang/String;", false, this));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 5));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "onPostBlockPlaced", "(Lnet/minecraft/world/World;IIILjava/lang/String;I)V", false, this));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformOnFallenUpon(final MethodNode method)
	{
		//BlockPhysics.onNeighborBlockChange(p_149724_1_, p_149724_2_, p_149724_3_, p_149724_4_, Block.blockRegistry.getNameForObject(this));
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in Block.onEntityWalking");
		}

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 2));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 3));
		toInject.add(new VarInsnNode(Opcodes.ILOAD, 4));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/block/Block", !ObfHelper.isObfuscated() ? "blockRegistry" : "field_149771_c", "Lnet/minecraft/util/RegistryNamespaced;", this));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/RegistryNamespaced", !ObfHelper.isObfuscated() ? "getNameForObject" : "func_148750_c", "(Ljava/lang/Object;)Ljava/lang/String;", false, this));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "onNeighborBlockChange", "(Lnet/minecraft/world/World;IIILjava/lang/String;)V", false, this));

		method.instructions.insertBefore(target, toInject);
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0) {}
}