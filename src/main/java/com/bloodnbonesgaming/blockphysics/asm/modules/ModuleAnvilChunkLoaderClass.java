package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.AALOAD;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEWARRAY;
import static org.objectweb.asm.Opcodes.SIPUSH;
import static org.objectweb.asm.Opcodes.T_BYTE;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleAnvilChunkLoaderClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.world.chunk.storage.AnvilChunkLoader"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformAnvilChunkLoaderClass";
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
		
		if (transformedName.equals("net.minecraft.world.chunk.storage.AnvilChunkLoader"))
		{
			ModInfo.Log.info("Transforming class: " + transformedName);
			//writeChunkToNBT
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_75820_a", "(Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)V");
            if (methodNode != null)
            {
            	transformWriteChunkToNBT(methodNode);
            }
            else
                throw new RuntimeException("Could not find writeChunkToNBT method in " + transformedName);
            
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_75823_a", "(Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/world/chunk/Chunk;");
            if (methodNode != null)
            {
            	transformReadChunkFromNBT(methodNode);
            }
            else
            	throw new RuntimeException("Could not find readChunkFromNBT method in " + transformedName);
            
            return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}
	
	public void transformWriteChunkToNBT(MethodNode method)
	{
		//nbttaglist.appendTag(nbttagcompound1);
		//InsnList toFind = new InsnList();
		//toFind.add(new VarInsnNode(ALOAD, 5));
		//toFind.add(new VarInsnNode(ALOAD, 9));
		AbstractInsnNode appendTag = ASMHelper.find(method.instructions, new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagList", "func_74742_a", "(Lnet/minecraft/nbt/NBTBase;)V", false));
		VarInsnNode nbtTagCompound = (VarInsnNode) ASMHelper.move(appendTag, -1);
		AbstractInsnNode target = ASMHelper.move(appendTag, -2);
		
		VarInsnNode extendedBlockStorage = (VarInsnNode) ASMHelper.move(ASMHelper.find(method.instructions, new InsnNode(AALOAD)), 1);
		
		if (appendTag == null || nbtTagCompound == null || target == null)
			throw new RuntimeException("Unexpected instruction pattern in AnvilChunkLoader.writeChunkToNBT");
		
		//nbttagcompound1.setByteArray("BPData", extendedblockstorage.getBPdataArray());
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, nbtTagCompound.var));
		toInject.add(new LdcInsnNode("BPData"));
		toInject.add(new VarInsnNode(ALOAD, extendedBlockStorage.var));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "getBPdataArray", "()[B", false));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74773_a", "(Ljava/lang/String;[B)V", false));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformReadChunkFromNBT(MethodNode method)
	{
		//extendedblockstorage.removeInvalidBlocks();
		InsnList toFind = new InsnList();
		toFind.add(new VarInsnNode(ALOAD, 13));
		toFind.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "func_76672_e", "()V", false));
		
		AbstractInsnNode target = ASMHelper.find(method.instructions, toFind);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in AnvilChunkLoader.readChunkFromNBT");
		
		//if (nbttagcompound1.hasKey("BPData"))
		//{
		//	extendedblockstorage.setBPdataArray(nbttagcompound1.getByteArray("BPData"));
		//}
		//else
		//{
		//	extendedblockstorage.setBPdataArray(new byte[4096]);
		//}
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 11));
		toInject.add(new LdcInsnNode("BPData"));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74764_b", "(Ljava/lang/String;)Z", false));
		LabelNode label1 = new LabelNode();
		toInject.add(new JumpInsnNode(IFEQ, label1));
		toInject.add(new VarInsnNode(ALOAD, 13));
		toInject.add(new VarInsnNode(ALOAD, 11));
		toInject.add(new LdcInsnNode(("BPData")));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74770_j", "(Ljava/lang/String;)[B", false));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "setBPdataArray", "([B)V", false));
		LabelNode label2 = new LabelNode();
		toInject.add(new JumpInsnNode(GOTO, label2));
		toInject.add(label1);
		toInject.add(new VarInsnNode(ALOAD, 13));
		toInject.add(new IntInsnNode(SIPUSH, 4096));
		toInject.add(new IntInsnNode(NEWARRAY, T_BYTE));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "setBPdataArray", "([B)V", false));
		toInject.add(label2);
		
		method.instructions.insertBefore(target, toInject);
	}
}