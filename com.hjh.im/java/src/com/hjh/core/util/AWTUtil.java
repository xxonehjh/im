/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.core.util;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Window;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * AWT工具类
 */
public class AWTUtil {

	public static void initLookAndFeel() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		// "javax.swing.plaf.metal.MetalLookAndFeel"
		UIManager
				.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	}

	/**
	 * 窗口居中显示
	 */
	public static void positionFrameOnScreen(final Window frame,
			final double horizontalPercent, final double verticalPercent) {

		final Rectangle s = frame.getGraphicsConfiguration().getBounds();
		final Dimension f = frame.getSize();
		final int w = Math.max(s.width - f.width, 0);
		final int h = Math.max(s.height - f.height, 0);
		final int x = (int) (horizontalPercent * w) + s.x;
		final int y = (int) (verticalPercent * h) + s.y;
		frame.setBounds(x, y, f.width, f.height);

	}

}
