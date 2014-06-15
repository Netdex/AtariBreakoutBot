import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import de.ksquared.system.keyboard.GlobalKeyListener;
import de.ksquared.system.keyboard.KeyAdapter;
import de.ksquared.system.keyboard.KeyEvent;
import de.ksquared.system.mouse.GlobalMouseListener;
import de.ksquared.system.mouse.MouseAdapter;
import de.ksquared.system.mouse.MouseEvent;

public class BreakoutBot {

	public static void main(String[] args) throws Exception {
		Robot r = new Robot();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Enter choppiness (ms) [default 0]: ");
		int chop;
		try{
			chop = Integer.parseInt(br.readLine());
		}catch(NumberFormatException nfe){
			chop = 0;
		}
		
		System.out.println("Enter randomness (px) [default 1]: ");
		int rand;
		try{
			rand = Integer.parseInt(br.readLine());
		}catch(NumberFormatException nfe){
			rand = 1;
		}
		
		System.out.println("Enter comma separated color (RGB) [Default 153,153,153]: ");
		String[] colorin;
		Color ballColor;
		try{
			colorin = br.readLine().split(",");
			ballColor = new Color(Integer.parseInt(colorin[0]),Integer.parseInt(colorin[1]),Integer.parseInt(colorin[2]));
		}catch(NumberFormatException nfe){
			String[] def = {"153","153","153"};
			colorin = def.clone();
			ballColor = new Color(153,153,153);
		}
		
		System.out.println(chop + "," + rand + ";" + colorin[0] + "," + colorin[1] + "," + colorin[2]);
		System.out.println("Choose border co-ordinates: ");
		
		final ArrayList<int[]> coords = new ArrayList<int[]>();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		
		new GlobalMouseListener().addMouseListener(new MouseAdapter() {
			@Override public void mouseReleased(MouseEvent event)  { 
				if(coords.size() <= 1){
					int[] temp = new int[2];
					temp[0] = event.getX();
					temp[1] = event.getY();
					coords.add(temp);
					System.out.println(event);
				}
			}
			
		});
		new GlobalKeyListener().addKeyListener(new KeyAdapter() {
			@Override public void keyReleased(KeyEvent event) {
				if(event.getVirtualKeyCode()==KeyEvent.VK_0){
					System.out.println("0 was pressed, terminating");
					System.exit(0);
				}
			}
		});
		
		
		
		while(coords.size() <= 1){
			Thread.sleep(1000);
			System.out.println("Waiting...");
		}
		
		
		
		Random ran = new Random();
		BufferedImage screen;
		
		while(true){
			screen = r.createScreenCapture(new Rectangle(0,0,(int)width,(int)height));
			loop:
			for(int x = coords.get(0)[0]; x < coords.get(1)[0]; x++){
				for(int y = coords.get(0)[1]; y < coords.get(1)[1]; y++){
					Color curr = getColor(screen.getRGB(x,y));
					if(curr.equals(ballColor)){
						r.mouseMove(x + ran.nextInt(rand) - ran.nextInt(rand), y + ran.nextInt(rand) - ran.nextInt(rand));
						System.out.println("Match at " + x + "," + y);
						break loop;
					}
				}
			}
			Thread.sleep(chop);
		}
		
	}
	
	public static Color getColor(int pixel) {
	    int alpha = (pixel >> 24) & 0xff;
	    int red = (pixel >> 16) & 0xff;
	    int green = (pixel >> 8) & 0xff;
	    int blue = (pixel) & 0xff;
	    Color pix = new Color(red,green,blue,alpha);
	    return pix;
	  }

}
