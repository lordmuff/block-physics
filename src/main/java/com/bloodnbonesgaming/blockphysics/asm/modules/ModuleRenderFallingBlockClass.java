package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bnbgaming.lib.core.ASMAdditionRegistry;
import com.bnbgaming.lib.core.insn.RedirectedMethodInsnNode;
import com.bnbgaming.lib.core.module.IClassTransformerModule;

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
	public byte[] transform(final String name, final String transformedName, final byte[] bytes)
	{
		final ClassNode classNode = ASMHelper.readClassFromBytes(bytes);

		if (transformedName.equals("net.minecraft.client.renderer.entity.RenderFallingBlock"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);

			//"doRender", "(Lnet/minecraft/entity/item/EntityFallingBlock;DDDFF)V"
			final MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_76986_a", "(Lnet/minecraft/entity/item/EntityFallingBlock;DDDFF)V");
			if (methodNode != null)
			{
				this.transformDoRender(methodNode);
			} else {
				throw new RuntimeException("Could not find doRender method in " + transformedName);
			}

			return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}

	public void transformDoRender(final MethodNode method)
	{
		//if (BClient.cancelRender(p_76986_1_))
		//{
		//	return;
		//}
		final AbstractInsnNode target = ASMHelper.findFirstInstruction(method);

		if (target == null) {
			throw new RuntimeException("Unexpected instruction pattern in RenderFallingBlock.doRender1");
		}

		final InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
		toInject.add(new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BClient", "cancelRender", "(Lnet/minecraft/entity/item/EntityFallingBlock;)Z", false, this));
		final LabelNode label1 = new LabelNode();
		toInject.add(new JumpInsnNode(Opcodes.IFEQ, label1));
		toInject.add(new InsnNode(Opcodes.RETURN));
		toInject.add(label1);

		method.instructions.insertBefore(target, toInject);

		//Remove:
		//if (block != null && block != world.getBlock(i, j, k))
		final AbstractInsnNode target2 = ASMHelper.find(method.instructions, new VarInsnNode(Opcodes.ISTORE, 14));
		final AbstractInsnNode start = ASMHelper.move(target2, 1);
		final AbstractInsnNode end = ASMHelper.move(start, 9);

		if (target2 == null || start == null || end == null)
		{
			throw new RuntimeException("Unexpected instruction pattern in RenderFallingBlock.doRender2");
			//TODO Fix this
			//ASMHelper.removeFromInsnListUntil(method.instructions, start, end);
		}

		//this.field_147920_a.renderBlockSandFalling(block, world, i, j, k, p_76986_1_.field_145814_a);
		final AbstractInsnNode toReplace = ASMHelper.find(method.instructions, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderBlocks", "func_147749_a", "(Lnet/minecraft/block/Block;Lnet/minecraft/world/World;IIII)V", false));
		//BClient.renderBlockSandFalling(this.field_147920_a, block, world, i, j, k, p_76986_1_.field_145814_a);
		final AbstractInsnNode replaceWith = new RedirectedMethodInsnNode(Opcodes.INVOKESTATIC, ModInfo.MAIN_PACKACE + "/blockphysics/BClient", "renderBlockSandFalling", "(Lnet/minecraft/client/renderer/RenderBlocks;Lnet/minecraft/block/Block;Lnet/minecraft/world/World;IIII)V", false, this);

		method.instructions.set(toReplace, replaceWith);
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0) {}
}