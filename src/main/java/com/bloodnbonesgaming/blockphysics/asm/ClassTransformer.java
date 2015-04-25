package com.bloodnbonesgaming.blockphysics.asm;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import com.bloodnbonesgaming.blockphysics.BlockPhysics;
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
import com.bnbgaming.lib.core.ASMAdditionHelper;
import com.bnbgaming.lib.core.BNBGamingClassTransformer;
import com.bnbgaming.lib.core.module.IClassTransformerModule;
import com.google.common.collect.Lists;

public class ClassTransformer extends BNBGamingClassTransformer
{
	
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

	@Override
	public List<IClassTransformerModule> createModules() {
		return Lists.newArrayList(new ModuleBlockClass(),
				new ModuleBlockAnvilClass(),
				new ModuleBlockChestClass(),
				new ModuleBlockDispenserClass(),
				new ModuleBlockDragonEggClass(),
				new ModuleBlockFallingClass(),
				new ModuleBlockFarmlandClass(),
				new ModuleBlockFurnaceClass(),
				new ModuleBlockPistonBaseClass(),
				new ModuleBlockRailBaseClass(),
				new ModuleBlockRedstoneLightClass(),
				new ModuleBlockTNTClass(),
				new ModuleBlockWebClass(),
				new ModuleNetHandlerPlayClientClass(),
				new ModuleRenderFallingBlockClass(),
				new ModuleEntityBoatClass(),
				new ModuleEntityFallingBlockClass(),
				new ModuleEntityMinecartClass(),
				new ModuleEntityTNTPrimedClass(),
				new ModuleEntityXPOrbClass(),
				new ModuleEntityClass(),
				new ModuleEntityTrackerClass(),
				new ModuleEntityTrackerEntryClass(),
				new ModuleMinecraftServerClass(),
				new ModuleTileEntityPistonClass(),
				new ModuleAnvilChunkLoaderClass(),
				new ModuleExtendedBlockStorageClass(),
				new ModuleChunkClass(),
				new ModuleExplosionClass(),
				new ModuleWorldClass(),
				new ModuleWorldServerClass());
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		return basicClass;
	}

	@Override
	public String getName() {
		return "blockphysct";
	}

	@Override
	public void setAdditionHelper(ASMAdditionHelper helper) {
		ASMPlugin.log.info("Received helper");
		BlockPhysics.asmHelper = helper;
	}
}