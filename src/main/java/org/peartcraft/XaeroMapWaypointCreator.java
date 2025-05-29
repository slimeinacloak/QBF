package org.peartcraft;

import net.sandrohc.schematic4j.schematic.types.SchematicBlockPos;

import java.util.ArrayList;
import java.util.List;

public class XaeroMapWaypointCreator implements WayPointCreator {
    @Override
    public List<String> createWaypoints(List<SchematicBlockPos> positions, ConfigData data) {
        List<String> waypoints = new ArrayList<>();

        int i = 0;
        for (SchematicBlockPos pos : positions) {
            waypoints.add(String.format("waypoint:%d:UB:%d:%d:%d:0:false:0:gui.xaero_default:false:0:0", i, pos.x, pos.y, pos.z));
            i++;
        }

        return waypoints;
    }
}
