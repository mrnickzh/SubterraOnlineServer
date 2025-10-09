package org.subterra.Utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ByteBuf {
    private byte[] buffer;
    private int position = 0;

    public ByteBuf(int capacity) {
        this.buffer = new byte[capacity];
    }

    public int readInt() {
        checkBounds(Integer.BYTES);
        int value = (buffer[position] & 0xFF) |
                ((buffer[position + 1] & 0xFF) << 8) |
                ((buffer[position + 2] & 0xFF) << 16) |
                ((buffer[position + 3] & 0xFF) << 24);
        position += Integer.BYTES;
        return value;
    }

    public void writeInt(int value) {
        checkBounds(Integer.BYTES);
        buffer[position++] = (byte) (value & 0xFF);
        buffer[position++] = (byte) ((value >> 8) & 0xFF);
        buffer[position++] = (byte) ((value >> 16) & 0xFF);
        buffer[position++] = (byte) ((value >> 24) & 0xFF);
    }

    public void writeFloat(float value) {
        checkBounds(Float.BYTES);
        int bits = Float.floatToIntBits(value);
        buffer[position++] = (byte) (bits & 0xFF);
        buffer[position++] = (byte) ((bits >> 8) & 0xFF);
        buffer[position++] = (byte) ((bits >> 16) & 0xFF);
        buffer[position++] = (byte) ((bits >> 24) & 0xFF);
    }

    public float readFloat() {
        checkBounds(Float.BYTES);
        int bits = (buffer[position] & 0xFF) |
                ((buffer[position + 1] & 0xFF) << 8) |
                ((buffer[position + 2] & 0xFF) << 16) |
                ((buffer[position + 3] & 0xFF) << 24);
        position += Float.BYTES;
        return Float.intBitsToFloat(bits);
    }

    public void writeString(String value) {
        byte[] strBytes = value.getBytes(StandardCharsets.UTF_8);
        writeInt(strBytes.length);
        checkBounds(strBytes.length);
        System.arraycopy(strBytes, 0, buffer, position, strBytes.length);
        position += strBytes.length;
    }

    public String readString() {
        int length = readInt();
        System.out.println("string length: " + length);
        checkBounds(length);
        String value = new String(buffer, position, length, StandardCharsets.UTF_8);
        position += length;
        return value;
    }

    public byte[] toByteArray() {
        return Arrays.copyOf(buffer, buffer.length);
    }

    public void fromByteArray(byte[] data) {
        this.buffer = Arrays.copyOf(data, data.length);
        this.position = 0;
    }

    public void fromString(String str) {
        this.buffer = str.getBytes(StandardCharsets.UTF_8);
        this.position = 0;
    }

    public String toString() {
        return new String(buffer, StandardCharsets.UTF_8);
    }

    private void checkBounds(int required) {
        System.out.println(required);
        if (position + required > buffer.length) {
            throw new IndexOutOfBoundsException(
                    position + required > buffer.length ? "Buffer overflow" : "Buffer underflow");
        }
    }
}