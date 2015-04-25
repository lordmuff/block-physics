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

	public BlockDef(final String id1, final String movedefs1, final String movedefs2, final int movenum1, final int moveflipnumber1, final int movechanger1, final int supportingblock1, final int fragile1, final boolean trapping1, final int pushtype1, final boolean randomtick1, final int tickrate1, final int placedmove1, final int mass1, final int strength1)
	{
		this.id = id1;
		this.movedefs[0] = movedefs1;
		this.movedefs[1] = movedefs2;
		this.movenum = movenum1;
		this.moveflipnumber = moveflipnumber1;
		this.movechanger = movechanger1;
		this.supportingblock = supportingblock1;
		this.fragile = fragile1;
		this.trapping = trapping1;
		this.pushtype = pushtype1;
		this.randomtick = randomtick1;
		this.tickrate = tickrate1;
		this.placedmove = placedmove1;
		this.mass = mass1;
		this.strength = strength1;
	}

	public BlockDef(final String id1)
	{
		this.id = id1;
		this.movenum = 0;
		this.movedefs[0] = "default";
		this.movedefs[1] = "default";
		this.moveflipnumber = 0;
		this.movechanger = 0;
		this.supportingblock = 0;
		this.fragile = 0;
		this.trapping = false;
		this.pushtype = 0;
		this.randomtick = false;
		this.tickrate = 10;
		this.placedmove = 0;
		this.mass = 1500;
		this.strength = 64000;
	}
}