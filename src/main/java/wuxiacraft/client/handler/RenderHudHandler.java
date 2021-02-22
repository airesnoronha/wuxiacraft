package wuxiacraft.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.util.MathUtils;

@OnlyIn(Dist.CLIENT)
public class RenderHudHandler {

    private static ResourceLocation HEALTH_BAR = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/health_bar.png");

    @SubscribeEvent
    public void onPreRenderHud(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
            assert Minecraft.getInstance().player != null;
            event.setCanceled(true);
            drawCustomHealthBar(event.getMatrixStack(), event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight());
        }
    }

    @SubscribeEvent
    public void onRenderHud(RenderGameOverlayEvent.Post event) {
        //if(event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE);
        //drawHudElements(event.getMatrixStack(), event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight());
    }

    private static void drawHudElements(MatrixStack stack, int width, int height) {
        assert Minecraft.getInstance().player != null;
        ICultivation cultivation = Cultivation.get(Minecraft.getInstance().player);

        String text = String.format("HP: %d/%d", (int) cultivation.getHP(), (int) cultivation.getFinalModifiers().maxHealth);
        Minecraft.getInstance().ingameGUI.getFontRenderer().drawString(stack, text, 30, 30, 0xFFAA00);
    }

    private void drawCustomHealthBar(MatrixStack stack, int resX, int resY) {
        Minecraft mc = Minecraft.getInstance();
        int i = resX / 2 - 91;
        int j = resY - ForgeIngameGui.left_height;
        //health
        mc.getTextureManager().bindTexture(HEALTH_BAR);
        mc.ingameGUI.blit(stack, i, j, 81, 9, 0, 0, 81, 9, 81, 18);
        assert mc.player != null;
        double max_hp = Cultivation.get(mc.player).getFinalModifiers().maxHealth;
        double hp = Cultivation.get(mc.player).getHP();
        int fill = (int) Math.min(Math.ceil((hp / max_hp) * 81), 81);
        mc.ingameGUI.blit(stack, i, j, fill, 9, 0, 9, fill, 9, 81, 18);
        //text
        String life = getShortHealthAmount((int) hp) + "/" + getShortHealthAmount((int) max_hp);
        int width = mc.fontRenderer.getStringWidth(life);
        mc.fontRenderer.drawString(stack, life, (i + (81f - width) / 2), j + 1, 0xFFFFFF);
        mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
        ForgeIngameGui.left_height += 11;
    }

    public static String getShortHealthAmount(int amount) {
        String value = "";
        if (amount < 0) {
            value += "-";
        }
        amount = Math.abs(amount);
        if (amount < 1000) {
            value += amount;
        } else if (amount < 10000) {
            float mills = amount / 1000f;
            value += String.format("%.1fk", mills);
        } else if (amount < 100000) {
            float mills = amount / 1000f;
            value += String.format("%.0fk", mills);
        } else if (amount < 1000000) {
            float mills = amount / 1000000f;
            value += String.format("%.2fM", mills);
        } else if (amount < 10000000) {
            float mills = amount / 1000000f;
            value += String.format("%.1fM", mills);
        } else if (amount < 100000000) {
            float mills = amount / 1000000f;
            value += String.format("%.0fM", mills);
        } else if (amount < 10000000000.0) {
            float mills = amount / 1000000000f;
            value += String.format("%.2fG", mills);
        } else if (amount < 100000000000.0) {
            float mills = amount / 1000000000f;
            value += String.format("%.1fG", mills);
        } else if (amount < 1000000000000.0) {
            float mills = amount / 1000000000f;
            value += String.format("%.0fG", mills);
        } else if (amount < 10000000000000.0) {
            float mills = amount / 1000000000000f;
            value += String.format("%.2fT", mills);
        } else if (amount < 100000000000000.0) {
            float mills = amount / 1000000000000f;
            value += String.format("%.1fT", mills);
        }
        return value;
    }

}
