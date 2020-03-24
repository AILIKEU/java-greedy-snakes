package com.hhit.tools;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class FrameTools {
	public static void initFrame(JFrame jf, int width, int height, String title) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		jf.setLocation((dimension.width - width) / 2, (dimension.height - height) / 2);
		jf.setTitle(title);
		jf.setSize(width, height);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);

	}
}
