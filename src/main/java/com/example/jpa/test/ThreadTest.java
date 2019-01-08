package com.example.jpa.test;

/**
 * @author Huangcz
 * @date 2018-06-25 16:31
 * @desc
 */
public class ThreadTest implements Runnable{

    private String name;
    private Object pre;
    private Object self;

    public ThreadTest(String name, Object pre, Object self) {
        this.name = name;
        this.pre = pre;
        this.self = self;
    }

    @Override
    public void run() {

        int count = 10;
        while (count > 0){
            synchronized (pre){
                synchronized (self){
                    System.out.print(name);
                    count --;
                    self.notify();
                }
                try {
                    pre.wait();
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                }
            }
        }


    }





    public static void main(String[] args) throws InterruptedException {
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();
        ThreadTest pa = new ThreadTest("A", c, a);
        ThreadTest pb = new ThreadTest("B", a, b);
        ThreadTest pc = new ThreadTest("C", b, c);

        new Thread(pa).start();
//        Thread.sleep(100);  //确保按顺序A、B、C执行
        new Thread(pb).start();
//        Thread.sleep(100);
        new Thread(pc).start();
//        Thread.sleep(100);
    }

}

