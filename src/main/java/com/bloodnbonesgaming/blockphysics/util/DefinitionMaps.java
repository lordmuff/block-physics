package com.bloodnbonesgaming.blockphysics.util;

import java.util.HashMap;

public class DefinitionMaps
{
	private static HashMap<String, MoveDef> moveDefMap = new HashMap<String, MoveDef>();
	private static HashMap<String, BlockDef> blockDefMap = new HashMap<String, BlockDef>();
	private static HashMap<String, String[]> blockToBlockDefMap = new HashMap<String, String[]>();


	public static void putBlockToBlockDef(final String blockName, final String[] metas)
	{
		if (DefinitionMaps.blockToBlockDefMap.containsKey(blockName) && DefinitionMaps.blockToBlockDefMap.get(blockName) != null)
		{
			throw new RuntimeException("You attempted to override pre-existing entries in the BlockToBlockDef Map: " + blockName);
		}
		else
		{
			DefinitionMaps.blockToBlockDefMap.put(blockName, metas);
		}
	}

	public static String getBlockToBlockDef(final String blockName, final int meta)
	{
		if (DefinitionMaps.blockToBlockDefMap.containsKey(blockName) && DefinitionMaps.blockToBlockDefMap.get(blockName)[meta] != null)
		{
			return DefinitionMaps.blockToBlockDefMap.get(blockName)[meta];
		}
		return "default";
	}

	public static BlockDef getBlockDef(final String blockName, final int meta)
	{
		return DefinitionMaps.getBlockDef(DefinitionMaps.getBlockToBlockDef(blockName, meta));
	}

	private static BlockDef getBlockDef(final String name)
	{
		if (DefinitionMaps.blockDefMap.containsKey(name) && DefinitionMaps.blockDefMap.get(name) != null)
		{
			return DefinitionMaps.blockDefMap.get(name);
		}
		else if (!DefinitionMaps.blockDefMap.containsKey("default") || DefinitionMaps.blockDefMap.get("default") == null)
		{
			DefinitionMaps.putBlockDef("default", new BlockDef("default"));
		}
		return DefinitionMaps.blockDefMap.get("default");
	}

	public static void putBlockDef(final String name, final BlockDef blockDef)
	{
		if (DefinitionMaps.blockDefMap.containsKey(name) && DefinitionMaps.moveDefMap.get(name) != null)
		{
			throw new RuntimeException("You attempted to override pre-existing entries in the BlockDef Map: " + name);
		}
		else
		{
			DefinitionMaps.blockDefMap.put(name, blockDef);
		}
	}

	public static MoveDef getMovedef(final String name)
	{
		if (DefinitionMaps.moveDefMap.containsKey(name) && DefinitionMaps.moveDefMap.get(name) != null)
		{
			return DefinitionMaps.moveDefMap.get(name);
		}
		else if (!DefinitionMaps.moveDefMap.containsKey("default") || DefinitionMaps.moveDefMap.get("default") == null)
		{
			DefinitionMaps.putMoveDef("default", new MoveDef("default"));
		}
		return DefinitionMaps.moveDefMap.get("default");
	}

	public static void putMoveDef(final String name, final MoveDef moveDef)
	{
		if (DefinitionMaps.moveDefMap.containsKey(name) && DefinitionMaps.moveDefMap.get(name) != null)
		{
			throw new RuntimeException("You attempted to override pre-existing entries in the MoveDef Map: " + name);
		}
		else
		{
			DefinitionMaps.moveDefMap.put(name, moveDef);
		}
	}
}