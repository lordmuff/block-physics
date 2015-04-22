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

public class ModuleBlockClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.block.Block"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformBlockClass";
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
		
		if (transformedName.equals("net.minecraft.block.Block"))
		{
			ModInfo.Log.info("Transforming class: " + transformedName);
			
			//"onBlockDestroyedByPlayer", "(Lnet/minecraft/world/World;IIII)V"
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149664_b", "(Lnet/minecraft/world/World;IIII)V");
            if (methodNode != null)
            {
            	transformOnBlockDestroyedByPlayer(methodNode);
            }
            else
                throw new RuntimeException("Could not find onBlockDestroyedByPlayer method in " + transformedName);
            //"onNeighborBlockChange", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149695_a", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;)V");
            if (methodNode != null)
            {
            	transformOnNeighborBlockChange(methodNode);
            }
            else
            	throw new RuntimeException("Could not find onNeighborBlockChange method in " + transformedName);
            //"onEntityWalking", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149724_b", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V");
            if (methodNode != null)
            {
            	transformOnEntityWalking(methodNode);
            }
            else
            	throw new RuntimeException("Could not find onEntityWalking method in " + transformedName);
            //"onEntityCollidedWithBlock", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149670_a", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V");
            if (methodNode != null)
            {
            	trasnformOnEntityCollidedWithBlock(methodNode);
            }
            else
            	throw new RuntimeException("Could not find onEntityCollidedWithBlock method in " + transformedName);
            //"onPostBlockPlaced", "(Lnet/minecraft/world/World;IIII)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149714_e", "(Lnet/minecraft/world/World;IIII)V");
            if (methodNode != null)
            {
            	transformOnPostBlockPlaced(methodNode);
            }
            else
            	throw new RuntimeException("Could not find onPostBlockPlaced method in " + transformedName);
            //"onFallenUpon", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;F)V"
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149746_a", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;F)V");
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
	
	public void transformOnBlockDestroyedByPlayer(MethodNode method)
	{
		//BlockPhysics.onBlockDestroyedByPlayer(p_149664_1_, p_149664_2_, p_149664_3_, p_149664_4_, p_149664_5_, Block.blockRegistry.getNameForObject(this));
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in Block.onBlockDestroyedByPlayer");
		
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
	
	public void transformOnNeighborBlockChange(MethodNode method)
	{
		//BlockPhysics.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, Block.blockRegistry.getNameForObject(this));
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in Block.onNeighborBlockChange");
		
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
	
	public void transformOnEntityWalking(MethodNode method)
	{
		//BlockPhysics.onNeighborBlockChange(p_149724_1_, p_149724_2_, p_149724_3_, p_149724_4_, Block.blockRegistry.getNameForObject(this));
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in Block.onEntityWalking");
		
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
	
	public void trasnformOnEntityCollidedWithBlock(MethodNode method)
	{
		//BlockPhysics.onEntityCollidedWithBlock(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_, Block.blockRegistry.getNameForObject(this), p_149670_5_);
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in Block.onEntityCollidedWithBlock");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new VarInsnNode(ILOAD, 2));
		toInject.add(new VarInsnNode(ILOAD, 3));
		toInject.add(new VarInsnNode(ILOAD, 4));
		toInject.add(new FieldInsnNode(GETSTATIC, "net/minecraft/block/Block", "field_149771_c", "Lnet/minecraft/util/RegistryNamespaced;"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/util/RegistryNamespaced", "func_148750_c", "(Ljava/lang/Object;)Ljava/lang/String;", false));
		toInject.add(new VarInsnNode(ALOAD, 5));
		toInject.add(new MethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "onEntityCollidedWithBlock", "(Lnet/minecraft/world/World;IIILjava/lang/String;Lnet/minecraft/entity/Entity;)V", false));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void transformOnPostBlockPlaced(MethodNode method)
	{
		//BlockPhysics.onPostBlockPlaced(p_149714_1_, p_149714_2_, p_149714_3_, p_149714_4_, Block.blockRegistry.getNameForObject(this), p_149714_5_);
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in Block.onPostBlockPlaced");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new VarInsnNode(ILOAD, 2));
		toInject.add(new VarInsnNode(ILOAD, 3));
		toInject.add(new VarInsnNode(ILOAD, 4));
		toInject.add(new FieldInsnNode(GETSTATIC, "net/minecraft/block/Block", "field_149771_c", "Lnet/minecraft/util/RegistryNamespaced;"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/util/RegistryNamespaced", "func_148750_c", "(Ljava/lang/Object;)Ljava/lang/String;", false));
		toInject.add(new VarInsnNode(ILOAD, 5));
		toInject.add(new MethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BlockPhysics", "onPostBlockPlaced", "(Lnet/minecraft/world/World;IIILjava/lang/String;I)V", false));
		
		method.instructions.insertBefore(target, toInject);
	}

	public void transformOnFallenUpon(MethodNode method)
	{
		//BlockPhysics.onNeighborBlockChange(p_149724_1_, p_149724_2_, p_149724_3_, p_149724_4_, Block.blockRegistry.getNameForObject(this));
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);

		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in Block.onEntityWalking");

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
}