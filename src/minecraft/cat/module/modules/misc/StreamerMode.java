package cat.module.modules.misc;

import cat.events.impl.TextEvent;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BooleanValue;
import cat.module.value.types.StringValue;
import com.google.common.eventbus.Subscribe;

@SuppressWarnings("unused")
public class StreamerMode extends Module {

    public StreamerMode(){
        super("Streamer", "", ModuleCategory.MISC);
    }
    private final BooleanValue hideName = new BooleanValue("Hide name", true, true, null);
    private final StringValue fakeName = new StringValue("Fake name", "", true, __ -> hideName.get());

    @Subscribe
    public void onTextEvent(TextEvent e){
        String username = mc.session.getUsername();
        if(e.getText().contains("omegacraft.cl")){
            e.setText("sigmaclient.info");
        }
        if(hideName.get() && e.getText().contains(username)) {
            e.setText(e.getText().replace(username, fakeName.get()));
        }
     }
}
