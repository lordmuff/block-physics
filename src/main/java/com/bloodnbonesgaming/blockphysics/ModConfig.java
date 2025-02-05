package com.bloodnbonesgaming.blockphysics;

import java.io.File;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import com.bloodnbonesgaming.blockphysics.util.BlockDef;
import com.bloodnbonesgaming.blockphysics.util.DefinitionMaps;
import com.bloodnbonesgaming.blockphysics.util.MoveDef;

public class ModConfig
{
	private static Configuration config;

	public static ConfigCategory categoryMap = new ConfigCategory(null);

	public static int fallRange;
	public static int fallRenderRange;
	public static int maxMovingBlocks;
	public static int maxTickTime;
	public static int explosionStrength;
	public static int explosionQueue;
	public static int explosionInterval;
	public static boolean catapult;
	public static boolean explosionFire;

	public static void init(final File file)
	{
		ModConfig.config = new Configuration(file);

		ModConfig.load();

		ModConfig.fallRange = ModConfig.config.get("Options", "Fall Range", "60", "The max distance from the player, within which the blocks will begin to move (in blocks).").getInt();
		ModConfig.fallRenderRange = ModConfig.config.get("Options", "Fall Render Range", "30", "The max distance from the player, within which the falling blocks will be rendered (in blocks).").getInt();
		ModConfig.maxMovingBlocks = ModConfig.config.get("Options", "Max Moving Blocks", "300", "Maximum number of moving blocks / world.").getInt();
		ModConfig.maxTickTime = ModConfig.config.get("Options", "Max Tick Time", "1850", "The time length of one tick when falling switches off.").getInt();
		ModConfig.explosionStrength = ModConfig.config.get("Options", "Explosion Strength", "80", "Explosion strength modifier 0 - 200 original:100, affects only blocks.").getInt();
		ModConfig.explosionQueue = ModConfig.config.get("Options", "Explosion Queue", "200", "Max size of the explosion queue / world.").getInt();
		ModConfig.explosionInterval = ModConfig.config.get("Options", "Explosion Interval", "1", "Interval between explosions in ticks.").getInt();
		ModConfig.catapult = ModConfig.config.get("Options", "Catapult", "true", "Enable / disable catapult piston.").getBoolean(true);
		ModConfig.explosionFire = ModConfig.config.get("Options", "Explosion Fire", "true", "Can explosions cause fire?").getBoolean(true);

		ModConfig.categoryMap = ModConfig.config.getCategory("move definitions");

		if (ModConfig.categoryMap == null || ModConfig.categoryMap.keySet().size() == 0)
		{
			ModConfig.config.get("move definitions", "anvil", new String[]{"movetype:1", "slidechance:30", "ceiling:0", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:0", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			ModConfig.config.get("move definitions", "brick", new String[]{"movetype:1", "slidechance:45", "ceiling:1", "smallarc:0", "bigarc:1", "corbel:1", "ncorbel:0", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			ModConfig.config.get("move definitions", "cobblestone", new String[]{"movetype:1", "slidechance:60", "ceiling:0", "smallarc:1", "bigarc:1", "corbel:0", "ncorbel:0", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			ModConfig.config.get("move definitions", "dirt", new String[]{"movetype:2", "slidechance:70", "ceiling:0", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:0", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			ModConfig.config.get("move definitions", "endstone", new String[]{"movetype:1", "slidechance:50", "ceiling:1", "smallarc:1", "bigarc:1", "corbel:1", "ncorbel:1", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			ModConfig.config.get("move definitions", "glass", new String[]{"movetype:1", "slidechance:40", "ceiling:1", "smallarc:0", "bigarc:3", "corbel:4", "ncorbel:0", "hanging:0", "attached:1", "floating:0", "branch:0"}).getStringList();
			ModConfig.config.get("move definitions", "grass", new String[]{"movetype:1", "slidechance:60", "ceiling:0", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:0", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			ModConfig.config.get("move definitions", "leaves", new String[]{"movetype:1", "slidechance:70", "ceiling:0", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:0", "hanging:0", "attached:0", "floating:3:minecraft:log", "branch:0"}).getStringList();
			ModConfig.config.get("move definitions", "leaves2", new String[]{"movetype:1", "slidechance:70", "ceiling:1", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:0", "hanging:1", "attached:2", "floating:0", "branch:0"}).getStringList();
			ModConfig.config.get("move definitions", "litpumpkin", new String[]{"movetype:1", "slidechance:60", "ceiling:0", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:0", "hanging:1", "attached:1", "floating:0", "branch:0"}).getStringList();
			ModConfig.config.get("move definitions", "netherbrick", new String[]{"movetype:1", "slidechance:30", "ceiling:1", "smallarc:0", "bigarc:3", "corbel:0", "ncorbel:3", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			ModConfig.config.get("move definitions", "planks", new String[]{"movetype:1", "slidechance:30", "ceiling:1", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:5", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			ModConfig.config.get("move definitions", "pumpkin", new String[]{"movetype:2", "slidechance:100", "ceiling:0", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:0", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			ModConfig.config.get("move definitions", "sand", new String[]{"movetype:2", "slidechance:90", "ceiling:0", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:0", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			ModConfig.config.get("move definitions", "stone", new String[]{"movetype:1", "slidechance:50", "ceiling:0", "smallarc:2", "bigarc:2", "corbel:0", "ncorbel:0", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			ModConfig.config.get("move definitions", "stonebrick", new String[]{"movetype:1", "slidechance:40", "ceiling:0", "smallarc:2", "bigarc:3", "corbel:2", "ncorbel:0", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			ModConfig.config.get("move definitions", "wood", new String[]{"movetype:1", "slidechance:70", "ceiling:1", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:6", "hanging:0", "attached:0", "floating:0", "branch:1"}).getStringList();
			ModConfig.config.get("move definitions", "wool", new String[]{"movetype:1", "slidechance:30", "ceiling:0", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:0", "hanging:5", "attached:5", "floating:0", "branch:0"}).getStringList();

			ModConfig.categoryMap = ModConfig.config.getCategory("move definitions");
		}

		for (final String key : ModConfig.categoryMap.keySet())
		{
			final String[] properties = ModConfig.categoryMap.get(key).getStringList();

			final String id = key;
			int movetype = 0;
			int slidechance = 0;
			boolean ceiling = false;
			int smallarc = 0;
			int bigarc = 0;
			int corbel = 0;
			int ncorbel = 0;
			int hanging = 0;
			int attached = 0;
			int floatingradius = 0;
			String floatingblock = null;
			//int floatingmeta = 0;
			boolean branch = false;
			int tree = 0;

			for (final String propertie : properties) {
				final int colonIndex = propertie.indexOf(":");
				final String keyString = propertie.substring(0, colonIndex);

				if (!keyString.equalsIgnoreCase("floating") && !keyString.equalsIgnoreCase("branch") && !keyString.equalsIgnoreCase("ceiling"))
				{
					final int value = Integer.parseInt(propertie.substring(colonIndex +1));
					if (keyString.equalsIgnoreCase("movetype"))
					{
						movetype = value;
					}
					else if (keyString.equalsIgnoreCase("slidechance"))
					{
						slidechance = value;
					}
					else if (keyString.equalsIgnoreCase("smallarc"))
					{
						smallarc = value;
					}
					else if (keyString.equalsIgnoreCase("bigarc"))
					{
						bigarc = value;
					}
					else if (keyString.equalsIgnoreCase("corbel"))
					{
						corbel = value;
					}
					else if (keyString.equalsIgnoreCase("ncorbel"))
					{
						ncorbel = value;
					}
					else if (keyString.equalsIgnoreCase("hanging"))
					{
						hanging = value;
					}
					else if (keyString.equalsIgnoreCase("attached"))
					{
						attached = value;
					}
					else if (keyString.equalsIgnoreCase("tree"))
					{
						tree = value;
					}
				}
				else if (keyString.equalsIgnoreCase("ceiling") || keyString.equalsIgnoreCase("branch"))
				{
					final Boolean value = Boolean.valueOf(propertie.substring(colonIndex +1));

					if (keyString.equalsIgnoreCase("ceiling"))
					{
						ceiling = value;
					}
					else if (keyString.equalsIgnoreCase("branch"))
					{
						branch = value;
					}
				}
				else if (keyString.equalsIgnoreCase("floating"))
				{

					final int value2 = propertie.indexOf(":", colonIndex +1);

					if (value2 != -1)
					{
						floatingradius = Integer.parseInt(propertie.substring(colonIndex +1, value2));
						//int value3 = properties[i].indexOf(":", value2);
						floatingblock = propertie.substring(value2 + 1);
						//BlockPhysics.instance.getLog().info("FLOATINGBLOCK = " + floatingblock);
						/*if (value3 != -1)
						{
							floatingblock = properties[i].substring(value2 +1, value3);
							int value4 = properties[i].indexOf(":", value3);

							if (value4 != -1)
							{
								floatingmeta = Integer.parseInt(properties[i].substring(value3 +1, value4));
							}
						}*/
					}
				}
			}
			DefinitionMaps.putMoveDef(id, new MoveDef(id, movetype, slidechance, ceiling, smallarc, bigarc, corbel, ncorbel, hanging, attached, floatingradius, floatingblock, branch));
		}

		ModConfig.categoryMap = ModConfig.config.getCategory("block definitions");

		if (ModConfig.categoryMap == null || ModConfig.categoryMap.keySet().size() == 0)
		{
			ModConfig.config.get("block definitions", "anvil", new String[]{"movenum:1", "movedef1:anvil", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:0", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:0", "mass:6000", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "bookcase", new String[]{"movenum:1", "movedef1:wool", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:2", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:0", "mass:200", "strength:100"}).getStringList();
			ModConfig.config.get("block definitions", "brick", new String[]{"movenum:2", "movedef1:default", "movedef2:brick", "moveflipnumber:15", "movechanger:2", "supportingblock:1", "fragile:0", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:1", "mass:1500", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "cobblestone", new String[]{"movenum:2", "movedef1:default", "movedef2:cobblestone", "moveflipnumber:15", "movechanger:2", "supportingblock:1", "fragile:0", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:1", "mass:2100", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "dirt", new String[]{"movenum:1", "movedef1:dirt", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:0", "trapping:true", "pushtype:1", "randomtick:true", "tickrate:5", "placedmove:0", "mass:1500", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "dispenser", new String[]{"movenum:0", "movedef1:default", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:1", "fragile:0", "trapping:false", "pushtype:0", "randomtick:false", "tickrate:4", "placedmove:0", "mass:1500", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "dragonegg", new String[]{"movenum:1", "movedef1:pumpkin", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:2", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:0", "mass:50", "strength:100"}).getStringList();
			ModConfig.config.get("block definitions", "endstone", new String[]{"movenum:2", "movedef1:default", "movedef2:endstone", "moveflipnumber:15", "movechanger:2", "supportingblock:1", "fragile:0", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:1", "mass:1000", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "gemblock", new String[]{"movenum:0", "movedef1:default", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:0", "trapping:false", "pushtype:1", "randomtick:false", "tickrate:4", "placedmove:0", "mass:1500", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "giantmushroom", new String[]{"movenum:0", "movedef1:default", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:1", "trapping:false", "pushtype:1", "randomtick:false", "tickrate:4", "placedmove:0", "mass:40", "strength:10"}).getStringList();
			ModConfig.config.get("block definitions", "glass", new String[]{"movenum:2", "movedef1:default", "movedef2:glass", "moveflipnumber:10", "movechanger:2", "supportingblock:1", "fragile:1", "trapping:false", "pushtype:1", "randomtick:false", "tickrate:4", "placedmove:1", "mass:500", "strength:1000"}).getStringList();
			ModConfig.config.get("block definitions", "grass", new String[]{"movenum:1", "movedef1:grass", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:0", "trapping:true", "pushtype:1", "randomtick:true", "tickrate:5", "placedmove:0", "mass:1500", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "ice", new String[]{"movenum:0", "movedef1:default", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:1", "trapping:false", "pushtype:1", "randomtick:false", "tickrate:4", "placedmove:0", "mass:1000", "strength:500"}).getStringList();
			ModConfig.config.get("block definitions", "leaves", new String[]{"movenum:1", "movedef1:leaves", "movedef2:leaves2", "moveflipnumber:15", "movechanger:0", "supportingblock:0", "fragile:1", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:1", "mass:40", "strength:10"}).getStringList();
			ModConfig.config.get("block definitions", "litpumpkin", new String[]{"movenum:1", "movedef1:litpumpkin", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:1", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:0", "mass:50", "strength:20"}).getStringList();
			ModConfig.config.get("block definitions", "metalblock", new String[]{"movenum:0", "movedef1:default", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:1", "fragile:0", "trapping:false", "pushtype:1", "randomtick:false", "tickrate:4", "placedmove:0", "mass:6000", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "misc", new String[]{"movenum:0", "movedef1:default", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:2", "trapping:false", "pushtype:0", "randomtick:false", "tickrate:4", "placedmove:0", "mass:10", "strength:0"}).getStringList();
			ModConfig.config.get("block definitions", "netherbrick", new String[]{"movenum:2", "movedef1:default", "movedef2:netherbrick", "moveflipnumber:15", "movechanger:2", "supportingblock:1", "fragile:0", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:1", "mass:500", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "netherrack", new String[]{"movenum:2", "movedef1:default", "movedef2:stone", "moveflipnumber:7", "movechanger:2", "supportingblock:1", "fragile:0", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:1", "mass:400", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "obsidian", new String[]{"movenum:0", "movedef1:default", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:1", "fragile:0", "trapping:false", "pushtype:1", "randomtick:false", "tickrate:4", "placedmove:0", "mass:3000", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "ore", new String[]{"movenum:0", "movedef1:default", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:1", "fragile:0", "trapping:false", "pushtype:1", "randomtick:false", "tickrate:4", "placedmove:0", "mass:3000", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "piston", new String[]{"movenum:0", "movedef1:default", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:1", "fragile:0", "trapping:false", "pushtype:1", "randomtick:false", "tickrate:4", "placedmove:0", "mass:1500", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "planks", new String[]{"movenum:2", "movedef1:default", "movedef2:planks", "moveflipnumber:15", "movechanger:2", "supportingblock:1", "fragile:1", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:1", "mass:125", "strength:1000"}).getStringList();
			ModConfig.config.get("block definitions", "plants", new String[]{"movenum:0", "movedef1:default", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:1", "trapping:false", "pushtype:0", "randomtick:false", "tickrate:4", "placedmove:0", "mass:10", "strength:0"}).getStringList();
			ModConfig.config.get("block definitions", "pumpkin", new String[]{"movenum:1", "movedef1:pumpkin", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:1", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:0", "mass:50", "strength:20"}).getStringList();
			ModConfig.config.get("block definitions", "rail", new String[]{"movenum:0", "movedef1:default", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:0", "trapping:false", "pushtype:2", "randomtick:false", "tickrate:4", "placedmove:0", "mass:100", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "sand", new String[]{"movenum:1", "movedef1:sand", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:0", "trapping:true", "pushtype:1", "randomtick:true", "tickrate:5", "placedmove:0", "mass:1700", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "snow", new String[]{"movenum:1", "movedef1:sand", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:1", "trapping:true", "pushtype:1", "randomtick:true", "tickrate:5", "placedmove:0", "mass:30", "strength:60"}).getStringList();
			ModConfig.config.get("block definitions", "stone", new String[]{"movenum:2", "movedef1:default", "movedef2:stone", "moveflipnumber:15", "movechanger:2", "supportingblock:1", "fragile:0", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:1", "mass:2500", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "stonebrick", new String[]{"movenum:2", "movedef1:default", "movedef2:stonebrick", "moveflipnumber:15", "movechanger:2", "supportingblock:1", "fragile:0", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:1", "mass:2500", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "tnt", new String[]{"movenum:1", "movedef1:grass", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:0", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:0", "mass:1500", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "wall", new String[]{"movenum:1", "movedef1:cobblestone", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:1", "fragile:0", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:0", "mass:700", "strength:64000"}).getStringList();
			ModConfig.config.get("block definitions", "web", new String[]{"movenum:0", "movedef1:default", "movedef2:default", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:1", "trapping:true", "pushtype:0", "randomtick:false", "tickrate:4", "placedmove:0", "mass:1", "strength:50"}).getStringList();
			ModConfig.config.get("block definitions", "wood", new String[]{"movenum:2", "movedef1:wood", "movedef2:default", "moveflipnumber:15", "movechanger:0", "supportingblock:0", "fragile:0", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:1", "mass:500", "strength:2000"}).getStringList();
			ModConfig.config.get("block definitions", "wool", new String[]{"movenum:2", "movedef1:default", "movedef2:wool", "moveflipnumber:5", "movechanger:2", "supportingblock:0", "fragile:1", "trapping:false", "pushtype:1", "randomtick:true", "tickrate:4", "placedmove:1", "mass:10", "strength:100"}).getStringList();

			ModConfig.categoryMap = ModConfig.config.getCategory("block definitions");
		}

		for (final String key : ModConfig.categoryMap.keySet())
		{
			final String[] properties = ModConfig.categoryMap.get(key).getStringList();


			final String id = key;
			String movedefs1 = "default";
			String movedefs2 = "default";
			int movenum = 0;
			int moveflipnumber = 0;
			int movechanger = 0;
			int supportingblock = 0;
			int	fragile = 0;
			boolean trapping = false;
			int pushtype = 0;
			boolean randomtick = false;
			int tickrate = 10;
			int placedmove = 0;
			int mass = 1500;
			int strength = 64000;

			for (final String propertie : properties) {
				final int colonIndex = propertie.indexOf(":");
				final String keyString = propertie.substring(0, colonIndex);


				if (keyString.equalsIgnoreCase("movenum") || keyString.equalsIgnoreCase("moveflipnumber") || keyString.equalsIgnoreCase("movechanger") || keyString.equalsIgnoreCase("supportingblock") || keyString.equalsIgnoreCase("fragile") || keyString.equalsIgnoreCase("pushtype") || keyString.equalsIgnoreCase("tickrate") || keyString.equalsIgnoreCase("placedmove") || keyString.equalsIgnoreCase("mass") || keyString.equalsIgnoreCase("strength"))
				{
					final int value = Integer.parseInt(propertie.substring(colonIndex +1));
					if (keyString.equalsIgnoreCase("movenum"))
					{
						movenum = value;
					}
					else if (keyString.equalsIgnoreCase("moveflipnumber"))
					{
						moveflipnumber = value;
					}
					else if (keyString.equalsIgnoreCase("movechanger"))
					{
						movechanger = value;
					}
					else if (keyString.equalsIgnoreCase("supportingblock"))
					{
						supportingblock = value;
					}
					else if (keyString.equalsIgnoreCase("fragile"))
					{
						fragile = value;
					}
					else if (keyString.equalsIgnoreCase("pushtype"))
					{
						pushtype = value;
					}
					else if (keyString.equalsIgnoreCase("tickrate"))
					{
						tickrate = value;
					}
					else if (keyString.equalsIgnoreCase("placedmove"))
					{
						placedmove = value;
					}
					else if (keyString.equalsIgnoreCase("mass"))
					{
						mass = value;
					}
					else if (keyString.equalsIgnoreCase("strength"))
					{
						strength = value;
					}
					else if (keyString.equalsIgnoreCase("tickrate"))
					{
						tickrate = value;
					}
				}
				else if (keyString.equalsIgnoreCase("trapping") || keyString.equalsIgnoreCase("randomtick"))
				{
					final Boolean value = Boolean.valueOf(propertie.substring(colonIndex +1));

					if (keyString.equalsIgnoreCase("trapping"))
					{
						trapping = value;
					}
					else if (keyString.equalsIgnoreCase("randomtick"))
					{
						randomtick = value;
					}
				}
				else if (keyString.equalsIgnoreCase("movedef1"))
				{
					movedefs1 = propertie.substring(colonIndex +1);
				}
				else if (keyString.equalsIgnoreCase("movedef2"))
				{
					movedefs2 = propertie.substring(colonIndex +1);
				}
			}
			DefinitionMaps.putBlockDef(id, new BlockDef(id, movedefs1, movedefs2, movenum, moveflipnumber, movechanger, supportingblock, fragile, trapping, pushtype, randomtick, tickrate, placedmove, mass, strength));
		}

		ModConfig.categoryMap = ModConfig.config.getCategory("blocks");

		if (ModConfig.categoryMap == null || ModConfig.categoryMap.keySet().size() == 0)
		{
			ModConfig.config.get("blocks", "anvil", new String[]{"minecraft:diamond_block", "minecraft:anvil"}).getStringList();
			ModConfig.config.get("blocks", "bookcase", new String[]{"minecraft:bookshelf", "minecraft:noteblock", "minecraft:jukebox"}).getStringList();
			ModConfig.config.get("blocks", "brick", new String[]{"minecraft:brick_block", "minecraft:brick_stairs"}).getStringList();
			ModConfig.config.get("blocks", "cobblestone", new String[]{"minecraft:cobblestone", "minecraft:mossy_cobblestone", "minecraft:stone_stairs"}).getStringList();
			ModConfig.config.get("blocks", "dirt", new String[]{"minecraft:dirt"}).getStringList();
			ModConfig.config.get("blocks", "dispenser", new String[]{"minecraft:dispenser", "minecraft:piston_head", "minecraft:furnace", "minecraft:lit_furnace", "minecraft:dropper", "minecraft:piston_extension"}).getStringList();
			ModConfig.config.get("blocks", "dragonegg", new String[]{"minecraft:dragon_egg"}).getStringList();
			ModConfig.config.get("blocks", "endstone", new String[]{"minecraft:end_stone"}).getStringList();
			ModConfig.config.get("blocks", "gemblock", new String[]{"minecraft:lapis_block", "minecraft:redstone_block", "minecraft:emerald_block", "minecraft:coal_block"}).getStringList();
			ModConfig.config.get("blocks", "giantmushroom", new String[]{"minecraft:brown_mushroom_block", "minecraft:red_mushroom_block"}).getStringList();
			ModConfig.config.get("blocks", "glass", new String[]{"minecraft:glass", "minecraft:glowstone", "minecraft:redstone_lamp", "minecraft:lit_redstone_lamp", "minecraft:stained_glass"}).getStringList();
			ModConfig.config.get("blocks", "grass", new String[]{"minecraft:grass", "minecraft:farmland", "minecraft:clay", "minecraft:mycelium"}).getStringList();
			ModConfig.config.get("blocks", "ice", new String[]{"minecraft:ice"}).getStringList();
			ModConfig.config.get("blocks", "leaves", new String[]{"minecraft:leaves", "minecraft:leaves2"}).getStringList();
			ModConfig.config.get("blocks", "litpumpkin", new String[]{"minecraft:lit_pumpkin"}).getStringList();
			ModConfig.config.get("blocks", "metalblock", new String[]{"minecraft:gold_block", "minecraft:iron_block", "minecraft:cauldron", "minecraft:hopper"}).getStringList();
			ModConfig.config.get("blocks", "misc", new String[]{"minecraft:torch", "minecraft:wheat", "minecraft:cactus", "minecraft:reeds", "minecraft:nether_wart", "minecraft:cocoa", "minecraft:tripwire", "minecraft:tripwire_hook", "minecraft:string", "minecraft:flower_pot", "minecraft:carrots", "minecraft:potatoes", "minecraft:standing_sign", "minecraft:wall_sign", "minecraft:ladder", "minecraft:lever", "minecraft:stone_pressure_plate", "minecraft:wooden_pressure_plate", "minecraft:light_weighted_pressure_plate", "minecraft:heavy_weighted_pressure_plate", "minecraft:unlit_redstone_torch", "minecraft:redstone_torch", "minecraft:stone_button", "minecraft:wooden_button", "minecraft:unpowered_repeater", "minecraft:powered_repeater", "minecraft:brewing_stand", "minecraft:unpowered_comparator", "minecraft:powered_comparator", "minecraft:daylight_detector", "minecraft:skull", "minecraft:carpet", "minecraft:redstone_wire"}).getStringList();
			ModConfig.config.get("blocks", "netherbrick", new String[]{"minecraft:nether_brick", "minecraft:nether_brick_fence", "minecraft:nether_brick_stairs"}).getStringList();
			ModConfig.config.get("blocks", "netherrack", new String[]{"minecraft:netherrack"}).getStringList();
			ModConfig.config.get("blocks", "obsidian", new String[]{"minecraft:obsidian", "minecraft:enchanting_table", "minecraft:ender_chest", "minecraft:beacon"}).getStringList();
			ModConfig.config.get("blocks", "ore", new String[]{"minecraft:gold_ore", "minecraft:iron_ore", "minecraft:coal_ore", "minecraft:lapis_ore", "minecraft:diamond_ore", "minecraft:redstone_ore", "minecraft:lit_redstone_ore", "minecraft:emerald_ore", "minecraft:quartz_ore"}).getStringList();
			ModConfig.config.get("blocks", "piston", new String[]{"minecraft:piston", "minecraft:sticky_piston"}).getStringList();
			ModConfig.config.get("blocks", "planks", new String[]{"minecraft:planks", "minecraft:oak_stairs", "minecraft:chest", "minecraft:trapped_chest", "minecraft:crafting_table", "minecraft:fence", "minecraft:double_wooden_slab", "minecraft:wooden_slab", "minecraft:spruce_stairs", "minecraft:birch_stairs", "minecraft:jungle_stairs", "minecraft:acacia_stairs", "minecraft:dark_oak_stairs"}).getStringList();
			ModConfig.config.get("blocks", "plants", new String[]{"minecraft:sapling", "minecraft:tallgrass", "minecraft:deadbush", "minecraft:yellow_flower", "minecraft:red_flower", "minecraft:double_plant", "minecraft:red_mushroom", "minecraft:brown_mushroom", "minecraft:snow_layer", "minecraft:glass_pane", "minecraft:stained_glass_pane", "minecraft:pumpkin_seeds", "minecraft:melon_stem", "minecraft:vine", "minecraft:waterlily", "minecraft:cake"}).getStringList();
			ModConfig.config.get("blocks", "pumpkin", new String[]{"minecraft:pumpkin", "minecraft:melon_block", "minecraft:hay_block"}).getStringList();
			ModConfig.config.get("blocks", "rail", new String[]{"minecraft:golden_rail", "minecraft:detector_rail", "minecraft:rail", "minecraft:activator_rail"}).getStringList();
			ModConfig.config.get("blocks", "sand", new String[]{"minecraft:sand", "minecraft:soul_sand", "minecraft:gravel"}).getStringList();
			ModConfig.config.get("blocks", "snow", new String[]{"minecraft:snow"}).getStringList();
			ModConfig.config.get("blocks", "stone", new String[]{"minecraft:stone", "minecraft:sandstone", "minecraft:sandstone_stairs", "minecraft:stained_hardened_clay", "minecraft:hardened_clay", "minecraft:packed_ice"}).getStringList();
			ModConfig.config.get("blocks", "stonebrick", new String[]{"minecraft:stonebrick", "minecraft:quartz_block", "minecraft:quartz_stairs", "minecraft:monster_egg", "minecraft:double_stone_slab", "minecraft:stone_slab", "minecraft:stone_brick_stairs", "minecraft:iron_bars"}).getStringList();
			ModConfig.config.get("blocks", "tnt", new String[]{"minecraft:tnt"}).getStringList();
			ModConfig.config.get("blocks", "unconfigured", new String[]{"minecraft:water", "minecraft:air", "minecraft:bedrock", "minecraft:flowing_water", "minecraft:flowing_lava", "minecraft:lava", "minecraft:fire", "minecraft:mob_spawner", "minecraft:bed", "minecraft:wooden_door", "minecraft:iron_door", "minecraft:portal", "minecraft:trapdoor", "minecraft:end_portal", "minecraft:end_portal_frame", "minecraft:command_block", "minecraft:fence_gate"}).getStringList();
			ModConfig.config.get("blocks", "wall", new String[]{"minecraft:cobblestone_wall"}).getStringList();
			ModConfig.config.get("blocks", "web", new String[]{"minecraft:web"}).getStringList();
			ModConfig.config.get("blocks", "wood", new String[]{"minecraft:log", "minecraft:log2"}).getStringList();
			ModConfig.config.get("blocks", "wool", new String[]{"minecraft:wool", "minecraft:sponge"}).getStringList();

			ModConfig.categoryMap = ModConfig.config.getCategory("blocks");
		}

		for (final String key : ModConfig.categoryMap.keySet())
		{
			final String[] properties = ModConfig.categoryMap.get(key).getStringList();


			for (final String propertie : properties) {
				final int index = propertie.indexOf(":");
				int index1 = propertie.indexOf(":", index +1);
				String block;
				if (index1 != -1)
				{
					block = propertie.substring(0, index1);
				}
				else
				{
					block = propertie.substring(0);
				}
				int meta = 0;
				final String[] blockDefs = new String[16];

				if (index1 == -1)
				{
					for (int metadata = 0; metadata < 16; metadata++)
					{
						blockDefs[metadata] = key;
					}
				}

				while (index1 != -1)
				{
					final int index2 = propertie.indexOf(":", index1 +1);
					String subString1;

					if (index2 != -1)
					{
						subString1 = propertie.substring(index1 +1, index2);
					}
					else
					{
						subString1 = propertie.substring(index1 +1);
					}

					meta = Integer.parseInt(subString1);
					blockDefs[meta] = key;
					index1 = index2;
				}
				/*if (meta == 0)
				{
					blockDefs[meta] = key;
				}*/
				DefinitionMaps.putBlockToBlockDef(block, blockDefs);
			}
		}

		ModConfig.save();
	}

	public static void save()
	{
		ModConfig.config.save();
	}

	public static void load()
	{
		ModConfig.config.load();
	}

	public static void readBlocks()
	{

	}
}