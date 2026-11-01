package com.estie.arcanetweaks.bossbehavior;

public class HurtResult {
    public final boolean cancel;
    public final boolean modify;
    public final float newAmount;
    
    private HurtResult(boolean cancel, boolean modify, float newAmount) {
        this.cancel = cancel;
        this.modify = modify;
        this.newAmount = newAmount;
    }
    
    public static HurtResult pass() {
        return new HurtResult(false, false, 0);
    }
    
    public static HurtResult cancel() {
        return new HurtResult(true, false, 0);
    }
    
    public static HurtResult modify(float amount) {
        return new HurtResult(false, true, amount);
    }
}
