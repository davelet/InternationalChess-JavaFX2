/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

/**
 *
 * @author DAVE
 */
class B {
    int  a ;
    public  void add(){
        this.a =A.a;
        a++;
        A.a=a;
    }

    public int getA() {
        A.a=a;
        return A.a;
    }

    public void setA(int a) {
        this.a = a;
    }

}
