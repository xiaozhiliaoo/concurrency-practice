package atomic;

import java.util.Vector;

public class Vector2Dim {

	/**
	 *lili
	 *2017-4-15 下午1:56:23
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Vector<Integer> v1 = new Vector<Integer>();
		v1.add(1);
		v1.add(2);
		v1.add(3);

		Vector<String> v2 = new Vector<String>();
		v2.add("sss");
		v2.add("vedvev");

		Vector<Vector> v = new Vector<Vector>();

		v.add(v1);
		v.add(v2);

		System.out.println(v);

	}

}
