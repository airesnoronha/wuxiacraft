package com.airesnor.wuxiacraft.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleportationUtil extends Teleporter{

    private final WorldServer worldIn;
    private double x, y, z;
    private float playerRotationYaw, playerRotationPitch;

    public TeleportationUtil(WorldServer worldIn, double x, double y, double z, float playerRotationYaw, float playerRotationPitch) {
        super(worldIn);
        this.worldIn = worldIn;
        this.x = x;
        this.y = y;
        this.z = z;
        this.playerRotationYaw = playerRotationYaw;
        this.playerRotationPitch = playerRotationPitch;
    }

    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw) {
        entityIn.setLocationAndAngles(x, y, z, playerRotationYaw, playerRotationPitch);
        entityIn.motionX = entityIn.motionY = entityIn.motionZ = 0f;
    }

    @Override
    public void removeStalePortalLocations(long worldTime) {}

    public static void teleportPlayerToDimension(EntityPlayerMP playerMP, int dimensionID, double x, double y, double z, float playerRotationYaw, float playerRotationPitch) {
        MinecraftServer minecraftServer = playerMP.getEntityWorld().getMinecraftServer();
        WorldServer worldServer = minecraftServer.getWorld(dimensionID);

        if (worldServer == null || minecraftServer == null) {
            TextComponentString message = new TextComponentString("That dimension does not exist!");
            message.getStyle().setColor(TextFormatting.RED);
            playerMP.sendMessage(message);
        }
        worldServer.getMinecraftServer().getPlayerList().transferPlayerToDimension(playerMP, dimensionID, new TeleportationUtil(worldServer, x, y, z, 0f, 0f));
        playerMP.setPositionAndRotation(x, y, z, playerRotationYaw, playerRotationPitch);
    }
}
