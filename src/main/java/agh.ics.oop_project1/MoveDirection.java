package agh.ics.oop_project1;

public enum MoveDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST,

    NORTHEAST,
    SOUTHEAST,
    NORTHWEST,
    SOUTHWEST;

    public String toString() {
        return switch(this) {
            case NORTH -> "N";
            case SOUTH -> "S";
            case WEST -> "W";
            case EAST -> "E";
            case NORTHEAST -> "NE";
            case SOUTHEAST -> "SE";
            case NORTHWEST -> "NW";
            case SOUTHWEST -> "SW";
        };
    }

    public Vector2d toUnitVector() {
        return switch(this) {
            case NORTH -> new Vector2d(0, 1);
            case SOUTH -> new Vector2d(0, -1);
            case WEST -> new Vector2d(-1, 0);
            case EAST -> new Vector2d(1, 0);
            case NORTHEAST -> new Vector2d(1, 1);
            case SOUTHEAST -> new Vector2d(1, -1);
            case NORTHWEST -> new Vector2d(-1, 1);
            case SOUTHWEST -> new Vector2d(-1, -1);
        };
    }

    private int toInt() {
        return switch(this) {
            case NORTH -> 0;
            case SOUTH -> 4;
            case WEST -> 6;
            case EAST -> 2;
            case NORTHEAST -> 1;
            case SOUTHEAST -> 3;
            case NORTHWEST -> 7;
            case SOUTHWEST -> 5;
        };
    }

    private MoveDirection intToMoveDir(int orientationInt) {
        return switch(orientationInt) {
            case 0 -> NORTH;
            case 4 -> SOUTH;
            case 6 -> WEST;
            case 2 -> EAST;
            case 1 -> NORTHEAST;
            case 3 -> SOUTHEAST;
            case 7 -> NORTHWEST;
            case 5 -> SOUTHWEST;
            default -> NORTH;
        };
    }

    public MoveDirection getRandomDirection() {
        return intToMoveDir(RandomNumberGenerator.getRandomNumber(0, 8));
    }

    public MoveDirection rotate(int rotation) {
        return intToMoveDir((this.toInt() + rotation) % 8);
    }

}
