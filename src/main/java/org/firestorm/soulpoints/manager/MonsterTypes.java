package org.firestorm.soulpoints.manager;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MonsterTypes {
    private final Set<String> monsters;

    public MonsterTypes() {
        monsters = new HashSet<>();
        monsters.add("ZOMBIE");
        monsters.add("SKELETON");
        monsters.add("SPIDER");
        monsters.add("CAVE_SPIDER");
        monsters.add("CREEPER");
        monsters.add("WITCH");
        monsters.add("BLAZE");
        monsters.add("ENDERMAN");
        monsters.add("WITHER_SKELETON");
        monsters.add("HUSK");
        monsters.add("STRAY");
        monsters.add("DROWNED");
        monsters.add("PHANTOM");
        monsters.add("ZOMBIE_VILLAGER");
        monsters.add("PILLAGER");
        monsters.add("VINDICATOR");
        monsters.add("EVOKER");
        monsters.add("RAVAGER");
        monsters.add("ILLUSIONER");
    }

    public boolean isMonster(Entity entity) {
        return monsters.contains(entity.getType().name());
    }
}
