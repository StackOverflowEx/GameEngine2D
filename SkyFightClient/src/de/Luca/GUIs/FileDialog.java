package de.Luca.GUIs;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileDialog extends JFileChooser {

	private static final long serialVersionUID = 1L;

	public FileDialog(String filterName, String rootDir, String... extensions) {

		this.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(filterName, extensions);
		this.addChoosableFileFilter(filter);
	}

	public int showToUser() {
		int i = showDialog(null, "Auswählen");
		return i;
	}

	public void hide() {

	}
//	public static JFileChooser open() {
//        try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
//				| UnsupportedLookAndFeelException e) {
//			e.printStackTrace();
//		}
//		JFileChooser chooser = new JFileChooser();
//		chooser.setAcceptAllFileFilterUsed(false);
//		chooser.showDialog(null, "Filebrowser");
//		return chooser;
//	}
//	
//	public static void setRoot(JFileChooser chooser, ) {
//		
//	}
//	
//	public static void addFilter(JFileChooser chooser, String name, String... extension) {
//		FileNameExtensionFilter filter = new FileNameExtensionFilter(name, extension);
//		chooser.addChoosableFileFilter(filter);
//	}

}
