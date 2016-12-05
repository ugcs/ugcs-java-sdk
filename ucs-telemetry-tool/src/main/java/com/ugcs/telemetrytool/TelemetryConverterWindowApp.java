package com.ugcs.telemetrytool;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.List;

import javax.swing.*;

public class TelemetryConverterWindowApp extends JFrame implements DropTargetListener {

	private final JLabel label;

	public TelemetryConverterWindowApp() {
		super("Telemetry converter");
		setSize(600, 400);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		label = new JLabel("Drag & drop telemetry file here!", SwingConstants.CENTER);

		add(label);
		getContentPane().add(BorderLayout.CENTER, label);

		new DropTarget(label, this);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setVisible(true);
	}

	public void dragEnter(DropTargetDragEvent event) {
		System.out.println("dragEnter");
	}

	public void dragExit(DropTargetEvent event) {
		System.out.println("Source: " + event.getSource());
		System.out.println("dragExit");
	}

	public void dragOver(DropTargetDragEvent event) {
		System.out.println("dragOver");
	}

	public void dropActionChanged(DropTargetDragEvent event) {
		System.out.println("dropActionChanged");
	}

	public void drop(DropTargetDropEvent event) {
		event.acceptDrop(DnDConstants.ACTION_COPY);
		StringBuilder messageText = new StringBuilder("Telemetry save files:\n");
		try {
			Transferable transfer = event.getTransferable();
			DataFlavor[] flavors = transfer.getTransferDataFlavors();
			for (DataFlavor flavor : flavors) {
				if (flavor.isFlavorJavaFileListType()) {
					List list = (List) transfer.getTransferData(flavor);
					for (Object aList : list) {
						messageText.append(aList.toString().substring(0, aList.toString().lastIndexOf('.'))).append(".csv\n");
					}
					event.dropComplete(true);
					JOptionPane.showMessageDialog(null, messageText);
					return;
				}
			}
			System.out.println("Drop failed: " + event);
			event.rejectDrop();
		} catch (Exception e) {
			e.printStackTrace();
			event.rejectDrop();
		}
	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TelemetryConverterWindowApp();
			}
		});
	}
}