package net.minecraft.entity.item;

import com.bloodnbonesgaming.blockphysics.BlockPhysics;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityFallingBlock extends Entity
{
    private Block field_145811_e;
    public int field_145814_a;
    public int field_145812_b;
    public boolean field_145813_c;
    private boolean field_145808_f;
    private boolean field_145809_g;
    private int field_145815_h;
    private float field_145816_i;
    public NBTTagCompound field_145810_d;
    private static final String __OBFID = "CL_00001668";
    
    //TODO Add fields
    public byte slideDir;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;
    public int bpdata;
    public String media;
    public byte dead;

    public EntityFallingBlock(World p_i1706_1_)
    {
        super(p_i1706_1_);
        this.field_145813_c = true;
        this.field_145815_h = 40;
        this.field_145816_i = 2.0F;
        //TODO Add stuff
        this.preventEntitySpawning = true;
		this.setSize(0.996f, 0.996f);
		this.yOffset = this.height/2.0f;
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		this.accelerationX = 0;
		this.accelerationY = -0.024525;
		this.accelerationZ = 0;
		this.slideDir = 0;
		this.noClip = true;
		this.entityCollisionReduction = 0.8F;
		this.dead = 4;
		this.bpdata = 0;
    }

    public EntityFallingBlock(World p_i45318_1_, double p_i45318_2_, double p_i45318_4_, double p_i45318_6_, Block p_i45318_8_)
    {
        this(p_i45318_1_, p_i45318_2_, p_i45318_4_, p_i45318_6_, p_i45318_8_, 0);
    }
    
	public EntityFallingBlock(World p_i45319_1_, double p_i45319_2_, double p_i45319_4_, double p_i45319_6_, Block p_i45319_8_, int p_i45319_9_)
	{
		super(p_i45319_1_);
		this.field_145813_c = true;
		this.field_145815_h = 40;
		this.field_145816_i = 2.0F;
		this.field_145811_e = p_i45319_8_;
		this.field_145814_a = p_i45319_9_;
		this.preventEntitySpawning = true;
		//TODO Increase size
		this.setSize(0.996f, 0.996f);
		this.yOffset = this.height / 2.0F;
		this.setPosition(p_i45319_2_, p_i45319_4_, p_i45319_6_);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = p_i45319_2_;
		this.prevPosY = p_i45319_4_;
		this.prevPosZ = p_i45319_6_;

		//TODO Add stuff
		this.accelerationX = 0;
		this.accelerationY = -0.024525;
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
    	//TODO change return
        return false;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
    	//TODO clear and add new call
    	BlockPhysics.fallingSandUpdate(this.worldObj, this);
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float p_70069_1_)
    {
        //TODO Clear method
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_)
    {
        p_70014_1_.setByte("Tile", (byte)Block.getIdFromBlock(this.field_145811_e));
        p_70014_1_.setInteger("TileID", Block.getIdFromBlock(this.field_145811_e));
        p_70014_1_.setByte("Data", (byte)this.field_145814_a);
        p_70014_1_.setByte("Time", (byte)this.field_145812_b);
        p_70014_1_.setBoolean("DropItem", this.field_145813_c);
        p_70014_1_.setBoolean("HurtEntities", this.field_145809_g);
        p_70014_1_.setFloat("FallHurtAmount", this.field_145816_i);
        p_70014_1_.setInteger("FallHurtMax", this.field_145815_h);

        if (this.field_145810_d != null)
        {
            p_70014_1_.setTag("TileEntityData", this.field_145810_d);
        }
        //TODO Add lines
        p_70014_1_.setTag("Acceleration", newDoubleNBTList(new double[] {this.accelerationX, this.accelerationY, this.accelerationZ}));
        p_70014_1_.setByte("BPData", (byte)this.bpdata);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_)
    {
    	//TODO Clear lines and add line
        //if (p_70037_1_.hasKey("TileID", 99))
        //{
        //    this.field_145811_e = Block.getBlockById(p_70037_1_.getInteger("TileID"));
        //}
        //else
        //{
        //    this.field_145811_e = Block.getBlockById(p_70037_1_.getByte("Tile") & 255);
        //}
    	this.field_145811_e = BlockPhysics.readFallingSandID(p_70037_1_);

        this.field_145814_a = p_70037_1_.getByte("Data") & 255;
        this.field_145812_b = p_70037_1_.getByte("Time") & 255;

        if (p_70037_1_.hasKey("HurtEntities", 99))
        {
            this.field_145809_g = p_70037_1_.getBoolean("HurtEntities");
            this.field_145816_i = p_70037_1_.getFloat("FallHurtAmount");
            this.field_145815_h = p_70037_1_.getInteger("FallHurtMax");
        }
        else if (this.field_145811_e == Blocks.anvil)
        {
            this.field_145809_g = true;
        }

        if (p_70037_1_.hasKey("DropItem", 99))
        {
            this.field_145813_c = p_70037_1_.getBoolean("DropItem");
        }

        if (p_70037_1_.hasKey("TileEntityData", 10))
        {
            this.field_145810_d = p_70037_1_.getCompoundTag("TileEntityData");
        }

        if (this.field_145811_e.getMaterial() == Material.air)
        {
            this.field_145811_e = Blocks.sand;
        }
        //TODO Add lines I bet I fucked this up.
		if (p_70037_1_.hasKey("Acceleration"))
		{
			NBTTagList acceleration = p_70037_1_.getTagList("Acceleration", Constants.NBT.TAG_LIST);
		 	this.accelerationX = acceleration.func_150309_d(0);
		 	this.accelerationY = acceleration.func_150309_d(1);
		 	this.accelerationZ = acceleration.func_150309_d(2);
		}
		else
		{
			this.accelerationX = 0.0;
			this.accelerationY = 0.0;
			this.accelerationZ = 0.0;
		}
		if (p_70037_1_.hasKey("BPData"))
		{
			this.bpdata = p_70037_1_.getByte("BPData");
		}
		else
		{
			this.bpdata = 0;
		}
    }

    public void func_145806_a(boolean p_145806_1_)
    {
        this.field_145809_g = p_145806_1_;
    }

    public void addEntityCrashInfo(CrashReportCategory p_85029_1_)
    {
        super.addEntityCrashInfo(p_85029_1_);
        p_85029_1_.addCrashSection("Immitating block ID", Integer.valueOf(Block.getIdFromBlock(this.field_145811_e)));
        p_85029_1_.addCrashSection("Immitating block data", Integer.valueOf(this.field_145814_a));
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    @SideOnly(Side.CLIENT)
    public World func_145807_e()
    {
        return this.worldObj;
    }

    /**
     * Return whether this entity should be rendered as on fire.
     */
    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire()
    {
    	//TODO return this.isInWater
        return this.isInWater();
    }

    public Block func_145805_f()
    {
        return this.field_145811_e;
    }
    
    //TODO Add methods MUST BE OBFUSCATED
	public AxisAlignedBB getBoundingBox()
	{
		return this.boundingBox;
	}
	
	public void setInWeb()
    {
    }
	
	public void moveEntity(double par1, double par2, double par3)
	{
		BlockPhysics.moveEntity(this.worldObj, this, par1, par2, par3);
	}
}