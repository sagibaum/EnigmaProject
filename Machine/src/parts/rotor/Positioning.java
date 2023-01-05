package parts.rotor;

import schem.out.CTEPositioning;

import java.io.Serializable;

public class Positioning implements Serializable {
    private String left;
    private String right;
    private boolean isNotch;

    public Positioning(CTEPositioning pos, boolean isThisNotch) {
        left = pos.getLeft();
        right = pos.getRight();
        this.isNotch = isThisNotch;
    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }

    public boolean isNotch() {
        return isNotch;
    }

    @Override
    public String toString() {
        return "Positioning{" +
                "left='" + left + '\'' +
                ", right='" + right + '\'' +
                ", isNotch=" + isNotch +
                '}';
    }
}
