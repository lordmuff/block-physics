package com.bloodnbonesgaming.blockphysics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.bloodnbonesgaming.blockphysics.util.DefinitionMaps;
import com.bloodnbonesgaming.blockphysics.util.TypeHelper;
import com.bloodnbonesgaming.lib.BNBGamingMod;
import com.bloodnbonesgaming.lib.core.ASMAdditionHelper;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = ModInfo.MODID, version = ModInfo.VERSION, dependencies = "required-after:BNBGamingLib@[1.0.1,)")
public class BlockPhysics extends BNBGamingMod
{
	@Instance("BlockPhysics")
	public static BlockPhysics instance;

	public static ASMAdditionHelper asmHelper;

	@EventHandler
	public void preInit(final FMLPreInitializationEvent event)
	{
		ModConfig.init(event.getSuggestedConfigurationFile());
		//MinecraftForge.EVENT_BUS.register(new BPEventHandler());
		//FMLCommonHandler.instance().bus().register(new BPEventHandler());
	}

	@EventHandler
	public void init(final FMLInitializationEvent event)
	{
	}

	@EventHandler
	public void postInit(final FMLPostInitializationEvent event)
	{
	}

	@EventHandler
	public void serverStarting(final FMLServerStartingEvent event)
	{
	}

	public static void printMethod(final MethodNode method)
	{
		BlockPhysics.instance.log.info("Printing: " + method.name);
		for (int i = 0; i < method.instructions.size() - 1; i++)
		{
			final AbstractInsnNode node = method.instructions.get(i);

			if (node instanceof LineNumberNode)
			{
				final LineNumberNode nodey = (LineNumberNode)node;
				BlockPhysics.instance.log.info("DEBUG111 - LineNumber: " + nodey.line);
			}
			else if (node instanceof MethodInsnNode)
			{
				final MethodInsnNode nodey = (MethodInsnNode)node;
				BlockPhysics.instance.log.info("DEBUG111 - MethodNode: " + nodey.owner + " : " + nodey.name + " : " + nodey.desc + " : " + nodey.getOpcode());
			}
			else if (node instanceof VarInsnNode)
			{
				final VarInsnNode nodey = (VarInsnNode)node;
				BlockPhysics.instance.log.info("DEBUG111 - VarNode: " + nodey.getOpcode() + " : " + nodey.var);
			}
			else if (node instanceof FieldInsnNode)
			{
				final FieldInsnNode nodey = (FieldInsnNode)node;
				BlockPhysics.instance.log.info("DEBUG111 - FieldNode: " + nodey.owner + " : " + nodey.name + " : " + nodey.desc);
			}
			else if (node instanceof LdcInsnNode)
			{
				final LdcInsnNode nodey = (LdcInsnNode)node;
				BlockPhysics.instance.log.info("DEBUG111 - LdcNode: " + nodey.getOpcode() + " : " + nodey.cst);
			}
			else if (node instanceof TypeInsnNode)
			{
				final TypeInsnNode nodey = (TypeInsnNode)node;
				BlockPhysics.instance.log.info("DEBUG111 - TypeNode: " + nodey.getOpcode() + " : " + nodey.desc);
			}
			else if (node instanceof FrameNode)
			{
				final FrameNode nodey = (FrameNode)node;
				BlockPhysics.instance.log.info("DEBUG111 - FrameNode: " + nodey.getOpcode() + " : " + nodey.type);
			}
			else if (node instanceof JumpInsnNode)
			{
				final JumpInsnNode nodey = (JumpInsnNode)node;
				BlockPhysics.instance.log.info("DEBUG111 - JumpNode: " + nodey.getOpcode() + " : " + nodey.label);
			}
			else if (node instanceof LabelNode)
			{
				final LabelNode nodey = (LabelNode)node;
				BlockPhysics.instance.log.info("DEBUG111 - LabelNode: " + nodey);
			}
			else if (node instanceof IntInsnNode)
			{
				final IntInsnNode nodey = (IntInsnNode)node;
				BlockPhysics.instance.log.info("DEBUG111 - IntNode: " + nodey.getOpcode() + " : " + nodey.operand);
			}
			else
			{
				BlockPhysics.instance.log.info("DEBUG111 - SomethingElse");
			}
		}
	}

	public static boolean skipTick;




	//Everything below here needs to be reorganised into proper classes
	protected static int updateLCG = new Random().nextInt();
	final static double[][][] slideSpeedz = {
		{{-0.125D,-0.005D,0D},{0D,-0.005D,-0.125D},{0D,-0.005D,0.125D},{0.125D,-0.005D,0D}},
		{{-0.25D,-0.06D,0D},{0D,-0.06D,-0.25D},{0D,-0.06D,0.25D},{0.25D,-0.06D,0D}},
		{{-0.3D,-0.25D,0D},{0D,-0.25D,-0.3D},{0D,-0.25D,0.3D},{0.3D,-0.25D,0D}},
		{{-0.18D,-0.35D,0D},{0D,-0.35D,-0.18D},{0D,-0.35D,0.18D},{0.18D,-0.35D,0D}},
		{{-0.05D,-0.15D,0D},{0D,-0.15D,-0.05D},{0D,-0.15D,0.05D},{0.05D,-0.15D,0D}}
	};

	protected static Random rand = new Random();
	public static boolean skipMove = false;
	static int nextrand = 0;

	final static byte prand[] = {
		71, 65, 10, 40, 75, 97, 98, 39, 50, 47, 52, 65, 22, 32, 46, 86, 84, 22, 10, 41,
		45, 15, 65, 67, 91, 28, 83, 49, 83, 13, 77, 89, 90, 38, 67, 69, 36, 30, 1, 41,
		30, 79, 87, 95, 48, 14, 42, 8, 19, 22, 73, 84, 99, 7, 7, 72, 15, 63, 94, 34, 27,
		31, 79, 85, 62, 68, 11, 86, 10, 83, 54, 4, 74, 78, 45, 26, 56, 7, 45, 25, 58, 90,
		79, 68, 21, 62, 1, 89, 32, 5, 17, 65, 59, 34, 71, 87, 0, 39, 5, 32, 43, 64, 78,
		27, 24, 2, 53, 37, 63, 57, 31, 51, 51, 49, 47, 42, 11, 31, 26, 41, 2, 10, 60, 1,
		21, 45, 14, 18, 76, 75, 29, 32, 36, 60, 88, 51, 62, 55, 57, 3, 20, 80, 2, 0, 35,
		42, 57, 34, 56, 89, 53, 42, 57, 55, 61, 11, 1, 26, 74, 35, 33, 17, 8, 16, 98, 5,
		35, 76, 53, 58, 95, 55, 98, 70, 60, 24, 12, 39, 93, 43, 35, 66, 78, 87, 25, 20,
		68, 33, 84, 6, 23, 13, 24, 20, 30, 0, 46, 49, 38, 76, 70, 49, 85, 31, 72, 18, 50,
		88, 4, 18, 75, 96, 43, 28, 93, 38, 21, 71, 69, 19, 53, 91, 48, 29, 88, 89, 37,
		59, 68, 93, 66, 44, 99, 40, 31, 27, 56, 46, 12, 92, 60, 30, 76, 26, 9, 99, 36,
		77, 70, 9, 29, 12, 66, 3, 33, 43, 19, 3, 97, 81, 67, 72, 97, 32, 28, 96, 62, 71,
		74, 61, 80, 93, 61, 57, 46, 18, 34, 79, 18, 48, 86, 94, 61, 6, 97, 17, 81, 68,
		51, 29, 17, 92, 82, 62, 91, 39, 36, 64, 41, 85, 56, 66, 13, 59, 69, 37, 3, 76,
		2, 28, 21, 36, 54, 49, 64, 87, 63, 23, 10, 78, 23, 8, 74, 54, 33, 86, 25, 44,
		83, 6, 14, 3, 50, 38, 73, 5, 65, 55, 9, 15, 82, 82, 22, 99, 22, 2, 52, 81, 16,
		27, 90, 75, 67, 60, 40, 52, 0, 29, 73, 26, 69, 5, 44, 50, 4, 0, 59, 82, 40, 17,
		75, 12, 13, 99, 73, 72, 4, 25, 29, 55, 77, 80, 46, 74, 92, 44, 85, 88, 48, 84,
		71, 90, 91, 6, 7, 78, 97, 20, 45, 11, 24, 4, 34, 59, 92, 80, 30, 40, 33, 7, 37,
		43, 8, 13, 14, 54, 84, 12, 23, 86, 56, 9, 89, 73, 53, 8, 9, 93, 81, 85, 96, 28,
		20, 14, 64, 80, 19, 51, 79, 16, 82, 19, 16, 38, 21, 63, 83, 98, 69, 77, 81, 77,
		42, 35, 95, 58, 1, 94, 72, 15, 95, 48, 6, 44, 98, 91, 52, 67, 27, 96, 47, 88, 96,
		15, 90, 25, 50, 61, 47, 66, 94, 16, 64, 87, 39, 58, 52, 47, 41, 58, 63, 70, 54,
		37, 94, 23, 70, 11, 24, 95
	};

	public static boolean setBlockBPdata( final World world, final int par1, final int par2, final int par3, final int par4 )
	{
		if (par1 >= -30000000 && par3 >= -30000000 && par1 < 30000000 && par3 < 30000000)
		{
			if (par2 < 0)
			{
				return false;
			}
			else if (par2 >= 256)
			{
				return false;
			}
			else
			{
				final Chunk chunk = world.getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
				final int j1 = par1 & 15;
				final int k1 = par3 & 15;
				return (Boolean) BlockPhysics.asmHelper.invoke(chunk, "setBlockBPdata", j1, par2, k1, par4);
			}
		}
		else
		{
			return false;
		}
	}

	public static void onBlockDestroyedByPlayer(final World par1World, final int par2, final int par3, final int par4, int meta, final String blockName)
	{
		if (!par1World.isRemote)
		{
			meta &= 15;
			if (DefinitionMaps.getBlockDef(blockName, meta).movenum == 2 && DefinitionMaps.getBlockDef(blockName, meta).movechanger > 1 ) {
				BlockPhysics.moveChangeMechanic(par1World, par2, par3, par4, blockName, 1, 0);
			}
			BlockPhysics.notifyMove(par1World, par2, par3, par4);
		}
	}

	public static void notifyMove(final World world, final int i, final int j, final int k)
	{
		for (int i1 = i-1; i1 <= i + 1; i1++)
		{
			for (int j1 = j-1; j1 <= j + 1; j1++)
			{
				for (int k1 = k-1; k1 <= k + 1; k1++) {
					((BTickList)BlockPhysics.asmHelper.get(world, "moveTickList")).scheduleBlockMoveUpdate(world, i1, j1, k1, Block.blockRegistry.getNameForObject(world.getBlock(i1, j1, k1)), world.getBlockMetadata(i1, j1, k1), false);
				}
			}
		}
	}

