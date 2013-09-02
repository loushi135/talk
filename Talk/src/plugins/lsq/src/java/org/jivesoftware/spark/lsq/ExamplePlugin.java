package org.jivesoftware.spark.lsq;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.text.BadLocationException;

import org.jivesoftware.MainWindow;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.spark.ChatManager;
import org.jivesoftware.spark.ChatMessageHandler;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.component.tabbedPane.SparkTabbedPane;
import org.jivesoftware.spark.plugin.ContextMenuListener;
import org.jivesoftware.spark.plugin.Plugin;
import org.jivesoftware.spark.search.SearchManager;
import org.jivesoftware.spark.ui.ChatContainer;
import org.jivesoftware.spark.ui.ChatRoom;
import org.jivesoftware.spark.ui.ChatRoomButton;
import org.jivesoftware.spark.ui.ChatRoomClosingListener;
import org.jivesoftware.spark.ui.ChatRoomListenerAdapter;
import org.jivesoftware.spark.ui.ChatRoomNotFoundException;
import org.jivesoftware.spark.ui.ContactItem;
import org.jivesoftware.spark.ui.ContactList;
import org.jivesoftware.spark.ui.GlobalMessageListener;
import org.jivesoftware.spark.ui.TranscriptWindow;

public class ExamplePlugin implements Plugin{

	@Override
	public void initialize() {
		System.out.println("212");
		System.out.println("Welcome To Spark");
		this.addChatRoomButton();
		this.addTabToSpark();
		this.addContactMenuRightClick();
		this.addMenuRightClickToChatRoom();
		this.addMenuToSpark();
		this.noticeMessage();
		SearchManager searchManager = SparkManager.getSearchManager();
		searchManager.addSearchService(new SearchMe());
	}

	@Override
	public void shutdown() {
		System.out.println("212");
		System.out.println("Welcome To Spark");
	}

	@Override
	public boolean canShutDown() {
		return true;
	}

	@Override
	public void uninstall() {
		
	}

