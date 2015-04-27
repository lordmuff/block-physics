package com.bloodnbonesgaming.blockphysics;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import com.bloodnbonesgaming.blockphysics.util.DefinitionMaps;

public class BTickList
{
	private final ArrayList bticklist;

	public BTickList()
	{
		this.bticklist  = new ArrayList();
	}

	public void scheduleBlockMoveUpdate(final World world, final int par1, final int par2, final int par3, final String blockName, final int meta, final boolean par5)
	{
		if (DefinitionMaps.getBlockDef(blockName, meta).movenum == 0 ) {
			return;
		}

		if (this.bticklist.size() >= 10000) {
			return;
		}

		final byte var7 = 8;

		if (world.checkChunksExist(par1 - var7, par2 - var7, par3 - var7, par1 + var7, par2 + var7, par3 + var7))
		{
			final BTickListEntry var6 = new BTickListEntry(par1, par2, par3, par5, DefinitionMaps.getBlockDef(blockName, meta).tickrate + world.getWorldInfo().getWorldTime());
			this.bticklist.add(var6);
		}
	}

	public void tickMoveUpdates(final World world)
	{
		if ( BlockPhysics.skipMove ) {
			return;
		}

		int siz = this.getSize();
		if ( siz == 0 ) {
			return;
		} else if (siz > 1000) {
			siz = 1000;
		}

		//ChunkCoordIntPair chunk;

		for (int var3 = 0; var3 < siz; ++var3)
		{
			final BTickListEntry var4 = (BTickListEntry)this.bticklist.remove(0);

			if (var4.scheduledTime > world.getWorldInfo().getWorldTime() + 60 ) {
				continue;
			}

			//chunk = new ChunkCoordIntPair(var4.xCoord / 16, var4.zCoord / 16);
			
			BlockPhysics.tryToMove(world, var4.xCoord, var4.yCoord, var4.zCoord, Block.blockRegistry.getNameForObject(world.getBlock(var4.xCoord, var4.yCoord, var4.zCoord)), world.getBlockMetadata(var4.xCoord, var4.yCoord, var4.zCoord), var4.slide);
		}
	}

	public int getSize()
	{
		if (this.bticklist == null) {
			return 0;
		}
		return this.bticklist.size();
	}

	public void reset()
	{
		this.bticklist.clear();
	}
}