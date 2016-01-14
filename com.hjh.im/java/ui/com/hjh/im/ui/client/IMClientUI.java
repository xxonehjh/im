/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.im.ui.client;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

import com.hjh.core.util.AWTUtil;

public class IMClientUI {

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {

		AWTUtil.initLookAndFeel();

		IMClientFrame imClientUI = new IMClientFrame();
		imClientUI.setResizable(false);
		imClientUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		imClientUI.setSize(500, 500);
		AWTUtil.positionFrameOnScreen(imClientUI, 0.5, 0.4);
		imClientUI.setVisible(true);

	}

}
