package ru.javawebinar.topjava;

/**
 * @see <a href="http://topjava.herokuapp.com">Demo application</a>
 * @see <a href="https://github.com/JavaOPs/topjava">Initial project</a>
 */
public class Main {
    public static void main(String[] args) {
        System.out.format("Hello TopJava Enterprise!");
        System.out.println(complexity(10));
        System.out.println(complexity1(10));
    }

    public static int complexity(int n) {
        int res = 0;
        for (int i = 0; i < n; i++) {
            res += (i + 1);
        }
        return res;
    }
    public static int complexity1(int n) {
        return n * (n + 1) / 2;
    }
}
