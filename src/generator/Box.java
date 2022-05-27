package generator;

public class Box {

	public int x, y, w, h;
	
	public Box(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	
	public int getS() {
		return w*h;
	}

	public int getCX() {
		return x + w/2;
	}
	public int getCY() {
		return y + h/2;
	}
}
