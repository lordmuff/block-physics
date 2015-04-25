package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.I2B;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.insn.RedirectedFieldInsnNode;
import com.bnbgaming.lib.core.insn.RedirectedMethodInsnNode;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleTileEntityPistonClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.tileentity.TileEntityPiston"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformTileEntityPistonClass";
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
		
		if (transformedName.equals("net.minecraft.tileentity.TileEntityPiston"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);
			
			//"<init>", "(Lnet/minecraft/block/Block;IIZZ)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/block/Block;IIZZ)V");
            if (methodNode != null)
            {
            	transformInit(methodNode);
            }
            else
                throw new RuntimeException("Could not find <init> method in " + transformedName);
            
            //"clearPistonTileEntity", "()V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_145866_f", "()V");
            if (methodNode != null)
            {
            	transformClearPistonTileEntity(methodNode);
            }
            else
                throw new RuntimeException("Could not find clearPistonTileEntity method in " + transformedName);
            
            //"updateEntity", "()V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_145845_h", "()V");
            if (methodNode != null)
            {
            	transformUpdateEntity(methodNode);
            }
            else
                throw new RuntimeException("Could not find updateEntity method in " + transformedName);
            
            //"readFromNBT", "(Lnet/minecraft/nbt/NBTTagCompound;)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_145839_a", "(Lnet/minecraft/nbt/NBTTagCompound;)V");
            if (methodNode != null)
            {
            	transformReadFromNBT(methodNode);
            }
            else
                throw new RuntimeException("Could not find readFromNBT method in " + transformedName);
            
            //"writeToNBT", "(Lnet/minecraft/nbt/NBTTagCompound;)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_145841_b", "(Lnet/minecraft/nbt/NBTTagCompound;)V");
            if (methodNode != null)
            {
            	transformWriteToNBT(methodNode);
            }
            else
                throw new RuntimeException("Could not find writeToNBT method in " + transformedName);
            
            return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}
	
	public void transformInit(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ACONST_NULL));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/tileentity/TileEntityPiston", "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", this));
		
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ICONST_0));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/tileentity/TileEntityPiston", "bpmeta", "I", this));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformClearPistonTileEntity(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.find(method.instructions, new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/World", "func_147460_e", "(IIILnet/minecraft/block/Block;)V", false));
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145850_b", "Lnet/minecraft/world/World;", this));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145851_c", "I", this));
		toInject.add(new VarInsnNode(ALOAD, 0));;
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145848_d", "I", this));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145849_e", "I", this));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "bpmeta", "I", this));
		toInject.add(new RedirectedMethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "setBlockBPdata", "(Lnet/minecraft/world/World;IIII)Z", false, this));
		toInject.add(new InsnNode(POP));
		
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", this));
		LabelNode label1 = new LabelNode();
		toInject.add(new JumpInsnNode(IFNULL, label1));
		
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145869_a", "Lnet/minecraft/block/Block;", this));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145850_b", "Lnet/minecraft/world/World;", this));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145876_i", "I", this));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/block/Block", "createTileEntity", "(Lnet/minecraft/world/World;I)Lnet/minecraft/tileentity/TileEntity;", false, this));
		toInject.add(new VarInsnNode(ASTORE, 1));
		
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", this));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/tileentity/TileEntity", "func_145839_a", "(Lnet/minecraft/nbt/NBTTagCompound;)V", false, this));
		
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145850_b", "Lnet/minecraft/world/World;", this));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145851_c", "I", this));
		toInject.add(new VarInsnNode(ALOAD, 0));;
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145848_d", "I", this));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145849_e", "I", this));
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/World", "func_147455_a", "(IIILnet/minecraft/tileentity/TileEntity;)V", false, this));
		toInject.add(label1);
		
		method.instructions.insert(target, toInject);
	}
	
	public void transformUpdateEntity(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.find(method.instructions, new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/World", "func_147460_e", "(IIILnet/minecraft/block/Block;)V", false));
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145850_b", "Lnet/minecraft/world/World;", this));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145851_c", "I", this));
		toInject.add(new VarInsnNode(ALOAD, 0));;
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145848_d", "I", this));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145849_e", "I", this));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "bpmeta", "I", this));
		toInject.add(new RedirectedMethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "setBlockBPdata", "(Lnet/minecraft/world/World;IIII)Z", false, this));
		toInject.add(new InsnNode(POP));
		
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", this));
		LabelNode label1 = new LabelNode();
		toInject.add(new JumpInsnNode(IFNULL, label1));
		
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145869_a", "Lnet/minecraft/block/Block;", this));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145850_b", "Lnet/minecraft/world/World;", this));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145876_i", "I", this));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/block/Block", "createTileEntity", "(Lnet/minecraft/world/World;I)Lnet/minecraft/tileentity/TileEntity;", false, this));
		toInject.add(new VarInsnNode(ASTORE, 1));
		
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", this));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/tileentity/TileEntity", "func_145839_a", "(Lnet/minecraft/nbt/NBTTagCompound;)V", false, this));
		
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145850_b", "Lnet/minecraft/world/World;", this));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145851_c", "I", this));
		toInject.add(new VarInsnNode(ALOAD, 0));;
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145848_d", "I", this));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "field_145849_e", "I", this));
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/World", "func_147455_a", "(IIILnet/minecraft/tileentity/TileEntity;)V", false, this));
		toInject.add(label1);
		
		method.instructions.insert(target, toInject);
	}
	
	public void transformReadFromNBT(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new LdcInsnNode("BPData"));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74764_b", "(Ljava/lang/String;)Z", false, this));
		LabelNode label1 = new LabelNode();
		toInject.add(new JumpInsnNode(IFEQ, label1));
		
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new LdcInsnNode("BPData"));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74771_c", "(Ljava/lang/String;)B", false, this));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/tileentity/TileEntityPiston", "bpmeta", "I", this));
		toInject.add(label1);
		
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new LdcInsnNode("TileEntityData"));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74764_b", "(Ljava/lang/String;)Z", false, this));
		LabelNode label2 = new LabelNode();
		toInject.add(new JumpInsnNode(IFEQ, label2));
		
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new LdcInsnNode("TileEntityData"));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74775_l", "(Ljava/lang/String;)Lnet/minecraft/nbt/NBTTagCompound;", false, this));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/tileentity/TileEntityPiston", "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", this));
		toInject.add(label2);
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformWriteToNBT(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new LdcInsnNode("BPData"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "bpmeta", "I", this));
		toInject.add(new InsnNode(I2B));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74774_a", "(Ljava/lang/String;B)V", false, this));
		
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", this));
		LabelNode label1 = new LabelNode();
		toInject.add(new JumpInsnNode(IFNULL, label1));
		
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new LdcInsnNode("TileEntityData"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityPiston", "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", this));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74782_a", "(Ljava/lang/String;Lnet/minecraft/nbt/NBTBase;)V", false, this));
		toInject.add(label1);
		
		method.instructions.insertBefore(target, toInject);
	}

	@Override
	public void registerAdditions(ASMAdditionRegistry registry) {
		registry.registerFieldAddition("net/minecraft/tileentity/TileEntityPiston", new FieldNode(ACC_PUBLIC, "movingBlockTileEntityData", "Lnet/minecraft/nbt/NBTTagCompound;", null, null));
		registry.registerFieldAddition("net/minecraft/tileentity/TileEntityPiston", new FieldNode(ACC_PUBLIC, "bpmeta", "I", null, null));
	}
}