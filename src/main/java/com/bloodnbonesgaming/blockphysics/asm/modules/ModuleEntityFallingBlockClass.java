package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.DASTORE;
import static org.objectweb.asm.Opcodes.DCONST_0;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.FCONST_2;
import static org.objectweb.asm.Opcodes.FDIV;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.I2B;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_2;
import static org.objectweb.asm.Opcodes.ICONST_3;
import static org.objectweb.asm.Opcodes.ICONST_4;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.NEWARRAY;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.T_DOUBLE;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
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
import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.insn.RedirectedFieldInsnNode;
import com.bnbgaming.lib.core.insn.RedirectedMethodInsnNode;
import com.bnbgaming.lib.core.insn.RedirectedMethodVisitor;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleEntityFallingBlockClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.entity.item.EntityFallingBlock"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformEntityFallingBlockClass";
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
		
		if (transformedName.equals("net.minecraft.entity.item.EntityFallingBlock"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);
			
			//"<init>", "(Lnet/minecraft/world/World;)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/World;)V");
            if (methodNode != null)
            {
            	transformInit1(methodNode);
            }
            else
                throw new RuntimeException("Could not find init(World) method in " + transformedName);
            
            //"<init>", "(Lnet/minecraft/world/World;DDDLnet/minecraft/block/Block;I)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/World;DDDLnet/minecraft/block/Block;I)V");
            if (methodNode != null)
            {
            	transformInit2(methodNode);
            }
            else
                throw new RuntimeException("Could not find init(World,D,D,D,Block,I) method in " + transformedName);
            
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
            
            //"fall", "(F)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_70069_a", "(F)V");
            if (methodNode != null)
            {
            	transformFall(methodNode);
            }
            else
                throw new RuntimeException("Could not find fall method in " + transformedName);
            
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
            
            //"canRenderOnFire", "()Z"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_90999_ad", "()Z");
            if (methodNode != null)
            {
            	transformCanRenderOnFire(methodNode);
            }
            else
                ASMPlugin.log.debug("Could not find canRenderOnFire method in " + transformedName + ". This must be a dedicated server.");
            
            return ASMHelper.writeClassToBytesNoDeobfSkipFrames(classNode);
		}
		return bytes;
	}
	
	public void createGetBoundingBox(ClassNode classNode)
	{
		MethodVisitor methodVisitor = classNode.visitMethod(ACC_PUBLIC, "func_70046_E", "()Lnet/minecraft/util/AxisAlignedBB;", null, null);
		RedirectedMethodVisitor rMethodVisitor = new RedirectedMethodVisitor(methodVisitor);
		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(ALOAD, 0);
		rMethodVisitor.visitRedirectedFieldInsn(GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70121_D", "Lnet/minecraft/util/AxisAlignedBB;", this);
		methodVisitor.visitInsn(ARETURN);
		methodVisitor.visitMaxs(1, 1);
		methodVisitor.visitEnd();
	}
	
	public void createSetInWeb(ClassNode classNode)
	{
		MethodVisitor methodVisitor = classNode.visitMethod(ACC_PUBLIC, "func_70110_aj", "()V", null, null);
		methodVisitor.visitCode();
		methodVisitor.visitInsn(RETURN);
		methodVisitor.visitMaxs(0, 1);
		methodVisitor.visitEnd();
	}
	
	public void createMoveEntity(ClassNode classNode)
	{
		MethodVisitor methodVisitor = classNode.visitMethod(ACC_PUBLIC, "func_70091_d", "(DDD)V", null, null);
		RedirectedMethodVisitor rMethodVisitor = new RedirectedMethodVisitor(methodVisitor);
		methodVisitor.visitCode();
		methodVisitor.visitVarInsn(ALOAD, 0);
		rMethodVisitor.visitRedirectedFieldInsn(GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70170_p", "Lnet/minecraft/world/World;", this);
		methodVisitor.visitVarInsn(ALOAD, 0);
		methodVisitor.visitVarInsn(DLOAD, 1);
		methodVisitor.visitVarInsn(DLOAD, 3);
		methodVisitor.visitVarInsn(DLOAD, 5);
		rMethodVisitor.visitRedirectedMethodInsn(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "moveEntity", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/item/EntityFallingBlock;DDD)V", false, this);
		methodVisitor.visitInsn(RETURN);
		methodVisitor.visitMaxs(8, 7);
		methodVisitor.visitEnd();
	}
	
	public void transformInit1(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		InsnList toInject = new InsnList();
		//this.preventEntitySpawning = true;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ICONST_1));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70156_m", "Z", this));
		
		//this.setSize(0.996f, 0.996f);
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new LdcInsnNode(new Float("0.996")));
		toInject.add(new LdcInsnNode(new Float("0.996")));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/item/EntityFallingBlock", "func_70105_a", "(FF)V", false, this));
		
		//this.yOffset = this.height/2.0f;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70131_O", "F", this));
		toInject.add(new InsnNode(FCONST_2));
		toInject.add(new InsnNode(FDIV));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70129_M", "F", this));
		
		//this.motionX = 0;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(DCONST_0));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70159_w", "D", this));
		
		//this.motionY = 0;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(DCONST_0));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70181_x", "D", this));
		
		//this.motionZ = 0;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(DCONST_0));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70179_y", "D", this));
		
		//this.accelerationX = 0;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(DCONST_0));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationX", "D", this));
		
		//this.accelerationY = -0.024525;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new LdcInsnNode(new Double("-0.024525")));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationY", "D", this));
		
		//this.accelerationZ = 0;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(DCONST_0));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationZ", "D", this));
		
		//this.slideDir = 0;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ICONST_0));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "slideDir", "B", this));
		
		//this.noClip = true;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ICONST_1));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70145_X", "Z", this));
		
		//this.entityCollisionReduction = 0.8F;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new LdcInsnNode(new Float("0.8")));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70144_Y", "F", this));
		
		//this.dead = 4;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ICONST_4));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "dead", "B", this));
		
		//this.bpdata = 0;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ICONST_0));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "bpdata", "I", this));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformInit2(MethodNode method)
	{
		AbstractInsnNode toReplace = ASMHelper.find(method.instructions, new LdcInsnNode(new Float("0.98")));
		while (toReplace != null)
		{
			LdcInsnNode replacement = new LdcInsnNode(new Float("0.996"));

			method.instructions.set(toReplace, replacement);
			
			toReplace = ASMHelper.find(method.instructions, new LdcInsnNode(new Float("0.98")));
		}

		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);

		InsnList toInject = new InsnList();

		//this.accelerationX = 0;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(DCONST_0));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationX", "D", this));

		//this.accelerationY = -0.024525;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new LdcInsnNode(new Double("-0.024525")));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationY", "D", this));

		//this.accelerationZ = 0;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(DCONST_0));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationZ", "D", this));

		//this.slideDir = 0;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ICONST_0));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "slideDir", "B", this));

		//this.noClip = true;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ICONST_1));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70145_X", "Z", this));

		//this.entityCollisionReduction = 0.8F;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new LdcInsnNode(new Float("0.8")));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70144_Y", "F", this));

		//this.dead = 4;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ICONST_4));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "dead", "B", this));

		//this.bpdata = 0;
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ICONST_0));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "bpdata", "I", this));

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
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70170_p", "Lnet/minecraft/world/World;", this));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedMethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "fallingSandUpdate", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/item/EntityFallingBlock;)V", false, this));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformFall(MethodNode method)
	{
		//Clear method of everything except end RETURN instruction
		AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		AbstractInsnNode end = ASMHelper.findLastInstructionWithOpcode(method, RETURN);

		if (start == null || end == null)
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.fall");

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
	}
	
	public void transformWriteEntityToNBT(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		//p_70014_1_.setTag("Acceleration", newDoubleNBTList(new double[] {this.accelerationX, this.accelerationY, this.accelerationZ}));
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new LdcInsnNode("Acceleration"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ICONST_3));
		toInject.add(new IntInsnNode(NEWARRAY, T_DOUBLE));
		toInject.add(new InsnNode(DUP));
		toInject.add(new InsnNode(ICONST_0));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationX", "D", this));
		toInject.add(new InsnNode(DASTORE));
		toInject.add(new InsnNode(DUP));
		toInject.add(new InsnNode(ICONST_1));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationY", "D", this));
		toInject.add(new InsnNode(DASTORE));
		toInject.add(new InsnNode(DUP));
		toInject.add(new InsnNode(ICONST_2));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationZ", "D", this));
		toInject.add(new InsnNode(DASTORE));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/item/EntityFallingBlock", "func_70087_a", "([D)Lnet/minecraft/nbt/NBTTagList;", false, this));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74782_a", "(Ljava/lang/String;Lnet/minecraft/nbt/NBTBase;)V", false, this));
		
		//p_70014_1_.setByte("BPData", (byte)this.bpdata);
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new LdcInsnNode("BPData"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", "bpdata", "I", this));
		toInject.add(new InsnNode(I2B));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74774_a", "(Ljava/lang/String;B)V", false, this));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformReadEntityFromNBT(MethodNode method)
	{
		//Remove "TileID" calls
		AbstractInsnNode start = ASMHelper.findFirstInstruction(method);
		AbstractInsnNode end = ASMHelper.move(start, 25);
		
		if (start == null || end == null)
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.readEntityFromNBT 1");

		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
		
		//this.field_145811_e = BlockPhysics.readFallingSandID(p_70037_1_);
		AbstractInsnNode target = ASMHelper.findFirstInstruction(method);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.readEntityFromNBT 2");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new RedirectedMethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "readFallingSandID", "(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/block/Block;", false, this));
		toInject.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_145811_e", "Lnet/minecraft/block/Block;", this));
		
		method.instructions.insertBefore(target, toInject);
		
		//if (p_70037_1_.hasKey("Acceleration"))
		//{
		//	NBTTagList acceleration = p_70037_1_.getTagList("Acceleration", Constants.NBT.TAG_LIST);
		// 	this.accelerationX = acceleration.func_150309_d(0);
		// 	this.accelerationY = acceleration.func_150309_d(1);
		// 	this.accelerationZ = acceleration.func_150309_d(2);
		//}
		//else
		//{
		//	this.accelerationX = 9.0;
		//	this.accelerationY = 0.0;
		//	this.accelerationZ = 0.0;
		//}
		//if (p_70037_1_.hasKey("BPData"))
		//{
		//	this.bpdata = p_70037_1_.getByte("BPData");
		//}
		//else
		//{
		//	this.bpdata = 0;
		//}
		//}
		AbstractInsnNode target2 = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target2 == null)
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.readEntityFromNBT 3");
		
		InsnList toInject2 = new InsnList();
		toInject2.add(new VarInsnNode(ALOAD, 1));
		toInject2.add(new LdcInsnNode("Acceleration"));
		toInject2.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74764_b", "(Ljava/lang/String;)Z", false, this));
		LabelNode label1 = new LabelNode();
		toInject2.add(new JumpInsnNode(IFEQ, label1));
		
		toInject2.add(new VarInsnNode(ALOAD, 1));
		toInject2.add(new LdcInsnNode("Acceleration"));
		toInject2.add(new IntInsnNode(BIPUSH, 9));
		toInject2.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_150295_c", "(Ljava/lang/String;I)Lnet/minecraft/nbt/NBTTagList;", false, this));
		toInject2.add(new VarInsnNode(ASTORE, 2));
		
		toInject2.add(new VarInsnNode(ALOAD, 0));
		toInject2.add(new VarInsnNode(ALOAD, 2));
		toInject2.add(new InsnNode(ICONST_0));
		toInject2.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagList", "func_150309_d", "(I)D", false, this));
		toInject2.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationX", "D", this));
		
		toInject2.add(new VarInsnNode(ALOAD, 0));
		toInject2.add(new VarInsnNode(ALOAD, 2));
		toInject2.add(new InsnNode(ICONST_1));
		toInject2.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagList", "func_150309_d", "(I)D", false, this));
		toInject2.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationY", "D", this));
		
		toInject2.add(new VarInsnNode(ALOAD, 0));
		toInject2.add(new VarInsnNode(ALOAD, 2));
		toInject2.add(new InsnNode(ICONST_2));
		toInject2.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagList", "func_150309_d", "(I)D", false, this));
		toInject2.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationZ", "D", this));
		
		LabelNode label2 = new LabelNode();
		toInject2.add(new JumpInsnNode(GOTO, label2));
		toInject2.add(label1);
		
		toInject2.add(new VarInsnNode(ALOAD, 0));
		toInject2.add(new InsnNode(DCONST_0));
		toInject2.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationX", "D", this));
		
		
		toInject2.add(new VarInsnNode(ALOAD, 0));
		toInject2.add(new InsnNode(DCONST_0));
		toInject2.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationY", "D", this));
		
		toInject2.add(new VarInsnNode(ALOAD, 0));
		toInject2.add(new InsnNode(DCONST_0));
		toInject2.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationZ", "D", this));
		toInject2.add(label2);
		
		toInject2.add(new VarInsnNode(ALOAD, 1));
		toInject2.add(new LdcInsnNode("BPData"));
		toInject2.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74764_b", "(Ljava/lang/String;)Z", false, this));
		LabelNode label3 = new LabelNode();
		toInject2.add(new JumpInsnNode(IFEQ, label3));
		
		toInject2.add(new VarInsnNode(ALOAD, 0));
		toInject2.add(new VarInsnNode(ALOAD, 1));
		toInject2.add(new LdcInsnNode("BPData"));
		toInject2.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74771_c", "(Ljava/lang/String;)B", false, this));
		toInject2.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "bpdata", "I", this));
		
		LabelNode label4 = new LabelNode();
		toInject2.add(new JumpInsnNode(GOTO, label4));
		toInject2.add(label3);
		
		toInject2.add(new VarInsnNode(ALOAD, 0));
		toInject2.add(new InsnNode(ICONST_0));
		toInject2.add(new RedirectedFieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "bpdata", "I", this));
		toInject2.add(label4);
		
		method.instructions.insertBefore(target2, toInject2);
	}
	
	public void transformCanRenderOnFire(MethodNode method)
	{
		//Remove return
		AbstractInsnNode toRemove = ASMHelper.find(method.instructions, new InsnNode(ICONST_0));
		
		if (toRemove == null)
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.canRenderOnFire 1");
		
		method.instructions.remove(toRemove);
		
		//return this.isInWater();
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, IRETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.canRenderOnFire 2");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/item/EntityFallingBlock", "func_70090_H", "()Z", false, this));
		
		method.instructions.insertBefore(target, toInject);
	}

	@Override
	public void registerAdditions(ASMAdditionRegistry registry) {
		registry.registerFieldAddition("net/minecraft/entity/item/EntityFallingBlock", new FieldNode(ACC_PUBLIC, "slideDir", "B", null, null));
		registry.registerFieldAddition("net/minecraft/entity/item/EntityFallingBlock", new FieldNode(ACC_PUBLIC, "accelerationX", "D", null, null));
		registry.registerFieldAddition("net/minecraft/entity/item/EntityFallingBlock", new FieldNode(ACC_PUBLIC, "accelerationY", "D", null, null));
		registry.registerFieldAddition("net/minecraft/entity/item/EntityFallingBlock", new FieldNode(ACC_PUBLIC, "accelerationZ", "D", null, null));
		registry.registerFieldAddition("net/minecraft/entity/item/EntityFallingBlock", new FieldNode(ACC_PUBLIC, "bpdata", "I", null, null));
		registry.registerFieldAddition("net/minecraft/entity/item/EntityFallingBlock", new FieldNode(ACC_PUBLIC, "media", "Ljava/lang/String;", null, null));
		registry.registerFieldAddition("net/minecraft/entity/item/EntityFallingBlock", new FieldNode(ACC_PUBLIC, "dead", "B", null, null));

		ClassNode classNode = new ClassNode();
		
		createGetBoundingBox(classNode);
		createSetInWeb(classNode);
		createMoveEntity(classNode);
		
		registry.registerMethodAddition("net/minecraft/entity/item/EntityFallingBlock", ASMHelper.findMethodNodeOfClass(classNode, "func_70046_E", "()Lnet/minecraft/util/AxisAlignedBB;"));
		registry.registerMethodAddition("net/minecraft/entity/item/EntityFallingBlock", ASMHelper.findMethodNodeOfClass(classNode, "func_70110_aj", "()V"));
		registry.registerMethodAddition("net/minecraft/entity/item/EntityFallingBlock", ASMHelper.findMethodNodeOfClass(classNode, "func_70091_d", "(DDD)V"));
	}
}