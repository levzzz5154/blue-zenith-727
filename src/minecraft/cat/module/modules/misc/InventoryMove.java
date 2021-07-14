package cat.module.modules.misc;

import cat.events.Subscriber;
import cat.events.impl.UpdateEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class InventoryMove extends Module {
    public InventoryMove() {
        super("InvMove", "", ModuleCategory.MISC, "InventoryMove");
    }
    @Subscriber
    public void onUpdate(UpdateEvent event){
        if(mc.currentScreen instanceof GuiChat){
            return;
        }
        set(mc.gameSettings.keyBindForward);
        set(mc.gameSettings.keyBindBack);
        set(mc.gameSettings.keyBindRight);
        set(mc.gameSettings.keyBindLeft);
        set(mc.gameSettings.keyBindJump);
        set(mc.gameSettings.keyBindSprint);
    }
    private void set(KeyBinding key){
        key.pressed = GameSettings.isKeyDown(key);
    }
}
