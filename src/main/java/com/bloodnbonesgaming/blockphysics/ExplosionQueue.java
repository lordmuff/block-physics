package com.bloodnbonesgaming.blockphysics;

import java.util.ArrayList;

import net.minecraft.world.Explosion;

public class ExplosionQueue
{
	private final ArrayList explosionQueue;
	private int xinterv;

	public ExplosionQueue()
	{
		this.explosionQueue = new ArrayList();
	}

	public void add( final Explosion expl)
	{
		if (this.explosionQueue.size() >= ModConfig.explosionQueue)
		{
			//BlockPhysics.writetoLog("Skipping explosion...");
			return;
		}
		this.explosionQueue.add(expl);
	}

	public void doNextExplosion()
	{
		this.xinterv++;
		if (this.xinterv < 0) {
			this.xinterv = 1000;
		}
		if ( this.explosionQueue.isEmpty() ) {
			return;
		}
		if ( this.xinterv < ModConfig.explosionInterval) {
			return;
		}
		this.xinterv = 0;
		final Explosion explosion = (Explosion)this.explosionQueue.remove(0);
		explosion.doExplosionA();
		explosion.doExplosionB(true);
	}


	public int getSize()
	{
		if (this.explosionQueue == null) {
			return 0;
		}
		return this.explosionQueue.size();
	}

	public void reset()
	{
		this.explosionQueue.clear();
	}
}