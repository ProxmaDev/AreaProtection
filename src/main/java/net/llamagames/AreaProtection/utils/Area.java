/*
    AreaProtection:

    Copyright (C) 2019 SchdowNVIDIA
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

    Thanks to ZAP-Hosting.com and JetBrains!

    ZAP-Hosting.com gave me a Server for testing all plugins.
    If you're interested in a cheap VPS or strong Rootserver follow the links below:
    VPS: https://zap-hosting.com/schdowvserver
    Rootserver: https://zap-hosting.com/schdowroot
    Code (10% Discount Lifetime): schdow-10
 */

package net.llamagames.AreaProtection.utils;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Area {

    private String name;

    private Vector3 pos1;
    private Vector3 pos2;
    private String world;
    private HashMap<String, AreaFlag> flags;

    public Area(String name, Vector3 pos1, Vector3 pos2, String world, HashMap<String, AreaFlag> flags) {
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.world = world;
        this.flags = flags;
    }

    public String getName() {
        return name;
    }

    public boolean isInArea(Position check) {
        if(!check.getLevel().getName().equalsIgnoreCase(world)) return false;

        double minX = Math.min(pos1.x, pos2.x);
        double maxX = Math.max(pos1.x, pos2.x);
        double minY = Math.min(pos1.y, pos2.y);
        double maxY = Math.max(pos1.y, pos2.y);
        double minZ = Math.min(pos1.z, pos2.z);
        double maxZ = Math.max(pos1.z, pos2.z);
        return check.x >= minX && check.x <= maxX && check.y >= minY && check.y <= maxY
                && check.z >= minZ && check.z <= maxZ;
    }

    public Vector3 getPos1() {
        return pos1;
    }

    public Vector3 getPos2() {
        return pos2;
    }

    public String getWorld() {
        return world;
    }

    public boolean isAllowed(String flag) {
        return flags.get(flag).allowed;
    }

    public void setAllowed(String flag, boolean is) {
        flags.get(flag).allowed = is;
    }

    public HashMap<String, AreaFlag> getFlags() {
        return flags;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public void setPos1(Vector3 pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Vector3 pos2) {
        this.pos2 = pos2;
    }

    public List<String> flagsAsStringList() {
        List<String> list = new ArrayList<>();
        flags.values().forEach((areaFlag -> list.add(areaFlag.name + ":" + areaFlag.allowed)));
        return list;
    }
}
