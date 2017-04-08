package net.minecraft.entity.item;

import blockphysics.BlockPhysics;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFallingSand extends Entity
{
	public double accelerationX;
	public double accelerationY;
	public double accelerationZ;
	public byte slideDir;
	public int media;
	public int bpdata;
    public byte dead;
	
	public int blockID;
    public int metadata;

    /** How long the block has been falling for. */
    public int fallTime;
    public boolean shouldDropItem;
    private boolean isBreakingAnvil;
    private boolean isAnvil;

    /** Maximum amount of damage dealt to entities hit by falling block */
    private int fallHurtMax;

    /** Actual damage dealt to entities hit by falling block */
    private float fallHurtAmount;
    public NBTTagCompound fallingBlockTileEntityData;

    public EntityFallingSand(World par1World)
    {
        super(par1World);
        this.shouldDropItem = true;
        this.fallHurtMax = 40;
        this.fallHurtAmount = 2.0F;
        
        this.preventEntitySpawning = true;
        this.setSize(0.996F, 0.996F);
        this.yOffset = this.height / 2.0F;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.accelerationX = 0;
        this.accelerationY = -0.024525D;
        this.accelerationZ = 0;
        this.slideDir = 0;
        this.noClip = true;
        this.entityCollisionReduction = 0.8F;
        this.dead = 4;
        this.bpdata = 0;
    }

    public EntityFallingSand(World par1World, double par2, double par4, double par6, int par8)
    {
        this(par1World, par2, par4, par6, par8, 0);
    }

    public EntityFallingSand(World par1World, double par2, double par4, double par6, int par8, int par9)
    {
        super(par1World);
        this.shouldDropItem = true;
        this.fallHurtMax = 40;
        this.fallHurtAmount = 2.0F;
        this.blockID = par8;
        this.metadata = par9;
        this.preventEntitySpawning = true;
        this.setSize(0.996F, 0.996F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(par2, par4, par6);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = par2;
        this.prevPosY = par4;
        this.prevPosZ = par6;
        
        this.accelerationX = 0;
        this.accelerationY = -0.024525D;
        this.accelerationZ = 0;
        this.slideDir = 0;
        this.noClip = true;
        this.entityCollisionReduction = 0.8F;
        this.dead = 4;
        this.bpdata = 0;
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit() {}

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return false;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        BlockPhysics.fallingSandUpdate(worldObj, this);
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float par1)
    {
        
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setByte("Tile", (byte)this.blockID);
        par1NBTTagCompound.setInteger("TileID", this.blockID);
        par1NBTTagCompound.setByte("Data", (byte)this.metadata);
        par1NBTTagCompound.setByte("Time", (byte)this.fallTime);
        par1NBTTagCompound.setBoolean("DropItem", this.shouldDropItem);
        par1NBTTagCompound.setBoolean("HurtEntities", this.isAnvil);
        par1NBTTagCompound.setFloat("FallHurtAmount", this.fallHurtAmount);
        par1NBTTagCompound.setInteger("FallHurtMax", this.fallHurtMax);

        if (this.fallingBlockTileEntityData != null)
        {
            par1NBTTagCompound.setCompoundTag("TileEntityData", this.fallingBlockTileEntityData);
        }
        
        par1NBTTagCompound.setTag("Acceleration", this.newDoubleNBTList(new double[] {this.accelerationX, this.accelerationY, this.accelerationZ}));
        par1NBTTagCompound.setByte("BPData", (byte)this.bpdata);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	this.blockID = BlockPhysics.readFallingSandID(par1NBTTagCompound);

        this.metadata = par1NBTTagCompound.getByte("Data") & 255;
        this.fallTime = par1NBTTagCompound.getByte("Time") & 255;

        if (par1NBTTagCompound.hasKey("HurtEntities"))
        {
            this.isAnvil = par1NBTTagCompound.getBoolean("HurtEntities");
            this.fallHurtAmount = par1NBTTagCompound.getFloat("FallHurtAmount");
            this.fallHurtMax = par1NBTTagCompound.getInteger("FallHurtMax");
        }
        else if (this.blockID == Block.anvil.blockID)
        {
            this.isAnvil = true;
        }

        if (par1NBTTagCompound.hasKey("DropItem"))
        {
            this.shouldDropItem = par1NBTTagCompound.getBoolean("DropItem");
        }

        if (par1NBTTagCompound.hasKey("TileEntityData"))
        {
            this.fallingBlockTileEntityData = par1NBTTagCompound.getCompoundTag("TileEntityData");
        }

        if (this.blockID == 0)
        {
            this.blockID = Block.sand.blockID;
        }
        
        if (par1NBTTagCompound.hasKey("Acceleration"))
        {
        	NBTTagList atl = par1NBTTagCompound.getTagList("Acceleration");
        	this.accelerationX = ((NBTTagDouble)atl.tagAt(0)).data;
        	this.accelerationY = ((NBTTagDouble)atl.tagAt(1)).data;
        	this.accelerationZ = ((NBTTagDouble)atl.tagAt(2)).data;
        }
        else
        {
        	this.accelerationX = 0;
        	this.accelerationY = 0;
        	this.accelerationZ = 0;
        }
        
        if (par1NBTTagCompound.hasKey("BPData"))
        {
            this.bpdata = par1NBTTagCompound.getByte("BPData");
        }
        else this.bpdata = 0;
    }

    public void setIsAnvil(boolean par1)
    {
        this.isAnvil = par1;
    }

    public void func_85029_a(CrashReportCategory par1CrashReportCategory)
    {
        super.func_85029_a(par1CrashReportCategory);
        par1CrashReportCategory.addCrashSection("Immitating block ID", Integer.valueOf(this.blockID));
        par1CrashReportCategory.addCrashSection("Immitating block data", Integer.valueOf(this.metadata));
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    @SideOnly(Side.CLIENT)
    public World getWorld()
    {
        return this.worldObj;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Return whether this entity should be rendered as on fire.
     */
    public boolean canRenderOnFire()
    {
        return this.isBurning();
    }
    
    public AxisAlignedBB getBoundingBox()
    {
       return this.boundingBox;
    }
    
    public void setInWeb(){}
    
    public void moveEntity( double par1, double par3, double par5)
    {
    	BlockPhysics.moveEntity(this.worldObj, this, par1, par3, par5);
    }
}
