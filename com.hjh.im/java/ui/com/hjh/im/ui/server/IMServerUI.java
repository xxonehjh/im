/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
 */
package com.hjh.im.ui.server;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

import com.hjh.core.util.AWTUtil;

public class IMServerUI {

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {

		AWTUtil.initLookAndFeel();

		IMServerFrame ui = new IMServerFrame();
		ui.setResizable(false);
		ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ui.setSize(500, 500);
		AWTUtil.positionFrameOnScreen(ui, 0.5, 0.4);
		ui.setVisible(true);

	}

}
