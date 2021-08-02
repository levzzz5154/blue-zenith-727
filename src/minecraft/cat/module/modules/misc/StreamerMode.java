package cat.module.modules.misc;

import cat.events.Subscriber;
import cat.events.impl.TextEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.util.ColorUtil;

public class StreamerMode extends Module {
    public StreamerMode(){
        super("StreamerMode", "", ModuleCategory.MISC);
    }
    @Subscriber
    public void onTextEvent(TextEvent e){
        if(e.getText().contains("omegacraft.cl")){
            System.out.println(ColorUtil.getFirstColor(e.getText()));
            e.setText(ColorUtil.getFirstColor(e.getText()) + "sigmaclient.info");
        }
    }
}
