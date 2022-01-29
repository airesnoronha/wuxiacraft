package wuxiacraft.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;
import wuxiacraft.networking.OpenScreenMessage;
import wuxiacraft.networking.WuxiaPacketHandler;

import java.util.HashMap;

public class InputHandler {
	public static final int OPEN_INTROSPECTION = 0;

	public static HashMap<Integer, KeyMapping> mappings;

	public static void registerKeyBindings() {
		mappings = new HashMap<>();
		mappings.put(OPEN_INTROSPECTION, new KeyMapping("wuxiacraft.key.introspection", GLFW.GLFW_KEY_K, "wuxiacraft.category.main"));
		for(var mapping : mappings.values()) {
			ClientRegistry.registerKeyBinding(mapping);
		}
	}

	@SubscribeEvent
	public static void onKeyPressed(InputEvent.KeyInputEvent event) {
		if (mappings.get(OPEN_INTROSPECTION).consumeClick()) {
			WuxiaPacketHandler.INSTANCE.sendToServer(new OpenScreenMessage(OpenScreenMessage.ScreenType.INTROSPECTION));
		}
	}

}
