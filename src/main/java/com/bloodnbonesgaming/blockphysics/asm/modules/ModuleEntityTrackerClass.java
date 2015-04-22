package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.INSTANCEOF;
import static org.objectweb.asm.Opcodes.ISUB;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.ClassTransformer;
import com.bloodnbonesgaming.blockphysics.asm.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleEntityTrackerClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.entity.EntityTracker"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformEntityTrackerClass";
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
		
		if (transformedName.equals("net.minecraft.entity.EntityTracker"))
		{
			ModInfo.Log.info("Transforming class: " + transformedName);
			
			createMovingBlocks(classNode);
			verifyFieldAdded(classNode, "movingblocks", "I");
			
			
			//"<init>", "(Lnet/minecraft/world/WorldServer;)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/WorldServer;)V");
            if (methodNode != null)
            {
            	transformInit(methodNode);
            }
            else
                throw new RuntimeException("Could not find <init>(WorldServer) method in " + transformedName);
            
            //"addEntityToTracker", "(Lnet/minecraft/entity/Entity;)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_72786_a", "(Lnet/minecraft/entity/Entity;)V");
            if (methodNode != null)
            {
            	transformAddEntityToTracker(methodNode);
            }
            else
                throw new RuntimeException("Could not find addEntityToTracker method in " + transformedName);
            
            //"removeEntityFromAllTrackingPlayers", "(Lnet/minecraft/entity/Entity;)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_72790_b", "(Lnet/minecraft/entity/Entity;)V");
            if (methodNode != null)
            {
            	transformRemoveEntityFromAllTrackingPlayers(methodNode);
            }
            else
                throw new RuntimeException("Could not find removeEntityFromAllTrackingPlayers method in " + transformedName);
            
            //func_72785_a (Lnet/minecraft/entity/Entity;IIZ)V
            /*methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_72785_a", "(Lnet/minecraft/entity/Entity;IIZ)V");
            if (methodNode != null)
            {
            	transformAddEntityToTracker2(methodNode);
            }
            else
                throw new RuntimeException("Could not find removeEntityFromAllTrackingPlayers method in " + transformedName);*/
            
            return ASMHelper.writeClassToBytesNoDeobfSkipFrames(classNode);
		}
		return bytes;
	}
	
	public void verifyFieldAdded(ClassNode classNode, String name, String desc)
	{
		FieldNode fieldNode = ClassTransformer.findFieldNodeOfClass(classNode, name, desc);
		if (fieldNode != null)
		{
			ModInfo.Log.info("Successfully added field: " + fieldNode.name + fieldNode.desc + " in " + classNode.name);
		} else
			throw new RuntimeException("Could not create field: " + name + desc + " in " + classNode.name);
	}
	
	public void createMovingBlocks(ClassNode classNode)
	{
		FieldVisitor fieldVisitor = classNode.visitField(ACC_PUBLIC, "movingblocks", "I", null, null);
		fieldVisitor.visitEnd();
	}
	
	public void transformInit(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		//this.movingblocks = 0;
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ICONST_0));
		toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/EntityTracker", "movingblocks", "I"));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformAddEntityToTracker(MethodNode method)
	{
		//Remove if else statement
		/*AbstractInsnNode target = ASMHelper.find(method.instructions, new TypeInsnNode(INSTANCEOF, "net/minecraft/entity/item/EntityTNTPrimed"));
		AbstractInsnNode start = ASMHelper.move(target, -1);
		AbstractInsnNode end = ASMHelper.move(start, 11);
		
		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
		
		//this.addEntityToTracker(p_72786_1_, 160, 40, true); - 20 to 40
		AbstractInsnNode target2 = ASMHelper.find(method.instructions, new TypeInsnNode(INSTANCEOF, "net/minecraft/entity/item/EntityFallingBlock"));
		AbstractInsnNode toReplace = ASMHelper.move(target2, 5);
		AbstractInsnNode replacement = new IntInsnNode(BIPUSH, 40);
		AbstractInsnNode target3 = ASMHelper.move(target2, 7);
		
		//method.instructions.set(toReplace, replacement);
		
		//this.movingblocks++;
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(DUP));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/EntityTracker", "movingblocks", "I"));
		toInject.add(new InsnNode(ICONST_1));
		toInject.add(new InsnNode(IADD));
		toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/EntityTracker", "movingblocks", "I"));
		
		method.instructions.insert(target3, toInject);*/
		
		
		for (int index = 0; index < method.instructions.size(); index++)
        {
			if ( method.instructions.get(index).getType() == AbstractInsnNode.TYPE_INSN && method.instructions.get(index).getOpcode() == INSTANCEOF && ((TypeInsnNode) method.instructions.get(index)).desc.equals("net/minecraft/entity/item/EntityTNTPrimed"))
            {//InstanceOf EntityTNTPrimed
				method.instructions.remove(method.instructions.get(index-1));
				/*
				 * else if (par1Entity instanceof EntityTNTPrimed)
				 * {
    			 * this.addEntityToTracker(par1Entity, 160, 10, true);
				 * }
				 * else if (par1Entity instanceof EntityFallingSand)
				 * Removes both ALOAD par1Entity before "instanceof"s and whole line this.addEntityToTracker(par1Entity, 160, 10, true);
				 */
				while ( method.instructions.get(index).getType() != AbstractInsnNode.TYPE_INSN || method.instructions.get(index).getOpcode() != INSTANCEOF )
				{
					method.instructions.remove(method.instructions.get(index-1));
				}
    			break;
            }
        }
		
		for (int index = 0; index < method.instructions.size(); index++)
        {
			if ( method.instructions.get(index).getType() == AbstractInsnNode.TYPE_INSN && method.instructions.get(index).getOpcode() == INSTANCEOF && ((TypeInsnNode) method.instructions.get(index)).desc.equals("net/minecraft/entity/item/EntityFallingBlock"))
            {//instanceof EntityFallingSand
				while ( method.instructions.get(index).getOpcode() != GOTO )
				{
					index++;
					if (method.instructions.get(index).getType() == AbstractInsnNode.INT_INSN && method.instructions.get(index).getOpcode() == BIPUSH && ((IntInsnNode) method.instructions.get(index)).operand == 20)
					{
						/*
						 * replace 20 with 40 on line 167
						 * this.addEntityToTracker(par1Entity, 160, 20, true);
						 */
						method.instructions.set(method.instructions.get(index),new IntInsnNode(BIPUSH, 40));
					}
				}
				/*
				 * Equivalent to injecting
				 * this.movingblocks += 1; or perhaps this.movingBlocks++;
				 * before end bracket on line 168
				 */
				InsnList toInject = new InsnList();
    			toInject.add(new VarInsnNode(ALOAD, 0));
    			toInject.add(new InsnNode(DUP));
    			toInject.add(new FieldInsnNode( GETFIELD, "net/minecraft/entity/EntityTracker", "movingblocks", "I"));
    			toInject.add(new InsnNode(ICONST_1));
    			toInject.add(new InsnNode(IADD));
            	toInject.add(new FieldInsnNode( PUTFIELD, "net/minecraft/entity/EntityTracker", "movingblocks", "I"));
            	
    			method.instructions.insertBefore(method.instructions.get(index),toInject);
    			break;
            }
        }
	}
	
	public void transformRemoveEntityFromAllTrackingPlayers(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		//if (p_72790_1_ instanceof EntityFallingBlock)
		//{
		//	this.movingblocks--;
		//}
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new TypeInsnNode(INSTANCEOF, "net/minecraft/entity/item/EntityFallingBlock"));
		LabelNode label1 = new LabelNode();
		toInject.add(new JumpInsnNode(IFEQ, label1));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(DUP));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/EntityTracker", "movingblocks", "I"));
		toInject.add(new InsnNode(ICONST_1));
		toInject.add(new InsnNode(ISUB));
		toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/EntityTracker", "movingblocks", "I"));
		toInject.add(label1);
		toInject.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformAddEntityToTracker2(MethodNode method)
	{
		//BlockPhysics.printMethod(method);
	}
}