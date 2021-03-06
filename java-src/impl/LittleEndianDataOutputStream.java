package impl;
import interfaces.UnsignedDataOutput;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import clojure.lang.BigInt;

public class LittleEndianDataOutputStream extends FilterOutputStream implements UnsignedDataOutput{

	private int count;

	public LittleEndianDataOutputStream(OutputStream out) {
		super(out);
		count = 0;
	}

	@Override
	public final void writeBoolean(boolean b) throws IOException {
		if (b)
			this.write(1);
		else
			this.write(0);
		count++;
	}

	@Override
	public final void writeByte(int b) throws IOException {
		out.write(b);
		count++;
	}

	@Override
	public final void writeShort(int s) throws IOException {
		out.write(s & 0xFF);
		out.write((s >>> 8) & 0xFF);
		count+=2;
	}
	@Override
	public final void writeUnsignedShort(int s) throws IOException {
		out.write((s >>> 0) & 0xFF);
        out.write((s >>> 8) & 0xFF);
        count+=2;
	}
	@Override
	public final void writeChar(int c) throws IOException {
		out.write(c & 0xFF);
		out.write((c >>> 8) & 0xFF);
		count+=2;
	}

	@Override
	public final void writeInt(int i) throws IOException {
		out.write(i & 0xFF);
		out.write((i >>> 8) & 0xFF);
		out.write((i >>> 16) & 0xFF);
		out.write((i >>> 24) & 0xFF);
		count+=4;
	}
	@Override
	public final void writeUnsignedInt(long i) throws IOException {
		out.write((int) (i & 0xFF));
		out.write((int) ((i >>> 8) & 0xFF));
		out.write((int) ((i >>> 16) & 0xFF));
		out.write((int) ((i >>> 24) & 0xFF));
		count+=4;
	}
	
	@Override
	public final void writeLong(long l) throws IOException {
		out.write((int) l & 0xFF);
		out.write((int) (l >>> 8) & 0xFF);
		out.write((int) (l >>> 16) & 0xFF);
		out.write((int) (l >>> 24) & 0xFF);
		out.write((int) (l >>> 32) & 0xFF);
		out.write((int) (l >>> 40) & 0xFF);
		out.write((int) (l >>> 48) & 0xFF);
		out.write((int) (l >>> 56) & 0xFF);
		count+=8;
	}
	@Override
	public void writeUnsignedLong(BigInt bi) throws IOException {
		byte[] toWrite = new byte[8];
		byte[] w = bi.toBigInteger().toByteArray();
		
		int arrayLength = w.length;
		boolean isLongerThanLong = arrayLength>8;
		if(isLongerThanLong && w[0]>1)
			throw new ArithmeticException("unsigned long is too big! Would truncate on write!");
		
		int offset = isLongerThanLong?1:0;
		int len = isLongerThanLong?8:arrayLength;
		System.arraycopy(w, offset, toWrite, 8-len, len);

		// reverse bytes
		for (int i = 0; i < 4; i++) {
			byte b = toWrite[i];
			toWrite[i]=toWrite[7-i];
			toWrite[7-i] = b;
		}
		out.write(toWrite,0,8);
		count+=8;
	}

	@Override
	public final void writeFloat(float f) throws IOException {
		this.writeInt(Float.floatToIntBits(f));
		count+=4;
	}

	public final void writeDouble(double d) throws IOException {
		this.writeLong(Double.doubleToLongBits(d));
		count+=8;
	}

	public void writeBytes(String s) throws IOException {
		throw new RuntimeException("unimplemented");
	}

	public final void writeChars(String s) throws IOException {
		throw new RuntimeException("unimplemented");
	}
	@Override
	public final void writeUTF(String s) throws IOException {
		throw new RuntimeException("unimplemented");
	}

	@Override
	public int size() {
		return count;
	}

	@Override
	public void write(int b) throws IOException {
		super.write(b);
		count++;
	}

}