	private void addChatRoomButton(){
		ChatManager chatManager = SparkManager.getChatManager();
		
		chatManager.addChatRoomListener(new ChatRoomListenerAdapter() {

			@Override
			public void chatRoomOpened(final ChatRoom room) {
				final ChatRoomButton button = new ChatRoomButton("click me");
				final ChatRoomButton btn = new ChatRoomButton("fuck it!");
				room.getToolBar().addChatRoomButton(button);
				room.addEditorComponent(btn);
				button.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						JOptionPane.showMessageDialog(room.getParent(), "you clicked me!"+room.getRoomTitle());
					}
				});
				btn.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						JOptionPane.showMessageDialog(room.getParent(), "you fucked it!"+room.getRoomTitle());
					}
				});
				room.addClosingListener(new ChatRoomClosingListener() {
					public void closing() {
						room.getToolBar().removeChatRoomButton(button);;
					}
				});
			}
			
		});
	}
	
	private void noticeMessage(){
		final ChatManager chatManager = SparkManager.getChatManager();
		chatManager.addChatMessageHandler(new ChatMessageHandler() {
			
			@Override
			public void messageReceived(Message message) {
				ChatContainer chatContainer = chatManager.getChatContainer();
				ChatRoom currentRoom;
				try {
					currentRoom = chatContainer.getActiveChatRoom();
					chatContainer.startFlashing(currentRoom.getParent(),true, "new message", "notice");
				} catch (ChatRoomNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		chatManager.addGlobalMessageListener(new GlobalMessageListener() {
			
			@Override
			public void messageSent(ChatRoom room, Message message) {

			}
			
			@Override
			public void messageReceived(ChatRoom room, Message message) {
				
			}
		});
	}
	
	private void addTabToSpark(){
		Workspace workspace = SparkManager.getWorkspace();
		
		SparkTabbedPane tabbedPane = workspace.getWorkspacePane();
		
		tabbedPane.addTab("my plugin",new Icon() {
			
			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public int getIconWidth() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getIconHeight() {
				// TODO Auto-generated method stub
				return 0;
			}
		},new JButton("Hello"));
		
		
	}
	
	private void addContactMenuRightClick(){
		Workspace workspace = SparkManager.getWorkspace();
		
		ContactList contactList = workspace.getContactList();
		
		final Action sayHelloAction = new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(SparkManager.getMainWindow(), "welcome to spark");
			}
		};
		sayHelloAction.putValue(Action.NAME, "Say Hello to Me");
		contactList.addContextMenuListener(new ContextMenuListener() {
			
			@Override
			public void poppingUp(Object object, JPopupMenu popup) {
				if(object instanceof ContactItem){
					popup.add(sayHelloAction);
				}
			}
			
			@Override
			public void poppingDown(JPopupMenu popup) {
				
			}
			
			@Override
			public boolean handleDefaultAction(MouseEvent e) {
				return false;
			}
		});
	}
	
	private void addMenuRightClickToChatRoom(){
		ChatManager chatManager = SparkManager.getChatManager();
		
		 // Add a ChatRoomListener to the ChatManager to allow for notifications
        // when a room is being opened. Note: I will use a ChatRoomListenerAdapter for brevity.
		chatManager.addChatRoomListener(new ChatRoomListenerAdapter() {

			@Override
			public void chatRoomOpened(final ChatRoom room) {
				room.getTranscriptWindow().addContextMenuListener(new ContextMenuListener() {
					
					@Override
					public void poppingUp(Object object, JPopupMenu popup) {
						final TranscriptWindow chatWindow = (TranscriptWindow)object;
						Action clearAction = new AbstractAction() {
							@Override
							public void actionPerformed(ActionEvent e) {
								try {
									chatWindow.insert("My own text in room:"+room.getRoomTitle()+"\n");
								} catch (BadLocationException e1) {
									e1.printStackTrace();
								}
							}
						};
						clearAction.putValue(Action.NAME, "Insert my own text");
						popup.add(clearAction);
					}
					
					@Override
					public void poppingDown(JPopupMenu popup) {
					}
					
					@Override
					public boolean handleDefaultAction(MouseEvent e) {
						return false;
					}
				});
			}
			
		});
		
	}
	
	public void addMenuToSpark(){
		final MainWindow mainWindow = SparkManager.getMainWindow();
		
		JMenu myPluginMenu = new JMenu("My plugin menu");
		
		Action showMessage = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(mainWindow, "Yeah, It works.");
			}
		};
		showMessage.putValue(Action.NAME, "check if it works");
		//contactadmin button
		Action contactAdmin = new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				/**
			     * Creates a person to person Chat Room and makes it the active chat.
			     */
				// Get the ChatManager from Sparkmanager
				ChatManager chatManager = SparkManager.getChatManager();
				// Create the room.
		        ChatRoom chatRoom = chatManager.createChatRoom(SparkManager.getUserManager().getFullJID("admin"), "admin", "admin");
		        // If you wish to make this the active chat room.
		        
		        // Get the ChatContainer (This is the container for all Chat Rooms)
		        ChatContainer chatContainer = chatManager.getChatContainer();
		        
		        // Ask the ChatContainer to make this chat the active chat.
		        chatContainer.activateChatRoom(chatRoom);
			}
		};
		contactAdmin.putValue(Action.NAME, "contact to admin");
		
		Action createConferenceRoom = new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get the ChatManager from Sparkmanager
		        ChatManager chatManager = SparkManager.getChatManager();

		        Collection serviceNames = null;

		        // Get the service name you wish to use.
		        try {
		            serviceNames = MultiUserChat.getServiceNames(SparkManager.getConnection());
		        }
		        catch (XMPPException ex) {
		            ex.printStackTrace();
		        }

		        // Create the room.
		        ChatRoom chatRoom = chatManager.createConferenceRoom("BusinessChat", (String)serviceNames.toArray()[0]);

		        // If you wish to make this the active chat room.

		        // Get the ChatContainer (This is the container for all Chat Rooms)
		        ChatContainer chatContainer = chatManager.getChatContainer();

		        // Ask the ChatContainer to make this chat the active chat.
		        chatContainer.activateChatRoom(chatRoom);
			}
		};
		createConferenceRoom.putValue(Action.NAME, "create a public conference room");
		
		Action sendIQTestrr = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					SendIQTestrr.run();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		};
		sendIQTestrr.putValue(Action.NAME, "test my IQ");
		myPluginMenu.add(showMessage);
		myPluginMenu.add(contactAdmin);
		myPluginMenu.add(createConferenceRoom);
		myPluginMenu.add(sendIQTestrr);
		mainWindow.getJMenuBar().add(myPluginMenu);
	}
}
