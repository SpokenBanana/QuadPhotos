
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/*
	This class will handle the launching of all the frames that user may create.
	Whether it be for a game or for an animation.
*/
public class Launcher extends JFrame {
	
	public static void main(String[] args) {
		Launcher launcher = new Launcher();
		Menu menu = new Menu(launcher);
		launcher.add(menu);
		launcher.pack();
		launcher.setLocationRelativeTo(null);
		launcher.setVisible(true);
	}
	
	public Launcher() {
		super("QuadPhotos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public void launchAnimation(BufferedImage image, boolean circled, int iterations, boolean algo) {
		JFrame frame = new JFrame("Animation");
		QuadPhotos quad = new QuadPhotos(image, circled, iterations, algo);
		frame.add(quad);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		Thread thread = new Thread(quad);
		thread.start();
	}
	
	public void launchGame(BufferedImage image, boolean circled, boolean drag) {
		JFrame frame = new JFrame("Click to reveal image!");
		QuadGame quad = new QuadGame(image, circled, drag);
		frame.add(quad);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		Thread thread = new Thread(quad);
		thread.start();
	}
}
