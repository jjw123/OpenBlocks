package openblocks.client;

import java.io.File;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import openblocks.OpenBlocks;
import openblocks.OpenBlocks.Config;
import openblocks.OpenBlocks.Gui;
import openblocks.client.gui.GuiLightbox;
import openblocks.client.renderer.BlockRenderingHandler;
import openblocks.client.renderer.entity.EntityGhostRenderer;
import openblocks.client.renderer.entity.EntityPlayerRenderer;
import openblocks.client.renderer.tileentity.TileEntityFlagRenderer;
import openblocks.client.renderer.tileentity.TileEntityGraveRenderer;
import openblocks.client.renderer.tileentity.TileEntityGuideRenderer;
import openblocks.client.renderer.tileentity.TileEntityLightboxRenderer;
import openblocks.client.renderer.tileentity.TileEntityTankRenderer;
import openblocks.client.renderer.tileentity.TileEntityTargetRenderer;
import openblocks.common.CommonProxy;
import openblocks.common.PlayerDeathHandler;
import openblocks.common.container.ContainerLightbox;
import openblocks.common.entity.EntityGhost;
import openblocks.common.tileentity.TileEntityFlag;
import openblocks.common.tileentity.TileEntityGrave;
import openblocks.common.tileentity.TileEntityGuide;
import openblocks.common.tileentity.TileEntityLightbox;
import openblocks.common.tileentity.TileEntityTarget;
import openblocks.common.tileentity.tank.TileEntityTank;
import openblocks.common.tileentity.tank.TileEntityTankValve;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

	public ClientProxy() {
		MinecraftForge.EVENT_BUS.register(new SoundLoader());
	}

	public void registerRenderInformation() {

		OpenBlocks.renderId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new BlockRenderingHandler());

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGuide.class, new TileEntityGuideRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLightbox.class, new TileEntityLightboxRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTarget.class, new TileEntityTargetRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGrave.class, new TileEntityGraveRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFlag.class, new TileEntityFlagRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTank.class, new TileEntityTankRenderer());
		
		RenderingRegistry.registerEntityRenderingHandler(EntityGhost.class, new EntityGhostRenderer());

		attachPlayerRenderer();

		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}

	private void attachPlayerRenderer() {
		if (Config.hookPlayerRenderer) {
			// Get current renderer and check that it's Mojangs
			Render render = (Render)RenderManager.instance.entityRenderMap.get(EntityPlayer.class);
			if (render.getClass().equals(net.minecraft.client.renderer.entity.RenderPlayer.class)) {
				EntityPlayerRenderer playerRenderer = new EntityPlayerRenderer();
				playerRenderer.setRenderManager(RenderManager.instance);
				RenderManager.instance.entityRenderMap.put(EntityPlayer.class, playerRenderer);
			}
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if ((world instanceof WorldClient)) {
			TileEntity tile = world.getBlockTileEntity(x, y, z);
			if (ID == Gui.Lightbox.ordinal()) { return new GuiLightbox(new ContainerLightbox(player.inventory, (TileEntityLightbox)tile)); }
		}
		return null;
	}

	@Override
	public File getWorldDir(World world) {
		return new File(OpenBlocks.getBaseDir(), "saves/"
				+ world.getSaveHandler().getWorldDirectoryName());
	}

	/**
	 * Is this the server
	 * 
	 * @return true if this is the server
	 */
	public boolean isServer() {
		return false;
	}

	/**
	 * Is this the client
	 * 
	 * @return true if this is the client
	 */
	public boolean isClient() {
		return true;
	}
}
