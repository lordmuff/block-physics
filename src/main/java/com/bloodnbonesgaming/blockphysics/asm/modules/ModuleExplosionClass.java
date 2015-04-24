package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
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
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleExplosionClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.world.Explosion"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformExplosionClass";
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
		
		if (transformedName.equals("net.minecraft.world.Explosion"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);
			
			//"<init>", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;DDDF)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;DDDF)V");
            if (methodNode != null)
            {
            	transformInit(methodNode);
            }
            else
                throw new RuntimeException("Could not find <init> method in " + transformedName);
            
            //"doExplosionA", "()V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_77278_a", "()V");
            if (methodNode != null)
            {
            	transformDoExplosionA(methodNode);
            }
            else
                throw new RuntimeException("Could not find doExplosionA method in " + transformedName);
            
            //"doExplosionB", "(Z)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_77279_a", "(Z)V");
            if (methodNode != null)
            {
            	transformDoExplosionB(methodNode);
            }
            else
                throw new RuntimeException("Could not find doExplosionB method in " + transformedName);
            
            return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}
	
	public void transformInit(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ICONST_0));
		toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/world/Explosion", "impact", "Z"));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformDoExplosionA(MethodNode method)
	{
		method.instructions.clear();
		method.localVariables.clear();
		
		
		//BlockPhysics.doExplosionA(this.worldObj, this);
		//AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/Explosion", "field_77287_j", "Lnet/minecraft/world/World;"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new MethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "doExplosionA", "(Lnet/minecraft/world/World;Lnet/minecraft/world/Explosion;)V", false));
		toInject.add(new InsnNode(RETURN));
		
		method.instructions.add(toInject);
		
		
	}
	
	public void transformDoExplosionB(MethodNode method)
	{
		method.instructions.clear();
		method.localVariables.clear();
		
		//BlockPhysics.doExplosionB(this.worldObj, this, p_77279_1_);
		//AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/Explosion", "field_77287_j", "Lnet/minecraft/world/World;"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new VarInsnNode(ILOAD, 1));
		toInject.add(new MethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "doExplosionB", "(Lnet/minecraft/world/World;Lnet/minecraft/world/Explosion;Z)V", false));
		toInject.add(new InsnNode(RETURN));
		
		method.instructions.add(toInject);
	}

	@Override
	public void registerAdditions(ASMAdditionRegistry registry) {
		registry.registerFieldAddition("net.minecraft.world.Explosion", new FieldNode(ACC_PUBLIC, "impact", "Z", null, null));
	}
}