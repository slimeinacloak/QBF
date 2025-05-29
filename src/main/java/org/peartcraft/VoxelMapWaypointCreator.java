package org.peartcraft;

import net.sandrohc.schematic4j.schematic.types.SchematicBlockPos;

import java.util.ArrayList;
import java.util.List;

public class VoxelMapWaypointCreator implements WayPointCreator {
    @Override
    public List<String> createWaypoints(List<SchematicBlockPos> positions, ConfigData data) {
        int x, y, z;
        x = Math.min(data.x1(), data.x2());
        y = Math.min(data.y1(), data.y2());
        z = Math.min(data.z1(), data.z2());

        List<String> waypoints = new ArrayList<>();
        Dimension dimension = Dimension.fromDisplayName(data.dimension());

        int i = 0;
        for (SchematicBlockPos pos : positions) {
            waypoints.add(String.format("name:%d,x:%d,z:%d,y:%d,enabled:true,red:1.0,green:1.0,blue:1.0,suffix:,world:,dimensions:%s#",
                    i,
                    (pos.x + x) * dimension.getMultiplier(),
                    (pos.z + z) * dimension.getMultiplier(),
                    (pos.y + y), // I would never accidentally multiply the y by 8...
                    dimension.getId()));
            i++;
        }
        return waypoints;
    }
}
