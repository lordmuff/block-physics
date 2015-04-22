package com.bloodnbonesgaming.blockphysics.asm;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleAnvilChunkLoaderClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleBlockAnvilClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleBlockChestClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleBlockClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleBlockDispenserClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleBlockDragonEggClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleBlockFallingClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleBlockFarmlandClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleBlockFurnaceClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleBlockPistonBaseClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleBlockRailBaseClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleBlockRedstoneLightClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleBlockTNTClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleBlockWebClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleChunkClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleEntityBoatClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleEntityClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleEntityFallingBlockClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleEntityMinecartClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleEntityTNTPrimedClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleEntityTrackerClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleEntityTrackerEntryClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleEntityXPOrbClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleExplosionClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleExtendedBlockStorageClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleMinecraftServerClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleNetHandlerPlayClientClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleRenderFallingBlockClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleTileEntityPistonClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleWorldClass;
import com.bloodnbonesgaming.blockphysics.asm.modules.ModuleWorldServerClass;

public class ClassTransformer implements IClassTransformer
{
	private static final List<IClassTransformerModule> transformerModules = new ArrayList<IClassTransformerModule>();
	static
	{
		registerTransformerModule(new ModuleBlockClass());
		registerTransformerModule(new ModuleBlockAnvilClass());
		registerTransformerModule(new ModuleBlockChestClass());
		registerTransformerModule(new ModuleBlockDispenserClass());
		registerTransformerModule(new ModuleBlockDragonEggClass());
		registerTransformerModule(new ModuleBlockFallingClass());
		registerTransformerModule(new ModuleBlockFarmlandClass());
		registerTransformerModule(new ModuleBlockFurnaceClass());
		registerTransformerModule(new ModuleBlockPistonBaseClass());
		registerTransformerModule(new ModuleBlockRailBaseClass());
		registerTransformerModule(new ModuleBlockRedstoneLightClass());
		registerTransformerModule(new ModuleBlockTNTClass());
		registerTransformerModule(new ModuleBlockWebClass());
		registerTransformerModule(new ModuleNetHandlerPlayClientClass());
		registerTransformerModule(new ModuleRenderFallingBlockClass());
		registerTransformerModule(new ModuleEntityBoatClass());
		registerTransformerModule(new ModuleEntityFallingBlockClass());
		registerTransformerModule(new ModuleEntityMinecartClass());
		registerTransformerModule(new ModuleEntityTNTPrimedClass());
		registerTransformerModule(new ModuleEntityXPOrbClass());
		registerTransformerModule(new ModuleEntityClass());
		registerTransformerModule(new ModuleEntityTrackerClass());
		registerTransformerModule(new ModuleEntityTrackerEntryClass());
		registerTransformerModule(new ModuleMinecraftServerClass());
		registerTransformerModule(new ModuleTileEntityPistonClass());
		registerTransformerModule(new ModuleAnvilChunkLoaderClass());
		registerTransformerModule(new ModuleExtendedBlockStorageClass());
		registerTransformerModule(new ModuleChunkClass());
		registerTransformerModule(new ModuleExplosionClass());
		registerTransformerModule(new ModuleWorldClass());
		registerTransformerModule(new ModuleWorldServerClass());
	}

	public static void registerTransformerModule(IClassTransformerModule transformerModule)
	{
		transformerModules.add(transformerModule);
	}

	public static void disableTransformerModule(String name)
	{
		IClassTransformerModule moduleToRemove = null;
		for (IClassTransformerModule transformerModule : transformerModules)
		{
			if (transformerModule.getModuleName().equals(name))
			{
				moduleToRemove = transformerModule;
				break;
			}
		}
		if (moduleToRemove != null)
			transformerModules.remove(moduleToRemove);
	}

	public static IClassTransformerModule[] getTransformerModules()
	{
		return transformerModules.toArray(new IClassTransformerModule[0]);
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		for (IClassTransformerModule transformerModule : transformerModules)
		{
			for (String classToTransform : transformerModule.getClassesToTransform())
			{
				if (classToTransform.equals(transformedName))
				{
					basicClass = transformerModule.transform(name, transformedName, basicClass);
				}
			}
		}
		return basicClass;
	}
	
	public static FieldNode findFieldNodeOfClass(ClassNode classNode, String fieldName, String fieldDesc)
	{
		for (FieldNode field : classNode.fields)
		{
			if (field.name.equals(fieldName) && (fieldDesc == null || field.desc.equals(fieldDesc)))
			{
				return field;
			}
		}
		return null;
	}
}