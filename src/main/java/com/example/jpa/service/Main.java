package com.example.jpa.service;

/**
 * @author Huangcz
 * @date 2018-11-06 17:05
 * @desc xxx
 */
public class Main {

    public static void main(String[] args) {
        CountImpl countImpl = new CountImpl();
        CountProxy countProxy = new CountProxy(countImpl);

        countProxy.updateCount();
        System.out.println("******************");
        countProxy.queryCount();

    }

}
