/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

/**
 *
 * @author DAVE
 */
public class A {
//    public static int a=0;

    static int a;

    public static void main(String[] args) {
        foo();
    }

    static void foo() {
        a = 1;
        B b = new B();
        b.add();
//        System.out.println(b.getA());
        System.out.println(a);
    }
}
