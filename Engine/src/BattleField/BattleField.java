package BattleField;

import schem.out.CTEBattlefield;


public class BattleField {

    private String level;

    private String battleName;

    private Integer allies;

    public BattleField(CTEBattlefield battlefield) {
        this.level =battlefield.getLevel().toUpperCase();
        this.battleName=battlefield.getBattleName();
        this.allies=battlefield.getAllies();
    }

    public BattleField() {

    }

    public String getLevel() {
        return level;
    }

    public String getBattleName() {
        return battleName;
    }

    public Integer getAllies() {
        return allies;
    }
}
