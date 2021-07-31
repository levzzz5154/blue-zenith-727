package cat.module.modules.misc;

import cat.events.Subscriber;
import cat.events.impl.TextEvent;
import cat.module.Module;
import cat.module.ModuleCategory;

public class StreamerMode extends Module {
    public StreamerMode(){
        super("StreamerMode", "", ModuleCategory.MISC);
    }
    @Subscriber
    public void onTextEvent(TextEvent e){
        if(e.getText().contains("omegacraft.cl")){
            e.setText("sigmaclient.info");
        }
    }
}
