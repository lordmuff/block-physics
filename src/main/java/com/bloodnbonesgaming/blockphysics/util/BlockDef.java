package com.bloodnbonesgaming.blockphysics.util;

public class BlockDef
{
	public String id;
	public String[] movedefs = new String[2];
	public int movenum;				// 0,1,2
	public int moveflipnumber;		// 0-15
	public int movechanger;		//0 no 1 weather 2 mechanical 3 both
	public int supportingblock; // 0 no 1 yes
	public int	fragile;	// 0 no, 1 yes no drop, 2 yes drop
	public boolean trapping;	//0,1
	public int pushtype;      // 0 not pushable, 1 pushable by all, 2 pushable by piston, 3 pushable by explosion
	public boolean randomtick; //0,1
	public int tickrate;     //0-255
	public int placedmove;		//0,1
	public int mass;			//1-40000
	public int strength;		//0-64000

	public BlockDef(String id1, String movedefs1, String movedefs2, int movenum1, int moveflipnumber1, int movechanger1, int supportingblock1, int fragile1, boolean trapping1, int pushtype1, boolean randomtick1, int tickrate1, int placedmove1, int mass1, int strength1)
	{
		id = id1;
		movedefs[0] = movedefs1;
		movedefs[1] = movedefs2;
		movenum = movenum1;
		moveflipnumber = moveflipnumber1;
		movechanger = movechanger1;
		supportingblock = supportingblock1;
		fragile = fragile1;
		trapping = trapping1;
		pushtype = pushtype1;
		randomtick = randomtick1;
		tickrate = tickrate1;
		placedmove = placedmove1;
		mass = mass1;
		strength = strength1;
	}

	public BlockDef(String id1)
	{
		id = id1;
		movenum = 0;	
		movedefs[0] = "default";	
		movedefs[1] = "default";
		moveflipnumber = 0;
		movechanger = 0;
		supportingblock = 0;
		fragile = 0;
		trapping = false;
		pushtype = 0;
		randomtick = false;
		tickrate = 10;
		placedmove = 0;
		mass = 1500;
		strength = 64000;
	}
}