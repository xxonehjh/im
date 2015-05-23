/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
 */
package com.hjh.im.ui.client;

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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.hjh.core.util.LogHelper;
import com.hjh.im.core.IMConfig;
import com.hjh.im.core.IMFactory;
import com.hjh.im.core.client.IMClient;
import com.hjh.im.core.client.IMClientHandler;

public class IMClientFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = -4362210855099327632L;

	private JTextPane talkcontent;
	private JTextArea words;
	private JComboBox<String> users;

	private IMClient client;

	private JTextField name;
	private JTextField ip;
	private JTextField port;
	private JButton btn_login;
	private JButton btn_logout;
	private JButton btn_send;

	private SimpleAttributeSet text_att_me;
	private SimpleAttributeSet text_att_me_msg;
	private SimpleAttributeSet text_att_from;
	private SimpleAttributeSet text_att_from_msg;

	public IMClientFrame() throws IOException {

		super("IM客户端");

		setLayout(new BorderLayout());

		JPanel top = new JPanel();
		this.add(top, BorderLayout.NORTH);
		fillTop(top);

		JPanel content = new JPanel();
		this.add(content, BorderLayout.CENTER);
		fillContent(content);

		name.setText("昵称");
		ip.setText("" + IMConfig.IP);
		port.setText("" + IMConfig.PORT);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				logout();
			}

		});

	}

	public void setVisible(boolean b) {
		super.setVisible(b);
		name.requestFocus();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == btn_login) {
			String nameStr = name.getText().trim();
			if (0 != nameStr.length()) {
				try {
					this.login(nameStr);
					btn_login.setEnabled(false);
					btn_logout.setEnabled(true);
					btn_send.setEnabled(true);
				} catch (IOException e) {
					LogHelper.error(e);
					JOptionPane.showMessageDialog(this,
							"登陆失败:" + e.getMessage());
				}
			}
		} else if (arg0.getSource() == btn_logout) {
			this.logout();
		}
	}

	private void logout() {
		if (null != client) {
			try {
				client.leave();
			} catch (IOException e) {
				LogHelper.error(e);
			}
		}
	}

	private synchronized void login(String name) throws IOException {
		if (null == client) {
			final IMClient m_client = IMFactory.getInstance().createClient();
			m_client.join(ip.getText(), Integer.parseInt(port.getText()),
					new IMClientHandler() {

						@Override
						public void from(String who, String msg) {
							if (client.getId().equals(who)) {
								return;
							}
							try {
								talkcontent.getDocument().insertString(
										talkcontent.getDocument().getLength(),
										who + ":\r\n", text_att_from);
								talkcontent.getDocument().insertString(
										talkcontent.getDocument().getLength(),
										msg + "\r\n", text_att_from_msg);
								talkcontent.setCaretPosition(talkcontent
										.getDocument().getLength());
							} catch (BadLocationException e) {
							}
						}

						@Override
						public void initUsers(String[] p_users) {
							users.addItem(IMConfig.USER_ALL);
							for (String item : p_users) {
								users.addItem(item);
							}
						}

						@Override
						public void join(String who) {
							users.addItem(who);
						}

						@Override
						public void leave(String who) {
							users.removeItem(who);
						}

						@Override
						public void shutdown() {
							users.removeAllItems();
							client = null;
							btn_login.setEnabled(true);
							btn_logout.setEnabled(false);
							btn_send.setEnabled(false);
						}

					});

			client = m_client;
		}

		if (!client.isLogin()) {
			String msg = client.login(name);
			if (null != msg) {
				throw new IOException(msg);
			}
			setTitle(name + "[IM客户端]");
		}

	}

	private void fillTop(JPanel top) {
		top.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 2;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 1, 1, 1);

		name = new JTextField();
		c.gridx++;
		top.add(name, c);

		c.gridx++;
		c.weightx = 5;
		ip = new JTextField();
		top.add(ip, c);

		port = new JTextField();

		c.gridx++;
		c.weightx = 2;
		top.add(port, c);

		btn_login = new JButton();
		btn_login.setText("登陆");

		c.gridx++;
		c.weightx = 1;
		top.add(btn_login, c);

		btn_logout = new JButton();
		btn_logout.setText("注销");

		c.gridx++;
		c.weightx = 1;
		top.add(btn_logout, c);

		btn_logout.addActionListener(this);
		btn_login.addActionListener(this);
		btn_logout.setEnabled(false);
	}

	private void fillContent(JPanel content) {
		content.setLayout(new GridBagLayout());

		talkcontent = new JTextPane();
		talkcontent.setEditable(false);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 3;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 1, 1, 1);

		content.add(new JScrollPane(talkcontent), c);

		users = new JComboBox<String>();
		c.gridy++;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 5, 5);
		content.add(users, c);

		words = new JTextArea();
		words.setLineWrap(true);

		c.gridy++;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 1, 1, 1);
		content.add(new JScrollPane(words), c);

		btn_send = new JButton();
		btn_send.setText(" Send ");
		c.gridy++;
		c.weighty = 0;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(5, 5, 5, 5);
		content.add(btn_send, c);

		initTextStyle();

		btn_send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (null == client) {
					return;
				}

				String to = ((String) users.getSelectedItem()).trim();
				String msg = words.getText();
				if (0 == msg.trim().length()) {
					return;
				}
				try {
					client.say(to, msg);
				} catch (IOException ex) {
					LogHelper.error(ex);
					return;
				}
				try {
					talkcontent.getDocument().insertString(
							talkcontent.getDocument().getLength(),
							"me(" + to + "):\r\n", text_att_me);
					talkcontent.getDocument().insertString(
							talkcontent.getDocument().getLength(),
							msg + "\r\n", text_att_me_msg);
					talkcontent.setCaretPosition(talkcontent.getDocument()
							.getLength());
				} catch (BadLocationException e) {
				}
				words.setText("");
			}

		});

		btn_send.setEnabled(false);
	}

	private void initTextStyle() {

		text_att_me = new SimpleAttributeSet();
		text_att_me_msg = new SimpleAttributeSet();
		text_att_from = new SimpleAttributeSet();
		text_att_from_msg = new SimpleAttributeSet();

		StyleConstants.setForeground(text_att_me, Color.gray);
		StyleConstants.setForeground(text_att_from, Color.blue);

	}

}
