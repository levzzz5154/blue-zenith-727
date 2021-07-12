package cat.module.modules.combat;

import cat.events.Subscriber;
import cat.events.impl.UpdateEvent;
import cat.module.Module;
import cat.module.ModuleCategory;

public class TestMod extends Module {
    public TestMod() {
        super("Tes", "Hi", ModuleCategory.RENDER, 0);
    }

    @Subscriber
    public void exec(UpdateEvent event) {
        System.out.println("hi");
    }
}
