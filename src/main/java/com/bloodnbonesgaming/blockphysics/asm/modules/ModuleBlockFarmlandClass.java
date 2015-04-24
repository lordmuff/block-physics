package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleBlockFarmlandClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.block.BlockFarmland"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformBlockFarmlandClass";
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
		
		if (transformedName.equals("net.minecraft.block.BlockFarmland"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);
			
			//"onFallenUpon", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;F)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149746_a", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;F)V");
            if (methodNode != null)
            {
            	transformOnFallenUpon(methodNode);
            }
            else
                throw new RuntimeException("Could not find onFallenUpon method in " + transformedName);
            
            return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}
	
	public void transformOnFallenUpon(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findFirstInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockFalling.onFallenUpon");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new VarInsnNode(ILOAD, 2));
		toInject.add(new VarInsnNode(ILOAD, 3));
		toInject.add(new VarInsnNode(ILOAD, 4));
		toInject.add(new FieldInsnNode(GETSTATIC, "net/minecraft/block/Block", "field_149771_c", "Lnet/minecraft/util/RegistryNamespaced;"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/util/RegistryNamespaced", "func_148750_c", "(Ljava/lang/Object;)Ljava/lang/String;", false));
		toInject.add(new MethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "onNeighborBlockChange", "(Lnet/minecraft/world/World;IIILjava/lang/String;)V", false));
		
		method.instructions.insertBefore(target, toInject);
		
		AbstractInsnNode target2 = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target2 == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockFalling.onFallenUpon");
		
		method.instructions.insertBefore(target2, toInject);
	}

	@Override
	public void registerAdditions(ASMAdditionRegistry arg0) {}
}