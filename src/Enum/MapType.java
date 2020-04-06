package Enum;

public enum MapType {
    MAIN("메인맵"),
    DUO("듀오맵");

    private String type;

    MapType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
