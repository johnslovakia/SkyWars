package cz.johnslovakia.skywars.api;

public enum Trail {
    SLIME("SLIME", "SLIME"),
    FLAME("FLAME", "FLAME"),
    REDSTONE("COLOURED_DUST", "REDSTONE"),
    NOTE("NOTE", "FLAME"),
    HEARTH("HEART", "HEART"),
    GREEN_SPARKS("FIREWORKS_SPARK", "COMPOSTER"),
    LAVA("LAVA", "LAVA");

    private final String v1_8;
    private final String v1_20;

    Trail(String v1_8, String v1_20) {
        this.v1_8 = v1_8;
        this.v1_20 = v1_20;
    }

    public String getV1_8() {
        return v1_8;
    }

    public String getV1_20() {
        return v1_20;
    }
}
