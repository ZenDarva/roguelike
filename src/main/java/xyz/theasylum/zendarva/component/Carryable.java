package xyz.theasylum.zendarva.component;


import com.google.gson.annotations.Expose;

public class Carryable implements Component {
    @Expose
    int weight;

    public Carryable(int weight) {
        this.weight = weight;
    }

    public Carryable() {
        this.weight=1;
    }
}
