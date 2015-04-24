package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleWorldServerClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.world.WorldServer"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformWorldServerClass";
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
		
		if (transformedName.equals("net.minecraft.world.WorldServer"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);
							
			//"tick", "()V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_72835_b", "()V");
            if (methodNode != null)
            {
            	transformTick(methodNode);
            }
            else
                throw new RuntimeException("Could not find tick method in " + transformedName);
            
            //"newExplosion", "(Lnet/minecraft/entity/Entity;DDDFZZ)Lnet/minecraft/world/Explosion;"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_72885_a", "(Lnet/minecraft/entity/Entity;DDDFZZ)Lnet/minecraft/world/Explosion;");
            if (methodNode != null)
            {
            	transformNewExplosion(methodNode);
            }
            else
                throw new RuntimeException("Could not find newExplosion method in " + transformedName);
            
            return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}
	
	public void transformTick(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in WorldServer.tick");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new MethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "tickBlocksRandomMove", "(Lnet/minecraft/world/WorldServer;)V", false));
		
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/WorldServer", "moveTickList", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/BTickList;"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, ModInfo.MAIN_PACKACE + "/blockphysics/BTickList", "tickMoveUpdates", "(Lnet/minecraft/world/World;)V", false));
		
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/WorldServer", "pistonMoveBlocks", "Ljava/util/HashSet;"));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "java/util/HashSet", "clear", "()V", false));
		
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/WorldServer", "explosionQueue", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue;"));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue", "doNextExplosion", "()V", false));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformNewExplosion(MethodNode method)
	{
		InsnList toRemove1 = new InsnList();
		toRemove1.add(new VarInsnNode(ALOAD, 11));
		toRemove1.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/Explosion", "func_77278_a", "()V", false));
		
		AbstractInsnNode start1 = ASMHelper.find(method.instructions, toRemove1);
		AbstractInsnNode end1 = ASMHelper.move(start1, 2);
		
		if (start1 == null || end1 == null)
			throw new RuntimeException("Unexpected instruction pattern in WorldServer.tick 1");
		
		ASMHelper.removeFromInsnListUntil(method.instructions, start1, end1);
		
		InsnList toRemove2 = new InsnList();
		toRemove2.add(new VarInsnNode(ALOAD, 11));
		toRemove2.add(new InsnNode(ICONST_0));
		toRemove2.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/Explosion", "func_77279_a", "(Z)V", false));
		
		AbstractInsnNode start2 = ASMHelper.find(method.instructions, toRemove2);
		AbstractInsnNode end2 = ASMHelper.move(start2, 3);
		
		if (start2 == null || end2 == null)
			throw new RuntimeException("Unexpected instruction pattern in WorldServer.tick 2");
		
		ASMHelper.removeFromInsnListUntil(method.instructions, start2, end2);
		
		AbstractInsnNode target = ASMHelper.find(method.instructions, new FrameNode(Opcodes.F_APPEND,1, new Object[] {"net/minecraft/world/Explosion"}, 0, null));
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in WorldServer.tick 3");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/World", "explosionQueue", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue;"));
		toInject.add(new VarInsnNode(ALOAD, 11));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue", "add", "(Lnet/minecraft/world/Explosion;)V", false));
		
		method.instructions.insert(target, toInject);
	}

	@Override
	public void registerAdditions(ASMAdditionRegistry arg0) {}
}