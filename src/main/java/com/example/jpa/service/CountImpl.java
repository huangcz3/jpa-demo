package com.example.jpa.service;

/**
 * @author Huangcz
 * @date 2018-11-06 17:00
 * @desc xxx
 */
public class CountImpl implements Count {
    @Override
    public void queryCount() {
        System.out.println("查看账户...");
    }

    @Override
    public void updateCount() {
        System.out.println("修改账户...");
    }
}
