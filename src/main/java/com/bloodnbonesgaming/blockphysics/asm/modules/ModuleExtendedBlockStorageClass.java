package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.BALOAD;
import static org.objectweb.asm.Opcodes.BASTORE;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.I2B;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.IMUL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.NEWARRAY;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.SIPUSH;
import static org.objectweb.asm.Opcodes.T_BYTE;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.insn.RedirectedFieldInsnNode;
import com.bnbgaming.lib.core.insn.RedirectedMethodVisitor;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

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
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		ClassNode classNode = ASMHelper.readClassFromBytes(bytes);
		
		if (transformedName.equals("net.minecraft.world.chunk.storage.ExtendedBlockStorage"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);
			
			//<init>(IZ)V
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(IZ)V");
            if (methodNode != null)
            {
            	transformInit(methodNode);
            }
            else
                throw new RuntimeException("Could not find <init>(IZ)V method in " + transformedName);
            
            
            return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}
	
	public void createSetBlockBPdata(ClassNode classNode)
	{
		//public void setBlockBPdata(int par1, int par2, int par3, int par4) 
		//{
		//	this.blockBPdataArray[par2 * 256 + par1 * 16 + par3] = (byte)par4; 
		//}
		
		MethodVisitor methodVisitor = classNode.visitMethod(ACC_PUBLIC, "setBlockBPdata", "(IIII)V", null, null);
		RedirectedMethodVisitor rMethodVisitor = new RedirectedMethodVisitor(methodVisitor);

		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(ALOAD, 0);
		rMethodVisitor.visitRedirectedFieldInsn(GETFIELD, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "blockBPdataArray", "[B", this);
		methodVisitor.visitVarInsn(ILOAD, 2);
		methodVisitor.visitIntInsn(SIPUSH, 256);
		methodVisitor.visitInsn(IMUL);
		methodVisitor.visitVarInsn(ILOAD, 1);
		methodVisitor.visitIntInsn(BIPUSH, 16);
		methodVisitor.visitInsn(IMUL);
		methodVisitor.visitInsn(IADD);
		methodVisitor.visitVarInsn(ILOAD, 3);
		methodVisitor.visitInsn(IADD);
		methodVisitor.visitVarInsn(ILOAD, 4);
		methodVisitor.visitInsn(I2B);
		methodVisitor.visitInsn(BASTORE);
		methodVisitor.visitInsn(RETURN);
		methodVisitor.visitMaxs(4, 5);
		methodVisitor.visitEnd();
	}
	
	public void createGetBlockBPdata(ClassNode classNode)
	{
		//public int getBlockBPdata(int par1, int par2, int par3)
		//{
		//	return this.blockBPdataArray[par2 * 256 + par1 * 16 + par3]; 
		//}
		
		MethodVisitor methodVisitor = classNode.visitMethod(ACC_PUBLIC, "getBlockBPdata", "(III)I", null, null);
		RedirectedMethodVisitor rMethodVisitor = new RedirectedMethodVisitor(methodVisitor);

		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(ALOAD, 0);
		rMethodVisitor.visitRedirectedFieldInsn(GETFIELD, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "blockBPdataArray", "[B", this);
		methodVisitor.visitVarInsn(ILOAD, 2);
		methodVisitor.visitIntInsn(SIPUSH, 256);
		methodVisitor.visitInsn(IMUL);
		methodVisitor.visitVarInsn(ILOAD, 1);
		methodVisitor.visitIntInsn(BIPUSH, 16);
		methodVisitor.visitInsn(IMUL);
		methodVisitor.visitInsn(IADD);
		methodVisitor.visitVarInsn(ILOAD, 3);
		methodVisitor.visitInsn(IADD);
		methodVisitor.visitInsn(BALOAD);
		methodVisitor.visitInsn(IRETURN);
		methodVisitor.visitMaxs(4, 4);
		methodVisitor.visitEnd();
	}
	
	public void createSetBPdataArray(ClassNode classNode)
	{
		//public void setBPdataArray(byte[] bytes)
		//{
		//	this.blockBPdataArray = bytes;
		//}
		
		MethodVisitor methodVisitor = classNode.visitMethod(ACC_PUBLIC, "setBPdataArray", "([B)V", null, null);
		RedirectedMethodVisitor rMethodVisitor = new RedirectedMethodVisitor(methodVisitor);

		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(ALOAD, 0);
		methodVisitor.visitVarInsn(ALOAD, 1);
		rMethodVisitor.visitRedirectedFieldInsn(PUTFIELD, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "blockBPdataArray", "[B", this);
		methodVisitor.visitInsn(RETURN);
		methodVisitor.visitMaxs(2, 2);
		methodVisitor.visitEnd();
	}
	
	public void createGetBPdataArray(ClassNode classNode)
	{
		//public byte[] getBPdataArray()
		//{
		//	return this.blockBPdataArray;
		//}
		
		MethodVisitor methodVisitor = classNode.visitMethod(ACC_PUBLIC, "getBPdataArray", "()[B", null, null);
		RedirectedMethodVisitor rMethodVisitor = new RedirectedMethodVisitor(methodVisitor);

		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(ALOAD, 0);
		rMethodVisitor.visitRedirectedFieldInsn(GETFIELD, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "blockBPdataArray", "[B", this);
		methodVisitor.visitInsn(ARETURN);
		methodVisitor.visitMaxs(1, 1);
		methodVisitor.visitEnd();
	}
	
	public void transformInit(MethodNode method)
	{
		//Method end
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in ExtendedBlockStorage.<init>");
		
		//this.blockBPdataArray = new byte[4096];
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new IntInsnNode(SIPUSH, 4096));
		toInject.add(new IntInsnNode(NEWARRAY, T_BYTE));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "blockBPdataArray", "[B", this));
		
		method.instructions.insertBefore(target, toInject);
	}

	@Override
	public void registerAdditions(ASMAdditionRegistry registry) {
		registry.registerFieldAddition("net/minecraft/world/chunk/storage/ExtendedBlockStorage", new FieldNode(ACC_PRIVATE, "blockBPdataArray", "[B", null, null));
		
		ClassNode classNode = new ClassNode();
		createSetBlockBPdata(classNode);
        createGetBlockBPdata(classNode);
        createSetBPdataArray(classNode);
		createGetBPdataArray(classNode);
		
		registry.registerMethodAddition("net/minecraft/world/chunk/storage/ExtendedBlockStorage", ASMHelper.findMethodNodeOfClass(classNode, "getBlockBPdata", "(III)I"));
		registry.registerMethodAddition("net/minecraft/world/chunk/storage/ExtendedBlockStorage", ASMHelper.findMethodNodeOfClass(classNode, "setBlockBPdata", "(IIII)V"));
		registry.registerMethodAddition("net/minecraft/world/chunk/storage/ExtendedBlockStorage", ASMHelper.findMethodNodeOfClass(classNode, "getBPdataArray", "()[B"));
		registry.registerMethodAddition("net/minecraft/world/chunk/storage/ExtendedBlockStorage", ASMHelper.findMethodNodeOfClass(classNode, "setBPdataArray", "([B)V"));
	}
}