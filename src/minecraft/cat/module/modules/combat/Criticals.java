package cat.module.modules.combat;

import cat.events.EventType;
import cat.events.impl.AttackEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.ui.notifications.NotificationManager;
import cat.ui.notifications.NotificationType;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.EntityLivingBase;

public class Criticals extends Module {

    public Criticals() {
        super("Criticals", "", ModuleCategory.COMBAT);
    }

    @Subscribe
    public void onAttack(AttackEvent event) {
        if(event.type == EventType.PRE && event.target instanceof EntityLivingBase) {
            NotificationManager.publish("attackin nigga " + event.target.getDisplayName().getFormattedText(), NotificationType.INFO, 2000);
        }
    }
}
