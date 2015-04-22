package net.minecraft.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.bloodnbonesgaming.blockphysics.BlockPhysics;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;

public class Explosion
{
    /** whether or not the explosion sets fire to blocks around it */
    public boolean isFlaming;
    /** whether or not this explosion spawns smoke particles */
    public boolean isSmoking = true;
    private int field_77289_h = 16;
    private Random explosionRNG = new Random();
    private World worldObj;
    public double explosionX;
    public double explosionY;
    public double explosionZ;
    public Entity exploder;
    public float explosionSize;
    /** A list of ChunkPositions of blocks affected by this explosion */
    public List affectedBlockPositions = new ArrayList();
    private Map field_77288_k = new HashMap();
    private static final String __OBFID = "CL_00000134";
    //TODO Add field
    public boolean impact;

    public Explosion(World p_i1948_1_, Entity p_i1948_2_, double p_i1948_3_, double p_i1948_5_, double p_i1948_7_, float p_i1948_9_)
    {
        this.worldObj = p_i1948_1_;
        this.exploder = p_i1948_2_;
        this.explosionSize = p_i1948_9_;
        this.explosionX = p_i1948_3_;
        this.explosionY = p_i1948_5_;
        this.explosionZ = p_i1948_7_;
        //TODO Add line
        this.impact = false;
    }

    /**
     * Does the first part of the explosion (destroy blocks)
     */
    public void doExplosionA()
    {
        //TODO Clear method and add
    	BlockPhysics.doExplosionA(this.worldObj, this);
    }

    /**
     * Does the second part of the explosion (sound, particles, drop spawn)
     */
    public void doExplosionB(boolean p_77279_1_)
    {
        //TODO Clear method and add
    	BlockPhysics.doExplosionB(this.worldObj, this, p_77279_1_);
    }

    public Map func_77277_b()
    {
        return this.field_77288_k;
    }

    /**
     * Returns either the entity that placed the explosive block, the entity that caused the explosion or null.
     */
    public EntityLivingBase getExplosivePlacedBy()
    {
        return this.exploder == null ? null : (this.exploder instanceof EntityTNTPrimed ? ((EntityTNTPrimed)this.exploder).getTntPlacedBy() : (this.exploder instanceof EntityLivingBase ? (EntityLivingBase)this.exploder : null));
    }
}