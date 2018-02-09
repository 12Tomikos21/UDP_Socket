
public class Data {

	private byte[] b;
	private int size;
	private char t;
	public Data(byte[] b,int size,char t){
		this.b = b;
		this.size = size;
		this.setT(t);
	}
	public byte[] getB() {
		return b;
	}
	public void setB(byte[] b) {
		this.b = b;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public char getT() {
		return t;
	}
	public void setT(char t) {
		this.t = t;
	}
}
