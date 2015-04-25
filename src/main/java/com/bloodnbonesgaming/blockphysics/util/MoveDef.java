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

	public MoveDef(final String id1, final int moveType1, final int slideChance1, final boolean ceiling1, final int smallArc1, final int bigArc1, final int corbel1, final int nCorbel1, final int hanging1, final int attached1, final int floatingRadius1, final String floatingBlock1, final boolean branch1)
	{
		this.id = id1;
		this.movetype = moveType1;
		this.slidechance = slideChance1;
		this.ceiling = ceiling1;
		this.smallarc = smallArc1;
		this.bigarc = bigArc1;
		this.corbel = corbel1;
		this.ncorbel = nCorbel1;
		this.hanging = hanging1;
		this.attached = attached1;
		this.floatingRadius = floatingRadius1;
		this.floatingBlock = floatingBlock1;
		//floatingMeta = floatingMeta1;
		this.branch = branch1;
	}

	public MoveDef(final String id1)
	{
		this.id = id1;
		this.movetype = 0;
		this.slidechance = 0;
		this.ceiling	= false;
		this.smallarc = 0;
		this.bigarc = 0;
		this.corbel = 0;
		this.ncorbel = 0;
		this.hanging = 0;
		this.attached = 0;
		this.floatingRadius = 0;
		this.floatingBlock = null;
		//floatingMeta = 0;
		this.branch = false;
	}
}