package game1;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import net.miginfocom.swing.MigLayout;

public class UndergroundKingdom implements ActionListener
{
	//Create JPanel with JFrame to hold it
	JFrame frmUndergroundKingdom = new JFrame("Blank Page");
	static JPanel panel = new JPanel();
	static JTextPane pane = new JTextPane(); //Creates the actual pane
	static JScrollPane jsp = new JScrollPane(pane);
	
	static int dir1 = 1;//the number of the page the button will be directing to
	static int dir2 = 0;
	static int dir3 = 0;
	static int pageNum = 0;
	static int choiceCounter = -1;
	
	static Font appleII = new Font("Calibri", Font.BOLD, 40);
	static final Color AMBER = new Color(239, 127, 27);

	
	static JButton option1 = new JButton("Page 1");
	static JButton option2 = new JButton();
	static JButton option3 = new JButton();
	static JButton backButton = new JButton("Back");
	
	static ArrayList<String> page = new ArrayList<String>();
	static ArrayList<String> buttonInfo = new ArrayList<String>();
	static ArrayList<Integer> choices = new ArrayList<Integer>();
	
	//Create default constructor to initialize layout of pane in frame
	public UndergroundKingdom()
	{
		panel.setLayout(new MigLayout());
		panel.add(jsp, "wrap, push");
		panel.add(option1, "split4, align center");
		panel.add(option2, "hidemode 3");
		panel.add(option3, "hidemode 3");
		panel.add(backButton, "");
		
		option1.addActionListener(this);
		option2.addActionListener(this);
		option3.addActionListener(this);
		backButton.addActionListener(this);
		
		jsp.setBorder(null);
		frmUndergroundKingdom.getContentPane().add(panel);
		frmUndergroundKingdom.setTitle("Underground Kingdom");
		frmUndergroundKingdom.setResizable(false);
		frmUndergroundKingdom.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//frame.setUndecorated(true); //<- potentially cool
		frmUndergroundKingdom.setSize(650,800);
		frmUndergroundKingdom.setLocationRelativeTo(null);
		frmUndergroundKingdom.setVisible(true);
	}
	
	public static void createAIIFont()
	{
		try
		{
			appleII = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("src/font/PRNumber3.ttf"))).deriveFont(Font.PLAIN, 20);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static void paneStyle()
	{
		pane.setFont(appleII);
		pane.setBackground(Color.BLACK);
		panel.setBackground(Color.BLACK);
		
		//Set button colors, comment out to make moving the layout easier
		option1.setFont(appleII);
		option1.setBackground(Color.BLACK);
		option1.setForeground(AMBER);
		option1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		option2.setFont(appleII);
		option2.setBackground(Color.BLACK);
		option2.setForeground(AMBER);
		option2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		option3.setFont(appleII);
		option3.setBackground(Color.BLACK);
		option3.setForeground(AMBER);
		option3.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		backButton.setFont(appleII);
		backButton.setBackground(Color.BLACK);
		backButton.setForeground(AMBER);
		backButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
	}
	
