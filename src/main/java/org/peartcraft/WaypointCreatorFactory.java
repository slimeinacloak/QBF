package org.peartcraft;

import java.util.Map;
import java.util.function.Supplier;

public class WaypointCreatorFactory {
    private static final Map<String, Supplier<WayPointCreator>> registry = Map.of(
            MapMod.VOXEL.name(), VoxelMapWaypointCreator::new,
            MapMod.XAERO.name(), XaeroMapWaypointCreator::new
    );

    public static WayPointCreator create(String name) {
        Supplier<WayPointCreator> supplier = registry.get(name);
        if (supplier == null) throw new IllegalArgumentException("Unknown Waypoint Creator");
        return supplier.get();
    }
}
