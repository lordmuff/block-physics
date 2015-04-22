package com.bloodnbonesgaming.blockphysics.util;

public class MoveDef
{
	public String id;
	public int movetype;		// 0  static, 1 fall, 2 slide + fall, 3 drop
	public int slidechance;		// chance to slide 0-100
	public boolean ceiling;		// 0,1
	public int smallarc;		// 0-6
	public int bigarc;			// 0-6
	public int corbel;			// 0-6
	public int ncorbel;			// 0-6
	public int hanging;			// 0-10
	public int attached;		// 0-6
	public int floatingRadius;
	public String floatingBlock;
	//public int floatingMeta;
	public boolean branch;		// 0,1
	
	public MoveDef(String id1, int moveType1, int slideChance1, boolean ceiling1, int smallArc1, int bigArc1, int corbel1, int nCorbel1, int hanging1, int attached1, int floatingRadius1, String floatingBlock1, boolean branch1)
	{
		id = id1;
		movetype = moveType1;
		slidechance = slideChance1;
		ceiling = ceiling1;
		smallarc = smallArc1;
		bigarc = bigArc1;
		corbel = corbel1;
		ncorbel = nCorbel1;
		hanging = hanging1;
		attached = attached1;
		floatingRadius = floatingRadius1;
		floatingBlock = floatingBlock1;
		//floatingMeta = floatingMeta1;
		branch = branch1;
	}
	
	public MoveDef(String id1)
	{
		id = id1;
		movetype = 0;
		slidechance = 0;
		ceiling	= false;
		smallarc = 0;
		bigarc = 0;
		corbel = 0;
		ncorbel = 0;
		hanging = 0;
		attached = 0;
		floatingRadius = 0;
		floatingBlock = null;
		//floatingMeta = 0;
		branch = false;
	}
}