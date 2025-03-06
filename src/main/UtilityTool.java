package main;

import java.awt.Robot;
import java.awt.event.KeyEvent;

public class UtilityTool {	
    public void clear() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public void fullscreen() {
    	try {
	        Robot robot = new Robot();

            if (System.getProperty("os.name").contains("Windows")) {
            	robot.keyPress(KeyEvent.VK_ALT);       // Press Alt
            	robot.keyPress(KeyEvent.VK_ENTER);     // Press Enter
            	
            	robot.keyRelease(KeyEvent.VK_ENTER);   // Release Enter
            	robot.keyRelease(KeyEvent.VK_ALT);     // Release Alt
            } else {
            	robot.keyPress(KeyEvent.VK_CONTROL);   // Press Ctrl key
            	robot.keyPress(KeyEvent.VK_SHIFT);     // Press Shift key
            	robot.keyPress(KeyEvent.VK_F);         // Press F key
            	
            	robot.keyRelease(KeyEvent.VK_F);       // Release F key
            	robot.keyRelease(KeyEvent.VK_SHIFT);   // Release Shift key
            	robot.keyRelease(KeyEvent.VK_CONTROL); // Release Ctrl key
            }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }

	public void moveCursorTo(int x, int y) {
	    System.out.printf("\033[%d;%dH", y, x);
	}
	
	public void change_colour(int a) {
	    System.out.printf("\033[48;5;%dm", a + 232);
	}
	
	public void reset_colour() {
	    System.out.printf("\033[0m");
	}
	
	public int countDigits(int a) {
		if (a == 0) return 1;
		return (int) (Math.log10(Math.abs(a))) + 1;
	}
	
    public void hideCursor() {
        System.out.print("\033[?25l");
    }

    public void showCursor() {
        System.out.print("\033[?25h");
    }
}
