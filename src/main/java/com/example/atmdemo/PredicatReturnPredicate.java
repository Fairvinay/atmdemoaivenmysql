package com.example.atmdemo;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Predicate;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class PredicatReturnPredicate {
	private static final String NEW_LINE = "new-line";
	 public static Predicate lowerCase (Predicate f ) {
		  
		 Predicate  ft = 
				 ( Object r) -> {  
					  // length()> 6;
					    String g = ((String)r).toLowerCase();
					       
					  return ((String) r).equals(g);
				   };
		   return f.and(ft);
	 }
	 public static void main(String[] args) {
		  
		 Predicate<String> threeLetter = (String a) -> { 
			         return a.length()>=3;
		 };
		 
		 System.out.println("(this) is valid 3 letter lowercase word  "+lowerCase(threeLetter).test("this"));
		 String html = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" /> </head><center><b>You can make bold the font with tag b</b><br>" +
	    		  "<i>You can make italic the font with tag i</i><br>"+"<font color=#42bcf4>You can change the color</font><br>"+
				  "<input name=\"name\" value=\" name\"> </input>"+
	    		  "<i><b><font color=#42bcf4>You can combine the tags</font></b></i><br>...";
		 /*
	      */
	      JEditorPane jEditorPane = new JEditorPane();
	      jEditorPane.setEditable( true );

	      HTMLEditorKit kit = new HTMLEditorKit();
	      jEditorPane.setEditorKit(kit);

	      try {    
	          //kit.getStyleSheet().importStyleSheet( new URL( "file://D:\\mycssfile.css" ) );
	          kit.getStyleSheet().importStyleSheet(new URL("file:///C:/TaskManagerStarter/TaskManagerApp/src/styles.css"));
	      } catch( MalformedURLException ex ) {
	    	  System.out.println("MarformExpection "+ex.getMessage());
	      }

	     
	      
	      
	      JFrame f=new JFrame();
	      UIManager.put("swing.boldMetal", false);
	      //jEditorPane.add(f);
	      f.setSize(400, 200);  
	      JLabel l=new JLabel(html);
	     // jEditorPane.add(l);
	      //f.getContentPane().add(l);
	     // f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      f.pack();
	     // f.setLocationRelativeTo(l);
	     // f.setVisible(true);
	      
	      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	        f.setSize(400, 200);  
	        
	        jEditorPane.setContentType( "text/html" );
	        Document doc = (HTMLDocument) kit.createDefaultDocument();
	        HTMLDocument hdoc = (HTMLDocument)doc;
	        try {
				kit.insertHTML(hdoc, hdoc.getLength(),html,0,0,null);
			} catch (BadLocationException | IOException e) {
				 
				System.out.println("BadLocationException "+e.getMessage());
			}
		      jEditorPane.setDocument(hdoc);
		      jEditorPane.setText(html); 
	       // HTMLDocument doc = (HTMLDocument)jEditorPane.getDocument();
	        //kit.insertHTML(doc, doc.getLength(), html, 0, 0, null); 
	       
		      InputMap input = jEditorPane.getInputMap();
		      KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
		      input.put(shiftEnter, NEW_LINE);

		      ActionMap actions = jEditorPane.getActionMap();
		      actions.put(NEW_LINE, new AbstractAction() {
		          @Override
		          public void actionPerformed(ActionEvent e) {
		              try {
		                  kit.insertHTML((HTMLDocument)jEditorPane.getDocument(), jEditorPane.getCaretPosition(),
		                          "<br>", 0,0, HTML.Tag.BR);
		                  jEditorPane.setCaretPosition(jEditorPane.getCaretPosition()); // This moves caret to next line
		              } catch (BadLocationException | IOException ex) {
		                  ex.printStackTrace();
		              }
		          }
		      });
	        /*jEditorPane.setText("Sleeping is necessary for a healthy body."  
	                + " But sleeping in unnecessary times may spoil our health, wealth and studies."  
	                + " Doctors advise that the sleeping at improper timings may lead for obesity during the students days.");  
	       */ f.setContentPane(jEditorPane);  
	        f.setVisible(true);  
	      
	      
	      
	      
	      
	      
	      
		 
	}
}
