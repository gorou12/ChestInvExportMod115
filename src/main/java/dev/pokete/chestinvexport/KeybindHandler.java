package dev.pokete.chestinvexport;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.awt.event.KeyEvent;

public class KeybindHandler {
    static KeyBinding record_key;
    static KeyBinding quit_key;

    static void init() {
        record_key = new KeyBinding("toggle Record and Save", KeyEvent.VK_M, "ChestInventoryExportMod");
        quit_key = new KeyBinding("escape Recording mode", KeyEvent.VK_N, "ChestInventoryExportMod");
        ClientRegistry.registerKeyBinding(record_key);
        ClientRegistry.registerKeyBinding(quit_key);
    }
}
