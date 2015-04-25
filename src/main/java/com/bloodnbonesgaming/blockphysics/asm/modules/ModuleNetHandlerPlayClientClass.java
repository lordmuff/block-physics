package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;

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

public class ModuleNetHandlerPlayClientClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.client.network.NetHandlerPlayClient"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformNetHandlerPlayClientClass";
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
		
		if (transformedName.equals("net.minecraft.client.network.NetHandlerPlayClient"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);
			
			//"handleSpawnObject", "(Lnet/minecraft/network/play/server/S0EPacketSpawnObject;)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_147235_a", "(Lnet/minecraft/network/play/server/S0EPacketSpawnObject;)V");
            if (methodNode != null)
            {
            	transformHandleSpawnObject(methodNode);
            }
            else
                throw new RuntimeException("Could not find handleSpawnObject method in " + transformedName);
            
            return ASMHelper.writeClassToBytesNoDeobfSkipFrames(classNode);
		}
		return bytes;
	}
	
	public void transformHandleSpawnObject(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.find(method.instructions, new TypeInsnNode(NEW, "net/minecraft/entity/item/EntityFallingBlock"));
		
		//object = BlockPhysics.createFallingsand(this.clientWorldController, d0, d1, d2, p_147235_1_);
		//p_147235_1_.func_149002_g(1);
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new RedirectedFieldInsnNode(GETFIELD, "net/minecraft/client/network/NetHandlerPlayClient", "field_147300_g", "Lnet/minecraft/client/multiplayer/WorldClient;", this));
		toInject.add(new VarInsnNode(DLOAD, 2));
		toInject.add(new VarInsnNode(DLOAD, 4));
		toInject.add(new VarInsnNode(DLOAD, 6));
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new RedirectedMethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "createFallingsand", "(Lnet/minecraft/world/World;DDDLnet/minecraft/network/play/server/S0EPacketSpawnObject;)Lnet/minecraft/entity/item/EntityFallingBlock;", false, this));
		toInject.add(new VarInsnNode(ASTORE, 8));
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new InsnNode(ICONST_1));
		toInject.add(new RedirectedMethodInsnNode(INVOKEVIRTUAL, "net/minecraft/network/play/server/S0EPacketSpawnObject", "func_149002_g", "(I)V", false, this));
		
		method.instructions.insertBefore(target, toInject);
		
		//Remove the calls we just replaced
		AbstractInsnNode start = ASMHelper.find(method.instructions, new TypeInsnNode(NEW, "net/minecraft/entity/item/EntityFallingBlock"));
		AbstractInsnNode end = ASMHelper.move(start, 23);
		
		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
	}

	@Override
	public void registerAdditions(ASMAdditionRegistry arg0) {}
}