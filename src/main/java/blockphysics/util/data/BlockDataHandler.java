package blockphysics.util.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import blockphysics.BlockDef;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDataHandler {
	
	private static final Map<String, BlockDef> blockDefMap = new HashMap<String, BlockDef>();
	private static final Map<IBlockState, BlockDef> stateToBlockDefMap = new HashMap<IBlockState, BlockDef>();
	//TODO state map must be filled completely.
	//TODO must properly set default via config
	private static final BlockDef defaultBlockDef = new BlockDef(0);
	
	private static final Map<IBlockState, HashSet<IBlockState>> sameBlockMap = new HashMap<IBlockState, HashSet<IBlockState>>();
	
	
	
	
	
	
	public static BlockDef getBlockDef(final World world, final int x, final int y, final int z)
	{
		return BlockDataHandler.getBlockDef(world, new BlockPos(x, y, z));
	}
	
	public static BlockDef getBlockDef(final World world, final BlockPos pos)
	{
		if (BlockDataHandler.stateToBlockDefMap.containsKey(world.getBlockState(pos)))
			return BlockDataHandler.stateToBlockDefMap.get(world.getBlockState(pos));
		else
			return BlockDataHandler.defaultBlockDef;
	}
	
	public static boolean sameBlock(final IBlockState state1, final IBlockState state2)
	{
		return BlockDataHandler.sameBlockMap.get(state1).contains(state2);
	}
	
	public static boolean sameBlock(final World world, final int x, final int y, final int z, final int x2, final int y2, final int z2)
	{
		return BlockDataHandler.sameBlock(world.getBlockState(new BlockPos(x, y, z)), world.getBlockState(new BlockPos(x2, y2, z2)));
	}
	
	public static void addSameBlockList(final IBlockState state, final HashSet<IBlockState> list)
	{
		final HashSet<IBlockState> mapList = BlockDataHandler.sameBlockMap.get(state);
		
		mapList.addAll(list);
	}
	
	public static void setupSameBlockMap()
	{
		BlockDataHandler.sameBlockMap.clear();
		//TODO could perhaps do something to create the map with the perfect size, slightly increasing its efficiency
		final Iterator<Block> iterator = Block.REGISTRY.iterator();
		
		while (iterator.hasNext())
		{
			final Block block = iterator.next();
			
			final ImmutableList<IBlockState> stateList = block.getBlockState().getValidStates();
			
			for (final IBlockState state : stateList)
			{
				BlockDataHandler.sameBlockMap.put(state, new HashSet<IBlockState>());
			}
		}
		//TODO properly fill the lists in the map
	}
}