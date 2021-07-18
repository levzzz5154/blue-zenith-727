package cat.module.modules.fun;

import cat.events.Subscriber;
import cat.events.impl.UpdateEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.IntegerValue;
import cat.module.value.types.StringValue;
import cat.util.MillisTimer;
import cat.util.PacketUtil;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class Spammer extends Module {
    public Spammer() {
        super("Spammer", "", ModuleCategory.FUN);
    }

    private final StringValue text = new StringValue("Text", "Buy Blue Zenith", true, null);
    //private final BooleanValue invis = new BooleanValue("Invis Characters", true, true, null);
    private final IntegerValue delay = new IntegerValue("Delay (ms)", 3000, 10, 10000, 10, true, null);

    private final MillisTimer timer = new MillisTimer();

    @Subscriber
    public void spam(UpdateEvent event) {
        if (timer.hasTimeReached(delay.get())) {
            PacketUtil.send(new C01PacketChatMessage(text.get()));
            timer.reset();
        }
    }
}
