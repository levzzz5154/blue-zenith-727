package cat.module.modules.fun;

import cat.events.Subscriber;
import cat.events.impl.UpdateEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BooleanValue;
import cat.module.value.types.IntegerValue;
import cat.module.value.types.ModeValue;
import cat.module.value.types.StringValue;
import cat.util.MillisTimer;
import cat.util.PacketUtil;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.apache.commons.lang3.RandomStringUtils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;

public class Spammer extends Module {
    public Spammer() {
        super("Spammer", "", ModuleCategory.FUN);
    }
    //private final BooleanValue invis = new BooleanValue("Invis Characters", true, true, null);
    private final IntegerValue delay = new IntegerValue("Delay (ms)", 3000, 10, 10000, 10, true, null);
    private final StringValue text = new StringValue("Text", "Buy Blue Zenith", true, null);
    private final ModeValue mode = new ModeValue("Bypass", "Random", true, null, "Random", "Invisible", "None");
    private final ModeValue randomMode = new ModeValue("Placing", "First", false, __ -> mode.is("Random"), "First", "Last");
    private final IntegerValue randomLength = new IntegerValue("String length", 5, 1, 20, 1, false, __ -> randomMode.isVisible());
    private final BooleanValue reset = new BooleanValue("Reset Text", true, true, (a1, a2) -> {text.set(""); return a1;}, null);
    private final BooleanValue paste = new BooleanValue("From Clipboard", true, true, (a1, a2) -> {if(!a2) fromClipboard(); return a1;}, null);

    private final MillisTimer timer = new MillisTimer();

    @Subscriber
    public void spam(UpdateEvent event) {
        if (timer.hasTimeReached(delay.get())) {
           if(mode.is("Random")) switch (randomMode.get()) {
                case "First":
                    PacketUtil.send(new C01PacketChatMessage("[" + (mode.is("Random") ? RandomStringUtils.randomAlphanumeric(randomLength.get()) : "") + "] " + text.get()));
                break;

                case "Last":
                   PacketUtil.send(new C01PacketChatMessage(text.get() + " [" + (mode.is("Random") ? RandomStringUtils.randomAlphanumeric(randomLength.get()) : "") + "]"));
                break;
            } else PacketUtil.send(new C01PacketChatMessage(text.get()));
            timer.reset();
        }
    }

    private void fromClipboard() {
        try {
            text.set(String.valueOf(Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor)));
        } catch(Exception ignored) { }
    }
}
