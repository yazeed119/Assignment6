package edu.najah.cap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.najah.cap.utils.Strings;

@SuppressWarnings("serial")
public class Editor extends JFrame implements ActionListener, DocumentListener {
	
	final int windowSize = 500;
    final int cencel = 1;// value if cancel is chosen.
	final int approve = 0;// value if approve (yes, ok) is chosen.
	final int showErrorDialog = 0;// means show Error Dialog
	public JEditorPane TP;//Text Panel
	private JMenuBar menu;//Menu
	public boolean changed = false;
	private File file;

	public static void main(String[] args) {
		new Editor();
	}
	
	public Editor() {
		super(Strings.appName);
		initialize();
	
	}
	
	private void initialize() {
		TP = new JEditorPane();
		// center means middle of container.
		add(new JScrollPane(TP), "Center");
		TP.getDocument().addDocumentListener(this);

		menu = new JMenuBar();
		setJMenuBar(menu);
		BuildMenu();
		setSize(windowSize,windowSize);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void BuildMenu() {
		buildFileMenu();
		buildEditMenu();
	}

	private void buildFileMenu() {
        // create file menu
		JMenu file = new JMenu(Strings.fileMenu);
		file.setMnemonic('F');
		menu.add(file);
		
		// create new menu item
		JMenuItem newMenu = new JMenuItem(Strings.newMenu);
		newMenu.setMnemonic('N');
		newMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		newMenu.addActionListener(this);
		file.add(newMenu);
		
		//create open menu item
		JMenuItem openMenu = new JMenuItem(Strings.openMenu);
		file.add(openMenu);
		openMenu.addActionListener(this);
		openMenu.setMnemonic('O');
		openMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		
		// create save menu item
		JMenuItem saveMenu = new JMenuItem(Strings.saveMenu);
		file.add(saveMenu);
		saveMenu.setMnemonic('S');
		saveMenu.addActionListener(this);
		saveMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		
		// create save as menu item
		JMenuItem saveAsMenu = new JMenuItem(Strings.saveAsMenu);
		saveAsMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		file.add(saveAsMenu);
		saveAsMenu.addActionListener(this);
		
		// create quit menu item
		JMenuItem quitMenu = new JMenuItem(Strings.quitMenu);
		file.add(quitMenu);
		quitMenu.addActionListener(this);
		quitMenu.setMnemonic('Q');
		quitMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
	}

	private void buildEditMenu() {		
		// create edit menu
		JMenu editMenu = new JMenu(Strings.editMenu);
		menu.add(editMenu);
		editMenu.setMnemonic('E');
		
		// create cut menu item 
		JMenuItem cutMenu;
		cutMenu = new JMenuItem(Strings.cutMenu);
		cutMenu.addActionListener(this);
		cutMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
		cutMenu.setMnemonic('T');
		editMenu.add(cutMenu);
		
		// create copy menu item
		JMenuItem copyMenu;
		copyMenu = new JMenuItem(Strings.copyMenu);
		copyMenu.addActionListener(this);
		copyMenu.setMnemonic('C');
		copyMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
		editMenu.add(copyMenu);
		
		// create paste menu item
		JMenuItem pasteMenu;
		pasteMenu = new JMenuItem(Strings.pasteMenu);
		pasteMenu.setMnemonic('P');
		pasteMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
		editMenu.add(pasteMenu);
		pasteMenu.addActionListener(this);
		
		// create find menu item
		JMenuItem findMenu = new JMenuItem(Strings.findMenu);
		findMenu.setMnemonic('F');
		findMenu.addActionListener(this);
		editMenu.add(findMenu);
		findMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
		
		// create select all menu item
		JMenuItem selectAllMenu = new JMenuItem(Strings.selectAllMenu);
		selectAllMenu.setMnemonic('A');
		selectAllMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		selectAllMenu.addActionListener(this);
		editMenu.add(selectAllMenu);
	}
	
	private int showSaveConfirmDialog() {
		return JOptionPane.showConfirmDialog(null,Strings.saveDialogMsg, Strings.saveDialogDesc, 0, 2);
	}

	
	private void savePressed() {
		int answer = 0;
		if (changed) {
			answer = showSaveConfirmDialog();
		}
		//1 value from class method if NO is chosen.
		if (answer != 1) {
			if (file == null) {
				saveAs(Strings.saveMenu);
			} else {
				String text = TP.getText();
				System.out.println(text);
				try (PrintWriter writer = new PrintWriter(file);){
					if (!file.canWrite())
						throw new Exception(Strings.connotWriteFileErrorMsg);
					writer.write(text);
					changed = false;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals(Strings.quitMenu)) {
			quitPressed();
		} else if (action.equals(Strings.openMenu)) {
			openPressed();
		} else if (action.equals(Strings.saveMenu)) {
			//Save file
			savePressed();
		} else if (action.equals(Strings.newMenu)) {
			//New file 
			newPressed();
		} else if (action.equals(Strings.saveAsMenu)) {
			saveAs(Strings.saveAsMenu);
		} else if (action.equals(Strings.selectAllMenu)) {
			selectAllPressed();
		} else if (action.equals(Strings.copyMenu)) {
			copyPressed();
		} else if (action.equals(Strings.cutMenu)) {
            cutPressed();
        } else if (action.equals(Strings.pasteMenu)) {
            pastePressed() ;
        } else if (action.equals(Strings.fileMenu)) {
	        filePressed();
		}
	}


	private void newPressed() {
		if (changed) {
			//Save file 
			if (changed) {
				int answer = showSaveConfirmDialog();
				//1 value from class method if NO is chosen.
				if (answer == 1)
					return;
			}
			if (file == null) {
				saveAs(Strings.saveMenu);
				return;
			}
			String text = TP.getText();
			System.out.println(text);
			try (PrintWriter writer = new PrintWriter(file);){
				if (!file.canWrite())
					throw new Exception(Strings.connotWriteFileErrorMsg);
				writer.write(text);
				changed = false;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		file = null;
		TP.setText("");
		changed = false;
		setTitle(Strings.appName);
	}
	
	private void cutPressed() {
		TP.cut();
	}
	
	private void copyPressed() {
		TP.copy();
	}
	
	private void pastePressed() {
		TP.paste();
	}
	
	private void filePressed() {
		FindDialog find = new FindDialog(this, true);
		find.showDialog();
	}
	
	private void quitPressed() {
		System.exit(0);
	}
	
	private void openPressed() {
		loadFile();
	}
	
	
	private void selectAllPressed() {
		TP.selectAll();
	}
	private void loadFile() {
		JFileChooser dialog = new JFileChooser(System.getProperty("user.home"));
		dialog.setMultiSelectionEnabled(false);
		try {
			int result = dialog.showOpenDialog(this);
			if (result == cencel)
				return;
			if (result == approve) {
				if (changed){
					//Save file
					if (changed) {
						int answer = showSaveConfirmDialog();
						if (answer == 1)// no option 
							return;
					}
					if (file == null) {
						saveAs(Strings.saveMenu);
						return;
					}
					String text = TP.getText();
					System.out.println(text);
					try (PrintWriter writer = new PrintWriter(file);){
						if (!file.canWrite())
							throw new Exception(Strings.connotWriteFileErrorMsg);
						writer.write(text);
						changed = false;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				file = dialog.getSelectedFile();
				//Read file 
				StringBuilder rs = new StringBuilder();
				try (	FileReader fr = new FileReader(file);		
						BufferedReader reader = new BufferedReader(fr);) {
					String line;
					while ((line = reader.readLine()) != null) {
						rs.append(line + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, Strings.connotReadFileErrorMsg, Strings.error, showErrorDialog);
				}
				
				TP.setText(rs.toString());
				changed = false;
				setTitle(Strings.appName + " - " + file.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e, Strings.error, showErrorDialog);
		}
	}

	
	private void saveAs(String dialogTitle) {
		JFileChooser dialog = new JFileChooser(System.getProperty("user.home"));
		dialog.setDialogTitle(dialogTitle);
		int result = dialog.showSaveDialog(this);
		if (result != approve)
			return;
		file = dialog.getSelectedFile();
		try (PrintWriter writer = new PrintWriter(file);){
			writer.write(TP.getText());
			changed = false;
			setTitle(Strings.appName + " - " + file.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		changed = true;
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		changed = true;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		changed = true;
	}
}