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
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleBlockTNTClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.block.BlockTNT"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformBlockTNTClass";
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
		
		if (transformedName.equals("net.minecraft.block.BlockTNT"))
		{
			ModInfo.Log.info("Transforming class: " + transformedName);
			
			//"onNeighborBlockChange", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149695_a", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;)V");
            if (methodNode != null)
            {
            	transformOnNeighborBlockChange(methodNode);
            }
            else
                throw new RuntimeException("Could not find onNeighborBlockChange method in " + transformedName);
            
            //"onBlockDestroyedByPlayer", "(Lnet/minecraft/world/World;IIII)V"
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149664_b", "(Lnet/minecraft/world/World;IIII)V");
            if (methodNode != null)
            {
            	transformOnBlockDestroyedByPlayer(methodNode);
            }
            else
                throw new RuntimeException("Could not find onBlockDestroyedByPlayer method in " + transformedName);
            
            return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}
	
	public void transformOnNeighborBlockChange(MethodNode method)
	{
		//BlockPhysics.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, Block.blockRegistry.getNameForObject(this));
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);

		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockTNT.onNeighborBlockChange");

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
	}
	
	public void transformOnBlockDestroyedByPlayer(MethodNode method)
	{
		//BlockPhysics.onBlockDestroyedByPlayer(p_149664_1_, p_149664_2_, p_149664_3_, p_149664_4_, p_149664_5_, Block.blockRegistry.getNameForObject(this));
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in BlockTNT.onBlockDestroyedByPlayer");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new VarInsnNode(ILOAD, 2));
		toInject.add(new VarInsnNode(ILOAD, 3));
		toInject.add(new VarInsnNode(ILOAD, 4));
		toInject.add(new VarInsnNode(ILOAD, 5));
		toInject.add(new FieldInsnNode(GETSTATIC, "net/minecraft/block/Block", "field_149771_c", "Lnet/minecraft/util/RegistryNamespaced;"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/util/RegistryNamespaced", "func_148750_c", "(Ljava/lang/Object;)Ljava/lang/String;", false));
		toInject.add(new MethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "onBlockDestroyedByPlayer", "(Lnet/minecraft/world/World;IIIILjava/lang/String;)V", false));
		
		method.instructions.insertBefore(target, toInject);
	}
}