package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.LADD;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LSTORE;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.insn.RedirectedMethodInsnNode;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleMinecraftServerClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.server.MinecraftServer"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformMinecraftServerClass";
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
		
		if (transformedName.equals("net.minecraft.server.MinecraftServer"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);
			
			//"run", "()V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "run", "()V");
            if (methodNode != null)
            {
            	transformRun(methodNode);
            }
            else
                throw new RuntimeException("Could not find run method in " + transformedName);
            
            return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}
	
	public void transformRun(MethodNode method)
	{
		InsnList toFind = new InsnList();
		toFind.add(new VarInsnNode(LLOAD, 3));
		toFind.add(new VarInsnNode(LLOAD, 7));
		toFind.add(new InsnNode(LADD));
		toFind.add(new VarInsnNode(LSTORE, 3));
		
		AbstractInsnNode target = ASMHelper.find(method.instructions, toFind);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in MinecraftServer.run");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(LLOAD, 3));
		toInject.add(new RedirectedMethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "setSkipMove", "(J)V", false, this));
		
		method.instructions.insertBefore(target, toInject);
	}

	@Override
	public void registerAdditions(ASMAdditionRegistry arg0) {}
}