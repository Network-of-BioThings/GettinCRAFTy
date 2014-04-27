/*
 *  Copyright (c) 2008--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: TermbankViewer.java 16296 2012-11-20 12:17:11Z adamfunk $
 */
package gate.termraider.gui;

import gate.Resource;
import gate.creole.ANNIEConstants;
import gate.creole.AbstractVisualResource;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.GuiType;
import gate.event.ProgressListener;
import gate.gui.MainFrame;
import gate.swing.JMenuButton;
import gate.swing.XJFileChooser;
import gate.termraider.bank.*;
import gate.termraider.gui.ColorMenu.Callback;
import gate.termraider.output.CloudGenerator;
import gate.termraider.util.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.xhtmlrenderer.simple.XHTMLPanel;


@CreoleResource(name = "Termbank Viewer",
        comment = "viewer for the TermRaider Termbank",
        guiType = GuiType.LARGE,
        mainViewer = true,
        resourceDisplayed = "gate.termraider.bank.AbstractTermbank")
public class TermbankViewer 
  extends AbstractVisualResource 
  implements ANNIEConstants, ProgressListener {

  private static final long serialVersionUID = 3838369793341101794L;
  
  private JPanel controlPanel;
  private SliderPanel sliderPanel;
  private JScrollPane treeScrollPane, freqScrollPane;
  private AbstractTermbank termbank;
  private JTree tree;
  private JTable freqTable;
  private JTabbedPane tabbedPane;
  private FrequencyTableModel freqTableModel;
  
  private XHTMLPanel termCloud = new XHTMLPanel();
  private JComboBox cloudType = new JComboBox(new String[]{"Term Score", "Term Frequency", "Document Frequency"});
  private JSlider cloudSize = new JSlider();
  
  private CloudGenerator cloudGenerator = null;
  private ColorMenu fontColorMenu, backgroundColorMenu;
  
  private JPopupMenu mnuLanguages = new JPopupMenu();
  private JMenuButton menuLanguageButton = new JMenuButton(mnuLanguages);
  
  private JPopupMenu mnuTypes = new JPopupMenu();
  private JMenuButton menuTypesButton = new JMenuButton(mnuTypes);
  
  @Override
  public Resource init() {
    initGuiComponents();
    return this;
  }


  protected void populateTree(DefaultMutableTreeNode root, AbstractTermbank termbank) {
    List<Term> typeSortedTerms = termbank.getTermsByDescendingScore();
    Map<Term, Double> typeTermScores = termbank.getTermScores();
    Map<Term, Set<String>> typeTermDocuments = termbank.getTermDocuments();
    
    DefaultMutableTreeNode node1, node2;
    Double minScore = sliderPanel.getValues();
    
    for (Term term : typeSortedTerms) {
      Double score = typeTermScores.get(term);
      if (score >= minScore) {
        node1 = new DefaultMutableTreeNode(term + "  " + score.toString());
        root.add(node1);
        
        for (String document : typeTermDocuments.get(term)) {
          node2 = new DefaultMutableTreeNode(document);
          node1.add(node2);
        }
      }
      else {  // the rest must be lower
        break;
      }
    }
  }

  
  
  private void initGuiComponents() {
    setLayout(new BorderLayout());
    generateEmptyTree();
    tabbedPane = new JTabbedPane();
    JPanel treeTab = new JPanel(new BorderLayout());
    tabbedPane.addTab("Tree", treeTab);
    JPanel tableTab = new JPanel(new BorderLayout());
    tabbedPane.addTab("Frequency", tableTab);

    JPanel cloudTab = new JPanel(new BorderLayout());
    tabbedPane.addTab("Term Cloud", cloudTab);
    
    controlPanel = new JPanel();
    treeTab.add(controlPanel, BorderLayout.NORTH);
    treeScrollPane = new JScrollPane(tree, 
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    treeTab.add(treeScrollPane, BorderLayout.CENTER);
    controlPanel.validate();
    controlPanel.repaint();
    
    freqTableModel = new FrequencyTableModel();
    freqTable = new JTable(freqTableModel);
    freqTable.setAutoCreateRowSorter(true);
    freqScrollPane = new JScrollPane(freqTable, 
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    tableTab.add(freqScrollPane, BorderLayout.CENTER);

    
    JScrollPane cloudScrollPane = new JScrollPane(termCloud,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JToolBar cloudBar = new JToolBar();
    cloudBar.setFloatable(false);
    JButton btnExport = new JButton(MainFrame.getIcon("Download"));
    
    cloudType.addActionListener(new ActionListener() {      
      @Override
      public void actionPerformed(ActionEvent arg0) {
        Map<Term, ? extends Number> freq = null;
        switch (cloudType.getSelectedIndex()) {
          case 0:
            freq = termbank.getTermScores();
            break;
          case 1:
            freq = termbank.getTermFrequencies();
            break;
          case 2:
            freq = termbank.getDocFrequencies();
        }
        
        cloudGenerator.setTerms(freq);
        displayCloud();        
      }
    });
    
    cloudSize.addChangeListener(new ChangeListener() {
      
      @Override
      public void stateChanged(ChangeEvent ce) {
        if (!cloudSize.getValueIsAdjusting()) {
          displayCloud();
        }
      }
    });
    
    Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer,JLabel>();
    labelTable.put(0, new JLabel(MainFrame.getIcon("Sunny")));
    labelTable.put(50, new JLabel(MainFrame.getIcon("Cloudy")));
    labelTable.put(100, new JLabel(MainFrame.getIcon("Overcast")));
    
    cloudSize.setOpaque(false);
    cloudSize.setLabelTable(labelTable);
    cloudSize.setPaintLabels(true);
    
    btnExport.setToolTipText("Export Cloud As HTML");
    btnExport.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent arg0) {
        XJFileChooser fileChooser = MainFrame.getFileChooser();
        fileChooser.setResource("HTMLTermCloud");
        fileChooser.setFileFilter(new FileNameExtensionFilter("HTML Files", "html"));
        
        if (fileChooser.showSaveDialog(MainFrame.getInstance()) != XJFileChooser.APPROVE_OPTION) return;
        
        String html = cloudGenerator.getHTML(cloudSize.getValue());
        
        PrintWriter out = null;
        try {
          out = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()));
          
          out.println(html);
          out.flush();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
        finally {
          if (out != null) {
            try {
              out.close();
            }
            catch (Exception e) {
              //ignore this
            }
          }
        }                
      }
    });
    
    JPopupMenu mnuFontColor = new JPopupMenu("Font Colour");
    fontColorMenu = new ColorMenu(mnuFontColor, false, false);
    fontColorMenu.setColor(Color.BLACK);
    fontColorMenu.setCallback(new Callback(){
      @Override
      public void colorChange(Color c) {
        if (cloudGenerator != null && !cloudGenerator.getForeground().equals(c)) {
          cloudGenerator.setForeground(c);
          displayCloud();
        }
      }
    });
    JMenuButton menuButton = new JMenuButton(mnuFontColor);
    menuButton.setIcon(MainFrame.getIcon("FontColour"));
    menuButton.setToolTipText("Font Colour");
    
    JPopupMenu mnuBgColor = new JPopupMenu("Background Colour");
    backgroundColorMenu = new ColorMenu(mnuBgColor, false, false);
    backgroundColorMenu.setColor(Color.WHITE);
    backgroundColorMenu.setCallback(new Callback() {
      @Override
      public void colorChange(Color c) {
        if (cloudGenerator != null && !cloudGenerator.getBackground().equals(c)) {
          cloudGenerator.setBackground(c);
          displayCloud();
        }
      }
    });
    JMenuButton menuBGButton = new JMenuButton(mnuBgColor);
    menuBGButton.setIcon(MainFrame.getIcon("BackgroundColor"));
    menuBGButton.setToolTipText("Background Colour");
    
    menuLanguageButton.setIcon(MainFrame.getIcon("Languages"));
    menuLanguageButton.setToolTipText("Set Displayed Languages");
    
    menuTypesButton.setIcon(MainFrame.getIcon("TermTypes"));
    menuTypesButton.setToolTipText("Set Displayed Term Types");
    
    cloudBar.add(btnExport);
    cloudBar.addSeparator();
    cloudBar.add(menuButton);
    cloudBar.add(menuBGButton);
    cloudBar.addSeparator();
    cloudBar.add(menuLanguageButton);
    cloudBar.add(menuTypesButton);
    cloudBar.addSeparator();
    cloudBar.add(new JLabel("Type: "));    
    cloudBar.add(cloudType);
    cloudBar.add(Box.createHorizontalStrut(15));
    cloudBar.add(new JLabel("Size:"));
    cloudBar.add(cloudSize);
    cloudBar.add(Box.createHorizontalGlue());
        
    cloudTab.add(cloudBar, BorderLayout.NORTH);
    cloudTab.add(cloudScrollPane, BorderLayout.CENTER);
    
    this.add(tabbedPane, BorderLayout.CENTER);
    tabbedPane.validate();
    tabbedPane.repaint();
  }
  
  
  private void populateControlPanel(JPanel controlPanel) {
    controlPanel.setLayout(new BorderLayout());
    sliderPanel = new SliderPanel(termbank, "display", false, this);
    controlPanel.add(sliderPanel, BorderLayout.CENTER);
    sliderPanel.reformat();
    freqTableModel.setTermbank(this.termbank);
  }


  public void processFinished() {
    setTarget(termbank);
  }

  public void progressChanged(int i) {
    // nothing?
  }  

  public void setTarget(Object target) {
    if(target == null || ! (target instanceof AbstractTermbank)) {
      throw new IllegalArgumentException("This Viewer cannot show a "
              + (target == null ? "null" : target.getClass().toString()));
    }
    
    termbank = (AbstractTermbank) target;
    populateControlPanel(controlPanel);

    if (this.termbank instanceof HyponymyTermbank) {
      tabbedPane.addTab("Hyponymy Debugger", new HyponymyDebugger((HyponymyTermbank) this.termbank));
    }
    
    Map<Term, ? extends Number> freq = null;
    switch (cloudType.getSelectedIndex()) {
      case 0:
        freq = termbank.getTermScores();
        break;
      case 1:
        freq = termbank.getTermFrequencies();
        break;
      case 2:
        freq = termbank.getDocFrequencies();
    }
    
    menuLanguageButton.setEnabled(termbank.getLanguages().size() > 1);
        
    ActionListener languageChanger = new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        
        JCheckBoxMenuItem cb = (JCheckBoxMenuItem)e.getSource();
        
        cloudGenerator.includeLanguage(cb.getText(), cb.isSelected());
        
        displayCloud();
      }
    };
    
    if (menuLanguageButton.isEnabled()) {
      mnuLanguages.removeAll();
      
      for (String lang : termbank.getLanguages()) {
        JCheckBoxMenuItem cb = new JCheckBoxMenuItem(lang,true);
        cb.addActionListener(languageChanger);
        mnuLanguages.add(cb);
      }
    }
    
    menuTypesButton.setEnabled(termbank.getTypes().size() > 1);
    
    ActionListener typesChanger = new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        
        JCheckBoxMenuItem cb = (JCheckBoxMenuItem)e.getSource();
        
        cloudGenerator.includeTermType(cb.getText(), cb.isSelected());
        
        displayCloud();
      }
    };
    
    if (menuTypesButton.isEnabled()) {
      mnuTypes.removeAll();
      
      for (String type : termbank.getTypes()) {
        JCheckBoxMenuItem cb = new JCheckBoxMenuItem(type,true);
        cb.addActionListener(typesChanger);
        mnuTypes.add(cb);
      }
    }
    
    cloudGenerator = new CloudGenerator(freq);
    cloudGenerator.setLanguages(termbank.getLanguages());
    cloudGenerator.setTermTypes(termbank.getTypes());
    cloudGenerator.setForeground(fontColorMenu.getColor());
    cloudGenerator.setBackground(backgroundColorMenu.getColor());

    regenerateTree();
    displayCloud();
  }
  
  private void displayCloud() {
    try {
      termCloud.setDocument(new ByteArrayInputStream(cloudGenerator.getHTML(cloudSize.getValue()).getBytes()),"http://term-cloud.com");
    }catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getHTML() {
    return cloudGenerator.getHTML(cloudSize.getValue());
  }
  
  protected void regenerateTree() {
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Terms");
    populateTree(root, termbank);
    tree.setModel(new DefaultTreeModel(root));
    controlPanel.repaint();
  }
  
  
  protected void generateEmptyTree() {
    tree = new JTree();
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Use the controls above to set the thresholds and generate this tree.");
    tree.setModel(new DefaultTreeModel(root));
  }

}


