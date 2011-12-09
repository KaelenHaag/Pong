import javax.swing.*;
import java.awt.event.*;

public class PongClientPanel extends JPanel
{
	private PongFrame pf;
	private PongClient pc;
	private JList hostInfoList;
	private DefaultListModel hostInfoListModel;
	private JScrollPane scrollPane;
	private JButton refreshButton;

	public PongClientPanel(PongFrame pf, PongClient pc)
	{
		this.pf = pf;
		this.pc = pc;
		createUserInterface();
	}

	private void createUserInterface()
	{
		setLayout(null);
		hostInfoList = new JList();
		//hostInfoList.setBounds(25, 25, PongFrame.WIDTH - 100, PongFrame.HEIGHT - 100);
		hostInfoListModel = new DefaultListModel();
		scrollPane = new JScrollPane(hostInfoList);
		scrollPane.setBounds((PongFrame.WIDTH / 2) - 100, 25, 200, PongFrame.HEIGHT - 100);
		add(scrollPane);
		hostInfoList.setModel(hostInfoListModel);

		refreshButton = new JButton("Refresh");
		refreshButton.setBounds((PongFrame.WIDTH / 2) - 100, PongFrame.HEIGHT - 50, 200, 25);
		refreshButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				hostInfoListModel.clear();
				pc.requestUpdatedHostInfo();
			}
		});
		add(refreshButton);

	}

	public void removeNotify()
	{
		super.removeNotify();
		pc.disconnect();

	}

	public void addHostInfoToList(String hostName)
	{
		hostInfoListModel.addElement(hostName);
	}
}