	public static boolean tryToMove(final World world, final int i, int j, final int k, String blid, final int meta, final boolean contslide)
	{
		if ( world.isRemote ) {
			return false;
		}
		if (DefinitionMaps.getBlockDef(blid, meta).movenum == 0 ) {
			return false;
		}

		final int players = world.playerEntities.size();
		if (players == 0) {
			return false;
		}

		if (Block.blockRegistry.getObject(blid) instanceof BlockPistonBase && !BlockPhysics.canmove(world,i,j,k, (BlockPistonBase) Block.blockRegistry.getObject(blid)) ) {
			return false;
		}

		boolean outofRenderRange = true;
		boolean outofFallRange = true;

		int ii;
		for ( ii = 0; ii < players; ii++ )
		{
			final EntityPlayer entityplayer = (EntityPlayer)world.playerEntities.get(ii);
			if ( Math.abs( i - MathHelper.floor_double(entityplayer.posX) ) <= ModConfig.fallRange && Math.abs( k - MathHelper.floor_double(entityplayer.posZ) ) <= ModConfig.fallRange )
			{
				outofFallRange = false;
				break;
			}
		}

		if ( outofFallRange ) {
			return false;
		}

		int move = 0;
		if (DefinitionMaps.getBlockDef(blid, meta).movenum == 2)
		{
			if ( (BlockPhysics.getBlockBPdata( world,i, j, k) &15) >= DefinitionMaps.getBlockDef(blid, meta).moveflipnumber) {
				move = 1;
			}
		}

		String movedefnum = DefinitionMaps.getBlockDef(blid, meta).movedefs[move];

		if (DefinitionMaps.getMovedef(movedefnum).floatingRadius > 0 )
		{
			if (BlockPhysics.floating(world,i,j,k,DefinitionMaps.getMovedef(movedefnum).floatingRadius,DefinitionMaps.getMovedef(movedefnum).floatingBlock)) {
				return false;
			}
			move = 1;
			movedefnum = DefinitionMaps.getBlockDef(blid, meta).movedefs[move];
			BlockPhysics.setBlockBPdata( world, i, j, k, 15);
		}

		if (DefinitionMaps.getMovedef(movedefnum).movetype == 3 )
		{
			if ( BlockPhysics.canMoveTo(world, i, j - 1, k, DefinitionMaps.getBlockDef(blid, meta).mass/10) )
			{
				int sv = DefinitionMaps.getMovedef(movedefnum).hanging;
				if ( sv > 0 && BlockPhysics.hanging(world, i, j, k, sv, blid, meta)) {
					return false;
				}
				sv = DefinitionMaps.getMovedef(movedefnum).attached;
				if ( sv > 0 && BlockPhysics.attached(world, i, j, k, sv, blid, meta)) {
					return false;
				}
				sv = DefinitionMaps.getMovedef(movedefnum).ncorbel;
				if ( sv > 0 && BlockPhysics.ncorbel(world, i, j, k, sv)) {
					return false;
				}
				sv = DefinitionMaps.getMovedef(movedefnum).corbel;
				if ( sv > 0 && BlockPhysics.corbel(world, i, j, k, sv, blid, meta)) {
					return false;
				}
				if ( DefinitionMaps.getMovedef(movedefnum).ceiling && BlockPhysics.ceiling(world, i, j, k)) {
					return false;
				}
				sv = DefinitionMaps.getMovedef(movedefnum).smallarc;
				if ( sv > 0 && BlockPhysics.smallArc(world, i, j, k, sv)) {
					return false;
				}
				sv = DefinitionMaps.getMovedef(movedefnum).bigarc;
				if ( sv > 0 && BlockPhysics.bigArc(world, i, j, k, sv)) {
					return false;
				}
				if (DefinitionMaps.getMovedef(movedefnum).branch && BlockPhysics.branch(world, i, j, k, blid, meta) ) {
					return false;
				}

				final Block block = (Block) Block.blockRegistry.getObject(blid);

				if ( block.hasTileEntity(meta) )
				{
					//TODO I hope this works....
					final TileEntity tileEntity = world.getTileEntity(i, j, k);
					
					if (tileEntity != null)
					{
						final NBTTagCompound nnn = new NBTTagCompound();
						tileEntity.writeToNBT(nnn);
						BlockPhysics.dropItemsNBT(world, i, j, k, nnn);
						world.removeTileEntity(i, j, k);
					}
				}

				block.dropBlockAsItem(world, i, j, k, meta, 0);

				world.setBlockToAir(i, j, k);

				world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

				return true;
			} else {
				return false;
			}
		}


		for (int iii = ii; iii < players; iii++)
		{
			final EntityPlayer entityplayer = (EntityPlayer)world.playerEntities.get(iii);

			if( Math.abs( i - MathHelper.floor_double(entityplayer.posX) ) <= ModConfig.fallRenderRange && Math.abs( k - MathHelper.floor_double(entityplayer.posZ) ) <= ModConfig.fallRenderRange )
			{
				if ( MathHelper.floor_double(entityplayer.posY) - j <= ModConfig.fallRenderRange )
				{
					outofRenderRange = false;
					break;
				}
			}
		}

		if ( !outofRenderRange && (Integer)BlockPhysics.asmHelper.get(((WorldServer)world).getEntityTracker(), "movingblocks") >= ModConfig.maxMovingBlocks ) {
			return false;
		}

		int movetype;
		if ( contslide ) {
			movetype = 2;
		} else {
			movetype = DefinitionMaps.getMovedef(movedefnum).movetype;
		}

		if ( movetype == 0) {
			return false;
		}
		final int ms = DefinitionMaps.getBlockDef(blid,meta).mass/10;
		final boolean canfall = BlockPhysics.canMoveTo(world, i, j - 1, k, ms);

		if ( !canfall )
		{
			if ( movetype == 1 ) {
				return false;
			}
			if ( DefinitionMaps.getMovedef(movedefnum).slidechance != 100 && ( DefinitionMaps.getMovedef(movedefnum).slidechance == 0 || DefinitionMaps.getMovedef(movedefnum).slidechance < BlockPhysics.prandnextint(100) + 1 )) {
				return false;
			}
		}

		if ( !contslide )
		{
			int sv = DefinitionMaps.getMovedef(movedefnum).hanging;
			if ( sv > 0 && BlockPhysics.hanging(world,i,j,k,sv,blid,meta)) {
				return false;
			}
			sv = DefinitionMaps.getMovedef(movedefnum).attached;
			if ( sv > 0 && BlockPhysics.attached(world, i, j, k, sv, blid, meta)) {
				return false;
			}
			sv = DefinitionMaps.getMovedef(movedefnum).ncorbel;
			if ( sv > 0 && BlockPhysics.ncorbel(world, i, j, k, sv)) {
				return false;
			}
			sv = DefinitionMaps.getMovedef(movedefnum).corbel;
			if ( sv > 0 && BlockPhysics.corbel(world, i, j, k, sv, blid, meta)) {
				return false;
			}
			if ( DefinitionMaps.getMovedef(movedefnum).ceiling && BlockPhysics.ceiling(world, i, j, k)) {
				return false;
			}
			sv = DefinitionMaps.getMovedef(movedefnum).smallarc;
			if ( sv > 0 && BlockPhysics.smallArc(world, i, j, k, sv)) {
				return false;
			}
			sv = DefinitionMaps.getMovedef(movedefnum).bigarc;
			if ( sv > 0 && BlockPhysics.bigArc(world, i, j, k, sv)) {
				return false;
			}
			if (DefinitionMaps.getMovedef(movedefnum).branch && BlockPhysics.branch(world, i, j, k, blid, meta)) {
				return false;
			}
		}

		final boolean canslide[] = new boolean[4];
		if ( movetype == 2 && !canfall )
		{
			canslide[0] = BlockPhysics.canMoveTo(world, i - 1, j - 1, k, ms);
			canslide[1] = BlockPhysics.canMoveTo(world, i, j - 1, k - 1, ms);
			canslide[2] = BlockPhysics.canMoveTo(world, i, j - 1, k + 1, ms);
			canslide[3] = BlockPhysics.canMoveTo(world, i + 1, j - 1, k, ms);
			if (!(canslide[0] || canslide[1] || canslide[2] || canslide[3])) {
				return false;
			}

			if (canslide[0]) {
				canslide[0] = BlockPhysics.canMoveTo(world, i - 1, j, k, ms);
			}
			if (canslide[1]) {
				canslide[1] = BlockPhysics.canMoveTo(world, i, j, k - 1, ms);
			}
			if (canslide[2]) {
				canslide[2] = BlockPhysics.canMoveTo(world, i, j, k + 1, ms);
			}
			if (canslide[3]) {
				canslide[3] = BlockPhysics.canMoveTo(world, i + 1, j, k, ms);
			}
			if (!(canslide[0] || canslide[1] || canslide[2] || canslide[3])) {
				return false;
			}
		}

		if (blid.equals(Block.blockRegistry.getNameForObject(Blocks.grass)) || blid.equals(Block.blockRegistry.getNameForObject(Blocks.farmland)) || blid.equals(Block.blockRegistry.getNameForObject(Blocks.mycelium)))
		{
			blid = Block.blockRegistry.getNameForObject(Blocks.dirt);
		}

		if ( outofRenderRange )
		{
			final int bpdata = BlockPhysics.getBlockBPdata( world,i, j, k);
			world.setBlockToAir(i, j, k);
			BlockPhysics.setBlockBPdata( world,i, j, k, 0);
			BlockPhysics.notifyMove(world, i, j, k);
			int jv = j;
			if (canfall)
			{
				for (; BlockPhysics.canMoveTo(world, i, jv - 1, k, ms) && jv > 0; jv--) {
					;
				}
				if (jv > 0)
				{
					world.setBlock(i, jv, k, (Block)Block.blockRegistry.getObject(blid), meta, 3);
					BlockPhysics.setBlockBPdata( world,i, jv, k, bpdata);
					BlockPhysics.notifyMove(world, i, jv, k);
				}
			}
			else
			{
				final byte slide[] = {0,0,0,0};
				byte count = 0;
				for (byte si = 0; si < 4; si++)
				{
					if (canslide[si])
					{
						slide[count] = si;
						count++;
					}
				}

				int id = 0;
				int kd = 0;
				int rr = 0;
				if ( count > 1 ) {
					rr = BlockPhysics.prandnextint(count);
				}
				switch (slide[rr])
				{
					case 0:
						id = - 1;
						break;
					case 1:
						kd = - 1;
						break;
					case 2:
						kd = + 1;
						break;
					case 3:
						id = + 1;
						break;
				}
				final int iv = i + id, kv = k + kd;
				for (; BlockPhysics.canMoveTo(world, iv, jv - 1, kv, ms) && jv > 0; jv--) {
					;
				}
				if (jv > 0)
				{
					world.setBlock(iv, jv, kv, (Block)Block.blockRegistry.getObject(blid), meta, 3);
					BlockPhysics.setBlockBPdata( world,iv, jv, kv, bpdata);
					BlockPhysics.notifyMove(world, iv, jv, kv);
				}
			}
			j++;
			BlockPhysics.tryToMove( world, i, j, k, Block.blockRegistry.getNameForObject(world.getBlock( i, j , k )), world.getBlockMetadata( i, j , k ),false );
			return true;
		}

		if ( canfall )
		{
			//int metadata = world.getBlockMetadata(i, j, k);
			final EntityFallingBlock entityfallingsand = new EntityFallingBlock(world, 0.5D + i, 0.5D + j, 0.5D + k, (Block)Block.blockRegistry.getObject(blid), meta);
			
			if ( ((Block)Block.blockRegistry.getObject(blid)).hasTileEntity(meta) )
			{
				final TileEntity tileEntity = world.getTileEntity(i, j, k);
				if (tileEntity != null)
				{
					entityfallingsand.field_145810_d = new NBTTagCompound();
					tileEntity.writeToNBT(entityfallingsand.field_145810_d);
					world.removeTileEntity(i, j, k);
				}
			}
			if (BlockPhysics.canBurn(blid) && world.getBlock(i, j+1, k) == Blocks.fire) {
				entityfallingsand.setFire(60);
			}
			BlockPhysics.asmHelper.set(entityfallingsand, "bpdata", BlockPhysics.getBlockBPdata( world,i, j, k));
			world.spawnEntityInWorld(entityfallingsand);
		}
		else
		{
			if (canslide[0]) {
				canslide[0] = BlockPhysics.isFallingEmpty(world, i - 1, j, k);
			}
			if (canslide[1]) {
				canslide[1] = BlockPhysics.isFallingEmpty(world, i, j, k - 1);
			}
			if (canslide[2]) {
				canslide[2] = BlockPhysics.isFallingEmpty(world, i, j, k + 1);
			}
			if (canslide[3]) {
				canslide[3] = BlockPhysics.isFallingEmpty(world, i + 1, j, k);
			}
			if (!(canslide[0] || canslide[1] || canslide[2] || canslide[3])) {
				return false;
			}

			final byte slide[] = {0,0,0,0};
			byte count = 0;
			for (byte si = 0; si < 4; si++)
			{
				if (canslide[si])
				{
					slide[count] = si;
					count++;
				}
			}

			int id = 0;
			int kd = 0;
			int rr = 0;
			if ( count > 1 ) {
				rr = BlockPhysics.prandnextint(count);
			}
			switch (slide[rr])
			{
				case 0:
					id = - 1;
					break;
				case 1:
					kd = - 1;
					break;
				case 2:
					kd = + 1;
					break;
				case 3:
					id = + 1;
					break;
			}
			//int metadata = world.getBlockMetadata(i, j, k);
			final EntityFallingBlock entityfallingsand = new EntityFallingBlock(world, 0.5D + i + 0.0625D * id, 0.5D + j - 0.0625D, 0.5D + k + 0.0625D * kd, (Block)Block.blockRegistry.getObject(blid), meta);
			if ( ((Block)Block.blockRegistry.getObject(blid)).hasTileEntity(meta) )
			{
				final TileEntity tileEntity = world.getTileEntity(i, j, k);
				
				if (tileEntity != null)
				{
					entityfallingsand.field_145810_d = new NBTTagCompound();
					tileEntity.writeToNBT(entityfallingsand.field_145810_d);
					world.removeTileEntity(i, j, k);
				}
			}

			BlockPhysics.asmHelper.set(entityfallingsand, "slideDir", (byte) (slide[rr]+1));
			if (BlockPhysics.canBurn(blid) && world.getBlock(i, j+1, k) == Blocks.fire) {
				entityfallingsand.setFire(60);
			}
			BlockPhysics.asmHelper.set(entityfallingsand, "bpdata", BlockPhysics.getBlockBPdata( world,i, j, k));
			world.spawnEntityInWorld(entityfallingsand);
		}
		world.setBlockToAir(i, j, k);
		BlockPhysics.setBlockBPdata( world,i, j, k, 0);
		j++;
		BlockPhysics.tryToMove(world, i, j, k, Block.blockRegistry.getNameForObject(world.getBlock(i, j, k)), world.getBlockMetadata(i, j, k), false);
		return true;
	}

	public static boolean canmove(final World world, final int i, final int j, final int k, final BlockPistonBase par1block)
	{
		int orient = BlockPistonBase.getPistonOrientation(world.getBlockMetadata(i, j, k));
		if ( orient > 5 ) {
			orient = 0;
		}
		final int i2 = i + Facing.offsetsXForSide[orient];
		final int j2 = j + Facing.offsetsYForSide[orient];
		final int k2 = k + Facing.offsetsZForSide[orient];

		final String blockName = Block.blockRegistry.getNameForObject(world.getBlock(i2, j2, k2));

		if (blockName != Block.blockRegistry.getNameForObject(Blocks.piston_extension) && blockName != Block.blockRegistry.getNameForObject(Blocks.piston_head)) {
			return true;
		}

		int orient2 = BlockPistonBase.getPistonOrientation(world.getBlockMetadata(i2, j2, k2) );
		if ( orient2 > 5 ) {
			orient2 = 0;
		}

		if (blockName == Block.blockRegistry.getNameForObject(Blocks.piston_head) && orient == orient2 ) {
			return false;
		}
		if (blockName == Block.blockRegistry.getNameForObject(Blocks.piston_extension))
		{
			final TileEntity var7 = world.getTileEntity(i2, j2, k2);
			if (var7 instanceof TileEntityPiston)
			{
				if ( ((TileEntityPiston)var7).getPistonOrientation() == orient )
				{
					return false;
				}
			}
		}
		return true;
	}

	public static void moveChangeMechanic(final World world, final int i, final int j, final int k, final String blockName, final int radius, final int strength)
	{
		int state,m;
		String bid;
		for (int ii = i - radius; ii <= i + radius; ii++ )
		{
			for (int jj = j - radius; jj <= j + radius; jj++ )
			{
				for (int kk = k - radius; kk <= k + radius; kk++ )
				{
					bid = Block.blockRegistry.getNameForObject(world.getBlock( ii, jj, kk ));
					m = world.getBlockMetadata(ii, jj, kk );
					if (DefinitionMaps.getBlockDef(blockName, m).movenum == 2 && DefinitionMaps.getBlockDef(blockName, m).movechanger > 1 /*&& ( blockID == 0 || blockID == bid )*/ )
					{
						int bpd = BlockPhysics.getBlockBPdata( world, ii, jj, kk );
						state = bpd & 15;
						bpd = bpd - state;
						if (state < 15 )
						{
							state = state + BlockPhysics.prandnextint(4) + strength;
							if ( state > 15 ) {
								state = 15;
							}
							BlockPhysics.setBlockBPdata( world, ii, jj, kk, bpd + state);
						}
					}
				}
			}
		}
	}

	public static int getBlockBPdata(final World world, int par1, final int par2, int par3)
	{
		if (par1 >= -30000000 && par3 >= -30000000 && par1 < 30000000 && par3 < 30000000)
		{
			if (par2 < 0)
			{
				return 0;
			}
			else if (par2 >= 256)
			{
				return 0;
			}
			else
			{
				final Chunk chunk = world.getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
				par1 &= 15;
				par3 &= 15;
				return (Integer) BlockPhysics.asmHelper.invoke(chunk, "getBlockBPdata", par1, par2, par3);
			}
		}
		else
		{
			return 0;
		}
	}

	public static int prandnextint(final int max)
	{
		BlockPhysics.nextrand++;
		if (BlockPhysics.nextrand > 99) {
			BlockPhysics.nextrand = BlockPhysics.nextrand - 100;
		}
		return BlockPhysics.prand[BlockPhysics.nextrand] % max;
	}

	public static boolean floating(final World world, final int i, final int j, final int k, final int rad, final String bid)
	{
		for (int jj = j-rad; jj <= j+rad; jj++ )
		{
			for (int ii = i-rad; ii <= i+rad; ii++ )
			{
				for (int kk = k-rad; kk <= k+rad; kk++ )
				{
					if (BlockPhysics.sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(ii , jj, kk )),bid)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean sameBlock(final String id1, final int meta1, final String id2, final int meta2)
	{
		if (!id1.equals(id2)) {
			return false;
		}
		if (Block.blockRegistry.getObject(id1) instanceof BlockSlab || Block.blockRegistry.getObject(id1) instanceof BlockSilverfish)
		{
			if (meta1 != meta2) {
				return false;
			}
		}
		return true;
	}

	public static boolean sameBlock(final String id1, final String id2)
	{
		if ((id1.equals("minecraft:log") || id1.equals("minecraft:log2") || id1.equals("harvestcraft:pamMaple") || id1.equals("MineFactoryReloaded:rubberwood.log") || id1.equals("TConstruct:slime.gel") || id1.equals("Natura:tree") || id1.equals("Natura:redwood") || id1.equals("Natura:Saguaro") || id1.equals("Natura:willow") || id1.equals("Natura:bloodwood") || id1.equals("Natura:DarkTree") || id1.equals("Natura:RareTree"))
				&& (id2.equals("minecraft:log") || id2.equals("minecraft:log2") || id2.equals("harvestcraft:pamMaple") || id2.equals("MineFactoryReloaded:rubberwood.log") || id2.equals("TConstruct:slime.gel") || id2.equals("Natura:tree") || id2.equals("Natura:redwood") || id2.equals("Natura:Saguaro") || id2.equals("Natura:willow") || id2.equals("Natura:bloodwood") || id2.equals("Natura:DarkTree") || id2.equals("Natura:RareTree")))
		{
			return true;
		}
		if (!id1.equals(id2)) {
			return false;
		}
		if (Block.blockRegistry.getObject(id1) instanceof BlockSlab || Block.blockRegistry.getObject(id1) instanceof BlockSilverfish)
		{
			return true;
		}
		return true;
	}

	public static boolean canMoveTo(final World world, final int i, final int j, final int k, final int e)
	{
		final Block l = world.getBlock(i, j, k);
		if (l == Blocks.air) {
			return true;
		}
		if (l == Blocks.water || l == Blocks.flowing_water || l == Blocks.lava || l == Blocks.flowing_lava)
		{
			return true;		// water, lava
		}
		if (l == Blocks.fire)
		{
			return true;				// fire
		}

		final Material mt = l.getMaterial();
		if ( mt.isLiquid() ) {
			return true;
		}
		final int m = world.getBlockMetadata(i, j, k);
		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(l), m).fragile > 0 && e > DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(l), m).strength ) {
			return true;
		}

		return false;
	}

	public static boolean hanging(final World world, final int i, int j, final int k, int hang, final String bid, final int met)
	{
		String b;
		int m;
		j++;
		hang = j+hang;
		for (int cc = j; cc < hang; cc++ )
		{
			b = Block.blockRegistry.getNameForObject(world.getBlock(i , cc, k ));
			m = world.getBlockMetadata(i , cc, k );
			if (DefinitionMaps.getBlockDef(b, m).supportingblock > 0 ) {
				return true;
			} else if (!BlockPhysics.sameBlock(bid,met,b,m)) {
				return false;
			}

		}
		return false;
	}

	public static boolean attached(final World world, final int i, final int j, final int k, final int att, final String bid, final int met)
	{
		String b;
		int m;
		int cc;
		for ( cc = 1; cc <= att; cc++ )
		{
			b = Block.blockRegistry.getNameForObject(world.getBlock(i + cc , j, k ));
			m = world.getBlockMetadata(i + cc , j, k );
			if (DefinitionMaps.getBlockDef(b, m).supportingblock > 0 ) {
				return true;
			} else if (!BlockPhysics.sameBlock(bid,met,b,m)) {
				break;
			}
		}

		for ( cc = 1; cc <= att; cc++ )
		{
			b = Block.blockRegistry.getNameForObject(world.getBlock(i - cc , j, k ));
			m = world.getBlockMetadata(i - cc , j, k );
			if (DefinitionMaps.getBlockDef(b, m).supportingblock > 0 ) {
				return true;
			} else if (!BlockPhysics.sameBlock(bid,met,b,m)) {
				break;
			}
		}

		for ( cc = 1; cc <= att; cc++ )
		{
			b = Block.blockRegistry.getNameForObject(world.getBlock(i ,j ,k + cc ));
			m = world.getBlockMetadata(i ,j ,k + cc );
			if (DefinitionMaps.getBlockDef(b, m).supportingblock > 0 ) {
				return true;
			} else if (!BlockPhysics.sameBlock(bid,met,b,m)) {
				break;
			}
		}

		for ( cc = 1; cc <= att; cc++ )
		{
			b = Block.blockRegistry.getNameForObject(world.getBlock(i ,j ,k - cc ));
			m = world.getBlockMetadata(i ,j ,k - cc );
			if (DefinitionMaps.getBlockDef(b, m).supportingblock > 0 ) {
				return true;
			} else if (!BlockPhysics.sameBlock(bid,met,b,m)) {
				break;
			}
		}

		return false;
	}

