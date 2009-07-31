package com.rs2hd.tools;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.rs2hd.util.XStreamUtil;

@SuppressWarnings("serial")
/**
 * GUI for installing/removing/updating packages.
 * @author Graham
 * 
 */
public class PackageManager extends JFrame {
	
	public static void main(String[] args) {
		try {
			File f = new File("versions.xml");
			if(!f.exists()) {
				if(f.createNewFile()) {
					BufferedWriter w = new BufferedWriter(new FileWriter(f));
					w.write("<map/>");
					w.flush();
					w.close();
				}
			}
			new PackageManager(args[0]);
		} catch(IOException ex) {
			JOptionPane.showMessageDialog(null, "Error: " + ex.getClass().getName() + ": " + ex.getMessage() + ".");
			System.exit(0);
		}
	}
	
	private Map<String, Package> packages;
	private Map<String, Integer> versions;
	private URL base;
	private JPanel table;
	private JButton updateButton;
	
	@SuppressWarnings("unchecked")
	public PackageManager(String base) throws IOException {
		super("Package Manager");
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				saveVersions();
			}
		}));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.base = new URL(base);
		showAlert("Parsing versions.xml...");
		this.versions = (Map<String, Integer>) XStreamUtil.getXStream().fromXML(new FileInputStream("versions.xml"));
		hideAlert();
		showAlert("Downloading and parsing site.xml...");
		this.packages = (Map<String, Package>) XStreamUtil.getXStream().fromXML(new URL(base+"site.xml").openStream());
		hideAlert();
		this.setLayout(new BorderLayout());
		JLabel server = new JLabel("PACKAGE SERVER: " + this.base);
		server.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		this.add(BorderLayout.NORTH, server);
		table = new JPanel();
		table.setLayout(new GridLayout(0, 5));
		updateTable();
		this.add(BorderLayout.CENTER, new JScrollPane(table));
		updateButton = new JButton();
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					public void run() {
						update();
					}
				}).start();
			}
		});
		updateUpdateButton();
		this.add(BorderLayout.SOUTH, updateButton);
		this.setSize(720, 300);
		this.setVisible(true);
		this.toFront();
	}
	
	private void saveVersions() {
		try {
			XStreamUtil.getXStream().toXML(versions, new FileOutputStream("versions.xml"));
		} catch(Exception ex) {}
	}
	
	private void update() {
		for(Package pkg : getPackagesToUpdate()) {
			toggle(pkg, true);
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				updateUpdateButton();
			}
		});
	}
	
	private void updateUpdateButton() {
		int total = getPackagesToUpdate().size();
		updateButton.setText("Update packages (" + total + " update" + (total != 1 ? "s" : "") + " currently available)");
		updateButton.setEnabled(total > 0);
	}

	private Collection<Package> getPackagesToUpdate() {
		List<Package> toUpdate = new ArrayList<Package>();
		for(Package pkg : packages.values()) {
			if(pkg.isInstalled() && versions.containsKey(pkg.key)) {
				int curVer = pkg.version;
				int ourVer = versions.get(pkg.key);
				if(curVer > ourVer) {
					toUpdate.add(pkg);
				}
			}
		}
		return toUpdate;
	}
	
	private void updateTable() {
		table.removeAll();
		JLabel l = new JLabel("Name");
		l.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		table.add(l);
		l = new JLabel("Author");
		l.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		table.add(l);
		l = new JLabel("Version");
		l.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		table.add(l);
		l = new JLabel("Installed");
		l.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		table.add(l);
		l = new JLabel("Actions");
		l.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		table.add(l);
		for(final Package pkg : packages.values()) {
			JLabel pname = new JLabel(pkg.name);
			pname.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			pname.setToolTipText(pkg.description);
			table.add(pname);
			JLabel author = new JLabel(pkg.author);
			author.setFont(author.getFont().deriveFont(Font.PLAIN));
			author.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			table.add(author);
			JLabel ver = new JLabel(""+pkg.version);
			ver.setFont(ver.getFont().deriveFont(Font.PLAIN));
			ver.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			table.add(ver);
			JLabel iver = new JLabel(versions.containsKey(pkg.key) ? ""+versions.get(pkg.key) : "N/A");
			iver.setFont(iver.getFont().deriveFont(Font.PLAIN));
			iver.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			table.add(iver);
			JButton install = new JButton(pkg.isInstalled() ? "Remove" : "Install");
			install.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					new Thread(new Runnable() {
						public void run() {
							toggle(pkg);
						}
					}).start();
				}
			});
			table.add(install);
		}
		table.validate();
		table.repaint();
	}

	private JFrame alert = null;
	
	private void toggle(Package pkg) {
		if(pkg.isInstalled()) {
			toggle(pkg, false);
		} else {
			toggle(pkg, true);
		}
	}
	
	private void toggle(Package pkg, boolean install) {
		try {
			if(!install) {
				List<Package> removed = new ArrayList<Package>();
				for(Package pkg2 : packages.values()) {
					if(pkg2.isInstalled() && Arrays.asList(pkg2.dependencies).contains(pkg.key)) {
						removed.add(pkg2);
					}
				}
				int result = JOptionPane.YES_OPTION;
				if(removed.size() > 0) {
					String removedString = "";
					for(Package pkgrm : removed) {
						removedString += " - " + pkgrm.name + "\r\n";
					}
					result = JOptionPane.showConfirmDialog(this, "If you remove the " + pkg.name + " package, the following will also be removed automatically:\r\n\r\n"+removedString+"\r\nAre you sure you wish to continue?", "Continue installing?", JOptionPane.YES_NO_OPTION);
				}
				if(result == JOptionPane.YES_OPTION) {
					pkg.getFile().delete();
					versions.remove(pkg.key);
					for(Package rm : removed) {
						toggle(rm, false);
					}
				}
			} else {
				showAlert((pkg.isInstalled() ? "Updating " : "Installing ") + pkg.key + "...");
				try {
					URL remote = new URL(base+pkg.key+".py");
					InputStream is = remote.openStream();
					File local = pkg.getFile();
					OutputStream os = new FileOutputStream(local);
					while(is.available() > 0) {
						byte[] data = new byte[2048];
						int len = is.read(data);
						if(len > -1) {
							os.write(data, 0, len);
						} else {
							break;
						}
					}
					os.flush();
					is.close();
					os.close();
					versions.put(pkg.key, pkg.version);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Error: " + ex.getClass().getName() + ": " + ex.getMessage() + ".");
				} finally {
					hideAlert();
					for(String dep : pkg.dependencies) {
						if(packages.containsKey(dep)) {
							if(!packages.get(dep).isInstalled()) {
								toggle(packages.get(dep), true);
							}
						}
					}
				}
			}
		} finally {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					updateTable();
					updateUpdateButton();
				}
			});
			saveVersions();
		}
	}
	
	private void showAlert(final String str) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(alert != null) {
					alert.setVisible(false);
					alert = null;
				}
				alert = new JFrame("Status") {{
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					JLabel lbl = new JLabel(str);
					lbl.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
					add(lbl);
					pack();
					setVisible(true);
					toFront();
				}};
			}
		});
	}
	
	private void hideAlert() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(alert == null) {
					return;
				}
				alert.setVisible(false);
				alert = null;
			}
		});
	}

}
