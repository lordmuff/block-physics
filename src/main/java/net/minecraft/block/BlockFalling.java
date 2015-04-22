package net.minecraft.block;

import java.util.Random;

import com.bloodnbonesgaming.blockphysics.BlockPhysics;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BlockFalling extends Block
{
    public static boolean fallInstantly;
    private static final String __OBFID = "CL_00000240";

    public BlockFalling()
    {
        super(Material.sand);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public BlockFalling(Material p_i45405_1_)
    {
        super(p_i45405_1_);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_)
    {
        p_149726_1_.scheduleBlockUpdate(p_149726_2_, p_149726_3_, p_149726_4_, this, this.tickRate(p_149726_1_));
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
    	//TODO Clear and add line
    	BlockPhysics.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, Block.blockRegistry.getNameForObject(this));
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_)
    {
    	//TODO Clear method
    }

    private void func_149830_m(World p_149830_1_, int p_149830_2_, int p_149830_3_, int p_149830_4_)
    {
    	//TODO Clear method
    }

    protected void func_149829_a(EntityFallingBlock p_149829_1_) {}

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World p_149738_1_)
    {
        return 2;
    }

    public static boolean func_149831_e(World p_149831_0_, int p_149831_1_, int p_149831_2_, int p_149831_3_)
    {
        //TODO Clear and add line
    	return BlockPhysics.canMoveTo(p_149831_0_, p_149831_1_, p_149831_2_, p_149831_3_, 0);
    }

    public void func_149828_a(World p_149828_1_, int p_149828_2_, int p_149828_3_, int p_149828_4_, int p_149828_5_) {}
}