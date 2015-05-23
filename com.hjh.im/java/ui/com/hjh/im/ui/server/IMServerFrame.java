/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
 */
package com.hjh.im.ui.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.hjh.core.util.LogHelper;
import com.hjh.im.core.IMConfig;
import com.hjh.im.core.IMFactory;
import com.hjh.im.core.net.NetUser;
import com.hjh.im.core.server.IMServer;
import com.hjh.im.core.server.SimpleIMServerHandler;

public class IMServerFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 3026262616580905456L;

	private IMServer server;
	private JTextField port;
	private JButton btn_start;
	private JButton btn_close;
	private DefaultListModel<NetUser> usersModel;
	private JList<NetUser> users;

	public IMServerFrame() throws IOException {

		this.setTitle("IM服务器");

		setLayout(new BorderLayout());

		JPanel top = new JPanel();
		this.add(top, BorderLayout.NORTH);
		fillTop(top);

		JPanel content = new JPanel();
		this.add(content, BorderLayout.CENTER);
		fillContent(content);

		port.setText("" + IMConfig.PORT);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				touchClose();
			}

		});

	}

	public void setVisible(boolean b) {
		super.setVisible(b);
		btn_start.requestFocus();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == btn_start) {
			touchStart();
		} else if (arg0.getSource() == btn_close) {
			touchClose();
		}
	}

	private void touchStart() {
		if (null == server) {
			usersModel.clear();
			server = IMFactory.getInstance().createServer();
			try {
				server.start(IMConfig.PORT, new SimpleIMServerHandler() {
					public boolean join(NetUser user, String name) {
						if (super.join(user, name)) {
							usersModel.addElement(user);
							return true;
						}
						return false;
					}

					public void exit(NetUser user, boolean exception) {
						super.exit(user, exception);
						usersModel.removeElement(user);
					}

					public void shutdown() {
						super.shutdown();
						server = null;
						usersModel.clear();
						btn_start.setEnabled(true);
						btn_close.setEnabled(false);
					}

				});
				btn_start.setEnabled(false);
				btn_close.setEnabled(true);
			} catch (IOException e) {
				LogHelper.error(e);
				server = null;
				JOptionPane.showMessageDialog(this, "启动失败:" + e.getMessage());
			}
		}
	}

	private void touchClose() {
		if (null != server) {
			try {
				server.close();
			} catch (IOException e) {
				LogHelper.error(e);
			}
			server = null;
		}
	}

	private void fillTop(JPanel top) {

		top.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.5;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 1, 1, 1);

		top.add(new JLabel("服务端口:"), c);

		port = new JTextField();

		c.gridx++;
		c.weightx = 5;
		top.add(port, c);

		btn_start = new JButton();
		btn_start.setText("启动");

		c.gridx++;
		c.weightx = 1;
		top.add(btn_start, c);

		btn_close = new JButton();
		btn_close.setText("关闭");

		c.gridx++;
		c.weightx = 1;
		top.add(btn_close, c);

		btn_close.addActionListener(this);
		btn_start.addActionListener(this);
		btn_close.setEnabled(false);
	}

	private void fillContent(JPanel content) {

		content.setLayout(new BorderLayout());

		usersModel = new DefaultListModel<NetUser>();
		users = new JList<NetUser>(usersModel);

		Border brd = BorderFactory.createLineBorder(Color.gray);
		users.setBorder(brd);

		content.add(users, BorderLayout.CENTER);
	}

}
