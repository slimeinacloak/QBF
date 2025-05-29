package org.peartcraft;

public enum Dimension {
    OVERWORLD("overworld", 1),
    NETHER("the_nether", 8),
    END("the_end", 1);

    private final String id;
    private final int multiplier;

    Dimension(String id, int multiplier) {
        this.id = id;
        this.multiplier = multiplier;
    }

    public String getId() {
        return id;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public static Dimension fromDisplayName(String displayName) {
        return switch (displayName) {
            case "Nether" -> NETHER;
            case "End" -> END;
            default -> OVERWORLD;
        };
    }
}

