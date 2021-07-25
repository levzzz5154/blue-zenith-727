package cat.module.modules.render;

import cat.module.Module;
import cat.module.ModuleCategory;
import cat.module.value.types.BooleanValue;
import cat.module.value.types.FloatValue;
import cat.module.value.types.IntegerValue;
import cat.module.value.types.ModeValue;

public class Animations extends Module {
    public ModeValue anim = new ModeValue("Animation", "SlideDown", true, null, "SlideDown", "Exhibition");
    public FloatValue itemPosX = new FloatValue( "ItemPosX",0, -100, 100, 0, true, null);
    public FloatValue itemPosY = new FloatValue( "ItemPosY",0, -100, 100, 0, true, null);
    public FloatValue itemPosZ = new FloatValue( "ItemPosZ",0, -100, 100, 0, true, null);
    public FloatValue itemScale = new FloatValue( "ItemScale",1, 0.1f, 5, 0, true, null);
    public FloatValue translatePosX = new FloatValue( "TranslatePosX",0, -100, 100, 0, true, null);
    public FloatValue translatePosY = new FloatValue( "TranslatePosY",0, -100, 100, 0, true, null);
    public FloatValue translatePosZ = new FloatValue( "TranslatePosZ",0, -100, 100, 0, true, null);
    public FloatValue translateScale = new FloatValue( "TranslateScale",1, 0.1f, 5, 0, true, null);
    public BooleanValue slowSwing = new BooleanValue("SlowSwing", false, true);
    public IntegerValue slowSwingValue = new IntegerValue("SlowSwingValue", 50, 0,100,0,true, __ -> slowSwing.get());
    @SuppressWarnings("unused")
    private final BooleanValue reset = new BooleanValue("Reset", false, true, (___, __) -> {
        this.anim.set("SlideDown");
        this.itemPosX.set(0f);
        this.itemPosY.set(0f);
        this.itemPosZ.set(0f);
        this.itemScale.set(1F);
        this.translatePosX.set(0f);
        this.translatePosY.set(0f);
        this.translatePosZ.set(0f);
        this.translateScale.set(1f);
        this.slowSwing.set(false);
        this.slowSwingValue.set(0);
        return false;
    }, null);
    public Animations() {
        super("Animations", "", ModuleCategory.RENDER, "animations", "anim");
    }
}
