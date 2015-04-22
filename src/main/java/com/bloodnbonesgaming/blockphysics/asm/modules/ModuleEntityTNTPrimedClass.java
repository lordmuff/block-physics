package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleEntityTNTPrimedClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.entity.item.EntityTNTPrimed"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformEntityTNTPrimedClass";
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
		
		if (transformedName.equals("net.minecraft.entity.item.EntityTNTPrimed"))
		{
			ModInfo.Log.info("Transforming class: " + transformedName);
			
			changeSuperClass(classNode);
			
			//"<init>", "(Lnet/minecraft/world/World;)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/World;)V");
            if (methodNode != null)
            {
            	transformInit1(methodNode);
            }
            else
                throw new RuntimeException("Could not find init(World) method in " + transformedName);
            
			//"canBeCollidedWith", "()Z"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_70067_L", "()Z");
			if (methodNode != null)
			{
				transformCanBeCollidedWith(methodNode);
			}
			else
				throw new RuntimeException("Could not find canBeCollidedWith method in " + transformedName);
			
			//"onUpdate", "()V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_70071_h_", "()V");
            if (methodNode != null)
            {
            	transformOnUpdate(methodNode);
            }
            else
                throw new RuntimeException("Could not find onUpdate method in " + transformedName);
            
            //"explode", "()V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_70515_d", "()V");
            if (methodNode != null)
            {
            	transformExplode(methodNode);
            }
            else
                throw new RuntimeException("Could not find explode method in " + transformedName);
            
            //"writeEntityToNBT", "(Lnet/minecraft/nbt/NBTTagCompound;)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_70014_b", "(Lnet/minecraft/nbt/NBTTagCompound;)V");
            if (methodNode != null)
            {
            	transformWriteEntityToNBT(methodNode);
            }
            else
                throw new RuntimeException("Could not find writeEntityToNBT method in " + transformedName);
            
            //"readEntityFromNBT", "(Lnet/minecraft/nbt/NBTTagCompound;)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_70037_a", "(Lnet/minecraft/nbt/NBTTagCompound;)V");
            if (methodNode != null)
            {
            	transformReadEntityFromNBT(methodNode);
            }
            else
                throw new RuntimeException("Could not find readEntityFromNBT method in " + transformedName);
			
            return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}
	
	public void changeSuperClass(ClassNode classNode)
	{
		classNode.superName = "net/minecraft/entity/item/EntityFallingBlock";
	}
	
	public void transformInit1(MethodNode method)
	{
		//Fix super call
		AbstractInsnNode toReplace = ASMHelper.find(method.instructions, new MethodInsnNode(INVOKESPECIAL, "net/minecraft/entity/Entity", "<init>", "(Lnet/minecraft/world/World;)V", false));
		AbstractInsnNode replacement = new MethodInsnNode(INVOKESPECIAL, "net/minecraft/entity/item/EntityFallingBlock", "<init>", "(Lnet/minecraft/world/World;)V", false);
		
		if (toReplace == null)
			throw new RuntimeException("Unexpected instruction pattern in EntityTNTPrimed.init 1");
		
		method.instructions.set(toReplace, replacement);
	}
	
	public void transformInit2(MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, RETURN);

		if (start == null || end == null)
			throw new RuntimeException("Unexpected instruction pattern in EntityTNTPrimed.<init>(World,D,D,D,EntityLivingBase)");

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
		
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		InsnList toInject = new InsnList();
		//super(p_i1730_1_, p_i1730_2_, p_i1730_4_, p_i1730_6_, Blocks.tnt, 0);
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new VarInsnNode(DLOAD, 2));
		toInject.add(new VarInsnNode(DLOAD, 4));
		toInject.add(new VarInsnNode(DLOAD, 6));
		toInject.add(new FieldInsnNode(GETSTATIC, "net/minecraft/init/Blocks", "field_150335_W", "Lnet/minecraft/block/Block;"));
		toInject.add(new InsnNode(ICONST_0));
		toInject.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/entity/item/EntityFallingBlock", "<init>", "(Lnet/minecraft/world/World;DDDLnet/minecraft/block/Block;I)V", false));
		
		//this.fuse = 80;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new IntInsnNode(BIPUSH, 80));
		toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityTNTPrimed", "field_70516_a", "I"));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformCanBeCollidedWith(MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, IRETURN);

		if (start == null || end == null)
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.canBeCollidedWith");

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);

		//return false;
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, IRETURN);

		InsnList toInject = new InsnList();
		toInject.add(new InsnNode(ICONST_0));

		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformOnUpdate(MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, RETURN);

		if (start == null || end == null)
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.onUpdate");

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
		
		//BlockPhysics.fallingSandUpdate(this.worldObj, this);
		AbstractInsnNode target = ASMHelper.findFirstInstruction(method);
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70170_p", "Lnet/minecraft/world/World;"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new MethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "fallingSandUpdate", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/item/EntityFallingBlock;)V", false));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformExplode(MethodNode method)
	{
		//Set access to public
		method.access = ACC_PUBLIC;
		
		//Replace all ACONST_NULL with ALOAD, 0 - Fixes a superclass change error I imagine
		AbstractInsnNode toReplace = ASMHelper.find(method.instructions, new InsnNode(ACONST_NULL));
		AbstractInsnNode replacement = new VarInsnNode(ALOAD, 0);
		
		while (toReplace != null)
		{
			method.instructions.set(toReplace, replacement);
			toReplace = ASMHelper.find(method.instructions, new InsnNode(ACONST_NULL));
		}
	}
	
	public void transformWriteEntityToNBT(MethodNode method)
	{
		//super.writeEntityToNBT(p_70014_1_);
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/entity/item/EntityFallingBlock", "func_70014_b", "(Lnet/minecraft/nbt/NBTTagCompound;)V", false));
		
		method.instructions.insertBefore(target,  toInject);
	}
	
	public void transformReadEntityFromNBT(MethodNode method)
	{
		//super.readEntityFromNBT(p_70037_1_);
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/entity/item/EntityFallingBlock", "func_70037_a", "(Lnet/minecraft/nbt/NBTTagCompound;)V", false));
		
		method.instructions.insertBefore(target,  toInject);
	}
}