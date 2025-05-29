package org.peartcraft;

import net.sandrohc.schematic4j.schematic.LitematicaSchematic;

import java.util.Set;

public record ConfigData(
        int x1, int y1, int z1,
        int x2, int y2, int z2,
        String dimension,
        String creator,
        Set<String> blockSet,
        LitematicaSchematic litematic
) {
}
