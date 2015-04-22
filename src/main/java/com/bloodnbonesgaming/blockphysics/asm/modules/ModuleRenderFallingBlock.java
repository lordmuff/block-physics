package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleRenderFallingBlock implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.client.renderer.entity.RenderFallingBlock"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformRenderFallingBlockClass";
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
		
		if (transformedName.equals("net.minecraft.client.renderer.entity.RenderFallingBlock"))
		{
			ModInfo.Log.info("Transforming class: " + transformedName);
			//doRender(EntityFallingBlock, Double, Double, Double, Float, Float)V
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_76986_a", "(Lnet/minecraft/entity/item/EntityFallingBlock;DDDFF)V");
            if (methodNode != null)
            {
            	transformDoRender(methodNode);
            }
            else
                throw new RuntimeException("Could not find doRender method in " + transformedName);
            
            return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}
	
	public void transformDoRender(MethodNode method)
	{
		MethodInsnNode toFind = new MethodInsnNode(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glPushMatrix", "()V", false);
		AbstractInsnNode end = ASMHelper.find(method.instructions, toFind);
		AbstractInsnNode start = ASMHelper.move(end, -10);
		
		ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
		
		MethodInsnNode toFind2 = new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderBlocks", "func_147749_a", "(Lnet/minecraft/block/Block;Lnet/minecraft/world/World;IIII)V", false);
		AbstractInsnNode target = ASMHelper.find(method.instructions, toFind2);
		MethodInsnNode replacement = new MethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/util/RenderFallingBlocks", "renderBlockSandFalling", "(Lnet/minecraft/client/renderer/RenderBlocks;Lnet/minecraft/block/Block;Lnet/minecraft/world/World;IIII)V", false);
		
		if (toFind2 == null || replacement == null || target == null)
			throw new RuntimeException("This isn't working!");
		//TODO put in replacement code
		method.instructions.insertBefore(target, replacement);
		method.instructions.remove(target);
		
		//MethodInsnNode toInject = new MethodInsnNode(INVOKESTATIC, "net/minecraft/client/renderer/RenderHelper", "func_74518_a", "()V", false);
		//AbstractInsnNode target2 = ASMHelper.find(method.instructions, new MethodInsnNode(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glDisable", "(I)V", false));
		
		//method.instructions.insert(target2, toInject);
	}
}