class FrequencyTableModel extends AbstractTableModel {
  private static final long serialVersionUID = -7654670667296912991L;
  private List<Term> terms;
  private String[] columnNames = {"term", "term frequency", "doc frequency"};
  private Map<Term, Integer> termFrequencies, docFrequencies; 

  public FrequencyTableModel() {
    this.termFrequencies = new HashMap<Term, Integer>();
    this.docFrequencies = new HashMap<Term, Integer>();
    this.terms = new ArrayList<Term>();
  }
  
  public void setTermbank(AbstractTermbank termbank) {
    this.termFrequencies = termbank.getTermFrequencies();
    this.docFrequencies = termbank.getDocFrequencies();
    this.terms = new ArrayList<Term>(termFrequencies.keySet());
    Collections.sort(this.terms, new TermComparator());
  }
  
  public int getColumnCount() {
    return 3;
  }

  public int getRowCount() {
    return this.terms.size();
  }

  public Object getValueAt(int row, int col) {
    Term term = this.terms.get(row); 
    if (col == 0) {
      return term.toString();
    }
    // implied else
    if (col == 1) {
      if (this.termFrequencies.containsKey(term)) {
        return this.termFrequencies.get(term);
      }
      return 0;
    }
    // implied else
    if (this.docFrequencies.containsKey(term)) {
      return this.docFrequencies.get(term);
    }
    return 0;
  }
  
  public Class<?> getColumnClass(int col) {
    if (col == 0) {
      return String.class;
    }
    // implied else
    return Integer.class;
  }
  
  public String getColumnName(int col) {
    return columnNames[col];
  }

}
