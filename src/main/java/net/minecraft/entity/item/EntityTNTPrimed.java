package net.minecraft.entity.item;

import com.bloodnbonesgaming.blockphysics.BlockPhysics;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

//TODO Change superclass to EntityFallingBlock
public class EntityTNTPrimed extends EntityFallingBlock
{
    /** How long the fuse is */
    public int fuse;
    private EntityLivingBase tntPlacedBy;
    private static final String __OBFID = "CL_00001681";

    public EntityTNTPrimed(World p_i1729_1_)
    {
        super(p_i1729_1_);
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
        //TODO Fix super call to call the new superclass
    }

    public EntityTNTPrimed(World p_i1730_1_, double p_i1730_2_, double p_i1730_4_, double p_i1730_6_, EntityLivingBase p_i1730_8_)
    {
    	//TODO Clear method and add
        super(p_i1730_1_, p_i1730_2_, p_i1730_4_, p_i1730_6_, Blocks.tnt, 0);
		this.fuse = 80;
		
        //this.tntPlacedBy = p_i1730_8_; May want to keep this, I imagine its useful
        
        
    }

    protected void entityInit() {}

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
    	//TODO Change return statement ICONST_0
        return false;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
    	//TODO Clear and inject
    	BlockPhysics.fallingSandUpdate(this.worldObj, this);
    }

    //TODO Change access to public
    public void explode()
    {
    	//TODO Fix error by setting ACONST_NULL to: ALOAD, 0
        float f = 4.0F;
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, f, true);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_)
    {
        p_70014_1_.setByte("Fuse", (byte)this.fuse);
        //TODO Add line
        super.writeEntityToNBT(p_70014_1_);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_)
    {
        this.fuse = p_70037_1_.getByte("Fuse");
        //TODO Add line
        super.readEntityFromNBT(p_70037_1_);
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    /**
     * returns null or the entityliving it was placed or ignited by
     */
    public EntityLivingBase getTntPlacedBy()
    {
        return this.tntPlacedBy;
    }
}