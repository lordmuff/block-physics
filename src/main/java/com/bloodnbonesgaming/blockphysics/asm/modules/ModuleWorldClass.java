package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleWorldClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.world.World"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformWorldClass";
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
		
		if (transformedName.equals("net.minecraft.world.World"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);
			
			//"<init>", "(Lnet/minecraft/world/storage/ISaveHandler;Ljava/lang/String;Lnet/minecraft/world/WorldProvider;Lnet/minecraft/world/WorldSettings;Lnet/minecraft/profiler/Profiler;)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/storage/ISaveHandler;Ljava/lang/String;Lnet/minecraft/world/WorldProvider;Lnet/minecraft/world/WorldSettings;Lnet/minecraft/profiler/Profiler;)V");
            if (methodNode != null)
            {
            	transformInit1(methodNode);
            }
            else
                ASMPlugin.log.debug("Could not find <init>1 method in " + transformedName + ". This must be a dedicated server.");
            
            //"<init>", "(Lnet/minecraft/world/storage/ISaveHandler;Ljava/lang/String;Lnet/minecraft/world/WorldSettings;Lnet/minecraft/world/WorldProvider;Lnet/minecraft/profiler/Profiler;)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/storage/ISaveHandler;Ljava/lang/String;Lnet/minecraft/world/WorldSettings;Lnet/minecraft/world/WorldProvider;Lnet/minecraft/profiler/Profiler;)V");
            if (methodNode != null)
            {
            	transformInit2(methodNode);
            }
            else
                throw new RuntimeException("Could not find <init>2 method in " + transformedName);
            
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
	
	public void transformInit1(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in World.<init> 1");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new TypeInsnNode(NEW, ModInfo.MAIN_PACKACE + "/blockphysics/BTickList"));
		toInject.add(new InsnNode(DUP));
		toInject.add(new MethodInsnNode(INVOKESPECIAL, ModInfo.MAIN_PACKACE + "/blockphysics/BTickList", "<init>", "()V", false));
		toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/world/World", "moveTickList", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/BTickList;"));
		
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new TypeInsnNode(NEW, "java/util/HashSet"));
		toInject.add(new InsnNode(DUP));
		toInject.add(new MethodInsnNode(INVOKESPECIAL, "java/util/HashSet", "<init>", "()V", false));
		toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/world/World", "pistonMoveBlocks", "Ljava/util/HashSet;"));
		
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new TypeInsnNode(NEW, ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue"));
		toInject.add(new InsnNode(DUP));
		toInject.add(new MethodInsnNode(INVOKESPECIAL, ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue", "<init>", "()V", false));
		toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/world/World", "explosionQueue", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue;"));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformInit2(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in World.<init> 2");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new TypeInsnNode(NEW, ModInfo.MAIN_PACKACE + "/blockphysics/BTickList"));
		toInject.add(new InsnNode(DUP));
		toInject.add(new MethodInsnNode(INVOKESPECIAL, ModInfo.MAIN_PACKACE + "/blockphysics/BTickList", "<init>", "()V", false));
		toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/world/World", "moveTickList", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/BTickList;"));
		
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new TypeInsnNode(NEW, "java/util/HashSet"));
		toInject.add(new InsnNode(DUP));
		toInject.add(new MethodInsnNode(INVOKESPECIAL, "java/util/HashSet", "<init>", "()V", false));
		toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/world/World", "pistonMoveBlocks", "Ljava/util/HashSet;"));
		
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new TypeInsnNode(NEW, ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue"));
		toInject.add(new InsnNode(DUP));
		toInject.add(new MethodInsnNode(INVOKESPECIAL, ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue", "<init>", "()V", false));
		toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/world/World", "explosionQueue", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue;"));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformNewExplosion(MethodNode method)
	{
		InsnList toRemove = new InsnList();
		toRemove.add(new VarInsnNode(ALOAD, 11));
		toRemove.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/Explosion", "func_77278_a", "()V", false));
		
		AbstractInsnNode start = ASMHelper.find(method.instructions, toRemove);
		AbstractInsnNode end = ASMHelper.move(start, 7);
		
		
		if (start == null || end == null)
			throw new RuntimeException("Unexpected instruction pattern in World.NewExplosion");
		
		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
		
		AbstractInsnNode target2 = ASMHelper.findLastInstructionWithOpcode(method, ARETURN);
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/World", "explosionQueue", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue;"));
		toInject.add(new VarInsnNode(ALOAD, 11));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue", "add", "(Lnet/minecraft/world/Explosion;)V", false));
		
		method.instructions.insertBefore(target2, toInject);
	}

	@Override
	public void registerAdditions(ASMAdditionRegistry registry) {
		registry.registerFieldAddition("net.minecraft.world.World", new FieldNode(ACC_PUBLIC, "moveTickList", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/BTickList;", null, null));
		registry.registerFieldAddition("net.minecraft.world.World", new FieldNode(ACC_PUBLIC, "pistonMoveBlocks", "Ljava/util/HashSet;", null, null));
		registry.registerFieldAddition("net.minecraft.world.World", new FieldNode(ACC_PUBLIC, "explosionQueue", "L" + ModInfo.MAIN_PACKACE + "/blockphysics/ExplosionQueue;", null, null));
	}
}