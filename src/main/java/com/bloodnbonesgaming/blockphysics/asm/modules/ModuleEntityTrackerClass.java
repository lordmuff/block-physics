package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
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

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;
import squeek.asmhelper.com.bloodnbonesgaming.lib.ObfHelper;

import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bloodnbonesgaming.lib.core.ASMAdditionRegistry;
import com.bloodnbonesgaming.lib.core.insn.RedirectedFieldInsnNode;
import com.bloodnbonesgaming.lib.core.module.IClassTransformerModule;

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
	public byte[] transform(final String name, final String transformedName, final byte[] bytes)
	{
		final ClassNode classNode = ASMHelper.readClassFromBytes(bytes);

		if (transformedName.equals("net.minecraft.entity.EntityTracker"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);


			//"<init>", "(Lnet/minecraft/world/WorldServer;)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/WorldServer;)V");
			if (methodNode != null)
			{
				this.transformInit(methodNode);
			} else {
				throw new RuntimeException("Could not find <init>(WorldServer) method in " + transformedName);
			}

			//"addEntityToTracker", "(Lnet/minecraft/entity/Entity;)V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "addEntityToTracker" : "func_72786_a", "(Lnet/minecraft/entity/Entity;)V");
			if (methodNode != null)
			{
				this.transformAddEntityToTracker(methodNode);
			} else {
				throw new RuntimeException("Could not find addEntityToTracker method in " + transformedName);
			}

			//"removeEntityFromAllTrackingPlayers", "(Lnet/minecraft/entity/Entity;)V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, !ObfHelper.isObfuscated() ? "removeEntityFromAllTrackingPlayers" : "func_72790_b", "(Lnet/minecraft/entity/Entity;)V");
			if (methodNode != null)
			{
				this.transformRemoveEntityFromAllTrackingPlayers(methodNode);
			} else {
				throw new RuntimeException("Could not find removeEntityFromAllTrackingPlayers method in " + transformedName);
			}

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

	public void transformInit(final MethodNode method)
	{
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		//this.movingblocks = 0;
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.ICONST_0));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/EntityTracker", "movingblocks", "I", this));

		method.instructions.insertBefore(target, toInject);
	}

	public void transformAddEntityToTracker(final MethodNode method)
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
			if ( method.instructions.get(index).getType() == AbstractInsnNode.TYPE_INSN && method.instructions.get(index).getOpcode() == Opcodes.INSTANCEOF && ((TypeInsnNode) method.instructions.get(index)).desc.equals("net/minecraft/entity/item/EntityTNTPrimed"))
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
				while ( method.instructions.get(index).getType() != AbstractInsnNode.TYPE_INSN || method.instructions.get(index).getOpcode() != Opcodes.INSTANCEOF )
				{
					method.instructions.remove(method.instructions.get(index-1));
				}
				break;
			}
		}

		for (int index = 0; index < method.instructions.size(); index++)
		{
			if ( method.instructions.get(index).getType() == AbstractInsnNode.TYPE_INSN && method.instructions.get(index).getOpcode() == Opcodes.INSTANCEOF && ((TypeInsnNode) method.instructions.get(index)).desc.equals("net/minecraft/entity/item/EntityFallingBlock"))
			{//instanceof EntityFallingSand
				while ( method.instructions.get(index).getOpcode() != Opcodes.GOTO )
				{
					index++;
					if (method.instructions.get(index).getType() == AbstractInsnNode.INT_INSN && method.instructions.get(index).getOpcode() == Opcodes.BIPUSH && ((IntInsnNode) method.instructions.get(index)).operand == 20)
					{
						/*
						 * replace 20 with 40 on line 167
						 * this.addEntityToTracker(par1Entity, 160, 20, true);
						 */
						method.instructions.set(method.instructions.get(index),new IntInsnNode(Opcodes.BIPUSH, 40));
					}
				}
				/*
				 * Equivalent to injecting
				 * this.movingblocks += 1; or perhaps this.movingBlocks++;
				 * before end bracket on line 168
				 */
				final InsnList toInject = new InsnList();
				toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
				toInject.add(new InsnNode(Opcodes.DUP));
				toInject.add(new RedirectedFieldInsnNode( Opcodes.GETFIELD, "net/minecraft/entity/EntityTracker", "movingblocks", "I", this));
				toInject.add(new InsnNode(Opcodes.ICONST_1));
				toInject.add(new InsnNode(Opcodes.IADD));
				toInject.add(new RedirectedFieldInsnNode( Opcodes.PUTFIELD, "net/minecraft/entity/EntityTracker", "movingblocks", "I", this));

				method.instructions.insertBefore(method.instructions.get(index),toInject);
				break;
			}
		}
	}

	public void transformRemoveEntityFromAllTrackingPlayers(final MethodNode method)
	{
		final AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);

		//if (p_72790_1_ instanceof EntityFallingBlock)
		//{
		//	this.movingblocks--;
		//}
		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/minecraft/entity/item/EntityFallingBlock"));
		final LabelNode label1 = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.IFEQ, label1));
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new InsnNode(Opcodes.DUP));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/EntityTracker", "movingblocks", "I", this));
		toInject.add(new InsnNode(Opcodes.ICONST_1));
		toInject.add(new InsnNode(Opcodes.ISUB));
		toInject.add(new RedirectedFieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/EntityTracker", "movingblocks", "I", this));
		toInject.add(label1);
		toInject.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

		method.instructions.insertBefore(target, toInject);
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry registry) {
		registry.registerFieldAddition("net/minecraft/entity/EntityTracker", new FieldNode(Opcodes.ACC_PUBLIC, "movingblocks", "I", null, null));
	}
}