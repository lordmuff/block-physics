package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bloodnbonesgaming.lib.core.ASMAdditionRegistry;
import com.bloodnbonesgaming.lib.core.insn.RedirectedFieldInsnNode;
import com.bloodnbonesgaming.lib.core.insn.RedirectedMethodVisitor;
import com.bloodnbonesgaming.lib.core.module.IClassTransformerModule;

public class ModuleExtendedBlockStorageClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
				"net.minecraft.world.chunk.storage.ExtendedBlockStorage"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformExtendedBlockStorageClass";
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

		if (transformedName.equals("net.minecraft.world.chunk.storage.ExtendedBlockStorage"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);

			this.createSetBlockBPdata(classNode);
			this.createGetBlockBPdata(classNode);

			//<init>(IZ)V
			final MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(IZ)V");
			if (methodNode != null)
			{
				this.transformInit(methodNode);
			} else {
				throw new RuntimeException("Could not find <init>(IZ)V method in " + transformedName);
			}


			return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}

	public void createSetBlockBPdata(final ClassNode classNode)
	{
		//public void setBlockBPdata(int par1, int par2, int par3, int par4)
		//{
		//	this.blockBPdataArray[par2 * 256 + par1 * 16 + par3] = (byte)par4;
		//}

		final MethodVisitor methodVisitor = classNode.visitMethod(Opcodes.ACC_PUBLIC, "setBlockBPdata", "(IIII)V", null, null);
		final RedirectedMethodVisitor rMethodVisitor = new RedirectedMethodVisitor(methodVisitor);

		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
		rMethodVisitor.visitRedirectedFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "blockBPdataArray", "[B", this);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 2);
		methodVisitor.visitIntInsn(Opcodes.SIPUSH, 256);
		methodVisitor.visitInsn(Opcodes.IMUL);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 1);
		methodVisitor.visitIntInsn(Opcodes.BIPUSH, 16);
		methodVisitor.visitInsn(Opcodes.IMUL);
		methodVisitor.visitInsn(Opcodes.IADD);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 3);
		methodVisitor.visitInsn(Opcodes.IADD);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 4);
		methodVisitor.visitInsn(Opcodes.I2B);
		methodVisitor.visitInsn(Opcodes.BASTORE);
		methodVisitor.visitInsn(Opcodes.RETURN);
		methodVisitor.visitMaxs(4, 5);
		methodVisitor.visitEnd();
	}

	public void createGetBlockBPdata(final ClassNode classNode)
	{
		//public int getBlockBPdata(int par1, int par2, int par3)
		//{
		//	return this.blockBPdataArray[par2 * 256 + par1 * 16 + par3];
		//}

		final MethodVisitor methodVisitor = classNode.visitMethod(Opcodes.ACC_PUBLIC, "getBlockBPdata", "(III)I", null, null);
		final RedirectedMethodVisitor rMethodVisitor = new RedirectedMethodVisitor(methodVisitor);

		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
		rMethodVisitor.visitRedirectedFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "blockBPdataArray", "[B", this);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 2);
		methodVisitor.visitIntInsn(Opcodes.SIPUSH, 256);
		methodVisitor.visitInsn(Opcodes.IMUL);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 1);
		methodVisitor.visitIntInsn(Opcodes.BIPUSH, 16);
		methodVisitor.visitInsn(Opcodes.IMUL);
		methodVisitor.visitInsn(Opcodes.IADD);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 3);
		methodVisitor.visitInsn(Opcodes.IADD);
		methodVisitor.visitInsn(Opcodes.BALOAD);
		methodVisitor.visitInsn(Opcodes.IRETURN);
		methodVisitor.visitMaxs(4, 4);
		methodVisitor.visitEnd();
	}

	public void createSetBPdataArray(final ClassNode classNode)
	{
		//public void setBPdataArray(byte[] bytes)
		//{
		//	this.blockBPdataArray = bytes;
		//}

		final MethodVisitor methodVisitor = classNode.visitMethod(Opcodes.ACC_PUBLIC, "setBPdataArray", "([B)V", null, null);
		final RedirectedMethodVisitor rMethodVisitor = new RedirectedMethodVisitor(methodVisitor);

		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 1);
		rMethodVisitor.visitRedirectedFieldInsn(Opcodes.PUTFIELD, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "blockBPdataArray", "[B", this);
		methodVisitor.visitInsn(Opcodes.RETURN);
		methodVisitor.visitMaxs(2, 2);
		methodVisitor.visitEnd();
	}

	public void createGetBPdataArray(final ClassNode classNode)
	{
		//public byte[] getBPdataArray()
		//{
		//	return this.blockBPdataArray;
		//}

		final MethodVisitor methodVisitor = classNode.visitMethod(Opcodes.ACC_PUBLIC, "getBPdataArray", "()[B", null, null);
		final RedirectedMethodVisitor rMethodVisitor = new RedirectedMethodVisitor(methodVisitor);

		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
		rMethodVisitor.visitRedirectedFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "blockBPdataArray", "[B", this);
		methodVisitor.visitInsn(Opcodes.ARETURN);
		methodVisitor.visitMaxs(1, 1);
		methodVisitor.visitEnd();
	}

	public void transformInit(final MethodNode method)
	{
		//Method end
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in ExtendedBlockStorage.<init>");
		}

		//this.blockBPdataArray = new byte[4096];
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new IntInsnNode(Opcodes.SIPUSH, 4096));
		toInject.add(new IntInsnNode(Opcodes.NEWARRAY, Opcodes.T_BYTE));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "blockBPdataArray", "[B", this));

		method.instructions.insertBefore(target, toInject);
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry registry) {
		registry.registerFieldAddition("net/minecraft/world/chunk/storage/ExtendedBlockStorage", new FieldNode(Opcodes.ACC_PRIVATE, "blockBPdataArray", "[B", null, null));

		final ClassNode classNode = new ClassNode();

		this.createSetBPdataArray(classNode);
		this.createGetBPdataArray(classNode);

		//registry.registerMethodAddition("net/minecraft/world/chunk/storage/ExtendedBlockStorage", ASMHelper.findMethodNodeOfClass(classNode, "getBlockBPdata", "(III)I"));
		//registry.registerMethodAddition("net/minecraft/world/chunk/storage/ExtendedBlockStorage", ASMHelper.findMethodNodeOfClass(classNode, "setBlockBPdata", "(IIII)V"));
		registry.registerMethodAddition("net/minecraft/world/chunk/storage/ExtendedBlockStorage", ASMHelper.findMethodNodeOfClass(classNode, "getBPdataArray", "()[B"));
		registry.registerMethodAddition("net/minecraft/world/chunk/storage/ExtendedBlockStorage", ASMHelper.findMethodNodeOfClass(classNode, "setBPdataArray", "([B)V"));
	}
}