	public static boolean ncorbel(final World world, final int i, final int j, final int k, final int ni)
	{
		int c;
		for ( c = 1; c <= ni; c++ )
		{
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i - c, j, k)),world.getBlockMetadata(i - c, j, k)).supportingblock > 0)
			{
				if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i - c , j - 1, k)),world.getBlockMetadata(i - c , j - 1, k)).supportingblock > 0) {
					return true;
				}
			} else {
				break;
			}
		}

		for ( c = 1; c <= ni; c++ )
		{
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i + c, j, k)),world.getBlockMetadata(i + c, j, k)).supportingblock > 0)
			{
				if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i + c , j - 1, k)),world.getBlockMetadata(i + c , j - 1, k)).supportingblock > 0) {
					return true;
				}
			} else {
				break;
			}
		}

		for ( c = 1; c <= ni; c++ )
		{
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i, j, k + c)),world.getBlockMetadata(i, j, k + c)).supportingblock > 0)
			{
				if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i, j - 1, k + c)),world.getBlockMetadata(i , j - 1, k + c)).supportingblock > 0) {
					return true;
				}
			} else {
				break;
			}
		}

		for ( c = 1; c <= ni; c++ )
		{
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i, j, k - c)),world.getBlockMetadata(i, j, k - c)).supportingblock > 0)
			{
				if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i, j - 1, k - c)),world.getBlockMetadata(i, j - 1, k - c)).supportingblock > 0) {
					return true;
				}
			} else {
				break;
			}
		}

		return false;
	}

	public static boolean corbel(final World world, final int i, final int j, final int k, final int ci, final String blid, final int meta)
	{
		if(DefinitionMaps.getBlockDef(blid,meta).supportingblock == 0) {
			return false;
		}
		int c;
		for ( c = 1; c <= ci; c++ )
		{
			if ( BlockPhysics.sameBlock(Block.blockRegistry.getNameForObject(world.getBlock( i+c, j, k )), world.getBlockMetadata( i+c, j, k), blid, meta ))
			{
				if ( BlockPhysics.sameBlock( Block.blockRegistry.getNameForObject(world.getBlock( i+c, j - 1, k )), world.getBlockMetadata( i+c, j - 1, k), blid, meta ) ) {
					return true;
				}
			} else {
				break;
			}
		}

		for ( c = 1; c <= ci; c++ )
		{
			if ( BlockPhysics.sameBlock( Block.blockRegistry.getNameForObject(world.getBlock( i-c, j, k )), world.getBlockMetadata( i-c, j, k), blid, meta ))
			{
				if ( BlockPhysics.sameBlock( Block.blockRegistry.getNameForObject(world.getBlock( i-c, j - 1, k )), world.getBlockMetadata( i-c, j - 1, k), blid, meta ) ) {
					return true;
				}
			} else {
				break;
			}
		}

		for ( c = 1; c <= ci; c++ )
		{
			if ( BlockPhysics.sameBlock( Block.blockRegistry.getNameForObject(world.getBlock( i, j, k+c )), world.getBlockMetadata( i, j, k+c), blid, meta ))
			{
				if ( BlockPhysics.sameBlock( Block.blockRegistry.getNameForObject(world.getBlock( i, j - 1, k+c )), world.getBlockMetadata( i, j - 1, k+c), blid, meta ) ) {
					return true;
				}
			} else {
				break;
			}
		}

		for ( c = 1; c <= ci; c++ )
		{
			if ( BlockPhysics.sameBlock( Block.blockRegistry.getNameForObject(world.getBlock( i, j, k-c )), world.getBlockMetadata( i, j, k-c), blid, meta ))
			{
				if ( BlockPhysics.sameBlock( Block.blockRegistry.getNameForObject(world.getBlock( i, j - 1, k-c )), world.getBlockMetadata( i, j - 1, k-c), blid, meta ) ) {
					return true;
				}
			} else {
				break;
			}
		}

		return false;
	}

	public static boolean ceiling(final World world, final int i, final int j, final int k)
	{
		if ( DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i-1, j, k)),world.getBlockMetadata(i-1, j, k)).supportingblock > 0 && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j, k )),world.getBlockMetadata(i + 1, j, k )).supportingblock > 0 ) {
			return true;
		}
		if ( DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i, j, k-1)),world.getBlockMetadata(i, j, k-1)).supportingblock > 0 && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i, j, k + 1 )),world.getBlockMetadata(i, j, k + 1 )).supportingblock > 0 ) {
			return true;
		}
		if ( DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i-1, j, k-1)),world.getBlockMetadata(i-1, j, k-1)).supportingblock > 0 && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j, k + 1 )),world.getBlockMetadata(i + 1, j, k + 1 )).supportingblock > 0 ) {
			return true;
		}
		if ( DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i-1, j, k+1)),world.getBlockMetadata(i-1, j, k+1)).supportingblock > 0 && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j, k-1 )),world.getBlockMetadata(i + 1, j, k-1 )).supportingblock > 0 ) {
			return true;
		}

		return false;
	}

	public static boolean smallArc(final World world, final int i, final int j, final int k, final int si)
	{
		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i - 1, j, k)),world.getBlockMetadata(i - 1, j, k)).supportingblock > 0 && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i + 1, j, k )),world.getBlockMetadata( i + 1, j, k )).supportingblock > 0 )
		{
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i - 1, j - 1, k )),world.getBlockMetadata( i - 1, j - 1, k )).supportingblock > 0 || DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j - 1, k)),world.getBlockMetadata(i + 1, j - 1, k)).supportingblock > 0) {
				return true;
			}
			if (si > 1)
			{
				int c;
				for ( c = 2; c <= si; c++ )
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i - c, j, k )),world.getBlockMetadata( i - c, j, k )).supportingblock > 0 )
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i - c, j - 1, k )),world.getBlockMetadata( i - c, j - 1, k )).supportingblock > 0 ) {
							return true;
						}
					} else {
						break;
					}
				}

				for ( c = 2; c <= si; c++ )
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i + c, j, k )),world.getBlockMetadata( i + c, j, k )).supportingblock > 0 )
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i + c, j - 1, k )),world.getBlockMetadata( i + c, j - 1, k )).supportingblock > 0 ) {
							return true;
						}
					} else {
						break;
					}
				}
			}

		}

		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i, j, k - 1)),world.getBlockMetadata(i, j, k - 1)).supportingblock > 0 && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i, j, k + 1)),world.getBlockMetadata( i, j, k + 1)).supportingblock > 0)
		{
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i, j - 1, k - 1)),world.getBlockMetadata(i, j - 1, k - 1)).supportingblock > 0 || DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i, j - 1, k + 1)),world.getBlockMetadata(i, j - 1, k + 1)).supportingblock > 0) {
				return true;
			}
			if (si > 1)
			{
				int c;
				for ( c = 2; c <= si; c++ )
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i, j, k - c )),world.getBlockMetadata( i, j, k - c )).supportingblock > 0 )
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i, j - 1, k - c )),world.getBlockMetadata( i, j - 1, k - c )).supportingblock > 0 ) {
							return true;
						}
					} else {
						break;
					}
				}

				for ( c = 2; c <= si; c++ )
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i, j, k + c )),world.getBlockMetadata( i, j, k + c )).supportingblock > 0 )
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i, j - 1, k + c )),world.getBlockMetadata( i, j - 1, k + c )).supportingblock > 0 ) {
							return true;
						}
					} else {
						break;
					}
				}
			}
		}

		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i - 1, j, k + 1)),world.getBlockMetadata(i - 1, j, k + 1)).supportingblock > 0 && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i + 1, j, k -1 )),world.getBlockMetadata( i + 1, j, k -1 )).supportingblock > 0)
		{
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i - 1, j - 1, k +1)),world.getBlockMetadata(i - 1, j - 1, k +1)).supportingblock > 0 || DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j - 1, k -1)),world.getBlockMetadata(i + 1, j - 1, k -1)).supportingblock > 0) {
				return true;
			}
			if (si > 1)
			{
				int c;
				for ( c = 2; c <= si; c++ )
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i - c, j, k + c )),world.getBlockMetadata( i - c, j, k + c )).supportingblock > 0 )
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i - c, j - 1, k + c )),world.getBlockMetadata( i - c, j - 1, k + c )).supportingblock > 0 ) {
							return true;
						}
					} else {
						break;
					}
				}

				for ( c = 2; c <= si; c++ )
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i + c, j, k - c)),world.getBlockMetadata( i + c, j, k - c)).supportingblock > 0 )
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i + c, j - 1, k - c )),world.getBlockMetadata( i + c, j - 1, k - c )).supportingblock > 0 ) {
							return true;
						}
					} else {
						break;
					}
				}
			}
		}

		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j, k + 1)),world.getBlockMetadata(i + 1, j, k + 1)).supportingblock > 0 && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i - 1, j, k - 1)),world.getBlockMetadata( i - 1, j, k - 1)).supportingblock > 0)
		{
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j - 1, k + 1)),world.getBlockMetadata(i + 1, j - 1, k + 1)).supportingblock > 0 || DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i -1, j - 1, k - 1)),world.getBlockMetadata(i -1, j - 1, k - 1)).supportingblock > 0) {
				return true;
			}
			if (si > 1)
			{
				int c;
				for ( c = 2; c <= si; c++ )
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i + c, j, k + c )),world.getBlockMetadata( i + c, j, k + c )).supportingblock > 0 )
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i + c, j - 1, k + c )),world.getBlockMetadata( i + c, j - 1, k + c )).supportingblock > 0 ) {
							return true;
						}
					} else {
						break;
					}
				}

				for ( c = 2; c <= si; c++ )
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i - c, j, k - c )),world.getBlockMetadata( i - c, j, k - c )).supportingblock > 0 )
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i - c, j - 1, k - c )),world.getBlockMetadata( i - c, j - 1, k - c )).supportingblock > 0 ) {
							return true;
						}
					} else {
						break;
					}
				}
			}
		}

		return false;
	}

	public static boolean bigArc(final World world, final int i, final int j, final int k, final int bi)
	{
		if(DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i,j+1,k)),world.getBlockMetadata(i,j+1,k)).supportingblock == 0) {
			return false;
		}

		int c;
		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i+1,j+1,k)),world.getBlockMetadata(i+1,j+1,k)).supportingblock > 0)
		{
			for ( c = 1; c <= bi; c++ )
			{
				if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i - c, j, k )),world.getBlockMetadata( i - c, j, k )).supportingblock > 0 )
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i - c, j - 1, k )),world.getBlockMetadata( i - c, j - 1, k )).supportingblock > 0 ) {
						return true;
					}
				} else {
					break;
				}
			}
		}

		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i-1,j+1,k)),world.getBlockMetadata(i-1,j+1,k)).supportingblock > 0)
		{
			for ( c = 1; c <= bi; c++ )
			{
				if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i + c, j, k )),world.getBlockMetadata( i + c, j, k )).supportingblock > 0 )
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i + c, j - 1, k )),world.getBlockMetadata( i + c, j - 1, k )).supportingblock > 0 ) {
						return true;
					}
				} else {
					break;
				}
			}
		}

		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i,j+1,k+1)),world.getBlockMetadata(i,j+1,k+1)).supportingblock > 0)
		{
			for ( c = 1; c <= bi; c++ )
			{
				if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i, j, k - c )),world.getBlockMetadata( i, j, k - c)).supportingblock > 0 )
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i , j - 1, k - c)),world.getBlockMetadata( i , j - 1, k - c)).supportingblock > 0 ) {
						return true;
					}
				} else {
					break;
				}
			}
		}

		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i,j+1,k-1)),world.getBlockMetadata(i,j+1,k-1)).supportingblock > 0)
		{
			for ( c = 1; c <= bi; c++ )
			{
				if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i, j, k + c )),world.getBlockMetadata( i, j, k + c)).supportingblock > 0 )
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i , j - 1, k + c)),world.getBlockMetadata( i , j - 1, k + c)).supportingblock > 0 ) {
						return true;
					}
				} else {
					break;
				}
			}
		}

		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i+1,j+1,k+1)),world.getBlockMetadata(i+1,j+1,k+1)).supportingblock > 0)
		{
			for ( c = 1; c <= bi; c++ )
			{
				if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i-c, j, k - c )),world.getBlockMetadata( i-c, j, k - c)).supportingblock > 0 )
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i -c, j - 1, k - c)),world.getBlockMetadata( i-c , j - 1, k - c)).supportingblock > 0 ) {
						return true;
					}
				} else {
					break;
				}
			}
		}

		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i-1,j+1,k-1)),world.getBlockMetadata(i-1,j+1,k-1)).supportingblock > 0)
		{
			for ( c = 1; c <= bi; c++ )
			{
				if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i+c, j, k + c )),world.getBlockMetadata( i+c, j, k + c)).supportingblock > 0 )
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i +c, j - 1, k + c)),world.getBlockMetadata( i+c , j - 1, k + c)).supportingblock > 0 ) {
						return true;
					}
				} else {
					break;
				}
			}
		}

		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i+1,j+1,k-1)),world.getBlockMetadata(i+1,j+1,k-1)).supportingblock > 0)
		{
			for ( c = 1; c <= bi; c++ )
			{
				if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i-c, j, k + c )),world.getBlockMetadata( i-c, j, k + c)).supportingblock > 0 )
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i -c, j - 1, k + c)),world.getBlockMetadata( i-c , j - 1, k + c)).supportingblock > 0 ) {
						return true;
					}
				} else {
					break;
				}
			}
		}

		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(i-1,j+1,k+1)),world.getBlockMetadata(i-1,j+1,k+1)).supportingblock > 0)
		{
			for ( c = 1; c <= bi; c++ )
			{
				if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i+c, j, k - c )),world.getBlockMetadata( i+c, j, k - c)).supportingblock > 0 )
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock( i +c, j - 1, k - c)),world.getBlockMetadata( i+c , j - 1, k - c)).supportingblock > 0 ) {
						return true;
					}
				} else {
					break;
				}
			}
		}

		return false;
	}

	public static boolean branch(final World world, final int i, final int j, final int k, final String bid, final int met)
	{
		if (BlockPhysics.sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(i+1 , j-1, k )),world.getBlockMetadata(i+1 , j-1, k ),bid,met)) {
			return true;
		}
		if (BlockPhysics.sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(i-1 , j-1, k )),world.getBlockMetadata(i-1 , j-1, k ),bid,met)) {
			return true;
		}
		if (BlockPhysics.sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(i , j-1, k+1 )),world.getBlockMetadata(i , j-1, k+1 ),bid,met)) {
			return true;
		}
		if (BlockPhysics.sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(i , j-1, k-1 )),world.getBlockMetadata(i , j-1, k-1 ),bid,met)) {
			return true;
		}
		if (BlockPhysics.sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(i+1 , j-1, k+1 )),world.getBlockMetadata(i+1 , j-1, k+1 ),bid,met)) {
			return true;
		}
		if (BlockPhysics.sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(i-1 , j-1, k-1 )),world.getBlockMetadata(i-1 , j-1, k-1 ),bid,met)) {
			return true;
		}
		if (BlockPhysics.sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(i-1 , j-1, k+1 )),world.getBlockMetadata(i-1 , j-1, k+1 ),bid,met)) {
			return true;
		}
		if (BlockPhysics.sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(i+1 , j-1, k-1 )),world.getBlockMetadata(i+1 , j-1, k-1 ),bid,met)) {
			return true;
		}
		return false;
	}

	public static void dropItemsNBT(final World world, final int i, final int j, final int k, final NBTTagCompound tileEntityData)
	{
		if ( tileEntityData == null ) {
			return;
		}

		final NBTTagList nbttaglist = tileEntityData.getTagList("Items", 10);

		for (int tl = 0; tl < nbttaglist.tagCount(); ++tl)
		{

			final ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(tl));

			if (itemstack != null)
			{
				final float f = BlockPhysics.rand.nextFloat() * 0.8F + 0.1F;
				final float f1 = BlockPhysics.rand.nextFloat() * 0.8F + 0.1F;
				EntityItem entityitem;

				for (final float f2 = BlockPhysics.rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld(entityitem))
				{
					int k1 = BlockPhysics.rand.nextInt(21) + 10;

					if (k1 > itemstack.stackSize)
					{
						k1 = itemstack.stackSize;
					}

					itemstack.stackSize -= k1;
					entityitem = new EntityItem(world, i + f, j + f1, k + f2, new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));

					if (itemstack.hasTagCompound())
					{
						entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
					}
				}
			}
		}
	}

	public static boolean canBurn(final String blid)
	{
		if(blid == null) {
			return false;
		}
		if (Blocks.fire.getFlammability((Block)Block.blockRegistry.getObject(blid)) != 0) {
			return true;
		}
		if ((Block)Block.blockRegistry.getObject(blid) == Blocks.netherrack) {
			return true;
		}
		return false;
	}

	public static boolean isFallingEmpty(final World world, final int i, final int j, final int k)
	{
		AxisAlignedBB Sandbbox;
		Sandbbox = AxisAlignedBB.getBoundingBox(i, j, k, (float)i + 1, (float)j + 1, (float)k + 1);
		final List ls = world.getEntitiesWithinAABB(net.minecraft.entity.item.EntityFallingBlock.class, Sandbbox);
		if (ls.size() != 0) {
			return false;
		}
		return true;
	}

	public static void doExplosionA(final World world, final Explosion explosion)
	{
		if ( world.isRemote ) {
			return;
		}
		final float var1 = explosion.explosionSize;
		final HashSet var2 = new HashSet();
		int var3;
		int var4;
		int var5;
		double var15;
		double var17;
		double var19;

		if ( !((Boolean) BlockPhysics.asmHelper.get(explosion, "impact")) )
		{
			explosion.explosionSize *= 2.0F;
			var3 = MathHelper.floor_double(explosion.explosionX - explosion.explosionSize - 1.0D);
			var4 = MathHelper.floor_double(explosion.explosionX + explosion.explosionSize + 1.0D);
			var5 = MathHelper.floor_double(explosion.explosionY - explosion.explosionSize - 1.0D);
			final int var27 = MathHelper.floor_double(explosion.explosionY + explosion.explosionSize + 1.0D);
			final int var7 = MathHelper.floor_double(explosion.explosionZ - explosion.explosionSize - 1.0D);
			final int var28 = MathHelper.floor_double(explosion.explosionZ + explosion.explosionSize + 1.0D);
			//TODO Hope this is right....
			final List var9 = world.getEntitiesWithinAABBExcludingEntity(explosion.exploder, AxisAlignedBB.getBoundingBox(var3, var5, var7, var4, var27, var28));
			//TODO I hope this is right....
			final Vec3 var29 = Vec3.createVectorHelper(explosion.explosionX, explosion.explosionY, explosion.explosionZ);

			for (int var11 = 0; var11 < var9.size(); ++var11)
			{
				final Entity var30 = (Entity)var9.get(var11);
				final double var13 = var30.getDistance(explosion.explosionX, explosion.explosionY, explosion.explosionZ) / explosion.explosionSize;

				if (var13 <= 1.0D)
				{
					var15 = var30.posX - explosion.explosionX;
					var17 = var30.posY + var30.getEyeHeight() - explosion.explosionY;
					var19 = var30.posZ - explosion.explosionZ;
					final double var32 = MathHelper.sqrt_double(var15 * var15 + var17 * var17 + var19 * var19);

					if (var32 != 0.0D)
					{
						var15 /= var32;
						var17 /= var32;
						var19 /= var32;
						final double var31 = world.getBlockDensity(var29, var30.boundingBox);
						double var33 = (1.0D - var13) * var31;

						if ( var30 instanceof EntityFallingBlock )
						{
							var33 *= MathHelper.sqrt_double( 1500D / DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(((EntityFallingBlock) var30).func_145805_f()),((EntityFallingBlock) var30).field_145814_a).mass);
							if (((EntityFallingBlock)var30).func_145805_f() == Blocks.tnt && !(var30 instanceof EntityTNTPrimed))
							{
								final EntityTNTPrimed entitytnt = new EntityTNTPrimed(world, var30.posX, var30.posY, var30.posZ, null);
								entitytnt.motionX = BlockPhysics.bSpeedR(var30.motionX + var15 * var33);
								entitytnt.motionY = BlockPhysics.bSpeedR(var30.motionY + var17 * var33);
								entitytnt.motionZ = BlockPhysics.bSpeedR(var30.motionZ + var19 * var33);
								entitytnt.fuse = 20 + BlockPhysics.prandnextint(40);
								world.spawnEntityInWorld(entitytnt);
								var30.setDead();
							}
							else
							{
								var30.motionX = BlockPhysics.bSpeedR(var30.motionX + var15 * var33);
								var30.motionY = BlockPhysics.bSpeedR(var30.motionY + var17 * var33);
								var30.motionZ = BlockPhysics.bSpeedR(var30.motionZ + var19 * var33);
								var30.velocityChanged = true;
							}
						}
						else
						{
							var30.attackEntityFrom(DamageSource.setExplosionSource(explosion), (int)((var33 * var33 + var33) / 2.0D * 8.0D * explosion.explosionSize + 1.0D));
							final double var36 = EnchantmentProtection.func_92092_a(var30, var33);
							var30.motionX = BlockPhysics.bSpeedR(var30.motionX + var15 * var36);
							var30.motionY = BlockPhysics.bSpeedR(var30.motionY + var17 * var36);
							var30.motionZ = BlockPhysics.bSpeedR(var30.motionZ + var19 * var36);
							var30.velocityChanged = true;

							if (var30 instanceof EntityPlayer)
							{
								//TODO Hope this is right still....
								explosion.func_77277_b().put(var30, Vec3.createVectorHelper(var15 * var33, var17 * var33, var19 * var33));
							}
						}
					}
				}
			}

			explosion.explosionSize = var1 * ModConfig.explosionStrength / 100F;
		}


		for (var3 = 0; var3 < 16; ++var3)
		{
			for (var4 = 0; var4 < 16; ++var4)
			{
				for (var5 = 0; var5 < 16; ++var5)
				{
					if (var3 == 0 || var3 == 16 - 1 || var4 == 0 || var4 == 16 - 1 || var5 == 0 || var5 == 16 - 1)
					{
						double var6 = var3 / (16 - 1.0F) * 2.0F - 1.0F;
						double var8 = var4 / (16 - 1.0F) * 2.0F - 1.0F;
						double var10 = var5 / (16 - 1.0F) * 2.0F - 1.0F;
						final double var12 = Math.sqrt(var6 * var6 + var8 * var8 + var10 * var10);
						var6 /= var12;
						var8 /= var12;
						var10 /= var12;
						float var14 = explosion.explosionSize * (0.7F + world.rand.nextFloat() * 0.6F);
						var15 = explosion.explosionX;
						var17 = explosion.explosionY;
						var19 = explosion.explosionZ;

						for (final float var21 = 0.3F; var14 > 0.0F; var14 -= var21 * 0.75F)
						{
							final int var22 = MathHelper.floor_double(var15);
							final int var23 = MathHelper.floor_double(var17);
							final int var24 = MathHelper.floor_double(var19);
							String var25 = Block.blockRegistry.getNameForObject(world.getBlock(var22, var23, var24));

							if (!var25.equals(Block.blockRegistry.getNameForObject(Blocks.air)) )
							{
								final int m = world.getBlockMetadata(var22, var23, var24);
								final Block var26 = (Block)Block.blockRegistry.getObject(var25);
								final float var27 = explosion.exploder != null ? explosion.exploder.func_145772_a(explosion, world, var24, var22, var23, var26) : var26.getExplosionResistance(explosion.exploder);
								var14 -= (var27 + 0.3F) * var21;

								if (var14 > 0.0F)
								{
									if ( !BlockPhysics.skipMove && (Integer)BlockPhysics.asmHelper.get(((WorldServer)world).getEntityTracker(), "movingblocks") < ModConfig.maxMovingBlocks && (DefinitionMaps.getBlockDef(var25,m).pushtype == 1 || DefinitionMaps.getBlockDef(var25,m).pushtype == 3 ) )
									{
										/*double speed = (double)MathHelper.sqrt_double( exploder.motionX * exploder.motionX + exploder.motionY * exploder.motionY + exploder.motionZ * exploder.motionZ );
	                            		double d6 = var22 + 0.5F - explosionX - (exploder.motionX / speed) * 4;
	            	                    double d8 = var23 + 0.5F - explosionY - (exploder.motionY / speed) * 4;
	            	                    double d10 = var24 + 0.5F - explosionZ - (exploder.motionZ / speed) * 4;*/
										double d6 = var22 + 0.5F - explosion.explosionX;
										double d8 = var23 + 0.5F - explosion.explosionY;
										double d10 = var24 + 0.5F - explosion.explosionZ;
										final double d11 = MathHelper.sqrt_double(d6 * d6 + d8 * d8 + d10 * d10);
										d6 /= d11;
										d8 /= d11;
										d10 /= d11;

										if (var25.equals(Block.blockRegistry.getNameForObject(Blocks.stone))) {
											var25 = Block.blockRegistry.getNameForObject(Blocks.stone);
										} else if ( var25.equals(Block.blockRegistry.getNameForObject(Blocks.grass)) || var25.equals(Block.blockRegistry.getNameForObject(Blocks.farmland)) || var25.equals(Block.blockRegistry.getNameForObject(Blocks.mycelium)) ) {
											var25 = Block.blockRegistry.getNameForObject(Blocks.dirt);
										}

										final double sm = MathHelper.sqrt_double( 1500D / DefinitionMaps.getBlockDef(var25,m).mass);
										double d7 = 0.5D * sm / (d11 / explosion.explosionSize + 0.10000000000000001D);

										d7 *= BlockPhysics.rand.nextFloat() * BlockPhysics.rand.nextFloat() + 0.6F;
										d6 *= d7;
										d8 *= d7;
										d10 *= d7;



										int bpdata = 0;
										if ( DefinitionMaps.getBlockDef(var25,m).movenum == 2 && DefinitionMaps.getBlockDef(var25,m).movechanger > 1 ) {
											bpdata = 15;
										} else {
											bpdata = BlockPhysics.getBlockBPdata( world,var22, var23, var24);
										}

										final int meta = world.getBlockMetadata(var22, var23, var24);

										if ( !((Boolean) BlockPhysics.asmHelper.get(explosion, "impact")) && var25.equals(Block.blockRegistry.getNameForObject(Blocks.tnt)) )
										{
											final EntityTNTPrimed entitytnt = new EntityTNTPrimed(world, var22 + 0.5F, var23 + 0.5F, var24 + 0.5F, null);
											entitytnt.motionX = BlockPhysics.bSpeedR(d6 - BlockPhysics.rand.nextGaussian() * 0.05D);
											entitytnt.motionY = BlockPhysics.bSpeedR(d8 - BlockPhysics.rand.nextGaussian() * 0.05D);
											entitytnt.motionZ = BlockPhysics.bSpeedR(d10 - BlockPhysics.rand.nextGaussian() * 0.05D);
											entitytnt.fuse = 20 + BlockPhysics.prandnextint(40);
											world.spawnEntityInWorld(entitytnt);
										}
										else
										{
											final EntityFallingBlock entityfallingsand = new EntityFallingBlock(world, var22 + 0.5F, var23 + 0.5F, var24 + 0.5F, (Block)Block.blockRegistry.getObject(var25), meta);
											entityfallingsand.motionX = BlockPhysics.bSpeedR(d6 - BlockPhysics.rand.nextGaussian() * 0.05D);
											entityfallingsand.motionY = BlockPhysics.bSpeedR(d8 - BlockPhysics.rand.nextGaussian() * 0.05D);
											entityfallingsand.motionZ = BlockPhysics.bSpeedR(d10 - BlockPhysics.rand.nextGaussian() * 0.05D);
											if ( ModConfig.explosionFire && ( !((Boolean) BlockPhysics.asmHelper.get(explosion, "impact")) || explosion.isFlaming ) && BlockPhysics.prandnextint(5) == 0 && BlockPhysics.canBurn(var25) ) {
												entityfallingsand.setFire(60);
											}
											BlockPhysics.asmHelper.set(entityfallingsand, "bpdata", bpdata);
											if ( ((Block)Block.blockRegistry.getObject(var25)).hasTileEntity(meta) )
											{
												final TileEntity tileEntity = world.getTileEntity(var22, var23, var24);

												if (tileEntity != null)
												{
													entityfallingsand.field_145810_d = new NBTTagCompound();
													tileEntity.writeToNBT(entityfallingsand.field_145810_d);
													world.removeTileEntity(var22, var23, var24);
												}
											}
											world.spawnEntityInWorld(entityfallingsand);
										}

										if ( ModConfig.explosionFire && ( !((Boolean) BlockPhysics.asmHelper.get(explosion, "impact")) || explosion.isFlaming ) )
										{
											final String k2 = Block.blockRegistry.getNameForObject(world.getBlock(var22, var23 - 1, var24));
											//TODO Hope this is right....
											if (((Block)Block.blockRegistry.getObject(k2)).func_149730_j() && BlockPhysics.prandnextint(5) == 0)
											{
												world.setBlock(var22, var23, var24, Blocks.fire ,0 ,3);
											}
											else
											{
												world.setBlockToAir(var22, var23, var24);
											}
										}
										else
										{
											world.setBlockToAir(var22, var23, var24);
										}
									}
									else
									{
										final Block block = (Block)Block.blockRegistry.getObject(var25);
										if (block.canDropFromExplosion(explosion)) {
											block.dropBlockAsItemWithChance(world, var22, var23, var24, world.getBlockMetadata(var22, var23, var24), 1.0F / explosion.explosionSize, 0);
										}
										block.onBlockExploded(world, var22, var23, var24, explosion);

										if ( ModConfig.explosionFire && ( !((Boolean) BlockPhysics.asmHelper.get(explosion, "impact")) || explosion.isFlaming ) )
										{
											final String k2 = Block.blockRegistry.getNameForObject(world.getBlock(var22, var23 - 1, var24));
											if (((Block)Block.blockRegistry.getObject(k2)).func_149730_j() && BlockPhysics.prandnextint(5) == 0)
											{
												world.setBlock(var22, var23, var24, Blocks.fire ,0 ,3);
											}
											else
											{
												world.setBlockToAir(var22, var23, var24);
											}
										}
										else
										{
											world.setBlockToAir(var22, var23, var24);
										}
									}
									BlockPhysics.setBlockBPdata( world,var22, var23, var24, 0);
									if ( !((Boolean) BlockPhysics.asmHelper.get(explosion, "impact")) ) {
										var2.add(new ChunkPosition(var22, var23, var24));
									}
								}
							}
							var15 += var6 * var21;
							var17 += var8 * var21;
							var19 += var10 * var21;
						}
					}
				}
			}
		}

		explosion.affectedBlockPositions.addAll(var2);

		BlockPhysics.moveChangeMechanic(world, MathHelper.floor_double(explosion.explosionX), MathHelper.floor_double(explosion.explosionY), MathHelper.floor_double(explosion.explosionZ), Block.blockRegistry.getNameForObject(Blocks.air), 2, 12);
	}

	public static double bSpeedR(final double speed)
	{
		return (int)(speed * 8000D) / 8000D;
	}

	public static void doExplosionB(final World world, final Explosion explosion, final boolean par1)
	{
		if ((Boolean) BlockPhysics.asmHelper.get(explosion, "impact")) {
			return;
		}

		world.playSoundEffect(explosion.explosionX, explosion.explosionY, explosion.explosionZ, "random.explode", 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
		if (explosion.explosionSize >= 2.0F && explosion.isSmoking)
		{
			world.spawnParticle("hugeexplosion", explosion.explosionX, explosion.explosionY, explosion.explosionZ, 1.0D, 0.0D, 0.0D);
		}
		else
		{
			world.spawnParticle("largeexplode", explosion.explosionX, explosion.explosionY, explosion.explosionZ, 1.0D, 0.0D, 0.0D);
		}


		ChunkPosition var3;
		int var4;
		int var5;
		int var6;
		String var7;

		if (explosion.isSmoking)
		{
			final Iterator var2 = explosion.affectedBlockPositions.iterator();

			while (var2.hasNext())
			{
				var3 = (ChunkPosition)var2.next();
				var4 = var3.chunkPosX;
				var5 = var3.chunkPosY;
				var6 = var3.chunkPosZ;
				var7 = Block.blockRegistry.getNameForObject(world.getBlock(var4, var5, var6));

				if (par1)
				{
					final double var8 = var4 + world.rand.nextFloat();
					final double var10 = var5 + world.rand.nextFloat();
					final double var12 = var6 + world.rand.nextFloat();
					double var14 = var8 - explosion.explosionX;
					double var16 = var10 - explosion.explosionY;
					double var18 = var12 - explosion.explosionZ;
					final double var20 = MathHelper.sqrt_double(var14 * var14 + var16 * var16 + var18 * var18);
					var14 /= var20;
					var16 /= var20;
					var18 /= var20;
					double var22 = 0.5D / (var20 / explosion.explosionSize + 0.1D);
					var22 *= world.rand.nextFloat() * world.rand.nextFloat() + 0.3F;
					var14 *= var22;
					var16 *= var22;
					var18 *= var22;
					world.spawnParticle("explode", (var8 + explosion.explosionX * 1.0D) / 2.0D, (var10 + explosion.explosionY * 1.0D) / 2.0D, (var12 + explosion.explosionZ * 1.0D) / 2.0D, var14, var16, var18);
					world.spawnParticle("smoke", var8, var10, var12, var14, var16, var18);
				}
			}
		}
	}

	public static S0EPacketSpawnObject spawnFallingSandPacket(final EntityFallingBlock ent)
	{
		int burn = 0;
		if (ent.isBurning()) {
			burn = 32768;
		}
		final int slide = (int)(Byte)BlockPhysics.asmHelper.get(ent, "slideDir") << 12;

		return new S0EPacketSpawnObject(ent, 70, Block.getIdFromBlock(ent.func_145805_f()) | ent.field_145814_a << 16 | burn | slide);
	}

	public static void fallingSandUpdate(final World world, final EntityFallingBlock fsand)
	{
		fsand.field_145812_b++;
		if (fsand.field_145812_b < 3)
		{
			if (fsand.field_145812_b == 1) {
				fsand.field_145814_a &= 15;
			}
			return;
		}

		int i = MathHelper.floor_double(fsand.posX);
		int j = MathHelper.floor_double(fsand.posY);
		int k = MathHelper.floor_double(fsand.posZ);

		if (fsand.field_145812_b == 4) {
			BlockPhysics.notifyMove(world,i,j,k);
		}

		if (!world.isRemote && (Byte)BlockPhysics.asmHelper.get(fsand, "dead") < 4 )
		{
			BlockPhysics.asmHelper.set(fsand, "dead", (Byte)BlockPhysics.asmHelper.get(fsand, "dead")-1);
			if ((Byte)BlockPhysics.asmHelper.get(fsand, "dead") <= 0) {
				fsand.setDead();
			}
			return;
		}
		Material mt;
		fsand.noClip = true;
		fsand.onGround = false;

		if ( j < -3  || fsand.field_145812_b > 600)
		{
			fsand.setDead();
			if ( !world.isRemote ) {
				BlockPhysics.dropFallingSand( fsand );
			}
		}
		BlockPhysics.asmHelper.set(fsand, "media", Block.blockRegistry.getNameForObject(world.getBlock(i,j, k)));

		if ((Byte)BlockPhysics.asmHelper.get(fsand, "slideDir") != 0)
		{
			if (fsand.field_145812_b < 8)
			{
				final int stime = fsand.field_145812_b - 3;
				final int sdir = (Byte)BlockPhysics.asmHelper.get(fsand, "slideDir") - 1;

				if (stime == 0)
				{
					switch ( sdir )
					{
						case 0:	fsand.setPosition(i - 0.0625D  + 0.5D, j - 0.0625D  + 0.5D, k  + 0.5D); break;
						case 1:	fsand.setPosition(i  + 0.5D, j - 0.0625D  + 0.5D, k - 0.0625D  + 0.5D); break;
						case 2: fsand.setPosition(i  + 0.5D, j - 0.0625D  + 0.5D, k + 0.0625D  + 0.5D); break;
						case 3:	fsand.setPosition(i + 0.0625D  + 0.5D, j - 0.0625D  + 0.5D, k  + 0.5D); break;
						default:
					}
				}

				fsand.motionX = BlockPhysics.slideSpeedz[stime][sdir][0];
				fsand.motionY = BlockPhysics.slideSpeedz[stime][sdir][1];
				fsand.motionZ = BlockPhysics.slideSpeedz[stime][sdir][2];
				BlockPhysics.asmHelper.set(fsand, "accelerationX", 0D);
				BlockPhysics.asmHelper.set(fsand, "accelerationY", 0D);
				BlockPhysics.asmHelper.set(fsand, "accelerationZ", 0D);
			}
			else
			{
				BlockPhysics.asmHelper.set(fsand, "slideDir", 0);
			}
		}

		if (fsand.motionX > 3.9D) {
			fsand.motionX = 3.9D;
		} else if (fsand.motionX < -3.9D) {
			fsand.motionX = -3.9D;
		}
		if (fsand.motionY > 3.9D) {
			fsand.motionY = 3.9D;
		} else if (fsand.motionY < -3.9D) {
			fsand.motionY = -3.9D;
		}
		if (fsand.motionZ > 3.9D) {
			fsand.motionZ = 3.9D;
		} else if (fsand.motionZ < -3.9D) {
			fsand.motionZ = -3.9D;
		}

		//writetoLog(";"+world.isRemote+";ID: ;"+fsand.entityId+";slidedir: ;"+fsand.slideDir+";mass: ;"+fsand.mass+"; field_145812_b: ;"+fsand.field_145812_b+";pos: ;"+fsand.posX+"; "+fsand.posY+"; "+fsand.posZ+";speed: ;"+fsand.motionX+"; "+fsand.motionY+"; "+fsand.motionZ+"; acceleration: ;"+fsand.accelerationX+"; "+fsand.accelerationY+"; "+fsand.accelerationZ);

		final double cmotionX = fsand.motionX;
		final double cmotionY = fsand.motionY;
		final double cmotionZ = fsand.motionZ;

		final double caccelerationX = (Double)BlockPhysics.asmHelper.get(fsand, "accelerationX");
		final double caccelerationY = (Double)BlockPhysics.asmHelper.get(fsand, "accelerationY");
		final double caccelerationZ = (Double)BlockPhysics.asmHelper.get(fsand, "accelerationZ");

		BlockPhysics.asmHelper.set(fsand, "accelerationX", 0D);
		BlockPhysics.asmHelper.set(fsand, "accelerationY", 0D);
		BlockPhysics.asmHelper.set(fsand, "accelerationZ", 0D);

		if ( (Byte)BlockPhysics.asmHelper.get(fsand, "slideDir") == 0 )
		{
			fsand.motionX = BlockPhysics.bSpeedR(fsand.motionX + caccelerationX);
			fsand.motionY = BlockPhysics.bSpeedR(fsand.motionY + caccelerationY);
			fsand.motionZ = BlockPhysics.bSpeedR(fsand.motionZ + caccelerationZ);
		}

		double moveX = cmotionX + caccelerationX * 0.5D;
		double moveY = cmotionY + caccelerationY * 0.5D;
		double moveZ = cmotionZ + caccelerationZ * 0.5D;

		final double axisaligned_maxmove = MathHelper.abs_max(MathHelper.abs_max(moveX, moveZ),moveY);
		double blockofsX, blockofsY, blockofsZ;  // point on the face of the block in the direction of moving
		if (axisaligned_maxmove != 0)
		{
			blockofsX = 0.498D * moveX / axisaligned_maxmove;
			blockofsY = 0.498D * moveY / axisaligned_maxmove;
			blockofsZ = 0.498D * moveZ / axisaligned_maxmove;
		}
		else
		{
			blockofsX = 0D;
			blockofsY = 0D;
			blockofsZ = 0D;
		}

		final double djumpdist2 = blockofsX * blockofsX + blockofsY * blockofsY + blockofsZ *blockofsZ;
		final double jumpdist2 = moveX * moveX + moveY * moveY + moveZ * moveZ;

		final int mass = DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(fsand.func_145805_f()),fsand.field_145814_a).mass;
		final int em = mass / 10 + (int)(0.5D * mass * jumpdist2 );
		//if (!world.isRemote) writetoLog(";"+em);

		if ( fsand.isBurning() && jumpdist2 > 4D && fsand.func_145805_f() != Blocks.netherrack ) {
			fsand.extinguish();
		}

		AxisAlignedBB Sandbbox = null;

		int ii;
		if ( djumpdist2 == 0 ) {
			ii = 0;
		} else {
			ii = ( int ) Math.ceil(MathHelper.sqrt_double(jumpdist2/djumpdist2));
		}

		double jumpPosX = 0;
		double jumpPosY = 0;
		double jumpPosZ = 0;

		int in = 0;
		int jn = 0;
		int kn = 0;
		int ip = i;
		int jp = j;
		int kp = k;

		for (int i1 = 1; i1 <= ii ; i1++)
		{
			if (i1 == ii)
			{
				jumpPosX = fsand.posX + moveX;
				jumpPosY = fsand.posY + moveY;
				jumpPosZ = fsand.posZ + moveZ;

				Sandbbox = fsand.boundingBox.copy();
				Sandbbox.offset(moveX,moveY,moveZ);
			}
			else if (i1 == 1)
			{
				jumpPosX = fsand.posX + blockofsX;
				jumpPosY = fsand.posY + blockofsY;
				jumpPosZ = fsand.posZ + blockofsZ;

				Sandbbox = fsand.boundingBox.copy();
				Sandbbox.offset(blockofsX,blockofsY,blockofsZ);
			}
			else
			{
				jumpPosX += blockofsX;
				jumpPosY += blockofsY;
				jumpPosZ += blockofsZ;

				Sandbbox.offset(blockofsX,blockofsY,blockofsZ);
			}

			//writetoLog(";"+world.isRemote+";ID: ;"+fsand.entityId+";slidedir: ;"+fsand.slideDir+";onground: ;"+fsand.onGround+";mass: ;"+blockSet[fsand.blockID][fsand.metadata].mass+"; field_145812_b: ;"+fsand.field_145812_b+";pos: ;"+jumpPosX+"; "+jumpPosY+"; "+jumpPosZ+";speed: ;"+fsand.motionX+"; "+fsand.motionY+"; "+fsand.motionZ+"; acceleration: ;"+fsand.accelerationX+"; "+fsand.accelerationY+"; "+fsand.accelerationZ);

			in = MathHelper.floor_double(jumpPosX);
			jn = MathHelper.floor_double(jumpPosY);
			kn = MathHelper.floor_double(jumpPosZ);

			if (jp != jn || ip != in || kp != kn)
			{
				String bidn = Block.blockRegistry.getNameForObject(world.getBlock(in, jn, kn));
				final int metan = world.getBlockMetadata(in, jn, kn);


				if (  DefinitionMaps.getBlockDef(bidn,metan).fragile > 0 )
				{
					final Block block = (Block)Block.blockRegistry.getObject(bidn);

					if (  !world.isRemote )
					{
						if ( DefinitionMaps.getBlockDef(bidn,metan).fragile > 0 )
						{
							if ( ((Block)Block.blockRegistry.getObject(bidn)).hasTileEntity(metan) )
							{
								final TileEntity tileEntity = world.getTileEntity(in, jn, kn);
								
								if (tileEntity != null)
								{
									final NBTTagCompound nnn = new NBTTagCompound();
									tileEntity.writeToNBT(nnn);
									BlockPhysics.dropItemsNBT(world, in, jn, kn, nnn);
									world.removeTileEntity(in, jn, kn);
								}
							}

							if ( DefinitionMaps.getBlockDef(bidn,metan).fragile == 2 ) {
								((Block)Block.blockRegistry.getObject(bidn)).dropBlockAsItem( world, in, jn, kn, metan, 0 );
							}

							world.setBlockToAir(in, jn, kn);
						}
					}
					bidn = Block.blockRegistry.getNameForObject(Blocks.air);

					world.playSoundEffect(in + 0.5F, jn + 0.5F, kn + 0.5F, block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

					final double sl = 1D - DefinitionMaps.getBlockDef(bidn,metan).strength / 64000D;
					fsand.motionX *= sl;
					fsand.motionY *= sl;
					fsand.motionZ *= sl;
				}

				if ( fsand.isBurning() && bidn.equals(Block.blockRegistry.getNameForObject(Blocks.air)) )
				{
					world.spawnParticle("largesmoke", (float)jumpPosX + BlockPhysics.rand.nextFloat(),(float)jumpPosY + BlockPhysics.rand.nextFloat(), (float)jumpPosZ + BlockPhysics.rand.nextFloat(), 0D, 0D, 0D);
					world.spawnParticle("flame", (float)jumpPosX + BlockPhysics.rand.nextFloat(), (float)jumpPosY + BlockPhysics.rand.nextFloat(), (float)jumpPosZ + BlockPhysics.rand.nextFloat(), 0D, 0.2D, 0D);
				}

				if (BlockPhysics.asmHelper.get(fsand, "media") != bidn)
				{
					if (!bidn.equals(Block.blockRegistry.getNameForObject(Blocks.air)))
					{
						mt = ((Block)Block.blockRegistry.getObject(bidn)).getMaterial();
						if ( mt.isLiquid() )
						{
							if ( mt == Material.lava )
							{
								if (BlockPhysics.canBurn(Block.blockRegistry.getNameForObject(fsand.func_145805_f()))) {
									fsand.setFire(60);
								} else {
									fsand.setFire(1);
								}
								world.playSoundAtEntity(fsand, "random.fizz", 1F, 1.0F + (BlockPhysics.rand.nextFloat() - BlockPhysics.rand.nextFloat()) * 0.4F);
							}
							else
							{
								fsand.extinguish();
								world.playSoundAtEntity(fsand, "random.splash", 1F, 1.0F + (BlockPhysics.rand.nextFloat() - BlockPhysics.rand.nextFloat()) * 0.4F);
							}
						}
						else if ( bidn.equals(Block.blockRegistry.getNameForObject(Blocks.fire)))
						{
							world.playSoundAtEntity(fsand, "random.fizz", 0.5F, 1.0F + (BlockPhysics.rand.nextFloat() - BlockPhysics.rand.nextFloat()) * 0.4F);
						}
					}
					BlockPhysics.asmHelper.set(fsand, "media", bidn);

					if ( (Byte)BlockPhysics.asmHelper.get(fsand, "slideDir") == 0 && !bidn.equals(Block.blockRegistry.getNameForObject(Blocks.fire)))
					{
						moveX = jumpPosX - fsand.posX;
						moveY = jumpPosY - fsand.posY;
						moveZ = jumpPosZ - fsand.posZ;

						break;
					}
				}

				ip = in;
				jp = jn;
				kp = kn;
			}

			if ( (Byte)BlockPhysics.asmHelper.get(fsand, "slideDir") == 0 )
			{

				if ( !BlockPhysics.canMoveTo(world, MathHelper.floor_double(jumpPosX + 0.499D), MathHelper.floor_double(jumpPosY +  0.499D),MathHelper.floor_double(jumpPosZ +  0.499D), em) || !BlockPhysics.canMoveTo(world, MathHelper.floor_double(jumpPosX +  0.499D), MathHelper.floor_double(jumpPosY + 0.499D),MathHelper.floor_double(jumpPosZ -  0.499D), em) || !BlockPhysics.canMoveTo(world, MathHelper.floor_double(jumpPosX + 0.499D), MathHelper.floor_double(jumpPosY - 0.499D),MathHelper.floor_double(jumpPosZ + 0.499D), em) || !BlockPhysics.canMoveTo(world, MathHelper.floor_double(jumpPosX + 0.499D), MathHelper.floor_double(jumpPosY - 0.499D),MathHelper.floor_double(jumpPosZ - 0.499D), em) || !BlockPhysics.canMoveTo(world, MathHelper.floor_double(jumpPosX - 0.499D), MathHelper.floor_double(jumpPosY + 0.499D), MathHelper.floor_double(jumpPosZ + 0.499D), em) || !BlockPhysics.canMoveTo(world, MathHelper.floor_double(jumpPosX - 0.499D), MathHelper.floor_double(jumpPosY + 0.499D),MathHelper.floor_double(jumpPosZ - 0.499D), em) || !BlockPhysics.canMoveTo(world, MathHelper.floor_double(jumpPosX - 0.499D), MathHelper.floor_double(jumpPosY - 0.499D),MathHelper.floor_double(jumpPosZ + 0.499D), em) || !BlockPhysics.canMoveTo(world, MathHelper.floor_double(jumpPosX - 0.499D), MathHelper.floor_double(jumpPosY - 0.499D),MathHelper.floor_double(jumpPosZ - 0.499D), em))
				{

					double eimp = 0.0005D * jumpdist2 * DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(fsand.func_145805_f()),fsand.field_145814_a).mass;

					if ( eimp > 0.5D )
					{
						if ( !world.isRemote )
						{
							if ( eimp > 3.5D ) {
								eimp = 3.5D;
							}
							//writetoLog(""+Math.sqrt(jumpdist2)+"       "+eimp);
							final Explosion var10 = new Explosion( world, fsand, jumpPosX, jumpPosY, jumpPosZ, (float) eimp );
							if ( fsand.isBurning() ) {
								var10.isFlaming = true;
							}
							BlockPhysics.asmHelper.set(var10, "impact", true);
							((ExplosionQueue)BlockPhysics.asmHelper.get(world, "explosionQueue")).add(var10);
						}

						fsand.motionX *= 0.7D;
						fsand.motionY *= 0.7D;
						fsand.motionZ *= 0.7D;
						fsand.velocityChanged = true;
					}

					if ( DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(fsand.func_145805_f()),fsand.field_145814_a).fragile > 0 && em > DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(fsand.func_145805_f()),fsand.field_145814_a).strength)
					{
						final Block block = fsand.func_145805_f();
						world.playSoundEffect(jumpPosX, jumpPosY, jumpPosZ, block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
						BlockPhysics.asmHelper.set(fsand, "dead", (Byte)BlockPhysics.asmHelper.get(fsand, "dead")-1);
						if (!world.isRemote)
						{
							if ( DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(fsand.func_145805_f()),fsand.field_145814_a).fragile == 2 )
							{
								fsand.posX = jumpPosX;
								fsand.posY = jumpPosY;
								fsand.posZ = jumpPosZ;
								BlockPhysics.dropFallingSand(fsand);
							}
							else if( DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(fsand.func_145805_f()),fsand.field_145814_a).fragile == 1 && fsand.field_145810_d != null ) {
								BlockPhysics.dropItemsNBT(world, MathHelper.floor_double(jumpPosX),  MathHelper.floor_double(jumpPosY),  MathHelper.floor_double(jumpPosZ), fsand.field_145810_d);
							}
						}
						return;
					}

					moveX = jumpPosX - fsand.posX;
					moveY = jumpPosY - fsand.posY;
					moveZ = jumpPosZ - fsand.posZ;

					fsand.noClip = false;

					break;
				}

				final Entity collent = world.findNearestEntityWithinAABB(Entity.class, Sandbbox, fsand);

				if ( collent != null )
				{
					if ( collent instanceof EntityFallingBlock)
					{

						final double m1 = DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(fsand.func_145805_f()),fsand.field_145814_a).mass;
						final double m2 = DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(((EntityFallingBlock)collent).func_145805_f()),((EntityFallingBlock)collent).field_145814_a).mass;
						final double smass =  m1 + m2;
						double vv;

						double is = m1 * fsand.motionX + m2 * collent.motionX;
						vv = BlockPhysics.bSpeedR( 0.98D * is / smass );

						fsand.motionX = vv;
						collent.motionX = vv;

						is = m1 * fsand.motionZ + m2 * collent.motionZ;
						vv = BlockPhysics.bSpeedR( 0.98D * is / smass );

						fsand.motionZ = vv;
						collent.motionZ = vv;

						is = m1 * fsand.motionY + m2 * collent.motionY;
						vv = BlockPhysics.bSpeedR( 0.98D * is / smass );

						fsand.motionY = vv;
						collent.motionY = vv;

						fsand.velocityChanged = true;
						collent.velocityChanged = true;

						if ( DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(((EntityFallingBlock)collent).func_145805_f()),((EntityFallingBlock)collent).field_145814_a).fragile > 0 && em > DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(((EntityFallingBlock)collent).func_145805_f()),((EntityFallingBlock)collent).field_145814_a).strength)
						{
							final Block block = ((EntityFallingBlock)collent).func_145805_f();
							world.playSoundEffect(collent.posX, collent.posY, collent.posZ, block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

							BlockPhysics.asmHelper.set(collent, "dead", (Byte)BlockPhysics.asmHelper.get(collent, "dead")-1);
							if (!world.isRemote)
							{
								if ( DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(((EntityFallingBlock)collent).func_145805_f()),((EntityFallingBlock)collent).field_145814_a).fragile == 2 ) {
									BlockPhysics.dropFallingSand((EntityFallingBlock)collent);
								} else if( DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(((EntityFallingBlock)collent).func_145805_f()),((EntityFallingBlock)collent).field_145814_a).fragile == 1 && ((EntityFallingBlock)collent).field_145810_d != null ) {
									BlockPhysics.dropItemsNBT(world, MathHelper.floor_double(collent.posX),  MathHelper.floor_double(collent.posY),  MathHelper.floor_double(collent.posZ), ((EntityFallingBlock)collent).field_145810_d);
								}
							}
						}

						if ( DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(fsand.func_145805_f()),fsand.field_145814_a).fragile > 0 && em > DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(fsand.func_145805_f()),fsand.field_145814_a).strength)
						{
							final Block block = fsand.func_145805_f();
							world.playSoundEffect(jumpPosX, jumpPosY, jumpPosZ, block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

							BlockPhysics.asmHelper.set(fsand, "dead", (Byte)BlockPhysics.asmHelper.get(fsand, "dead")-1);
							if (!world.isRemote)
							{
								if ( DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(fsand.func_145805_f()),fsand.field_145814_a).fragile == 2 )
								{
									fsand.posX = jumpPosX;
									fsand.posY = jumpPosY;
									fsand.posZ = jumpPosZ;
									BlockPhysics.dropFallingSand(fsand);
								}
								else if( DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(fsand.func_145805_f()),fsand.field_145814_a).fragile == 1 && fsand.field_145810_d != null ) {
									BlockPhysics.dropItemsNBT(world, MathHelper.floor_double(jumpPosX),  MathHelper.floor_double(jumpPosY),  MathHelper.floor_double(jumpPosZ), fsand.field_145810_d);
								}
							}
							return;
						}

						fsand.noClip = false;

						moveX = jumpPosX - fsand.posX;
						moveY = jumpPosY - fsand.posY;
						moveZ = jumpPosZ - fsand.posZ;
						break;
					}
					else if ( collent instanceof EntityLivingBase )
					{
						//entityCollide(world, fsand, collent, (double)((EntityLiving) collent).getMaxHealth() * 3.0D, true);

						final double m1 = DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(fsand.func_145805_f()),fsand.field_145814_a).mass;
						final double m2 = ((EntityLivingBase) collent).getMaxHealth() * 3.0D;
						final double smass =  m1 + m2;
						double vv;
						double damage = fsand.motionX*fsand.motionX + fsand.motionY*fsand.motionY + fsand.motionZ*fsand.motionZ;

						double is = m1 * fsand.motionX + m2 * ( collent.posX - collent.prevPosX );
						vv = BlockPhysics.bSpeedR( 0.98D * is / smass );
						damage -= vv * vv;
						fsand.motionX = vv;
						collent.motionX = vv;

						is = m1 * fsand.motionZ + m2 * ( collent.posZ - collent.prevPosZ );
						vv = BlockPhysics.bSpeedR( 0.98D * is / smass );
						damage -= vv * vv;
						fsand.motionZ = vv;
						collent.motionZ = vv;

						if ( fsand.motionY < 0 && collent.onGround)
						{
							if (fsand.motionY < - 0.3D) {
								vv = BlockPhysics.bSpeedR(0.5D * fsand.motionY);
							} else {
								vv = fsand.motionY;
							}
						}
						else
						{
							is = m1 * fsand.motionY + m2 * ( collent.posY - collent.prevPosY );
							vv = BlockPhysics.bSpeedR( 0.98D * is / smass );
						}

						damage -= vv * vv;
						fsand.motionY = vv;
						collent.motionY = vv;

						fsand.velocityChanged = true;
						collent.velocityChanged = true;

						final int d = (int)(0.083D * m1 * damage);
						if ( d > 0 )
						{
							//if ( !world.isRemote ) writetoLog(""+d);
							if ( d > 4 )
							{
								world.playSoundAtEntity(collent, "damage.fallbig", 1.0F, 1.0F);
							}

							if ( !world.isRemote )
							{
								//BlockPhysics.instance.log.info("Attacking entity for " + d + " damage.");
								((EntityLivingBase)collent).attackEntityFrom(DamageSource.fallingBlock, d);
							}
						}

						if ( !world.isRemote )
						{
							if ( collent instanceof EntityPlayerMP  )
							{
								//TODO Hopefully this is right
								((EntityPlayerMP)collent).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(collent.getEntityId(), collent.motionX, collent.motionY, collent.motionZ));
							}
						}

						moveX = jumpPosX - fsand.posX;
						moveY = jumpPosY - fsand.posY;
						moveZ = jumpPosZ - fsand.posZ;
						break;
					}
					else if ( collent instanceof EntityItem )
					{
						collent.motionX = fsand.motionX;
						collent.motionY = fsand.motionY;
						collent.motionZ = fsand.motionZ;
						collent.velocityChanged = true;
					}
					else
					{
						BlockPhysics.asmHelper.set(fsand, "accelerationX", (Double)BlockPhysics.asmHelper.get(fsand, "accelerationX")-fsand.motionX);
						BlockPhysics.asmHelper.set(fsand, "accelerationY", (Double)BlockPhysics.asmHelper.get(fsand, "accelerationY")-fsand.motionY);
						BlockPhysics.asmHelper.set(fsand, "accelerationZ", (Double)BlockPhysics.asmHelper.get(fsand, "accelerationZ")-fsand.motionZ);
						//fsand.velocityChanged = true;

						moveX = jumpPosX - fsand.posX;
						moveY = jumpPosY - fsand.posY;
						moveZ = jumpPosZ - fsand.posZ;
						break;
					}
				}
			}
			else
			{
				final Entity collent = world.findNearestEntityWithinAABB(Entity.class, Sandbbox, fsand);

				if ( collent != null && ( collent instanceof EntityLiving || collent instanceof EntityItem ) )
				{
					collent.motionX = collent.motionX * 0.2D + fsand.motionX * 0.8D;
					collent.motionY = collent.motionY * 0.2D + fsand.motionY * 0.8D;
					collent.motionZ = collent.motionZ * 0.2D + fsand.motionZ * 0.8D;
					collent.velocityChanged = true;
					if ( !world.isRemote )
					{
						if ( collent instanceof EntityPlayerMP  )
						{
							((EntityPlayerMP)collent).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(collent.getEntityId(), collent.motionX, collent.motionY, collent.motionZ));
						}
					}
				}
			}


		}

		double density = 1.25D;

		if (!((String)BlockPhysics.asmHelper.get(fsand, "media")).equals(Block.blockRegistry.getNameForObject(Blocks.air)) )
		{
			mt = ((Block)Block.blockRegistry.getObject((String)BlockPhysics.asmHelper.get(fsand, "media"))).getMaterial();
			if ( mt.isLiquid() )
			{
				if ( mt == Material.lava)
				{
					density = 2000D;
					//fsand.accelerationY += 49.05D / (double)fsand.mass;
				}
				else
				{
					density = 1000D;
					//fsand.accelerationY += 24.525D / (double)fsand.mass;
				}
			}
			else if ( !world.isRemote && !TypeHelper.instanceOf(EntityTNTPrimed.class, fsand) )
			{
				BlockPhysics.placeBlock( world, fsand, jumpPosX, jumpPosY, jumpPosZ, in, jn, kn);
				return;
			}
		}

		density = -0.5D * 0.8D * density / DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(fsand.func_145805_f()),fsand.field_145814_a).mass;
		double aaccX = density * fsand.motionX * Math.abs(fsand.motionX);
		double aaccY = density * fsand.motionY * Math.abs(fsand.motionY);
		double aaccZ = density * fsand.motionZ * Math.abs(fsand.motionZ);

		BlockPhysics.asmHelper.set(fsand, "accelerationY", (Double)BlockPhysics.asmHelper.get(fsand, "accelerationY")-0.024525D);

		double mmot = fsand.motionX + aaccX;
		if ( fsand.motionX < 0 && mmot > 0 || fsand.motionX > 0 && mmot < 0 )
		{
			aaccX = -0.9D * fsand.motionX;
		}

		mmot = fsand.motionY + aaccY;
		if ( fsand.motionY < 0 && mmot > 0 || fsand.motionY > 0 && mmot < 0)
		{
			aaccY = -0.9D * fsand.motionY;
		}

		mmot = fsand.motionZ + aaccZ;
		if ( fsand.motionZ < 0 && mmot > 0 || fsand.motionZ > 0 && mmot < 0 )
		{
			aaccZ = -0.9D * fsand.motionZ;
		}
		BlockPhysics.asmHelper.set(fsand, "accelerationX", (Double)BlockPhysics.asmHelper.get(fsand, "accelerationX") + aaccX);
		BlockPhysics.asmHelper.set(fsand, "accelerationY", (Double)BlockPhysics.asmHelper.get(fsand, "accelerationY") + aaccY);
		BlockPhysics.asmHelper.set(fsand, "accelerationZ", (Double)BlockPhysics.asmHelper.get(fsand, "accelerationZ") + aaccZ);

		fsand.prevPosX = fsand.posX;
		fsand.prevPosY = fsand.posY;
		fsand.prevPosZ = fsand.posZ;

		BlockPhysics.moveEntity(world, fsand, moveX, moveY, moveZ);

		i = MathHelper.floor_double(fsand.posX);
		j = MathHelper.floor_double(fsand.posY);
		k = MathHelper.floor_double(fsand.posZ);

		if (fsand.onGround)
		{
			fsand.motionX *= 0.9D;
			fsand.motionZ *= 0.9D;
			//fsand.motionY *= -0.5D;
		}

		if (TypeHelper.instanceOf(EntityTNTPrimed.class, fsand))
		{
			final EntityTNTPrimed primed = TypeHelper.cast(EntityTNTPrimed.class, fsand);
			if (!world.isRemote && primed.fuse-- <= 0 )
			{
				fsand.setDead();
				primed.explode();
			}

			world.spawnParticle("smoke", fsand.posX, fsand.posY + 0.5D, fsand.posZ, 0.0D, 0.0D, 0.0D);

		}
		else if ( fsand.onGround )
		{
			if ( jumpdist2 < 0.05D && BlockPhysics.canMoveTo(world, i, j, k, em) )
			{
				final Block block = fsand.func_145805_f();
				world.playSoundEffect(fsand.posX, fsand.posY, fsand.posZ, block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

				BlockPhysics.asmHelper.set(fsand, "dead", (Byte)BlockPhysics.asmHelper.get(fsand, "dead")-1);
				if (!world.isRemote)
				{
					world.setBlock(i, j, k, fsand.func_145805_f(), fsand.field_145814_a, 3);
					BlockPhysics.setBlockBPdata( world,i, j, k, (Integer)BlockPhysics.asmHelper.get(fsand, "bpdata"));
					if (fsand.field_145810_d != null)
					{
						final TileEntity tile = fsand.func_145805_f().createTileEntity(world, fsand.field_145814_a);
						tile.readFromNBT(fsand.field_145810_d);
						world.setTileEntity(i, j, k, tile);
					}
					if (fsand.isBurning() && world.getBlock(i, j+1, k) == Blocks.air) {
						world.setBlock(i, j +1, k, Blocks.fire, 0, 3);
					}
					((BTickList)BlockPhysics.asmHelper.get(world, "moveTickList")).scheduleBlockMoveUpdate(world, i, j, k, Block.blockRegistry.getNameForObject(fsand.func_145805_f()), fsand.field_145814_a, true);
					BlockPhysics.notifyMove(world, i,j,k);
				}

			}
		}

	}

	public static void dropFallingSand(final EntityFallingBlock fallingBlock)
	{
		if (fallingBlock.field_145810_d != null) {
			BlockPhysics.dropItemsNBT(fallingBlock.worldObj, MathHelper.floor_double(fallingBlock.posX), MathHelper.floor_double(fallingBlock.posY), MathHelper.floor_double(fallingBlock.posZ), fallingBlock.field_145810_d);
		}

		if (fallingBlock.func_145805_f() != null) {
			fallingBlock.entityDropItem(new ItemStack(fallingBlock.func_145805_f(), 1, fallingBlock.func_145805_f().damageDropped(fallingBlock.field_145814_a)), 0.0F);
		}
	}

	protected static void placeBlock(final World world, final EntityFallingBlock fsand, final double jumpPosX, final double jumpPosY, final double jumpPosZ,  final int i, final int j, final int k)
	{
		double dist2 = 100;
		double dist22, s1 = 0, s2 = 0, s3 = 0;
		int x = 0;
		int y = 0;
		int z = 0;

		for (int ii = i-1; ii < i+2; ii++)
		{
			for (int jj = j-1; jj < j+2; jj++)
			{
				for (int kk = k-1; kk < k+2; kk++)
				{
					if (BlockPhysics.canMoveTo(world, ii, jj, kk, 0))
					{
						s1 = 0.5D + ii - jumpPosX;
						s2 = 0.5D + jj - jumpPosY;
						s3 = 0.5D + kk - jumpPosZ;
						dist22 = s1 * s1 + s2 * s2 + s3 * s3;
						if (dist22 < dist2 )
						{
							dist2 = dist22;
							x = ii;
							y = jj;
							z = kk;
						}
					}
				}
			}
		}

		fsand.setDead();
		//if (world.isRemote) return;

		if ( dist2 < 100 )
		{
			world.setBlock(x, y, z, fsand.func_145805_f(), fsand.field_145814_a, 3);
			BlockPhysics.setBlockBPdata( world,x, y, z, (Integer)BlockPhysics.asmHelper.get(fsand, "bpdata"));
			if (fsand.field_145810_d != null)
			{
				final TileEntity tile = fsand.func_145805_f().createTileEntity(world, fsand.field_145814_a);
				tile.readFromNBT(fsand.field_145810_d);
				world.setTileEntity(x, y, z, tile);
			}
			((BTickList)BlockPhysics.asmHelper.get(world, "moveTickList")).scheduleBlockMoveUpdate(world, x, y, z, Block.blockRegistry.getNameForObject(fsand.func_145805_f()), fsand.field_145814_a, true);
		}
		else
		{
			fsand.posX = 0.5D + i;
			fsand.posY = 0.5D + j;
			fsand.posZ = 0.5D + k;
			BlockPhysics.dropFallingSand ( fsand );
		}
	}

	public static void moveEntity(final World world, final EntityFallingBlock fsand, double par1, double par3, double par5)
	{
		if (fsand.noClip)
		{
			fsand.boundingBox.offset(par1, par3, par5);
			fsand.posX = (fsand.boundingBox.minX + fsand.boundingBox.maxX) / 2.0D;
			fsand.posY = fsand.boundingBox.minY + fsand.yOffset - fsand.ySize;
			fsand.posZ = (fsand.boundingBox.minZ + fsand.boundingBox.maxZ) / 2.0D;
		}
		else
		{
			fsand.ySize *= 0.4F;
			final double d3 = fsand.posX;
			final double d4 = fsand.posY;
			final double d5 = fsand.posZ;

			final double d6 = par1;
			final double d7 = par3;
			final double d8 = par5;
			final AxisAlignedBB axisalignedbb = fsand.boundingBox.copy();

			List list = world.getCollidingBoundingBoxes(fsand, fsand.boundingBox.addCoord(par1, par3, par5));

			for (int i = 0; i < list.size(); ++i)
			{
				par3 = ((AxisAlignedBB)list.get(i)).calculateYOffset(fsand.boundingBox, par3);
			}

			fsand.boundingBox.offset(0.0D, par3, 0.0D);

			if (!fsand.field_70135_K && d7 != par3)
			{
				par5 = 0.0D;
				par3 = 0.0D;
				par1 = 0.0D;
			}

			final boolean flag1 = fsand.onGround || d7 != par3 && d7 < 0.0D;
			int j;

			for (j = 0; j < list.size(); ++j)
			{
				par1 = ((AxisAlignedBB)list.get(j)).calculateXOffset(fsand.boundingBox, par1);
			}

			fsand.boundingBox.offset(par1, 0.0D, 0.0D);

			if (!fsand.field_70135_K && d6 != par1)
			{
				par5 = 0.0D;
				par3 = 0.0D;
				par1 = 0.0D;
			}

			for (j = 0; j < list.size(); ++j)
			{
				par5 = ((AxisAlignedBB)list.get(j)).calculateZOffset(fsand.boundingBox, par5);
			}

			fsand.boundingBox.offset(0.0D, 0.0D, par5);

			if (!fsand.field_70135_K && d8 != par5)
			{
				par5 = 0.0D;
				par3 = 0.0D;
				par1 = 0.0D;
			}

			double d10;
			double d11;
			int k;
			double d12;

			if (fsand.stepHeight > 0.0F && flag1 && fsand.ySize < 0.05F && (d6 != par1 || d8 != par5))
			{
				d12 = par1;
				d10 = par3;
				d11 = par5;
				par1 = d6;
				par3 = fsand.stepHeight;
				par5 = d8;
				final AxisAlignedBB axisalignedbb1 = fsand.boundingBox.copy();
				fsand.boundingBox.setBB(axisalignedbb);
				list = world.getCollidingBoundingBoxes(fsand, fsand.boundingBox.addCoord(d6, par3, d8));

				for (k = 0; k < list.size(); ++k)
				{
					par3 = ((AxisAlignedBB)list.get(k)).calculateYOffset(fsand.boundingBox, par3);
				}

				fsand.boundingBox.offset(0.0D, par3, 0.0D);

				if (!fsand.field_70135_K && d7 != par3)
				{
					par5 = 0.0D;
					par3 = 0.0D;
					par1 = 0.0D;
				}

				for (k = 0; k < list.size(); ++k)
				{
					par1 = ((AxisAlignedBB)list.get(k)).calculateXOffset(fsand.boundingBox, par1);
				}

				fsand.boundingBox.offset(par1, 0.0D, 0.0D);

				if (!fsand.field_70135_K && d6 != par1)
				{
					par5 = 0.0D;
					par3 = 0.0D;
					par1 = 0.0D;
				}

				for (k = 0; k < list.size(); ++k)
				{
					par5 = ((AxisAlignedBB)list.get(k)).calculateZOffset(fsand.boundingBox, par5);
				}

				fsand.boundingBox.offset(0.0D, 0.0D, par5);

				if (!fsand.field_70135_K && d8 != par5)
				{
					par5 = 0.0D;
					par3 = 0.0D;
					par1 = 0.0D;
				}

				if (!fsand.field_70135_K && d7 != par3)
				{
					par5 = 0.0D;
					par3 = 0.0D;
					par1 = 0.0D;
				}
				else
				{
					par3 = -fsand.stepHeight;

					for (k = 0; k < list.size(); ++k)
					{
						par3 = ((AxisAlignedBB)list.get(k)).calculateYOffset(fsand.boundingBox, par3);
					}

					fsand.boundingBox.offset(0.0D, par3, 0.0D);
				}

				if (d12 * d12 + d11 * d11 >= par1 * par1 + par5 * par5)
				{
					par1 = d12;
					par3 = d10;
					par5 = d11;
					fsand.boundingBox.setBB(axisalignedbb1);
				}
			}

			fsand.posX = (fsand.boundingBox.minX + fsand.boundingBox.maxX) / 2.0D;
			fsand.posY = fsand.boundingBox.minY + fsand.yOffset - fsand.ySize;
			fsand.posZ = (fsand.boundingBox.minZ + fsand.boundingBox.maxZ) / 2.0D;
			fsand.isCollidedHorizontally = d6 != par1 || d8 != par5;
			fsand.isCollidedVertically = d7 != par3;
			fsand.onGround = d7 != par3 && d7 < 0.0D;
			fsand.isCollided = fsand.isCollidedHorizontally || fsand.isCollidedVertically;
			//fsand.updateFallState(par3, fsand.onGround);

			if (d6 != par1)
			{
				fsand.motionX = 0.0D;
			}

			if (d7 != par3)
			{
				fsand.motionY = 0.0D;
			}

			if (d8 != par5)
			{
				fsand.motionZ = 0.0D;
			}

			d12 = fsand.posX - d3;
			d10 = fsand.posY - d4;
			d11 = fsand.posZ - d5;
		}
	}

	public static void tickBlocksRandomMove(final WorldServer wserver)
	{
		if ( BlockPhysics.skipMove ) {
			return;
		}

		final Iterator var3 = wserver.activeChunkSet.iterator();

		while (var3.hasNext())
		{
			final ChunkCoordIntPair var4 = (ChunkCoordIntPair)var3.next();
			final int var5 = var4.chunkXPos * 16;
			final int var6 = var4.chunkZPos * 16;
			final Chunk var7 = wserver.getChunkFromChunkCoords(var4.chunkXPos, var4.chunkZPos);

			int var9;
			int var10;
			int var13;

			final ExtendedBlockStorage[] var19 = var7.getBlockStorageArray();
			var9 = var19.length;

			for (var10 = 0; var10 < var9; ++var10)
			{
				final ExtendedBlockStorage var21 = var19[var10];

				if (var21 != null )
				{
					for (int var20 = 0; var20 < 3; ++var20)
					{
						BlockPhysics.updateLCG = BlockPhysics.updateLCG * 3 + 1013904223;
						var13 = BlockPhysics.updateLCG >> 2;
					final int var14 = var13 & 15;
					final int var15 = var13 >> 8 & 15;
			final int var16 = var13 >> 16 & 15;
				final String var17 = Block.blockRegistry.getNameForObject(var21.getBlockByExtId(var14, var16, var15));
				final int m = var21.getExtBlockMetadata(var14, var16, var15);

				if (DefinitionMaps.getBlockDef(var17,m).randomtick)
				{
					BlockPhysics.tryToMove(wserver, var14 + var5, var16 + var21.getYLocation(), var15 + var6, var17, m, false);
				}
					}
				}
			}
		}
		/*if (BlockPhysics.skipMove) {
			return;
		}

		final Iterator chunkIterator = worldServer.activeChunkSet.iterator();

		while (chunkIterator.hasNext())
		{
			final ChunkCoordIntPair chunkCoords = (ChunkCoordIntPair)chunkIterator.next();
			final int x = chunkCoords.chunkXPos * 16;
			final int z = chunkCoords.chunkXPos * 16;

			final Chunk chunk = worldServer.getChunkFromChunkCoords(chunkCoords.chunkXPos, chunkCoords.chunkZPos);

			int var13;

			final ExtendedBlockStorage[] blockStorageArray = chunk.getBlockStorageArray();
			final int storageArrayLength = blockStorageArray.length;

			for (int i = 0; i < storageArrayLength; i++)
			{
				final ExtendedBlockStorage blockStorage = blockStorageArray[i];

				if (blockStorage != null )
				{
					for (int var20 = 0; var20 < 3; ++var20)
					{
						BlockPhysics.updateLCG = BlockPhysics.updateLCG * 3 + 1013904223;
						var13 = BlockPhysics.updateLCG >> 2;
					final int xx = var13 & 15;
					final int zz = var13 >> 8 & 15;
			final int yy = var13 >> 16 & 15;
				final String blockName = Block.blockRegistry.getNameForObject(blockStorage.getBlockByExtId(xx, yy, zz));
				final int m = blockStorage.getExtBlockMetadata(xx, yy, zz);

				if (DefinitionMaps.getBlockDef(blockName, m).randomtick)
				{
					//BlockPhysics.tryToMove(worldServer, xx + x, yy + blockStorage.getYLocation(), zz + z, blockName, m, false);
				}
					}
				}
			}
		}*/
	}

	public static void setSkipMove(final long tickTime)
	{

		if ( tickTime > ModConfig.maxTickTime )
		{
			if ( BlockPhysics.skipMove == false )
			{
				BlockPhysics.instance.log.info("Switching off physics ( "+tickTime+" ).");
				BlockPhysics.skipMove = true;
			}
		}
		else
		{
			if ( BlockPhysics.skipMove == true )
			{
				BlockPhysics.instance.log.info("Physics are working again ( "+tickTime+" ).");
				BlockPhysics.skipMove = false;
				BlockPhysics.nextrand = BlockPhysics.rand.nextInt(100);
			}
		}
	}

	public static Block readFallingSandID(final NBTTagCompound nbt)
	{
		int bid;
		if (nbt.hasKey("TileID")) {
			bid = nbt.getInteger("TileID");
		} else if (nbt.hasKey("BlockID")) {
			bid = nbt.getShort("BlockID");
		} else {
			bid = nbt.getByte("Tile") & 255;
		}
		if (bid < 1 || bid > 4095 ) {
			bid = 3;
		}
		return (Block) Block.blockRegistry.getObjectById(bid);
	}

	public static EntityFallingBlock createFallingsand(final World world, final double var2, final double var4, final double var6, final S0EPacketSpawnObject par1Packet23VehicleSpawn )
	{
		final EntityFallingBlock var8 = new EntityFallingBlock(world, var2, var4, var6, Block.getBlockById(par1Packet23VehicleSpawn.func_149009_m() & 4095), par1Packet23VehicleSpawn.func_149009_m() >> 16);
		if ((par1Packet23VehicleSpawn.func_149009_m() >> 15 & 1) == 1) {
			var8.setFire(60);
		}
		BlockPhysics.asmHelper.set(var8, "slideDir", (byte) (par1Packet23VehicleSpawn.func_149009_m() >> 12 & 7));

		return var8;
	}

	public static void onNeighborBlockChange(final World par1World, final int par2, final int par3, final int par4, final String blockID)
	{
		if (par1World.isRemote ) {
			return;
		}
		if ((BTickList)BlockPhysics.asmHelper.get(par1World, "moveTickList") == null)
		{
			BlockPhysics.instance.log.info("World tickList null - " + par1World.provider.dimensionId);
			return;
		}

		((BTickList)BlockPhysics.asmHelper.get(par1World, "moveTickList")).scheduleBlockMoveUpdate(par1World, par2, par3, par4, blockID,
				par1World.getBlockMetadata(par2, par3, par4), false);
	}

	public static void onEntityCollidedWithBlock( final World world, final int par1, final int par2, final int par3, final String blockID, final Entity par5Entity)
	{
		if ( DefinitionMaps.getBlockDef(blockID,world.getBlockMetadata(par1, par2, par3)).trapping ) {
			par5Entity.setInWeb();
		}
	}

	public static void onPostBlockPlaced(final World par1World, final int par2, final int par3, final int par4, final String blockID, int meta)
	{
		if (!par1World.isRemote)
		{
			meta = meta & 15;
			if (DefinitionMaps.getBlockDef(blockID,meta).movenum == 2 ) {
				BlockPhysics.setBlockBPdata( par1World,par2, par3, par4, 15 * DefinitionMaps.getBlockDef(blockID,meta).placedmove);
			}
			BlockPhysics.notifyMove(par1World, par2, par3, par4);
		}
	}

	public static void updatePistonState(final World par1World, final int par2, final int par3, final int par4, final BlockPistonBase par1block, final boolean isSticky)
	{
		//BlockPhysics.instance.log.info("Attempting to update piston state");
		if (par1World.isRemote) {
			return;
		}
		final int var5 = par1World.getBlockMetadata(par2, par3, par4);
		int var6 = BlockPistonBase.getPistonOrientation(var5);
		if ( var6 > 5 ) {
			var6 = 0;
		}

		int vv = 0;
		if ( par1block.isIndirectlyPowered(par1World, par2, par3, par4, var6)) {
			vv = 8;
		}

		if (vv == (var5 & 8)) {
			return;
		}

		final int i2 = par2 + Facing.offsetsXForSide[var6];
		final int j2 = par3 + Facing.offsetsYForSide[var6];
		final int k2 = par4 + Facing.offsetsZForSide[var6];

		if ( par1World.getBlock(i2, j2, k2) == Blocks.piston_extension || ((HashSet<String>)BlockPhysics.asmHelper.get(par1World, "pistonMoveBlocks")).contains(""+par2+"."+par3+"."+par4)) {
			return;
		}

		int meta;
		String blid2;

		if ( vv == 8 )  //extending
		{
			int ext = 0;

			if (ModConfig.catapult && !BlockPhysics.skipMove && (Integer)BlockPhysics.asmHelper.get(((WorldServer)par1World).getEntityTracker(), "movingblocks") < ModConfig.maxMovingBlocks)
			{
				//BlockPhysics.instance.log.info("Attempting to check for dispensers");
				boolean catapultpowered = false;
				boolean catapultprecise = true;
				final int[] power = {0,0,0,0,0,0,0,0,0};
				final int xpw = par2 - Facing.offsetsXForSide[var6];
				final int ypw = par3 - Facing.offsetsYForSide[var6];
				final int zpw = par4 - Facing.offsetsZForSide[var6];

				if ( par1World.getBlock(xpw,ypw,zpw) == Blocks.dispenser )
				{
					final TileEntityDispenser tileentitydispenser = (TileEntityDispenser)par1World.getTileEntity(xpw, ypw, zpw);
					if (tileentitydispenser != null)
					{
						//BlockPhysics.instance.log.info("Found tile entity dispenser");
						ItemStack powstack;
						for ( int pp = 0; pp < 9; pp++)
						{
							powstack = tileentitydispenser.getStackInSlot(pp);

							if ( catapultprecise && (pp == 0 || pp == 2 || pp == 6 || pp == 8) && ( powstack == null || powstack.getItem() != Items.gold_ingot || powstack.stackSize < 4 ) ) {
								catapultprecise = false;
							}

							if ( powstack != null && powstack.getItem() == Items.redstone)
							{
								catapultpowered = true;
								power[pp] = powstack.stackSize;
								//BlockPhysics.instance.log.info("Found redstone in dispenser");
							}
						}
					}
				}

				if ( catapultpowered )
				{
					ext = BlockPhysics.canExtend(par1World, par2, par3, par4, var6, par1block, true);
					if ( ext == 0 ) {
						return;
					}
					//BlockPhysics.instance.log.info("Piston is powered and can extend");
					double pspX = 0, pspY = 0, pspZ = 0;
					switch ( var6 )
					{
						case 0:
						{
							pspX = -(double)power[0] / 3D + power[2] / 3D - power[6] / 3D + power[8] / 3D - power[3] / 2D + power[5] / 2D;
							pspY = -( power[4] + power[0] / 3D + power[6] / 3D + power[2] / 3D + power[8] / 3D + power[1] / 2D + power[7] / 2D + power[3] / 2D + power[5] / 2D);
							pspZ = -(double)power[0] / 3D - power[2] / 3D + power[6] / 3D + power[8] / 3D - power[1] / 2D + power[7] / 2D;
							break;
						}
						case 1:
						{
							pspX = -(double)power[0] / 3D + power[2] / 3D - power[6] / 3D + power[8] / 3D - power[3] / 2D + power[5] / 2D;
							pspY = power[4] + power[0] / 3D + power[6] / 3D + power[2] / 3D + power[8] / 3D + power[1] / 2D + power[7] / 2D + power[3] / 2D + power[5] / 2D;
							pspZ = -(double)power[0] / 3D - power[2] / 3D + power[6] / 3D + power[8] / 3D - power[1] / 2D + power[7] / 2D;
							break;
						}
						case 2:   	// - Z
						{
							pspX = power[0] / 3D - power[2] / 3D + power[6] / 3D - power[8] / 3D + power[3] / 2D - power[5] / 2D;
							pspY = power[0] / 3D - power[6] / 3D + power[2] / 3D - power[8] / 3D + power[1] / 2D - power[7] / 2D;
							pspZ = -( power[4] + power[0] / 3D + power[6] / 3D + power[2] / 3D + power[8] / 3D + power[1] / 2D + power[7] / 2D + power[3] / 2D + power[5] / 2D);
							break;
						}
						case 3:		// + Z
						{
							pspX = -( power[0] / 3D - power[2] / 3D + power[6] / 3D - power[8] / 3D + power[3] / 2D - power[5] / 2D );
							pspY = power[0] / 3D - power[6] / 3D + power[2] / 3D - power[8] / 3D + power[1] / 2D - power[7] / 2D;
							pspZ = power[4] + power[0] / 3D + power[6] / 3D + power[2] / 3D + power[8] / 3D + power[1] / 2D + power[7] / 2D + power[3] / 2D + power[5] / 2D;
							break;
						}
						case 4:		// - X
						{
							pspX = -( power[4] + power[0] / 3D + power[6] / 3D + power[2] / 3D + power[8] / 3D + power[1] / 2D + power[7] / 2D + power[3] / 2D + power[5] / 2D);
							pspY = power[0] / 3D - power[6] / 3D + power[2] / 3D - power[8] / 3D + power[1] / 2D - power[7] / 2D;
							pspZ = -( power[0] / 3D - power[2] / 3D + power[6] / 3D - power[8] / 3D + power[3] / 2D - power[5] / 2D );
							break;
						}
						case 5:		// + X
						{
							pspX = power[4] + power[0] / 3D + power[6] / 3D + power[2] / 3D + power[8] / 3D + power[1] / 2D + power[7] / 2D + power[3] / 2D + power[5] / 2D;
							pspY = power[0] / 3D - power[6] / 3D + power[2] / 3D - power[8] / 3D + power[1] / 2D - power[7] / 2D;
							pspZ = power[0] / 3D - power[2] / 3D + power[6] / 3D - power[8] / 3D + power[3] / 2D - power[5] / 2D;
							break;
						}
					}

					pspX = pspX * 40D;
					pspY = pspY * 40D;
					pspZ = pspZ * 40D;

					double dirX = 1D;
					if (pspX < 0) {
						dirX = -1D;
					}
					double dirY = 1D;
					if (pspY < 0) {
						dirY = -1D;
					}
					double dirZ = 1D;
					if (pspZ < 0) {
						dirZ = -1D;
					}

					double error = 1D;
					if ( catapultprecise ) {
						error = 0D;
					}

					int sticky = 0;
					if ( isSticky ) {
						sticky = 1;
					}

					int xx = par2 + ext * Facing.offsetsXForSide[var6];
					int yy = par3 + ext * Facing.offsetsYForSide[var6];
					int zz = par4 + ext * Facing.offsetsZForSide[var6];

					final String blid = Block.blockRegistry.getNameForObject(par1World.getBlock(xx, yy, zz));
					meta = par1World.getBlockMetadata(xx, yy, zz);

					if ( DefinitionMaps.getBlockDef(blid,meta).fragile > 0 )
					{
						if ( ((Block)Block.blockRegistry.getObject(blid)).hasTileEntity(meta) )
						{
							final TileEntity tileEntity = par1World.getTileEntity(xx, yy, zz);
							
							if (tileEntity != null)
							{
								final NBTTagCompound nnn = new NBTTagCompound();
								tileEntity.writeToNBT(nnn);
								BlockPhysics.dropItemsNBT(par1World, xx, yy, zz, nnn);
								par1World.removeTileEntity(xx, yy, zz);
							}
						}

						if ( DefinitionMaps.getBlockDef(blid,meta).fragile == 2 ) {
							((Block)Block.blockRegistry.getObject(blid)).dropBlockAsItem( par1World, xx, yy, zz, meta, 0 );
						}

						par1World.setBlockToAir(xx, yy, zz);
					}

					int sstick = 1;
					if (ext == 1) {
						sstick = 0;
					}

					final int bxx = par2 + (ext + sticky * sstick) * Facing.offsetsXForSide[var6];
					final int byy = par3 + (ext + sticky * sstick) * Facing.offsetsYForSide[var6];
					final int bzz = par4 + (ext + sticky * sstick) * Facing.offsetsZForSide[var6];

					double smass = 0;

					for (int i = ext-1; i > sticky; i--)
					{
						xx -= Facing.offsetsXForSide[var6];
						yy -= Facing.offsetsYForSide[var6];
						zz -= Facing.offsetsZForSide[var6];
						blid2 = Block.blockRegistry.getNameForObject(par1World.getBlock(xx, yy, zz ));
						meta = par1World.getBlockMetadata(xx, yy, zz);
						final Block bb = (Block)Block.blockRegistry.getObject(blid2);
						if ( bb == Blocks.grass || bb == Blocks.farmland || bb == Blocks.mycelium ) {
							blid2 = Block.blockRegistry.getNameForObject(Blocks.dirt);
						}
						smass += DefinitionMaps.getBlockDef(blid2,meta).mass;
					}

					if ( BlockPhysics.canMoveTo(par1World, bxx + Facing.offsetsXForSide[var6], byy + Facing.offsetsYForSide[var6], bzz + Facing.offsetsZForSide[var6], 0) )
					{
						final AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(bxx, byy, bzz, bxx + 1, byy + 1, bzz + 1 );
						final List list = par1World.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
						for (final Iterator iterator = list.iterator(); iterator.hasNext(); )
						{
							if ( ((Entity)iterator.next()).ticksExisted < 6  ) {
								iterator.remove();
							}
						}

						if (!list.isEmpty())
						{
							Entity entity;

							for (final Iterator iterator = list.iterator(); iterator.hasNext(); )
							{
								entity = (Entity)iterator.next();
								if (entity instanceof EntityFallingBlock ) {
									smass += DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(((EntityFallingBlock)entity).func_145805_f()),((EntityFallingBlock)entity).field_145814_a).mass;
								} else if (entity instanceof EntityLiving ) {
									smass += ((EntityLiving)entity).getMaxHealth() * 3D;
								} else {
									smass += 25D;
								}
							}

							double sx,sy,sz;
							for (final Iterator iterator = list.iterator(); iterator.hasNext(); )
							{
								entity = (Entity)iterator.next();	//optimize!

								sx = MathHelper.sqrt_double(Math.abs(pspX) / smass) * dirX + error * BlockPhysics.rand.nextGaussian() * 0.02D;
								sy = MathHelper.sqrt_double(Math.abs(pspY) / smass) * dirY + error * BlockPhysics.rand.nextGaussian() * 0.02D;
								sz = MathHelper.sqrt_double(Math.abs(pspZ) / smass) * dirZ + error * BlockPhysics.rand.nextGaussian() * 0.02D;
								entity.setPosition(entity.posX + Facing.offsetsXForSide[var6], entity.posY + Facing.offsetsYForSide[var6], entity.posZ + Facing.offsetsZForSide[var6]);
								entity.addVelocity(BlockPhysics.bSpeedR(sx), BlockPhysics.bSpeedR(sy), BlockPhysics.bSpeedR(sz));
								entity.velocityChanged = true;
								if ( entity instanceof net.minecraft.entity.player.EntityPlayerMP  )
								{
									((EntityPlayerMP)entity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(entity.getEntityId(), BlockPhysics.bSpeedR(sx), BlockPhysics.bSpeedR(sy), BlockPhysics.bSpeedR(sz)));
								}
							}
						}
					}

					xx = par2 + ext * Facing.offsetsXForSide[var6];
					yy = par3 + ext * Facing.offsetsYForSide[var6];
					zz = par4 + ext * Facing.offsetsZForSide[var6];

					pspX = MathHelper.sqrt_double(Math.abs(pspX) / smass) * dirX + error * BlockPhysics.rand.nextGaussian() * 0.02D;
					pspY = MathHelper.sqrt_double(Math.abs(pspY) / smass) * dirY + error * BlockPhysics.rand.nextGaussian() * 0.02D;
					pspZ = MathHelper.sqrt_double(Math.abs(pspZ) / smass) * dirZ + error * BlockPhysics.rand.nextGaussian() * 0.02D;

					for (int i = ext-1; i > sticky; i--)
					{
						xx -= Facing.offsetsXForSide[var6];
						yy -= Facing.offsetsYForSide[var6];
						zz -= Facing.offsetsZForSide[var6];
						blid2 = Block.blockRegistry.getNameForObject(par1World.getBlock(xx, yy, zz ));
						meta = par1World.getBlockMetadata(xx , yy, zz);
						final Block bb = (Block)Block.blockRegistry.getObject(blid2);
						if ( bb == Blocks.grass || bb == Blocks.farmland || bb == Blocks.mycelium ) {
							blid2 = Block.blockRegistry.getNameForObject(Blocks.dirt);
						}
						if ( bb == Blocks.piston || bb == Blocks.sticky_piston ) {
							meta = meta & 7;
						}

						final EntityFallingBlock entityfallingsand = new EntityFallingBlock(par1World, 0.5D + xx + Facing.offsetsXForSide[var6], 0.5D + yy + Facing.offsetsYForSide[var6], 0.5D + zz + Facing.offsetsZForSide[var6], (Block)Block.blockRegistry.getObject(blid2), meta);
						entityfallingsand.motionX = BlockPhysics.bSpeedR(pspX + error * BlockPhysics.rand.nextGaussian() * 0.01D);
						entityfallingsand.motionY = BlockPhysics.bSpeedR(pspY + error * BlockPhysics.rand.nextGaussian() * 0.01D);
						entityfallingsand.motionZ = BlockPhysics.bSpeedR(pspZ + error * BlockPhysics.rand.nextGaussian() * 0.01D);
						if (BlockPhysics.canBurn(blid2) )
						{
							if ( i == ext - 1 && blid.equals(Block.blockRegistry.getNameForObject(Blocks.fire)) ) {
								entityfallingsand.setFire(60);
							}
							if ( par1World.getBlock(xx, yy+1, zz) == Blocks.fire ) {
								entityfallingsand.setFire(60);
							}
						}
						BlockPhysics.asmHelper.set(entityfallingsand, "bpdata", BlockPhysics.getBlockBPdata( par1World, xx, yy, zz ));
						if ( ((Block)Block.blockRegistry.getObject(blid2)).hasTileEntity(meta) )
						{
							final TileEntity tileEntity = par1World.getTileEntity(xx, yy, zz);

							if (tileEntity != null)
							{
								entityfallingsand.field_145810_d = new NBTTagCompound();
								tileEntity.writeToNBT(entityfallingsand.field_145810_d);
								par1World.removeTileEntity(xx, yy, zz);
							}
						}
						par1World.setBlockToAir( xx, yy, zz);
						BlockPhysics.setBlockBPdata( par1World, xx, yy, zz, 0 );
						par1World.spawnEntityInWorld(entityfallingsand);
					}

					if ( sticky == 1 )
					{
						par1World.addBlockEvent(par2, par3, par4, par1block, 3, var6);   // var5??
						BlockPhysics.pistonMoveMark(par1World, par2, par3, par4, 2, var6);
					}
					else
					{
						par1World.addBlockEvent(par2, par3, par4, par1block, 2, var6);
						BlockPhysics.pistonMoveMark(par1World, par2, par3, par4, 1, var6);
					}
				}
				else
				{
					ext = BlockPhysics.canExtend(par1World, par2, par3, par4, var6, par1block, false);
					if ( ext == 0 ) {
						return;
					}
					par1World.addBlockEvent(par2, par3, par4, par1block, ext+1, var6);
					BlockPhysics.pistonMoveMark(par1World, par2, par3, par4, ext, var6);
				}
			}
			else
			{
				ext = BlockPhysics.canExtend(par1World, par2, par3, par4, var6, par1block, false);
				if ( ext == 0 ) {
					return;
				}
				par1World.addBlockEvent(par2, par3, par4, par1block, ext+1, var6);
				BlockPhysics.pistonMoveMark(par1World, par2, par3, par4, ext, var6);
			}

			par1World.setBlockMetadataWithNotify(par2, par3, par4, var5 | 8, 0);
		}
		else
		{

			if ( isSticky )
			{
				final int xx = par2 + 2 * Facing.offsetsXForSide[var6];
				final int yy = par3 + 2 * Facing.offsetsYForSide[var6];
				final int zz = par4 + 2 * Facing.offsetsZForSide[var6];
				blid2 = Block.blockRegistry.getNameForObject(par1World.getBlock(xx, yy, zz));
				meta = par1World.getBlockMetadata(xx, yy, zz);
				boolean pull = true;
				final Block bb = (Block)Block.blockRegistry.getObject(blid2);
				final boolean empty = bb == Blocks.air || bb == Blocks.water || bb == Blocks.flowing_water || bb == Blocks.lava || bb == Blocks.flowing_lava || bb == Blocks.fire || bb.getMaterial().isLiquid();
				if (((HashSet<String>)BlockPhysics.asmHelper.get(par1World, "pistonMoveBlocks")).contains(""+xx+"."+yy+"."+zz) || empty || DefinitionMaps.getBlockDef(blid2,meta).pushtype != 1 && DefinitionMaps.getBlockDef(blid2,meta).pushtype != 2 ) {
					pull = false;
				} else if ( ( bb == Blocks.piston || bb == Blocks.sticky_piston ) && !BlockPhysics.canmove(par1World, xx, yy, zz, par1block)) {
					pull = false;
				}

				if ( pull )
				{
					par1World.addBlockEvent(par2, par3, par4, par1block, 1, var6);
					BlockPhysics.pistonMoveMark(par1World, par2, par3, par4, 2, var6);
				}
				else
				{
					par1World.addBlockEvent(par2, par3, par4, par1block, 0, var6);
					BlockPhysics.pistonMoveMark(par1World, par2, par3, par4, 1, var6);
				}
			}
			else
			{
				par1World.addBlockEvent(par2, par3, par4, par1block, 0, var6);
				BlockPhysics.pistonMoveMark(par1World, par2, par3, par4, 1, var6);
			}

			par1World.setBlockMetadataWithNotify(par2, par3, par4, var5 & 7, 0);
		}
	}

	private static int canExtend(final World par0World, int par1, int par2, int par3, final int par4, final BlockPistonBase par1block, final boolean catp)
	{
		if (((HashSet<String>)BlockPhysics.asmHelper.get(par0World, "pistonMoveBlocks")).contains(""+par1+"."+par2+"."+par3)) {
			return 0;
		}
		int var8, meta;
		String blid;
		boolean empty;
		//TileEntity ent;
		for ( var8 = 1; var8 < 14; var8++ )
		{

			par1 += Facing.offsetsXForSide[par4];
			par2 += Facing.offsetsYForSide[par4];
			par3 += Facing.offsetsZForSide[par4];

			if (((HashSet<String>)BlockPhysics.asmHelper.get(par0World, "pistonMoveBlocks")).contains(""+par1+"."+par2+"."+par3)) {
				return 0;
			}

			blid = Block.blockRegistry.getNameForObject(par0World.getBlock(par1, par2, par3));
			final Block bb = (Block)Block.blockRegistry.getObject(blid);
			empty = bb == Blocks.air || bb == Blocks.water || bb == Blocks.flowing_water || bb == Blocks.lava || bb == Blocks.flowing_lava || bb == Blocks.fire || bb.getMaterial().isLiquid();
			if ( empty )
			{

				if (par2 <= 0 || par2 >= par0World.getHeight() - 1) {
					return 0;
				} else {
					return var8;
				}

			}

			meta = par0World.getBlockMetadata(par1, par2, par3);
			if ( DefinitionMaps.getBlockDef(blid,meta).pushtype == 0 )
			{
				if (DefinitionMaps.getBlockDef(blid,meta).fragile == 0) {
					return 0;
				} else if (DefinitionMaps.getBlockDef(blid,meta).strength > 10) {
					return 0;
				} else {
					return var8;
				}
			}
			if (catp)
			{
				if ( DefinitionMaps.getBlockDef(blid,meta).pushtype == 2 ) {
					return 0;
				}
			}
			else
			{
				if ( DefinitionMaps.getBlockDef(blid,meta).pushtype == 3 ) {
					return 0;
				}
			}


			//ent =  par0World.getBlockTileEntity(par1, par2, par3);
			//if ( ent != null ) return 0;

			if ( (bb == Blocks.piston || bb == Blocks.sticky_piston) && !BlockPhysics.canmove(par0World, par1, par2, par3, par1block)) {
				return 0;
			}
		}
		return 0;
	}

	static void pistonMoveMark(final World world, int i, int j, int k, final int lngth, final int orient )
	{
		final int io = Facing.offsetsXForSide[orient];
		final int jo = Facing.offsetsYForSide[orient];
		final int ko = Facing.offsetsZForSide[orient];
		for (int l = 0; l <= lngth; l++)
		{
			((HashSet<String>)BlockPhysics.asmHelper.get(world, "pistonMoveBlocks")).add(""+i+"."+j+"."+k);
			i = i + io;
			j = j + jo;
			k = k + ko;
		}
	}

	public static boolean onBlockPistonEventReceived(final World par1World, int par2, int par3, int par4, int par5, final int par6, final BlockPistonBase par1block, final boolean isSticky)
	{
		if ( par5 > 1 )	//extending
		{
			par5--;
			int xx = par2 + par5 * Facing.offsetsXForSide[par6];
			int yy = par3 + par5 * Facing.offsetsYForSide[par6];
			int zz = par4 + par5 * Facing.offsetsZForSide[par6];

			if (!par1World.isRemote)
			{
				final String blid = Block.blockRegistry.getNameForObject(par1World.getBlock(xx, yy, zz));
				final int meta = par1World.getBlockMetadata(xx, yy, zz);

				if ( DefinitionMaps.getBlockDef(blid,meta).fragile > 0 )
				{
					if ( ((Block)Block.blockRegistry.getObject(blid)).hasTileEntity(meta) )
					{
						final TileEntity tileEntity = par1World.getTileEntity(xx, yy, zz);
						
						if (tileEntity != null)
						{
							final NBTTagCompound nnn = new NBTTagCompound();
							tileEntity.writeToNBT(nnn);
							BlockPhysics.dropItemsNBT(par1World, xx, yy, zz, nnn);
							par1World.removeTileEntity(xx, yy, zz);
						}
					}

					if ( DefinitionMaps.getBlockDef(blid,meta).fragile == 2 ) {
						((Block)Block.blockRegistry.getObject(blid)).dropBlockAsItem( par1World, xx, yy, zz, meta, 0 );
					}

					par1World.setBlockToAir(xx, yy, zz);
				}
			}

			for (int i = par5; i > 1; i--)
			{
				final int xxf =  xx - Facing.offsetsXForSide[par6];
				final int yyf =  yy - Facing.offsetsYForSide[par6];
				final int zzf =  zz - Facing.offsetsZForSide[par6];

				final String var12 = Block.blockRegistry.getNameForObject(par1World.getBlock(xxf, yyf, zzf));
				int var13 = par1World.getBlockMetadata(xxf, yyf, zzf);
				final int bpmeta = BlockPhysics.getBlockBPdata(par1World, xxf, yyf, zzf);

				final Block bb = (Block)Block.blockRegistry.getObject(var12);

				if ( bb == Blocks.piston || bb == Blocks.sticky_piston ) {
					var13 = var13 & 7;
				}

				final TileEntityPiston tePiston = new TileEntityPiston(bb, var13, par6, true, false);
				BlockPhysics.asmHelper.set(tePiston, "bpmeta", bpmeta);

				if ( ((Block)Block.blockRegistry.getObject(var12)).hasTileEntity(var13) )
				{
					final TileEntity tileEntity = par1World.getTileEntity(xxf, yyf, zzf);
					
					if (tileEntity != null)
					{
						final NBTTagCompound compound = new NBTTagCompound();
						BlockPhysics.asmHelper.set(tePiston, "movingBlockTileEntityData", compound);
						tileEntity.writeToNBT(compound);
						par1World.removeTileEntity(xxf, yyf, zzf);
					}
				}

				par1World.setBlock(xx, yy, zz, Blocks.piston_extension, var13, 2);
				par1World.setTileEntity(xx, yy, zz, tePiston);
				xx -= Facing.offsetsXForSide[par6];
				yy -= Facing.offsetsYForSide[par6];
				zz -= Facing.offsetsZForSide[par6];
			}
			par1World.setBlock(par2 + Facing.offsetsXForSide[par6], par3 + Facing.offsetsYForSide[par6], par4 + Facing.offsetsZForSide[par6], Blocks.piston_extension, par6 | (isSticky ? 8 : 0), 2);
			par1World.setTileEntity(par2 + Facing.offsetsXForSide[par6], par3 + Facing.offsetsYForSide[par6], par4 + Facing.offsetsZForSide[par6], BlockPistonMoving.getTileEntity(Blocks.piston_head, par6 | (isSticky ? 8 : 0), par6, true, true));
			par1World.setBlock(par2, par3, par4, par1block, par6 | 8, 2);

			par1World.playSoundEffect(par2 + 0.5D, par3 + 0.5D, par4 + 0.5D, "tile.piston.out", 0.5F, BlockPhysics.rand.nextFloat() * 0.25F + 0.6F);
		}
		else				//retracting  (0 normal, 1 sticky )
		{
			par1World.setBlock(par2, par3, par4, Blocks.piston_extension, par6, 2);
			par1World.setTileEntity(par2, par3, par4, BlockPistonMoving.getTileEntity(par1block, par6, par6, false, true));

			if ( par5 == 0 )
			{
				par1World.setBlockToAir(par2 + Facing.offsetsXForSide[par6], par3 + Facing.offsetsYForSide[par6], par4 + Facing.offsetsZForSide[par6]);
			}
			else
			{
				final int var8 = par2 + Facing.offsetsXForSide[par6] * 2;
				final int var9 = par3 + Facing.offsetsYForSide[par6] * 2;
				final int var10 = par4 + Facing.offsetsZForSide[par6] * 2;
				final String var11 = Block.blockRegistry.getNameForObject(par1World.getBlock(var8, var9, var10));
				final int var12 = par1World.getBlockMetadata(var8, var9, var10);
				final int bpmeta = BlockPhysics.getBlockBPdata(par1World, var8, var9, var10);

				final TileEntityPiston tePiston = new TileEntityPiston((Block)Block.blockRegistry.getObject(var11), var12, par6, false, false);
				BlockPhysics.asmHelper.set(tePiston, "bpmeta", bpmeta);

				if ( ((Block)Block.blockRegistry.getObject(var11)).hasTileEntity(var12) )
				{
					final TileEntity tileEntity = par1World.getTileEntity(var8, var9, var10);
					
					if (tileEntity != null)
					{
						final NBTTagCompound compound = new NBTTagCompound();
						BlockPhysics.asmHelper.set(tePiston, "movingBlockTileEntityData", compound);
						tileEntity.writeToNBT(compound);
						par1World.removeTileEntity(var8, var9, var10);
					}
				}

				par2 += Facing.offsetsXForSide[par6];
				par3 += Facing.offsetsYForSide[par6];
				par4 += Facing.offsetsZForSide[par6];
				par1World.setBlock(par2, par3, par4, Blocks.piston_extension, var12, 0);
				par1World.setTileEntity(par2, par3, par4, tePiston);
				par1World.setBlockToAir(var8, var9, var10);
			}
			par1World.playSoundEffect(par2 + 0.5D, par3 + 0.5D, par4 + 0.5D, "tile.piston.in", 0.5F, BlockPhysics.rand.nextFloat() * 0.15F + 0.6F);
		}
		return true;
	}
}