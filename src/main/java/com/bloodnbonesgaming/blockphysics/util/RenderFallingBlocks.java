package com.bloodnbonesgaming.blockphysics.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.World;

public class RenderFallingBlocks
{
	public static void renderBlockSandFalling(RenderBlocks renderBlocks, Block block, World world, int x, int y, int z, int meta)
	{
		if (!block.renderAsNormalBlock())
		{
			if (block.getRenderType() == 18)
			{
				renderBlocks.renderBlockSandFalling(block, world, x, y, z, meta);
			}
			else
			{
				renderBlocks.renderBlockAsItem(block, meta, 0.8F);
			}
		}
		else
		{
			int colormult;
			if (block instanceof BlockLeaves)
			{
				colormult = colorLeaves(world, x, z, meta);
			}
			else
			{
				colormult = block.colorMultiplier(world, x, y, z);
			}

			float cm1 = (float) (colormult >> 16 & 255) / 255.0F;
			float cm2 = (float) (colormult >> 8 & 255) / 255.0F;
			float cm3 = (float) (colormult & 255) / 255.0F;

			renderBlocks.enableAO = false;
			boolean var9 = false;
			float var10 = 0.5F;
			float var11 = 1.0F;
			float var12 = 0.8F;
			float var13 = 0.6F;
			float var14 = var11 * cm1;
			float var15 = var11 * cm2;
			float var16 = var11 * cm3;
			float var17 = var10 * cm1;
			float var18 = var12 * cm1;
			float var19 = var13 * cm1;
			float var20 = var10 * cm2;
			float var21 = var12 * cm2;
			float var22 = var13 * cm2;
			float var23 = var10 * cm3;
			float var24 = var12 * cm3;
			float var25 = var13 * cm3;

			Tessellator tesselator = Tessellator.instance;

			tesselator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
			tesselator.startDrawingQuads();

			tesselator.setColorOpaque_F(var17, var20, var23);
			renderBlocks.renderFaceYNeg(block, -0.5D, -0.5D, -0.5D, block.getIcon(0, meta));

			tesselator.setColorOpaque_F(var14, var15, var16);
			renderBlocks.renderFaceYPos(block, -0.5D, -0.5D, -0.5D, block.getIcon(1, meta));

			tesselator.setColorOpaque_F(var18, var21, var24);
			renderBlocks.renderFaceZNeg(block, -0.5D, -0.5D, -0.5D, block.getIcon(2, meta));

			tesselator.setColorOpaque_F(var18, var21, var24);
			renderBlocks.renderFaceZPos(block, -0.5D, -0.5D, -0.5D, block.getIcon(3, meta));

			tesselator.setColorOpaque_F(var19, var22, var25);
			renderBlocks.renderFaceXNeg(block, -0.5D, -0.5D, -0.5D, block.getIcon(4, meta));

			tesselator.setColorOpaque_F(var19, var22, var25);
			renderBlocks.renderFaceXPos(block, -0.5D, -0.5D, -0.5D, block.getIcon(5, meta));

			tesselator.draw();
		}
	}

	public static int colorLeaves(World par1World, int par2, int par4, int var5)
	{

		if ((var5 & 3) == 1)
		{
			return ColorizerFoliage.getFoliageColorPine();
		}
		else if ((var5 & 3) == 2)
		{
			return ColorizerFoliage.getFoliageColorBirch();
		}
		else
		{
			int var6 = 0;
			int var7 = 0;
			int var8 = 0;

			for (int var9 = -1; var9 <= 1; ++var9)
			{
				for (int var10 = -1; var10 <= 1; ++var10)
				{//64 Is a guess :P
					int var11 = par1World.getBiomeGenForCoords(par2 + var10, par4 + var9).getBiomeFoliageColor(par2 + var10, 64, par4 + var9);
					var6 += (var11 & 16711680) >> 16;
					var7 += (var11 & 65280) >> 8;
					var8 += var11 & 255;
				}
			}

			return (var6 / 9 & 255) << 16 | (var7 / 9 & 255) << 8 | var8 / 9 & 255;
		}
	}
}