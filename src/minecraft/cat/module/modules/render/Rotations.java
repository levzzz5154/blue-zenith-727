package cat.module.modules.render;

import cat.BlueZenith;
import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.ListValue;
import cat.module.value.types.StringValue;
import cat.util.ClientUtils;

public class Rotations extends Module {

    private final StringValue clientName = new StringValue("Client name", BlueZenith.name, true, null);
    private final ListValue testValue = new ListValue("hi", true, "option 1", "option 2", "option 3");
    public Rotations() {
        super("Rotations", "", ModuleCategory.RENDER, "rotations", "rot");
    }
    public float yaw = 0;
    public float pitch = 0;
    public float prevYaw = 0;
    public float prevPitch = 0;

    @Override
    public void onEnable() {
        testValue.getSelectedOptions().forEach(ClientUtils::fancyMessage);
    }
}
