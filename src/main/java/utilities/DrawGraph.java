package utilities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;

public class DrawGraph {

	boolean imageExist = false;
	int width = 600;
	int height = 600;
	int centerX = width/2;
	int centerY = height/2;
	double step = 0.1;
	
	BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	Graphics2D ig2 = bi.createGraphics();
	
	public void drawGraph(){
		//ig2.setPaint(Color.black);
		ig2.drawLine(centerX, 0, centerX, height);
		ig2.drawLine(0, centerY,width, centerY);
		 try {
			imageExist = ImageIO.write(bi, "PNG", new File("src\\main\\resources\\image\\coordinates.PNG"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class TestDraw{
	public static void main(String[] args) {
		DrawGraph dg = new DrawGraph();
		dg.drawGraph();
	}
}
