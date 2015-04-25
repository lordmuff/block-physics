package com.bloodnbonesgaming.blockphysics.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.World;

public class RenderFallingBlocks
{
	public static void renderBlockSandFalling(final RenderBlocks renderBlocks, final Block block, final World world, final int x, final int y, final int z, final int meta)
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
				colormult = RenderFallingBlocks.colorLeaves(world, x, z, meta);
			}
			else
			{
				colormult = block.colorMultiplier(world, x, y, z);
			}

			final float cm1 = (colormult >> 16 & 255) / 255.0F;
			final float cm2 = (colormult >> 8 & 255) / 255.0F;
			final float cm3 = (colormult & 255) / 255.0F;

			renderBlocks.enableAO = false;
			final boolean var9 = false;
			final float var10 = 0.5F;
			final float var11 = 1.0F;
			final float var12 = 0.8F;
			final float var13 = 0.6F;
			final float var14 = var11 * cm1;
			final float var15 = var11 * cm2;
			final float var16 = var11 * cm3;
			final float var17 = var10 * cm1;
			final float var18 = var12 * cm1;
			final float var19 = var13 * cm1;
			final float var20 = var10 * cm2;
			final float var21 = var12 * cm2;
			final float var22 = var13 * cm2;
			final float var23 = var10 * cm3;
			final float var24 = var12 * cm3;
			final float var25 = var13 * cm3;

			final Tessellator tesselator = Tessellator.instance;

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

	public static int colorLeaves(final World par1World, final int par2, final int par4, final int var5)
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
					final int var11 = par1World.getBiomeGenForCoords(par2 + var10, par4 + var9).getBiomeFoliageColor(par2 + var10, 64, par4 + var9);
					var6 += (var11 & 16711680) >> 16;
				var7 += (var11 & 65280) >> 8;
				var8 += var11 & 255;
				}
			}

			return (var6 / 9 & 255) << 16 | (var7 / 9 & 255) << 8 | var8 / 9 & 255;
		}
	}
}