package com.bloodnbonesgaming.blockphysics;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BClient
{


	/*public static void loadWorld(GuiScreen screen, String var2, String var3 )
    {
    	BlockPhysicsUtil.resetConfig();
		if (BlockPhysicsUtil.loadConfig(new File(BlockPhysics.gameDir, "saves"+File.separator +var2 +File.separator+BlockPhysics.conffile))) Minecraft.getMinecraft().launchIntegratedServer(var2, var3, (WorldSettings)null);
		else Minecraft.getMinecraft().displayGuiScreen(new BGui(screen, var2, var3, (WorldSettings)null, false));
    }*/

	public static void renderBlockSandFalling(final RenderBlocks sandRenderBlocks, final Block par1Block, final World par2World, final int par3, final int par4, final int par5, final int par6)
	{
		if ( !par1Block.renderAsNormalBlock() )
		{
			if ( par1Block.getRenderType() == 18) {
				sandRenderBlocks.renderBlockSandFalling(par1Block, par2World, par3, par4, par5, par6);
			} else {
				sandRenderBlocks.renderBlockAsItem(par1Block, par6, 0.8F);
			}
		}
		else
		{
			int colormult;
			if (par1Block instanceof BlockLeaves) {
				colormult = BClient.colorLeaves(par2World, par3, par5, par6);
			} else {
				colormult = par1Block.colorMultiplier(par2World, par3, par4, par5);
			}

			final float cm1 = (colormult >> 16 & 255) / 255.0F;
			final float cm2 = (colormult >> 8 & 255) / 255.0F;
			final float cm3 = (colormult & 255) / 255.0F;

			sandRenderBlocks.enableAO = false;
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

			final Tessellator var8 = Tessellator.instance;

			var8.setBrightness(par1Block.getMixedBrightnessForBlock(par2World, par3, par4, par5));
			var8.startDrawingQuads();

			var8.setColorOpaque_F(var17, var20, var23);
			sandRenderBlocks.renderFaceYNeg(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getIcon(0, par6));

			var8.setColorOpaque_F(var14, var15, var16);
			sandRenderBlocks.renderFaceYPos(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getIcon(1, par6));

			var8.setColorOpaque_F(var18, var21, var24);
			sandRenderBlocks.renderFaceZNeg(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getIcon(2, par6));

			var8.setColorOpaque_F(var18, var21, var24);
			sandRenderBlocks.renderFaceZPos(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getIcon(3, par6));

			var8.setColorOpaque_F(var19, var22, var25);
			sandRenderBlocks.renderFaceXNeg(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getIcon(4, par6));

			var8.setColorOpaque_F(var19, var22, var25);
			sandRenderBlocks.renderFaceXPos(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getIcon(5, par6));

			var8.draw();
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
				{
					final int var11 = par1World.getBiomeGenForCoords(par2 + var10, par4 + var9).getBiomeFoliageColor(par2 + var10, 64, par4 + var9);
					var6 += (var11 & 16711680) >> 16;
				var7 += (var11 & 65280) >> 8;
				var8 += var11 & 255;
				}
			}

			return (var6 / 9 & 255) << 16 | (var7 / 9 & 255) << 8 | var8 / 9 & 255;
		}
	}

	public static boolean cancelRender(final EntityFallingBlock par1EntityFallingSand)
	{
		//if ( par1EntityFallingSand.fallTime < 4 || par1EntityFallingSand.dead < 4 )
		//{
		if (par1EntityFallingSand.worldObj.getBlock(MathHelper.floor_double(par1EntityFallingSand.posX), MathHelper.floor_double(par1EntityFallingSand.posY), MathHelper.floor_double(par1EntityFallingSand.posZ)) != Blocks.air ) {
			return true;
		}
		//}
		return false;
	}
}