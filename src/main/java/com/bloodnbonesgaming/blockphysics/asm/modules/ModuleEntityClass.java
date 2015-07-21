package com.bloodnbonesgaming.blockphysics.asm.modules;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ASMHelper;

import com.bloodnbonesgaming.blockphysics.asm.ASMPlugin;
import com.bloodnbonesgaming.lib.core.ASMAdditionRegistry;
import com.bloodnbonesgaming.lib.core.module.IClassTransformerModule;

public class ModuleEntityClass implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
				"net.minecraft.entity.Entity"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformEntityClass";
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

		if (transformedName.equals("net.minecraft.entity.Entity"))
		{
			ASMPlugin.log.info("Transforming class: " + transformedName);

			//"func_145775_I", "()V"
			final MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_145775_I", "()V");
			if (methodNode != null)
			{
				this.transformFunc_145775_I(methodNode);
			} else {
				throw new RuntimeException("Could not find func_145775_I method in " + transformedName);
			}

			return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}

	public void transformFunc_145775_I(final MethodNode method)
	{
		//Replace all 0.001D with 0.07D
		/*AbstractInsnNode toReplace = ASMHelper.find(method.instructions, new LdcInsnNode(new Double("0.001")));
		AbstractInsnNode replacement = new LdcInsnNode(new Double("0.07"));

		while (toReplace != null)
		{
			method.instructions.set(toReplace, replacement);
			toReplace = ASMHelper.find(method.instructions, new LdcInsnNode(new Double("0.001")));
		}*/

		for (int index = 0; index < method.instructions.size() - 1 ; index++ )
		{
			if ( method.instructions.get(index).getType() == AbstractInsnNode.LDC_INSN && ((LdcInsnNode)method.instructions.get(index)).cst.equals(0.001D) )
			{
				/*
				 * Equivalent to changing all 0.001D to 0.07D
				 */
				method.instructions.set( method.instructions.get(index), new LdcInsnNode(new Double("0.07")));
			}
		}
	}

	@Override
	public void registerAdditions(final ASMAdditionRegistry arg0) {}
}