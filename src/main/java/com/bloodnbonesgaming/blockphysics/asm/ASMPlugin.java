package com.bloodnbonesgaming.blockphysics.asm;

import java.util.Map;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ObfHelper;

import com.bnbgaming.lib.core.IBNBGamingPlugin;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@MCVersion("1.7.10")
@SortingIndex(1001)
@TransformerExclusions("com.bloodnbonesgaming.blockphysics.asm")
public class ASMPlugin implements IBNBGamingPlugin
{
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[]{ClassTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		ObfHelper.setObfuscated((Boolean) data.get("runtimeDeobfuscationEnabled"));
	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}