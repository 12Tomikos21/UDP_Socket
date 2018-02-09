import java.nio.ByteBuffer;

public class Hlavicka {
	
private int number;
private int size;
private int all_size;
private char type;
private long CRC;

	public Hlavicka(int number,int size,int all_size,char type,long crc){
	this.number = number;
	this.size = size;
	this.all_size = all_size;
	this.type = type;
	this.CRC = crc;
	}

	public int getSize() {
	return size;
	}
	public void setSize(int size) {
	this.size = size;
	}
	public int getNumber() {
	return number;
	}
	public void setNumber(int number) {
	this.number = number;
	}
	public int getAll_size() {
	return all_size;
	}
	public void setAll_size(int all_size) {
	this.all_size = all_size;
	}
	public char getType() {
	return type;
	}
	public void setType(char type) {
	this.type = type;
	}
	public long getCRC() {
	return CRC;
	}
	public void setCRC(long l) {
	this.CRC = l;
	}
public byte[] vlkadanie(Hlavicka a){
		
		byte[] hlavicka = new byte[22];
		ByteBuffer header_byte = ByteBuffer.allocate(22);
		header_byte.putInt(a.number);
		header_byte.putInt(a.size);
		header_byte.putInt(a.all_size);
		header_byte.putChar(a.type);
		header_byte.putLong(a.CRC);
		
		hlavicka = header_byte.array();
		return hlavicka;
	}
public Hlavicka vyberanie(byte[] data){
	
	ByteBuffer header_byte = ByteBuffer.wrap(data);
	number = header_byte.getInt();
	size = header_byte.getInt();
	all_size = header_byte.getInt();
	type = header_byte.getChar();
	CRC = header_byte.getLong();
	
	Hlavicka header = new Hlavicka(number,size,all_size,type,CRC);
	
	return header;
}
public int velkost_h(){
	return 3*4+2+8;
}
public void insert(Hlavicka header, int number,int size,int all,char znak,long CRC){
	header.setNumber(number);
	header.setSize(size);
	header.setAll_size(all);
	header.setType(znak);
	header.setCRC(CRC);
}

}
		
