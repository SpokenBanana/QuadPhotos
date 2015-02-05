
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Rectangle;

/*
	This is the data structure that holds a region of the photo and determines an
	average color for the region as well as an error ranking found by finding how
	different the average color is from the actual colors of the picture.
*/
public class QuadTree extends Rectangle {
	
	private ArrayList<QuadTree> children;
	private BufferedImage image;
	private boolean circled;
	private Color color;
	private int error;
	
	public QuadTree(int x, int y, int width, int height, BufferedImage img, boolean circle) {
		super(x, y, width, height);
		image = img;
		circled = circle;
		setAverageColor();
	}

	/*
		We go through each color in the region, and save each "unique" color. We
		define a color "unique" if the difference of the color and any color
		previosly saved is not lower than the pre-determined limit of difference.
		If this is true, then we save the color, and add the difference of the color
		and the average color to the error. If it is false, then we ignore the color.
		After collecting all the unique colors and the total error amount, we 
		then multiply them, and we have this region's error ranking.
	*/
	public boolean determineError() {
		ArrayList<Color> colors = new ArrayList<>();
		int err = 0;
		for (int i = y; i < y + height; i++) {
			for(int j = x; j  < x + width; j++) {
				Color c = new Color(image.getRGB(j, i));
				err += getTotalDifference(c, color);
				if (colors.isEmpty())
					colors.add(c);
				else {
					if (!colors.stream().anyMatch(current -> getTotalDifference(current, c) < 70)) {
						colors.add(c);
					}
				}
			}
		}

		err /= width * height;
		// the closest algorithm I can think of to give a sort of accurate error ranking, works well
		error = colors.size() * err * width * height;
		
		return width / 2 >= 2 && height / 2 >= 2 && colors.size() >= 2;
	}
	public int getError() {
		return error;
	}

	public ArrayList<QuadTree> getChildren() {
		return children;
	}
	
	public void split() {
		if (width / 2 < 2 || height / 2 < 2)
			return;
		children = new ArrayList<>();
		children.add(new QuadTree(x, y, width/2, height/2, image, circled));
		
		children.add(new QuadTree(x + width/2, y, width/2, height/2, image, circled));
		
		children.add(new QuadTree(x + width / 2, y + height / 2, width/2, height/2, image, circled));
		
		children.add(new QuadTree(x, y + height/2, width/2, height/2, image, circled));
	}
	
	public void draw(Graphics g) {
		if (children != null) {
			for (QuadTree quad : children) {
				quad.draw(g);
			}
		}
		else {
			g.setColor(color);
			if (circled)
				g.fillOval(x, y, width, height);
			else
				g.fillRect(x, y, width, height);
		}
	}
	
	public void drawOutline(Graphics g) {
		if (circled)
			return;
		if (children != null) {
			for (QuadTree quad : children)
				quad.drawOutline(g);
		}
		else {
			g.setColor(Color.black);
			g.drawRect(x, y, width, height);
		}
	}
		
	private void setAverageColor() {
		int r = 0, g = 0, b = 0, count;
		for (int i = y; i < y + height; i++) {
			for (int j = x; j < x + width; j++) {
				Color c = new Color(image.getRGB(j, i));
				r += c.getRed();
				g += c.getGreen();
				b += c.getBlue();
			}
		}
		count = width * height;
		color = new Color(r / count, g / count, b / count);
	}
	
	private int getTotalDifference(Color first, Color second) {
		return Math.abs((first.getRed() - second.getRed())) + 
			   Math.abs((first.getGreen() - second.getGreen())) +
			   Math.abs((first.getBlue() - second.getBlue()));
	}
	
}
