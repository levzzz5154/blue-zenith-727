package cat.ui.arraylist;

import cat.events.impl.Render2DEvent;
import cat.module.Module;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;

public interface IArraylistRenderer {
    void doRender(final Render2DEvent event, final ArrayList<Module> modules, ScaledResolution resolution, float margin, boolean shadow, int opacity, boolean textShadow);
}
