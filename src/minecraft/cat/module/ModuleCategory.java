package cat.module;

public enum ModuleCategory {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    MISC("Misc"),
    PLAYER("Player"),
    WORLD("World"),
    RENDER("Render");
    public String displayName;
    ModuleCategory(String displayName) {
        this.displayName = displayName;
    }
}
