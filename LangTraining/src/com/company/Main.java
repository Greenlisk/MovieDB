/**
 * Created by iserbai on 28.01.16.
 */
package com.company;
import com.company.DataProcessor.*;

import javax.swing.*;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.*;
import java.util.concurrent.ExecutionException;


class EX<T>  {
    EX() {
    }

}

public class Main {
    public static  int counter = 0;

    static class Friend {
        private final String name;

        public Friend (String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public synchronized void bow(Friend bower) {
            System.out.format("%s : %s bowed to me.%n", this.name, bower.getName());
            bower.bowBack(this);
        }

        public synchronized void bowBack(Friend bower) {
            System.out.format("%s : %s has bowed back to me.%n", this.name, bower.getName());
        }
    }

    public static void main(String[] args) throws IOException{

        class MyThread extends Thread {

            private volatile boolean enough = false;

            public synchronized void enough() {
                this.enough = true;
                notifyAll();
            }

            public synchronized void run() {
                while (!enough) {
                    try {
                        wait();
                    } catch (InterruptedException iex) {
                        System.out.println("mythread_interrupted");
                        return;
                    }

                }
                System.out.println("Finishing thread");
            }
        }
        Thread otherThread = new Thread() {
            public void run() {
                for (int i = 0; i < 50; i++)
                    try {
                        System.out.println("ololo");
                        sleep(1000);
                    } catch(InterruptedException iex) {
                        System.out.println("");
                    }

            }
        };

        /*otherThread.start();
        MyThread myThread = new MyThread();
        myThread.start();
        try {
            System.out.println("sleeping");
            Thread.sleep(3000);
            myThread.enough();
            if (myThread.isAlive()) {
                myThread.interrupt();
                myThread.join();
            } else {

            }
        } catch (InterruptedException iex) {
            System.out.println("main_interrupted");
        }
        System.out.println("other Thread finished!");

        final Friend alphonse = new Friend("Alphonse");
        final Friend gaston = new Friend("Gaston");

        new Thread(new Runnable() {
            @Override
            public void run() {
                alphonse.bow(gaston);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                gaston.bow(alphonse);
            }
        }).start();*/

        int[] i = {1,2,3,4};
        int[] j = {1,2,3,4};
        System.out.println(i == j);
    }

}

