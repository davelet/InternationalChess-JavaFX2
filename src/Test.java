public class Test {
	public static void main(String[] args) {
		A1 a=new A1();
		System.out.println(a.num);
		B1 b=new B1();
		b.changeA(10);
		System.out.println(a.num);
	}
}

class A1{
	public static int num=2;
}

class B1{
	public void changeA(int num){
		A1.num=num;
	}
}