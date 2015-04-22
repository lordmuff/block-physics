package com.bloodnbonesgaming.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.IClassTransformerModule;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

public class ModuleRenderFallingBlockClass implements IClassTransformerModule
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
			
			//"doRender", "(Lnet/minecraft/entity/item/EntityFallingBlock;DDDFF)V"
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
		//if (BClient.cancelRender(p_76986_1_))
		//{
		//	return;
		//}
		AbstractInsnNode target = ASMHelper.findFirstInstruction(method);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in RenderFallingBlock.doRender1");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new MethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BClient", "cancelRender", "(Lnet/minecraft/entity/item/EntityFallingBlock;)Z", false));
		LabelNode label1 = new LabelNode();
		toInject.add(new JumpInsnNode(IFEQ, label1));
		toInject.add(new InsnNode(RETURN));
		toInject.add(label1);
		
		method.instructions.insertBefore(target, toInject);
		
		//Remove:
		//if (block != null && block != world.getBlock(i, j, k))
		AbstractInsnNode target2 = ASMHelper.find(method.instructions, new VarInsnNode(ISTORE, 14));
		AbstractInsnNode start = ASMHelper.move(target2, 1);
		AbstractInsnNode end = ASMHelper.move(start, 9);
		
		if (target2 == null || start == null || end == null)
			throw new RuntimeException("Unexpected instruction pattern in RenderFallingBlock.doRender2");
		//TODO Fix this
		//ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
		
		//this.field_147920_a.renderBlockSandFalling(block, world, i, j, k, p_76986_1_.field_145814_a);
		AbstractInsnNode toReplace = ASMHelper.find(method.instructions, new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderBlocks", "func_147749_a", "(Lnet/minecraft/block/Block;Lnet/minecraft/world/World;IIII)V", false));
		//BClient.renderBlockSandFalling(this.field_147920_a, block, world, i, j, k, p_76986_1_.field_145814_a);
		AbstractInsnNode replaceWith = new MethodInsnNode(INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BClient", "renderBlockSandFalling", "(Lnet/minecraft/client/renderer/RenderBlocks;Lnet/minecraft/block/Block;Lnet/minecraft/world/World;IIII)V", false);
		
		method.instructions.set(toReplace, replaceWith);
	}
}