package com.infy;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;

public class FileExtractorTool extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<String> filesList = new LinkedList<String>();
	List<String> propsList = new LinkedList<String>();
	
   public FileExtractorTool() {
	   JFrame mainFrame = new JFrame("Extract Files");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(350, 200);

    Container c = mainFrame.getContentPane();
    c.setLayout(new FlowLayout(FlowLayout.CENTER,50,50));
    
    JButton openButton = new JButton("Open List");
    JButton saveButton = new JButton("Properties List");
    JButton dirButton = new JButton("Execute");
    JLabel statusbar = 
                 new JLabel("Output of your selection will go here");

    // Create a file chooser that opens up as an Open dialog
    openButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent ae) {
    		JFileChooser chooser = new JFileChooser("C:\\Temp");
    		int option = chooser.showOpenDialog(FileExtractorTool.this);
    		if (option == JFileChooser.APPROVE_OPTION) {
    			File[] sf = chooser.getSelectedFiles();
    			System.out.println(chooser.getSelectedFile());
    			try {
    				Scanner scanner =  new Scanner(Paths.get(chooser.getSelectedFile().getAbsolutePath()),StandardCharsets.UTF_8.name());
    				while (scanner.hasNextLine()){
    					filesList.add(scanner.nextLine().substring(1).trim());
    				} 
    				System.out.println(filesList.size());
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    			openButton.setEnabled(false);
    			statusbar.setText("Selected File: " + chooser.getSelectedFile());
    		}
    		else {
    			statusbar.setText("You canceled.");
    		}
    	}
    });

    // Create a file chooser that opens up as a Save dialog
    saveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        JFileChooser chooser = new JFileChooser("C:\\Temp");
        int option = chooser.showOpenDialog(FileExtractorTool.this);
        if (option == JFileChooser.APPROVE_OPTION) {
          statusbar.setText("You choose " + ((chooser.getSelectedFile()!=null)?
                            chooser.getSelectedFile().getName():"nothing"));
          
          System.out.println(chooser.getSelectedFile());
			try {
				Scanner scanner =  new Scanner(Paths.get(chooser.getSelectedFile().getAbsolutePath()),StandardCharsets.UTF_8.name());
				
				while (scanner.hasNextLine()){
					propsList.add(scanner.nextLine().trim());
				} 
				saveButton.setEnabled(false);
				statusbar.setText("Selected File: " + chooser.getSelectedFile());
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        else {
          statusbar.setText("You canceled.");
        }
      }
    });

    // Create a file chooser that allows you to pick a directory
    // rather than a file
    dirButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent ae) {
    		try{
    			StringBuffer sb = new StringBuffer();
    			List<String> properties = new LinkedList<String>();
    			for(String props:propsList){
    				if((props.contains("%%%DMGINDXBEG%%%")	|| props.contains("%%%DMG2NDXBEG%%%"))){
    					if(sb.length()>0) {
    						properties.add(sb.toString());
    						sb=new StringBuffer();
    					}
    					sb.append(props);
    					sb.append(System.getProperty("line.separator"));

    				}else{
    					sb.append(props);
    					sb.append(System.getProperty("line.separator"));
    				}
    			}
    			properties.add(sb.toString());

    			for(int i=0;i<filesList.size();i++){
    				PrintWriter  buf=new PrintWriter (
    						new FileWriter ("C:/Temp/Generate/" + filesList.get(i) + ".txt")  );
    				for(int j=0;j<properties.size();j++){
    					if(i==j){
    						buf.write(properties.get(j));
    						buf.flush();
    						buf.close();
    					}
    				}
    			}
    			JOptionPane.showMessageDialog(mainFrame,"Successfully Executed!","Alert",JOptionPane.INFORMATION_MESSAGE);
    			statusbar.setText("Execution successful");
    			
    		}catch(Exception e){
    			e.printStackTrace();
    			JOptionPane.showMessageDialog(mainFrame,"Failed Executing, please check logs!","Warning",JOptionPane.WARNING_MESSAGE);
    			statusbar.setText("Execution failed");
    		}
    	}
    });

    c.add(openButton);
    c.add(saveButton);
    c.add(dirButton);
    c.add(statusbar);
    mainFrame.pack();
    mainFrame.setLocationRelativeTo(null);
	mainFrame.setVisible(true);
  }

  public static void main(String args[]) {
	  FileExtractorTool fet = new FileExtractorTool();
  }
  
}