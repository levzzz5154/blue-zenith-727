package cat.module.modules.player;

import cat.events.Subscriber;
import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.IntegerValue;
import cat.util.MillisTimer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public final class ChestStealer extends Module {

    private final IntegerValue delay = new IntegerValue("Delay", 50, 0, 300, 10, true, null);
    private final MillisTimer timer = new MillisTimer();
    public ChestStealer() {
        super("ChestStealer", "", ModuleCategory.PLAYER, "stealer");
    }

    @Subscriber
    public void onUpdate(UpdatePlayerEvent event) {
        if(mc.currentScreen != null) {
            if(mc.currentScreen instanceof GuiChest) {
                int i = getValidItem((GuiChest)mc.currentScreen);
                if(i != -1 && timer.hasTimeReached(delay.get())) {
                    mc.playerController.windowClick(((GuiChest) mc.currentScreen).inventorySlots.windowId, i, 0, 1, mc.thePlayer);
                    timer.reset();
                } else mc.thePlayer.closeScreen();
            }
        }
    }

    private int getValidItem(GuiChest gui) {
        for(int i = 0; i < gui.upperChestInventory.getSizeInventory(); i++) {
            if(gui.lowerChestInventory.getStackInSlot(i) != null) {
                return i;
            }
        }
        return -1;
    }

    private boolean hasItemOfType(ItemStack item) {
        return item.getItem() instanceof ItemSword;
    }

    @Override
    public String getTag() {
        return this.delay.get().toString();
    }
}
