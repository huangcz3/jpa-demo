package com.example.jpa.test;

/**
 * @author Huangcz
 * @date 2018-06-19 16:23
 * @desc
 */
public class Test {
    WaterSource source;
    int i;
    float f;
    private String valve1, valve2, valve3, valve4;

    public static void main(String[] args) {
        Test x = new Test();
        x.print();
    }

    void print() {
        System.out.println("valve1 = " + valve1);
        System.out.println("valve2 = " + valve2);
        System.out.println("valve3 = " + valve3);
        System.out.println("valve4 = " + valve4);
        System.out.println("i = " + i);
        System.out.println("f = " + f);
        System.out.println("source = " + source);
    }
}

class WaterSource {
    private String s;

    WaterSource() {
        System.out.println("WaterSource()");
        s = new String("Constructed");
    }

    @Override
    public String toString() {
        return s;
    }
}


