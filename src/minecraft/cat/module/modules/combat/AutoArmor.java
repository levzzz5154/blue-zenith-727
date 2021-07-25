package cat.module.modules.combat;

import cat.events.Subscriber;
import cat.events.impl.UpdatePlayerEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.IntegerValue;
import cat.util.ClientUtils;
import cat.util.MillisTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

/**
 * @Author Fxy
 */
@SuppressWarnings("SpellCheckingInspection")
public class AutoArmor extends Module {
    private final IntegerValue maxDelay = new IntegerValue("MaxDelay", 400, 0, 600, 0, true, (old_, new_) -> {updateTime(); return new_ < getMinDelay().get() ? old_ : new_;}, null);
    private final IntegerValue minDelay = new IntegerValue("MinDelay", 400, 0, 600, 0, true, (old_, new_) -> {updateTime(); return new_ > maxDelay.get() ? old_ : new_;}, null);

    private IntegerValue getMinDelay() {
        return minDelay;
    }

    private final int[] chestplate = new int[]{311, 307, 315, 303, 299};
    private final int[] leggings = new int[]{312, 308, 316, 304, 300};
    private final int[] boots = new int[]{313, 309, 317, 305, 301};
    private final int[] helmet = new int[]{310, 306, 314, 302, 298};
    private final boolean best;
    private final MillisTimer timer;
    private long waitTime;
    public AutoArmor() {
        super("AutoArmor", "", ModuleCategory.COMBAT, Keyboard.KEY_L, "autoArmor", "aut", "AutoArmor");
        best = true;
        timer = new MillisTimer();
        updateTime();
    }
    @Override
    public void onEnable() {
        updateTime();
    }
    @Subscriber
    public void onEvent(UpdatePlayerEvent e) {
        if(mc.thePlayer == null){
            return;
        }
        autoArmor();
        betterArmor();
    }
    public void autoArmor() {
        if(best)
            return;
        int item = -1;
        if(timer.hasTimeReached(waitTime)) {
            if(mc.thePlayer.inventory.armorInventory[0] == null) {
                int[] boots;
                int length = (boots = this.boots).length;
                for(int i =0; i < length; i++) {
                    int id = boots[i];
                    if(getItem(id) != -1) {
                        item = getItem(id);
                        break;
                    }
                }
            }
            if(mc.thePlayer.inventory.armorInventory[1] == null) {
                int[] leggings;
                int length = (leggings = this.leggings).length;
                for(int i = 0; i < length; i++) {
                    int id = leggings[i];
                    if(getItem(id) != -1) {
                        item = getItem(id);
                        break;
                    }
                }
            }
            if(mc.thePlayer.inventory.armorInventory[2] == null) {
                int[] chestplate;
                int length = (chestplate = this.chestplate).length;
                for(int i = 0; i < length; i++) {
                    int id = chestplate[i];
                    if(getItem(id) != -1) {
                        item = getItem(id);
                        break;
                    }
                }
            }
            if(mc.thePlayer.inventory.armorInventory[3] == null) {
                int[] helmet;
                int length = (helmet = this.helmet).length;
                for(int i = 0; i < length; i++) {
                    int id = helmet[i];
                    if(getItem(id) != -1) {
                        item = getItem(id);
                        break;
                    }
                }
            }
            if(item != -1) {
                mc.playerController.windowClick(0, item, 0, 1, mc.thePlayer);
            }
            updateTime();
            timer.reset();
        }
    }

    public void betterArmor() {
        if(!best)
            return;
        if(timer.hasTimeReached(waitTime) && (mc.thePlayer.openContainer == null || mc.thePlayer.openContainer.windowId == 0)) {
            boolean switchArmor = false;
            int item = -1;
            int[] array;
            int i;
            if(mc.thePlayer.inventory.armorInventory[0] == null) {
                int j = (array = this.boots).length;
                for(i = 0; i < j; i++) {
                    int id = array[i];
                    if(getItem(id) != -1) {
                        item = getItem(id);
                        break;
                    }
                }
            }
            if (isBetterArmor(0, this.boots)) {
                item = 8;
                switchArmor = true;
            }
            if(mc.thePlayer.inventory.armorInventory[3] == null) {
                int j = (array = this.helmet).length;
                for(i = 0; i < j; i++) {
                    int id = array[i];
                    if(getItem(id) != -1) {
                        item = getItem(id);
                        break;
                    }
                }
            }
            if (isBetterArmor(3, this.helmet)) {
                item = 5;
                switchArmor = true;
            }
            if(mc.thePlayer.inventory.armorInventory[1] == null) {
                int j = (array = this.leggings).length;
                for(i = 0; i < j; i++) {
                    int id = array[i];
                    if(getItem(id) != -1) {
                        item = getItem(id);
                        break;
                    }
                }
            }
            if (isBetterArmor(1, this.leggings)) {
                item = 7;
                switchArmor = true;
            }
            if(mc.thePlayer.inventory.armorInventory[2] == null) {
                int j = (array = this.chestplate).length;
                for(i = 0; i < j; i++) {
                    int id = array[i];
                    if(getItem(id) != -1) {
                        item = getItem(id);
                        break;
                    }
                }
            }
            if (isBetterArmor(2, this.chestplate)) {
                item = 6;
                switchArmor = true;
            }
            boolean b = false;
            ItemStack[] stackArray;
            int k = (stackArray = mc.thePlayer.inventory.mainInventory).length;
            for(int j = 0; j < k; j++) {
                ItemStack stack = stackArray[j];
                if(stack == null) {
                    b = true;
                    break;
                }
            }
            switchArmor = switchArmor && !b;
            if(item != -1) {
                mc.playerController.windowClick(0, item, 0, switchArmor ? 4 : 1, mc.thePlayer);
            }
            updateTime();
            timer.reset();
        }
    }
    public static boolean isBetterArmor(int slot, int[] armorType) {
        if(Minecraft.getMinecraft().thePlayer.inventory.armorInventory[slot] != null) {
            int currentIndex = 0;
            int invIndex = 0;
            int finalCurrentIndex = -1;
            int finalInvIndex = -1;
            int[] array;
            int j = (array = armorType).length;
            for(int i = 0; i < j; i++) {
                int armor = array[i];
                if(Item.getIdFromItem(Minecraft.getMinecraft().thePlayer.inventory.armorInventory[slot].getItem()) == armor) {
                    finalCurrentIndex = currentIndex;
                    break;
                }
                currentIndex++;
            }
            j = (array = armorType).length;
            for(int i = 0; i < j; i++) {
                int armor = array[i];
                if(getItem(armor) != -1) {
                    finalInvIndex = invIndex;
                    break;
                }
                invIndex++;
            }
            if(finalInvIndex > -1)
                return finalInvIndex < finalCurrentIndex;
        }
        return false;
    }

    public static int getItem(int id) {
        for(int i = 9; i < 45; i++) {
            ItemStack item = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
            if(item != null && Item.getIdFromItem(item.getItem()) == id)
                return i;
        }
        return -1;
    }

    private void updateTime(){
        waitTime = ClientUtils.getRandomInt(minDelay.get(), maxDelay.get());
    }
}