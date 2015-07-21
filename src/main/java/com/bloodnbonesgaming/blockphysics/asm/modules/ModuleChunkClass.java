package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;
import squeek.asmhelper.com.bloodnbonesgaming.lib.ObfHelper;

import com.bloodnbonesgaming.lib.core.ASMAdditionRegistry;
import com.bloodnbonesgaming.lib.core.insn.RedirectedMethodVisitor;
import com.bloodnbonesgaming.lib.core.module.IClassTransformerModule;

public class ModuleChunkClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
				"net.minecraft.world.chunk.Chunk"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformChunkClass";
	}

	@Override
	public boolean canBeDisabled()
	{
		return false;
	}

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] bytes)
	{
		return bytes;
	}

	public void createSetBlockBPdata(final ClassNode classNode)
	{
		//public boolean setBlockBPdata(int par1, int par2, int par3, int par4)
		//{
		//	ExtendedBlockStorage blockStorage = this.storageArrays[par2 >> 4];
		//
		//	if (blockStorage == null)
		//	{
		//		return false;
		//	}
		//	int blockBPdata = blockStorage.getBlockBPdata(par1, par2 & 15, par3);
		//
		//	if (blockBPdata == par4)
		//	{
		//		return false;
		//	}
		//	this.isModified = true;
		//	blockStorage.setBlockBPdata(par1, par2 & 15, par3, par4);
		//	return true;
		//}

		final MethodVisitor methodVisitor = classNode.visitMethod(Opcodes.ACC_PUBLIC, "setBlockBPdata", "(IIII)Z", null, null);
		final RedirectedMethodVisitor rMethodVisitor = new RedirectedMethodVisitor(methodVisitor);

		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
		rMethodVisitor.visitRedirectedFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/chunk/Chunk", !ObfHelper.isObfuscated() ? "storageArrays" : "field_76652_q", "[Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;", this);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 2);
		methodVisitor.visitInsn(Opcodes.ICONST_4);
		methodVisitor.visitInsn(Opcodes.ISHR);
		methodVisitor.visitInsn(Opcodes.AALOAD);
		methodVisitor.visitVarInsn(Opcodes.ASTORE, 5);
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 5);
		final Label label1 = new Label();
		methodVisitor.visitJumpInsn(Opcodes.IFNONNULL, label1);
		methodVisitor.visitInsn(Opcodes.ICONST_0);
		methodVisitor.visitInsn(Opcodes.IRETURN);
		methodVisitor.visitLabel(label1);
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 5);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 1);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 2);
		methodVisitor.visitIntInsn(Opcodes.BIPUSH, 15);
		methodVisitor.visitInsn(Opcodes.IAND);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 3);
		rMethodVisitor.visitRedirectedMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "getBlockBPdata", "(III)I", false, this);
		methodVisitor.visitVarInsn(Opcodes.ISTORE, 6);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 6);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 4);
		final Label label2 = new Label();
		methodVisitor.visitJumpInsn(Opcodes.IF_ICMPNE, label2);
		methodVisitor.visitInsn(Opcodes.ICONST_0);
		methodVisitor.visitInsn(Opcodes.IRETURN);
		methodVisitor.visitLabel(label2);
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
		methodVisitor.visitInsn(Opcodes.ICONST_1);
		rMethodVisitor.visitRedirectedFieldInsn(Opcodes.PUTFIELD, "net/minecraft/world/chunk/Chunk", !ObfHelper.isObfuscated() ? "isModified" : "field_76643_l", "Z", this);
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 5);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 1);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 2);
		methodVisitor.visitIntInsn(Opcodes.BIPUSH, 15);
		methodVisitor.visitInsn(Opcodes.IAND);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 3);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 4);
		rMethodVisitor.visitRedirectedMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "setBlockBPdata", "(IIII)V", false, this);
		methodVisitor.visitInsn(Opcodes.ICONST_1);
		methodVisitor.visitInsn(Opcodes.IRETURN);
		methodVisitor.visitMaxs(5, 7);
		methodVisitor.visitEnd();
	}

	public void createGetBlockBPdata(final ClassNode classNode)
	{
		//public int getBlockBPdata(int par1, int par2, int par3)
		//{
		//	if (par2 >> 4 >= this.storageArrays.length)
		//	{
		//		return 0;
		//	}
		//
		//	ExtendedBlockStorage blockStorage = this.storageArrays[par2 >> 4];
		//
		//	if (blockStorage != null)
		//	{
		//		return blockStorage.getBlockBPdata(par1, par2 & 15, par3);
		//	}
		//	else
		//	{
		//		return 0;
		//	}
		//}

		final MethodVisitor methodVisitor = classNode.visitMethod(Opcodes.ACC_PUBLIC, "getBlockBPdata", "(III)I", null, null);
		final RedirectedMethodVisitor rMethodVisitor = new RedirectedMethodVisitor(methodVisitor);

		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 2);
		methodVisitor.visitInsn(Opcodes.ICONST_4);
		methodVisitor.visitInsn(Opcodes.ISHR);
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
		rMethodVisitor.visitRedirectedFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/chunk/Chunk", !ObfHelper.isObfuscated() ? "storageArrays" : "field_76652_q", "[Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;", this);
		methodVisitor.visitInsn(Opcodes.ARRAYLENGTH);
		final Label l1 = new Label();
		methodVisitor.visitJumpInsn(Opcodes.IF_ICMPLT, l1);
		methodVisitor.visitInsn(Opcodes.ICONST_0);
		methodVisitor.visitInsn(Opcodes.IRETURN);
		methodVisitor.visitLabel(l1);
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
		rMethodVisitor.visitRedirectedFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/chunk/Chunk", !ObfHelper.isObfuscated() ? "storageArrays" : "field_76652_q", "[Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;", this);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 2);
		methodVisitor.visitInsn(Opcodes.ICONST_4);
		methodVisitor.visitInsn(Opcodes.ISHR);
		methodVisitor.visitInsn(Opcodes.AALOAD);
		methodVisitor.visitVarInsn(Opcodes.ASTORE, 4);
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 4);
		final Label l4 = new Label();
		methodVisitor.visitJumpInsn(Opcodes.IFNULL, l4);
		methodVisitor.visitVarInsn(Opcodes.ALOAD, 4);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 1);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 2);
		methodVisitor.visitIntInsn(Opcodes.BIPUSH, 15);
		methodVisitor.visitInsn(Opcodes.IAND);
		methodVisitor.visitVarInsn(Opcodes.ILOAD, 3);
		rMethodVisitor.visitRedirectedMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "getBlockBPdata", "(III)I", false, this);
		methodVisitor.visitInsn(Opcodes.IRETURN);
		methodVisitor.visitLabel(l4);
		methodVisitor.visitInsn(Opcodes.ICONST_0);
		methodVisitor.visitInsn(Opcodes.IRETURN);
		methodVisitor.visitMaxs(4, 5);
		methodVisitor.visitEnd();
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry registry) {
		final ClassNode classNode = new ClassNode();

		this.createGetBlockBPdata(classNode);
		this.createSetBlockBPdata(classNode);

		registry.registerMethodAddition("net/minecraft/world/chunk/Chunk", ASMHelper.findMethodNodeOfClass(classNode, "getBlockBPdata", "(III)I"));
		registry.registerMethodAddition("net/minecraft/world/chunk/Chunk", ASMHelper.findMethodNodeOfClass(classNode, "setBlockBPdata", "(IIII)Z"));
	}
}