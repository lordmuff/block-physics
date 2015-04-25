package com.bloodnbonesgaming.blockphysics.asm;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import squeek.asmhelper.com.bloodnbonesgaming.lib.ObfHelper;

import com.bloodnbonesgaming.blockphysics.ModInfo;
import com.bnbgaming.lib.util.LogHelper;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.DependsOn;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@DependsOn("BNBGamingLibCore")
@MCVersion("1.7.10")
@SortingIndex(1002)
@TransformerExclusions({"com.bloodnbonesgaming.blockphysics.asm", "com.bloodnbonesgaming.blockphysics.BlockPhysics"})
public class ASMPlugin extends AccessTransformer implements IFMLLoadingPlugin
{

	public static final Logger log = LogManager.getLogger("BlockPhysicsCore");

	public ASMPlugin() throws IOException{
		super(ModInfo.MODID.toLowerCase()+"_at.cfg");
	}

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
	public void injectData(final Map<String, Object> data)
	{
		ObfHelper.setObfuscated((Boolean) data.get("runtimeDeobfuscationEnabled"));
		LogHelper.class.getName();
	}

	@Override
	public String getAccessTransformerClass()
	{
		return this.getClass().getName();
	}
}