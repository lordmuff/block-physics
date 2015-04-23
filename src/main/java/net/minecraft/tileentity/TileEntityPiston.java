package net.minecraft.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bloodnbonesgaming.blockphysics.BlockPhysics;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityPiston extends TileEntity
{
    private Block storedBlock;
    private int storedMetadata;
    /** the side the front of the piston is on */
    private int storedOrientation;
    /** if this piston is extending or not */
    private boolean extending;
    private boolean shouldHeadBeRendered;
    private float progress;
    /** the progress in (de)extending */
    private float lastProgress;
    private List pushedObjects = new ArrayList();
    private static final String __OBFID = "CL_00000369";
    //TODO Add fields
    public NBTTagCompound movingBlockTileEntityData;
    public int bpmeta;

    public TileEntityPiston() {}

    public TileEntityPiston(Block p_i45444_1_, int p_i45444_2_, int p_i45444_3_, boolean p_i45444_4_, boolean p_i45444_5_)
    {
        this.storedBlock = p_i45444_1_;
        this.storedMetadata = p_i45444_2_;
        this.storedOrientation = p_i45444_3_;
        this.extending = p_i45444_4_;
        this.shouldHeadBeRendered = p_i45444_5_;
        //TODO Add lines
        this.movingBlockTileEntityData = null;
        this.bpmeta = 0;
    }

    public Block getStoredBlockID()
    {
        return this.storedBlock;
    }

    public int getBlockMetadata()
    {
        return this.storedMetadata;
    }

    /**
     * Returns true if a piston is extending
     */
    public boolean isExtending()
    {
        return this.extending;
    }

    public int getPistonOrientation()
    {
        return this.storedOrientation;
    }

    @SideOnly(Side.CLIENT)
    public boolean func_145867_d()
    {
        return this.shouldHeadBeRendered;
    }

    public float func_145860_a(float p_145860_1_)
    {
        if (p_145860_1_ > 1.0F)
        {
            p_145860_1_ = 1.0F;
        }

        return this.lastProgress + (this.progress - this.lastProgress) * p_145860_1_;
    }

    private void func_145863_a(float p_145863_1_, float p_145863_2_)
    {
        if (this.extending)
        {
            p_145863_1_ = 1.0F - p_145863_1_;
        }
        else
        {
            --p_145863_1_;
        }

        AxisAlignedBB axisalignedbb = Blocks.piston_extension.func_149964_a(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.storedBlock, p_145863_1_, this.storedOrientation);

        if (axisalignedbb != null)
        {
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, axisalignedbb);

            if (!list.isEmpty())
            {
                this.pushedObjects.addAll(list);
                Iterator iterator = this.pushedObjects.iterator();

                while (iterator.hasNext())
                {
                    Entity entity = (Entity)iterator.next();
                    entity.moveEntity((double)(p_145863_2_ * (float)Facing.offsetsXForSide[this.storedOrientation]), (double)(p_145863_2_ * (float)Facing.offsetsYForSide[this.storedOrientation]), (double)(p_145863_2_ * (float)Facing.offsetsZForSide[this.storedOrientation]));
                }

                this.pushedObjects.clear();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public float func_145865_b(float p_145865_1_)
    {
        return this.extending ? (this.func_145860_a(p_145865_1_) - 1.0F) * (float)Facing.offsetsXForSide[this.storedOrientation] : (1.0F - this.func_145860_a(p_145865_1_)) * (float)Facing.offsetsXForSide[this.storedOrientation];
    }

    @SideOnly(Side.CLIENT)
    public float func_145862_c(float p_145862_1_)
    {
        return this.extending ? (this.func_145860_a(p_145862_1_) - 1.0F) * (float)Facing.offsetsYForSide[this.storedOrientation] : (1.0F - this.func_145860_a(p_145862_1_)) * (float)Facing.offsetsYForSide[this.storedOrientation];
    }

    @SideOnly(Side.CLIENT)
    public float func_145859_d(float p_145859_1_)
    {
        return this.extending ? (this.func_145860_a(p_145859_1_) - 1.0F) * (float)Facing.offsetsZForSide[this.storedOrientation] : (1.0F - this.func_145860_a(p_145859_1_)) * (float)Facing.offsetsZForSide[this.storedOrientation];
    }

    /**
     * removes a piston's tile entity (and if the piston is moving, stops it)
     */
    public void clearPistonTileEntity()
    {
        if (this.lastProgress < 1.0F && this.worldObj != null)
        {
            this.lastProgress = this.progress = 1.0F;
            this.worldObj.removeTileEntity(this.xCoord, this.yCoord, this.zCoord);
            this.invalidate();

            if (this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) == Blocks.piston_extension)
            {
                this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.storedBlock, this.storedMetadata, 3);
                this.worldObj.notifyBlockOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.storedBlock);
                //TODO Add lines
                BlockPhysics.setBlockBPdata(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.bpmeta);
				if (this.movingBlockTileEntityData != null)
				{
					TileEntity tileEntity = storedBlock.createTileEntity(this.worldObj, this.storedMetadata);
					tileEntity.readFromNBT(this.movingBlockTileEntityData);
					this.worldObj.setTileEntity(xCoord, yCoord, zCoord, tileEntity);
				}
			}
		}
    }

    public void updateEntity()
    {
        this.lastProgress = this.progress;

        if (this.lastProgress >= 1.0F)
        {
            this.func_145863_a(1.0F, 0.25F);
            this.worldObj.removeTileEntity(this.xCoord, this.yCoord, this.zCoord);
            this.invalidate();

            if (this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) == Blocks.piston_extension)
            {
                this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.storedBlock, this.storedMetadata, 3);
                this.worldObj.notifyBlockOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.storedBlock);
                //TODO Add lines
                BlockPhysics.setBlockBPdata(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.bpmeta);
   			 	if (this.movingBlockTileEntityData != null)
   			 	{
   			 		TileEntity tileEntity = this.storedBlock.createTileEntity(this.worldObj, this.storedMetadata);
   			 		tileEntity.readFromNBT(this.movingBlockTileEntityData);
   			 		this.worldObj.setTileEntity(this.xCoord, this.yCoord, this.zCoord, tileEntity);
   			 	}
            }
        }
        else
        {
            this.progress += 0.5F;

            if (this.progress >= 1.0F)
            {
                this.progress = 1.0F;
            }

            if (this.extending)
            {
                this.func_145863_a(this.progress, this.progress - this.lastProgress + 0.0625F);
            }
        }
    }

    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        this.storedBlock = Block.getBlockById(p_145839_1_.getInteger("blockId"));
        this.storedMetadata = p_145839_1_.getInteger("blockData");
        this.storedOrientation = p_145839_1_.getInteger("facing");
        this.lastProgress = this.progress = p_145839_1_.getFloat("progress");
        this.extending = p_145839_1_.getBoolean("extending");
        //TODO Add lines
        if (p_145839_1_.hasKey("BPData"))
   		{
        	this.bpmeta = p_145839_1_.getByte("BPData");
   		}
   		if (p_145839_1_.hasKey("TileEntityData"))
   		{
   			this.movingBlockTileEntityData = p_145839_1_.getCompoundTag("TileEntityData");
   		}
    }

    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setInteger("blockId", Block.getIdFromBlock(this.storedBlock));
        p_145841_1_.setInteger("blockData", this.storedMetadata);
        p_145841_1_.setInteger("facing", this.storedOrientation);
        p_145841_1_.setFloat("progress", this.lastProgress);
        p_145841_1_.setBoolean("extending", this.extending);
        //TODO Add stuff
        p_145841_1_.setByte("BPData", (byte)this.bpmeta);
		if (this.movingBlockTileEntityData != null)
		{
			p_145841_1_.setTag("TileEntityData", this.movingBlockTileEntityData);
		}
    }
}