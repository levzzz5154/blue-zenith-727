package cat.module;

public enum ModuleCategory {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    MISC("Misc"),
    RENDER("Render");
    String displayName;
    ModuleCategory(String displayName) {
        this.displayName = displayName;
    }
}
