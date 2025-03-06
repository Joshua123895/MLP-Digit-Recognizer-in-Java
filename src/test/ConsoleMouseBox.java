package test;

import java.awt.MouseInfo;
import java.awt.Point;

public class ConsoleMouseBox {
    public static void main(String[] args) {
    	for (int i = 0; i < 100; i++) {
    		try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		Point point = MouseInfo.getPointerInfo().getLocation();
    		point.translate(100, 100);
    		double mouseX = point.getX();
    		double mouseY = point.getY();
    		System.out.println("mouse x: " + mouseX);
    		System.out.println("mouse y: " + mouseY);
    	}
    }
}