	public static String getText(String fileName) throws FileNotFoundException, IOException 
	{
		InputStream in = new FileInputStream(new File(fileName));		
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));	
	    StringBuilder out = new StringBuilder();	    
	    String line;
	    
	    while ((line = reader.readLine()) != null) 
	    {
	        out.append(line);
	    }
	    
	    reader.close();
	    return out.toString();
	}
	
	public static void setText(String text)
	{
        SimpleAttributeSet attribs = new SimpleAttributeSet();

        StyleConstants.setAlignment(attribs , StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontSize(attribs, 20);
        StyleConstants.setForeground(attribs, AMBER);
        StyleConstants.setFontFamily(attribs, appleII.getFamily());
        
        int len = pane.getDocument().getLength();
        pane.setCaretPosition(len);
        pane.setParagraphAttributes(attribs,true);
        pane.replaceSelection(text);
		
	}
	
	public static void parsePages() throws FileNotFoundException, IOException
	{
		String toParse = UndergroundKingdom.getText("src/textfiles/AllPages.txt");
		String tempHold;
		String tempPage;
		//Parse page breaks and write pages to the page array
		while (toParse.indexOf("\\p") != -1)
		{			
			page.add(new String(toParse.substring(0, toParse.indexOf("\\p"))));
			tempHold = toParse.substring(toParse.indexOf("\\p") + 2);
			toParse = tempHold;
		}
		//Parse line break System.getProperty("line.separator");
		for (int i=0; i < page.size(); i++)
		{
			tempPage = page.get(i);
			tempPage = tempPage.replace("\\n", System.getProperty("line.separator"));
			page.set(i, tempPage);
		}
		
		
	}
	
	public static void parseButtons() throws FileNotFoundException, IOException
	{//Parse PageNumbers.txt into an array of format 123 where 123 are where the page points. 0 for non-visible.
		String toParse = UndergroundKingdom.getText("src/textfiles/PageNumbers.txt");
		for (int i = 0; i < page.size(); i++)
		{
			if (i < 10)
				buttonInfo.add(toParse.substring(toParse.indexOf("#" + i) + 3, toParse.indexOf("#" + i) + 12));
			else if (i < 99)
				buttonInfo.add(toParse.substring(toParse.indexOf("#" + i) + 4, toParse.indexOf("#" + i) + 13));
			else if (i > 99)
				buttonInfo.add(toParse.substring(toParse.indexOf("#" + i) + 5, toParse.indexOf("#" + i) + 14));
		}
	}
	
	public static void setButt(int pageNumber)
	{
		String tempInfo = buttonInfo.get(pageNumber); //tempInfo now contains 9 char number code
		int butt1 = Integer.parseInt(tempInfo.substring(0,3));
		int butt2 = Integer.parseInt(tempInfo.substring(3,6));
		int butt3 = Integer.parseInt(tempInfo.substring(6,9));
		
		dir1 = butt1;
		
		if (butt2 == 0)
		{
			option2.setVisible(false);
		}
		else
		{
			option2.setVisible(true);
			dir2 = butt2;	
		}
		
		if (butt3 == 0)
		{
			option3.setVisible(false);
		}
		else
		{
			option3.setVisible(true);
			dir3 = butt3;	
		}
		
		if (pageNumber == 0)
		{
			backButton.setVisible(false);
		}
		else
		{
			backButton.setVisible(true);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent evt)
	{
		Object src = evt.getSource();
		if(src == option1)
			{
				choiceCounter++;
				choices.add(pageNum);
				pane.setText(page.get(dir1));
				pageNum = dir1;
				setButt(pageNum);
				option1.setText("Page " + dir1);
				option2.setText("Page " + dir2);
				option3.setText("Page " + dir3);
							}
		if(src == option2)
			{
				choiceCounter++;
				choices.add(pageNum);
				pane.setText(page.get(dir2));
				pageNum = dir2;
				setButt(pageNum);
				option1.setText("Page " + dir1);
				option2.setText("Page " + dir2);
				option3.setText("Page " + dir3);
				choiceCounter++;
				choices.add(pageNum);
			}
		if(src == option3)
			{
				choiceCounter++;
				choices.add(pageNum);
				pane.setText(page.get(dir3));
				pageNum = dir3;
				setButt(pageNum);
				option1.setText("Page " + dir1);
				option2.setText("Page " + dir2);
				option3.setText("Page " + dir3);
				choiceCounter++;
				choices.add(pageNum);
			}
		if (src == backButton)
			{
				pane.setText(page.get(choices.get(choiceCounter)));
				setButt(choices.get(choiceCounter));
				option1.setText("Page " + dir1);
				option2.setText("Page " + dir2);
				option3.setText("Page " + dir3);
				choiceCounter--;
			}
	}
	
    public static void main(String... args) throws FileNotFoundException, IOException 
    {
    	createAIIFont();
    	parsePages();
    	parseButtons();
    	
    	setButt(pageNum);
    	setText(page.get(pageNum));
    	
    	paneStyle();
    	pane.setEditable(false);
    	
    	new UndergroundKingdom();
    }
	
}
