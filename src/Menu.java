
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
	This class will set the parameters for the animation and the "game" for the 
	user to set so that they get the experience they want from it.
*/
public class Menu extends JPanel {
	private final JRadioButton rectangle, circle, priority, list;
	private final ButtonGroup drawGroup, algorithmGroup;
	private final JButton browse, start, gameStart;
	private JTextField picture, iterations;
	private final JLabel iterationLabel;
	private Launcher parent;
	private BufferedImage image;
	private String lastPath;
	private JCheckBox enableDrag;
	
	public Menu(Launcher p) {
		parent = p;
		Color bkg = new Color(220,225,255);
		setLayout(null);
		
		// restoring the last file location browsed in last session.
		File last = new File("last.txt");
		try (Scanner scanner = new Scanner(last)) {
			lastPath = scanner.next();
		} catch (Exception e ) {}
		
		setBackground(bkg);
		
		/**** layout boiler plate (( sorry )) ****/
		
		// for the drawing group
		JLabel drawingLabel = new JLabel("Drawing method");
		drawingLabel.setBounds(230, 170, 100, 45);
		add(drawingLabel);
		rectangle = new JRadioButton("Rectangles");
		circle = new JRadioButton("Circles");
		drawGroup = new ButtonGroup();
		circle.setBackground(bkg);
		rectangle.setBackground(bkg);
		drawGroup.add(rectangle);
		drawGroup.add(circle);
		rectangle.setSelected(true);
		rectangle.setBounds(100,200,100, 45);
		circle.setBounds(350, 200, 100, 45);
		
		add(rectangle);
		add(circle);
		
		//for the algorithm choice
		JLabel algorithmLabel = new JLabel("Animation Algorithm");
		algorithmLabel.setBounds(230, 25, 150, 45);
		add(algorithmLabel);
		
		priority = new JRadioButton("Priority algorithm");
		priority.setBackground(bkg);
		priority.setBounds(10, 60, 130, 45);
		list = new JRadioButton("List algorithm");
		list.setBackground(bkg);
		list.setBounds(170, 60, 120, 45);
		list.setSelected(true);
		algorithmGroup = new ButtonGroup();
		algorithmGroup.add(priority);
		algorithmGroup.add(list);
		add(priority);
		add(list);
		
		//iterations
		iterationLabel = new JLabel("Iterations");
		iterations = new JTextField(4);
		iterations.setBounds(400, 75, 100, 20);
		iterationLabel.setBounds(320, 68, 100, 30);
		iterations.setText("8");
		add(iterationLabel);
		add(iterations);
		
		// picture
		JLabel pictureLabel = new JLabel("Picture");
		pictureLabel.setBounds(230, 270, 100, 45);
		add(pictureLabel);
		browse = new JButton("broswe picture");
		browse.setBounds(20, 330, 200, 45);
		
		start = new JButton("Start Animation!");
		start.setBounds(50, 530, 200, 45);
		
		// displaying path to picture
		picture = new JTextField();
		picture.setEditable(false);
		picture.setBounds(250, 330, 300, 45);
		
		add(browse);
		add(circle);
		add(picture);
		add(start);
		
		// gameStart button
		gameStart = new JButton("Start Game!");
		gameStart.setBounds(300, 530, 200, 45);
		
		// starts a new frame with the "game" loaded on to it.
		add(gameStart);

		// enable dragging
		enableDrag = new JCheckBox("Enable drag to reveal");
		enableDrag.setBackground(bkg);
		enableDrag.setBounds(300, 480, 200, 30);
		add(enableDrag);
		
		
		setPreferredSize(new Dimension(600,600));

		// *** Action Listeners ***

		priority.addActionListener((ActionEvent e) -> iterations.setText("3000"));
		
		
		list.addActionListener((ActionEvent e) ->{
			// the "list" algorithm takes a lot less iterations to see results,
			// no need to make it do more work than it needs
			iterations.setText("8");
		});
		
		gameStart.addActionListener((ActionEvent e) -> {
			if (image != null)
				parent.launchGame(image, circle.isSelected(), enableDrag.isSelected());
		});
		
		browse.addActionListener((ActionEvent e) -> {
			JFileChooser chooser = new JFileChooser();
			if (lastPath!=null)
				chooser.setCurrentDirectory(new File(lastPath));
			
			chooser.setFileFilter(new FileNameExtensionFilter("JPG & GIF Images","jpg", "png"));
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			
			int val = chooser.showOpenDialog(frame);
			if (val == JFileChooser.APPROVE_OPTION) {
				String path = chooser.getSelectedFile().getPath();
				String directory = null;
				try {
					image = ImageIO.read(new File(path));
					picture.setText(path);
					lastPath = path;
					directory = chooser.getSelectedFile().getParent();
				} catch (Exception ex) {
					System.out.println("file not loaded");
				}
				
				// save  the last location for next session
				try (PrintWriter writer = new PrintWriter(new File("last.txt"))) {
					if (directory != null)
						writer.println(directory);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		// starts the cool animation!
		start.addActionListener((ActionEvent e) -> {
			int iter = 0;
			try {
				iter = Integer.parseInt(iterations.getText());
			} catch (Exception ex) {
				// default values
				iter = list.isSelected() ? 8 : 3000;
			}
			
			if (image != null)
				parent.launchAnimation(image, circle.isSelected(), iter, list.isSelected());
		});
	}
	
}
