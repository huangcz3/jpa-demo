package com.example.jpa.service;

/**
 * @author Huangcz
 * @date 2018-11-06 17:02
 * @desc xxx
 */
public class CountProxy implements Count {

    private CountImpl countImpl;

    public CountProxy(CountImpl countImpl) {
        this.countImpl = countImpl;
    }

    @Override
    public void queryCount() {
        System.out.println("查询账户的预处理-----------");
        countImpl.queryCount();
        System.out.println("查询账户之后    -----------");
    }

    @Override
    public void updateCount() {
        System.out.println("修改账户的预处理-----------");
        countImpl.updateCount();
        System.out.println("修改账户之后    -----------");
    }
}
