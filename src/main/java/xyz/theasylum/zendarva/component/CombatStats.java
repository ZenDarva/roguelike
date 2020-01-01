package xyz.theasylum.zendarva.component;

public class CombatStats implements Component {
    int hp;
    int maxHp;
    int damage;
    boolean active=true;

    public CombatStats(int hp, int maxHp, int damage) {
        this.hp = hp;
        this.maxHp = maxHp;
        this.damage = damage;
    }

    public void doDamage(int amount){
        hp-=amount;
    }
    public void heal(int amount){
        hp+= amount;
        if (hp>maxHp){
            hp=maxHp;
        }
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }




}
