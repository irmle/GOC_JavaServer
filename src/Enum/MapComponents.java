package Enum;

public enum MapComponents implements Cloneable {


    LAND(1),
    TURRET_AREA(2),
    BARRIER_AREA(3),
    RESPAWN_POINT(4),
    MONSTER_SPAWN_POINT(5),
    JUNGLE_SPAWN_POINT(6),
    MOVE_POINT(7),
    SPAWN_POINT(8),

    WALL(21),
    TURRET(22),
    BARRIER(23),
    CRYSTAL_AREA(24),
    SHOP_AREA(25);


    private int component;

    MapComponents(int component) {
        this.component = component;
    }

    public int getcomponent() {
        return component;
    }


}