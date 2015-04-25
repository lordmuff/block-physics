package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INSTANCEOF;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.insn.RedirectedFieldInsnNode;
import com.bnbgaming.lib.core.insn.RedirectedMethodInsnNode;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleEntityTrackerEntryClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.entity.EntityTrackerEntry"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformEntityTrackerEntryClass";
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
		
		if (transformedName.equals("net.minecraft.entity.EntityTrackerEntry"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);
			
			//"func_151260_c", "()Lnet/minecraft/network/Packet;"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_151260_c", "()Lnet/minecraft/network/Packet;");
            if (methodNode != null)
            {
            	transformFunc_151260_c(methodNode);
            }
            else
                throw new RuntimeException("Could not find func_151260_c method in " + transformedName);
            
            return ASMHelper.writeClassToBytesNoDeobfSkipFrames(classNode);
		}
		return bytes;
	}
	
	public void transformFunc_151260_c(MethodNode method)
	{
		//Remove old return
		AbstractInsnNode target = ASMHelper.find(method.instructions, new TypeInsnNode(INSTANCEOF, "net/minecraft/entity/item/EntityFallingBlock"));
		AbstractInsnNode start = ASMHelper.move(target, 2);
		AbstractInsnNode end = ASMHelper.move(start, 23);
		AbstractInsnNode target2 = ASMHelper.move(target, 1);
		
		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
		
		//return BlockPhysics.spawnFallingSandPacket((EntityFallingBlock)this.myEntity);
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/entity/EntityTrackerEntry", "field_73132_a", "Lnet/minecraft/entity/Entity;", this));
		toInject.add(new TypeInsnNode(CHECKCAST, "net/minecraft/entity/item/EntityFallingBlock"));
		toInject.add(new RedirectedMethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "spawnFallingSandPacket", "(Lnet/minecraft/entity/item/EntityFallingBlock;)Lnet/minecraft/network/play/server/S0EPacketSpawnObject;", false, this));
		toInject.add(new InsnNode(ARETURN));
		
		method.instructions.insert(target2, toInject);
		
		//BlockPhysics.printMethod(method);
	}

	@Override
	public void registerAdditions(ASMAdditionRegistry arg0) {}
}