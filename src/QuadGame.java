
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

/*
	Simple game. You can click a region and it will split into four smaller regions,
	colored by the average color of that region of the photo loaded.
*/
public class QuadGame extends JPanel  implements MouseListener, MouseMotionListener, Runnable{
	
	private final boolean dragEnabled;
	private final BufferedImage image;
	private ArrayList<QuadTree> trees;

	public QuadGame(BufferedImage img, boolean circle, boolean drag) {
		dragEnabled = drag;
		image = img;
		trees = new ArrayList<>();
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		trees.add(new QuadTree(0, 0, image.getWidth(), image.getHeight(), img, circle));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		for (QuadTree tree : trees)
			tree.draw(g);
		for (QuadTree tree : trees)
			tree.drawOutline(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		reveal(e.getPoint());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (dragEnabled)
			reveal(e.getPoint());
	}
	
	private void reveal(Point p) {
		QuadTree toSplit = null;
		for (QuadTree tree : trees) {
			if (tree.contains(p)) {
				toSplit = tree;
				toSplit.split();
				break;
			}
		}
		if (toSplit != null && toSplit.getChildren() != null) {
			// don't want repeats.
			trees.remove(toSplit);
			trees.addAll(toSplit.getChildren());
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void run() {}
	@Override
	public void mouseMoved(MouseEvent e) {}
}
