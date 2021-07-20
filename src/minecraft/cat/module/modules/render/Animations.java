package cat.module.modules.render;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.FloatValue;
import cat.module.value.types.ModeValue;

public class Animations extends Module {
    public ModeValue anim = new ModeValue("Animation", "SlideDown", true, null, "SlideDown");
    public FloatValue itemPosX = new FloatValue( "ItemPosX",0, -100, 100, 0, true, null);
    public FloatValue itemPosY = new FloatValue( "ItemPosY",0, -100, 100, 0, true, null);
    public FloatValue itemPosZ = new FloatValue( "ItemPosZ",0, -100, 100, 0, true, null);
    public FloatValue itemScale = new FloatValue( "ItemScale",1, 0.1f, 5, 0, true, null);
    public FloatValue translatePosX = new FloatValue( "TranslatePosX",0, -100, 100, 0, true, null);
    public FloatValue translatePosY = new FloatValue( "TranslatePosY",0, -100, 100, 0, true, null);
    public FloatValue translatePosZ = new FloatValue( "TranslatePosZ",0, -100, 100, 0, true, null);
    public FloatValue translateScale = new FloatValue( "TranslateScale",1, 0.1f, 5, 0, true, null);
    public Animations() {
        super("Animations", "", ModuleCategory.RENDER, "animations", "anim");
    }
}
