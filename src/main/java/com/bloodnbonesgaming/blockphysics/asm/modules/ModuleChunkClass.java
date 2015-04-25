package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.AALOAD;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARRAYLENGTH;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.IAND;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_4;
import static org.objectweb.asm.Opcodes.IFNONNULL;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.IF_ICMPLT;
import static org.objectweb.asm.Opcodes.IF_ICMPNE;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.ISHR;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.PUTFIELD;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;

import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.insn.RedirectedMethodVisitor;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

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
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		return bytes;
	}
	
	public void createSetBlockBPdata(ClassNode classNode)
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
		
		MethodVisitor methodVisitor = classNode.visitMethod(ACC_PUBLIC, "setBlockBPdata", "(IIII)Z", null, null);
		RedirectedMethodVisitor rMethodVisitor = new RedirectedMethodVisitor(methodVisitor);
		
		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(ALOAD, 0);
		rMethodVisitor.visitRedirectedFieldInsn(GETFIELD, "net/minecraft/world/chunk/Chunk", "field_76652_q", "[Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;", this);
		methodVisitor.visitVarInsn(ILOAD, 2);
		methodVisitor.visitInsn(ICONST_4);
		methodVisitor.visitInsn(ISHR);
		methodVisitor.visitInsn(AALOAD);
		methodVisitor.visitVarInsn(ASTORE, 5);
		methodVisitor.visitVarInsn(ALOAD, 5);
		Label label1 = new Label();
		methodVisitor.visitJumpInsn(IFNONNULL, label1);
		methodVisitor.visitInsn(ICONST_0);
		methodVisitor.visitInsn(IRETURN);
		methodVisitor.visitLabel(label1);
		methodVisitor.visitVarInsn(ALOAD, 5);
		methodVisitor.visitVarInsn(ILOAD, 1);
		methodVisitor.visitVarInsn(ILOAD, 2);
		methodVisitor.visitIntInsn(BIPUSH, 15);
		methodVisitor.visitInsn(IAND);
		methodVisitor.visitVarInsn(ILOAD, 3);
		rMethodVisitor.visitRedirectedMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "getBlockBPdata", "(III)I", false, this);
		methodVisitor.visitVarInsn(ISTORE, 6);
		methodVisitor.visitVarInsn(ILOAD, 6);
		methodVisitor.visitVarInsn(ILOAD, 4);
		Label label2 = new Label();
		methodVisitor.visitJumpInsn(IF_ICMPNE, label2);
		methodVisitor.visitInsn(ICONST_0);
		methodVisitor.visitInsn(IRETURN);
		methodVisitor.visitLabel(label2);
		methodVisitor.visitVarInsn(ALOAD, 0);
		methodVisitor.visitInsn(ICONST_1);
		rMethodVisitor.visitRedirectedFieldInsn(PUTFIELD, "net/minecraft/world/chunk/Chunk", "field_76643_l", "Z", this);
		methodVisitor.visitVarInsn(ALOAD, 5);
		methodVisitor.visitVarInsn(ILOAD, 1);
		methodVisitor.visitVarInsn(ILOAD, 2);
		methodVisitor.visitIntInsn(BIPUSH, 15);
		methodVisitor.visitInsn(IAND);
		methodVisitor.visitVarInsn(ILOAD, 3);
		methodVisitor.visitVarInsn(ILOAD, 4);
		rMethodVisitor.visitRedirectedMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "setBlockBPdata", "(IIII)V", false, this);
		methodVisitor.visitInsn(ICONST_1);
		methodVisitor.visitInsn(IRETURN);
		methodVisitor.visitMaxs(5, 7);
		methodVisitor.visitEnd();
	}
	
	public void createGetBlockBPdata(ClassNode classNode)
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
		
		MethodVisitor methodVisitor = classNode.visitMethod(ACC_PUBLIC, "getBlockBPdata", "(III)I", null, null);
		RedirectedMethodVisitor rMethodVisitor = new RedirectedMethodVisitor(methodVisitor);
		
		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(ILOAD, 2);
		methodVisitor.visitInsn(ICONST_4);
		methodVisitor.visitInsn(ISHR);
		methodVisitor.visitVarInsn(ALOAD, 0);
		rMethodVisitor.visitRedirectedFieldInsn(GETFIELD, "net/minecraft/world/chunk/Chunk", "field_76652_q", "[Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;", this);
		methodVisitor.visitInsn(ARRAYLENGTH);
		Label l1 = new Label();
		methodVisitor.visitJumpInsn(IF_ICMPLT, l1);
		methodVisitor.visitInsn(ICONST_0);
		methodVisitor.visitInsn(IRETURN);
		methodVisitor.visitLabel(l1);
		methodVisitor.visitVarInsn(ALOAD, 0);
		rMethodVisitor.visitRedirectedFieldInsn(GETFIELD, "net/minecraft/world/chunk/Chunk", "field_76652_q", "[Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;", this);
		methodVisitor.visitVarInsn(ILOAD, 2);
		methodVisitor.visitInsn(ICONST_4);
		methodVisitor.visitInsn(ISHR);
		methodVisitor.visitInsn(AALOAD);
		methodVisitor.visitVarInsn(ASTORE, 4);
		methodVisitor.visitVarInsn(ALOAD, 4);
		Label l4 = new Label();
		methodVisitor.visitJumpInsn(IFNULL, l4);
		methodVisitor.visitVarInsn(ALOAD, 4);
		methodVisitor.visitVarInsn(ILOAD, 1);
		methodVisitor.visitVarInsn(ILOAD, 2);
		methodVisitor.visitIntInsn(BIPUSH, 15);
		methodVisitor.visitInsn(IAND);
		methodVisitor.visitVarInsn(ILOAD, 3);
		rMethodVisitor.visitRedirectedMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "blockphysctgetBlockBPdata", "(III)I", false, this);
		methodVisitor.visitInsn(IRETURN);
		methodVisitor.visitLabel(l4);
		methodVisitor.visitInsn(ICONST_0);
		methodVisitor.visitInsn(IRETURN);
		methodVisitor.visitMaxs(4, 5);
		methodVisitor.visitEnd();
	}

	@Override
	public void registerAdditions(ASMAdditionRegistry registry) {
		ClassNode classNode = new ClassNode();
		
		createGetBlockBPdata(classNode);
		createSetBlockBPdata(classNode);
		
		registry.registerMethodAddition("net/minecraft/world/chunk/Chunk", ASMHelper.findMethodNodeOfClass(classNode, "getBlockBPdata", "(III)I"));
		registry.registerMethodAddition("net/minecraft/world/chunk/Chunk", ASMHelper.findMethodNodeOfClass(classNode, "setBlockBPdata", "(IIII)Z"));
	}
}