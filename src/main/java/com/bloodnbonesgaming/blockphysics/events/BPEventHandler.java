package com.bloodnbonesgaming.blockphysics.events;

import java.util.Iterator;

import com.bloodnbonesgaming.blockphysics.BlockPhysics;
import com.bloodnbonesgaming.blockphysics.ExplosionQueue;
import com.bnbgaming.lib.core.ASMAdditionHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.ExplosionEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BPEventHandler
{
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onPlayerBlockPlace(ExplosionEvent.Start event)
	{
		event.setCanceled(true);
		
		((ExplosionQueue)BlockPhysics.asmHelper.get(event.world, "explosionQueue")).add(event.explosion);
		
		if (event.world instanceof WorldServer)
		{
	        if (!event.explosion.isSmoking)
	        {
	            event.explosion.affectedBlockPositions.clear();
	        }

	        Iterator iterator = event.world.playerEntities.iterator();

	        while (iterator.hasNext())
	        {
	            EntityPlayer entityplayer = (EntityPlayer)iterator.next();

	            if (entityplayer.getDistanceSq(event.explosion.explosionX, event.explosion.explosionY, event.explosion.explosionZ) < 4096.0D)
	            {
	                ((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(new S27PacketExplosion(event.explosion.explosionX, event.explosion.explosionY, event.explosion.explosionZ, event.explosion.explosionSize, event.explosion.affectedBlockPositions, (Vec3)event.explosion.func_77277_b().get(entityplayer)));
	            }
	        }
		}
	}
}
