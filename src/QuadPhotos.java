
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import javax.swing.JPanel;

/*
	This class will handle the drawing and running of the animation of the QuadTree
	doing its work.
*/
public class QuadPhotos extends JPanel implements Runnable {
	
	private final BufferedImage image;
	private final int iterations;
	private final boolean listAlgo;
	private boolean done;
	private int current;
	private QuadTree tree;
	private ArrayList<QuadTree> quads;
	private Queue<QuadTree> priority;
	
	public QuadPhotos(BufferedImage img, boolean circle, int iterations, boolean algo) {
		image = img;
		tree = new QuadTree(0, 0, image.getWidth(), image.getHeight(), img, circle);
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		this.iterations = iterations;
		listAlgo = algo;
		done = true;
		current = 0;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (done)
			return;
		g.setColor(Color.black);
		g.fillRect(0,0, getWidth(), getHeight());
		
		tree.draw(g);
		tree.drawOutline(g);
		
		done = true; // we set this flag because otherwise we run into some concurrency error
	}

	public void update() {
		while (current < iterations) {
			// can't manipulate the QuadTree until repaint() is finished.
			if (done) {
				done = false;
				if (listAlgo)
					listAlgorithm();
				else
					priorityAlgorithm();
				current++;
				repaint();				
			}
			try{
				// list will need a longer delay to see what's going on better
				Thread.sleep(listAlgo ? 250 : 7);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
		This method makes use a priority queue. We give the regions with a the
		higher error a higher precedence than those with lower errors. This way
		we now split the regions with the highest error first, so the regions
		that are more complicated and more important to seeing the picture are 
		seen more clearly first than the regions that are not so different than
		it's assigned average color.
	*/
	public void priorityAlgorithm() {
		// check for null instead of empty, because once it is empty, we are 
		// finished with animation
		if (priority == null) {
			priority = new PriorityQueue<>((QuadTree o1, QuadTree o2) -> {
				if (o1 == o2) return 0;
				if (o1.getError() > o2.getError()) return -1;
				return 1;
			});
			tree.split();
			ArrayList<QuadTree> next = tree.getChildren();
			next.forEach(child -> child.determineError());
			priority.addAll(next);
		}
		else {
			if (!priority.isEmpty()) {
				QuadTree quad = priority.remove();
				quad.split();
				ArrayList<QuadTree> next = quad.getChildren();
				// sometime's we try to split a region that's too small, so it won't split
				if (next != null) {
					next.forEach(child -> child.determineError());
					priority.addAll(next);
				}
			}
		}
	}
	
	/*
		This algorithm goes and splits up every region it comes to until it cannot
		any longer. We stop splitting up a region if splitting it causes us to have
		to have a region smaller than 2x2 pixels and or when the average color 
		does not differ so much from the actual colors in the region of the picture
	*/
	
	public void listAlgorithm() {
		if (quads == null) {
			tree.split();
			quads = new ArrayList<>(tree.getChildren());
		}
		else {
			ArrayList<QuadTree> next = new ArrayList<>();
			for (QuadTree q : quads) {
				ArrayList<QuadTree> other = q.getChildren();
				// only split those with no children and have a high enough error
				if (other == null && q.determineError()) {
					q.split();
					next.addAll(q.getChildren());
				}
			}
			quads.addAll(next);
		}
	}
	
	@Override
	public void run() {
		update();
	}
	
}
