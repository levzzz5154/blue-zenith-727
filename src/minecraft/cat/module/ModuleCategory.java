package cat.module;

public enum ModuleCategory {
    COMBAT("Combat"),
    FUN("Fun"),
    MOVEMENT("Movement"),
    MISC("Misc"),
    PLAYER("Player"),
    RENDER("Render"),
    WORLD("World");
    public String displayName;
    ModuleCategory(String displayName) {
        this.displayName = displayName;
    }
}
