package main.GUI;

import main.Slang.Slang;
import main.Slang.SlangMap;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * com.GUI
 * Created by Hieu Tran Trung
 * Date 12/18/2021 - 6:08 PM
 * Description: ...
 */
public class SlangApp {
    private static SlangMap slangMap;
    private JPanel MainPanel;
    private JTabbedPane tabbedPane1;
    private JPanel HomePanel;
    private JPanel QuizPanel;
    private JPanel searchPanel;
    private JButton searchButton;
    private JRadioButton byWordRadioButton;
    private JRadioButton byDefinitionRadioButton;
    private JList<String> wordList;
    private JTextPane definitionTextPane;
    private JPanel historyPanel;
    private JPanel exitPanel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton resetButton;
    private JList<String> historyList;
    private JButton deleteAllHistoryButton;
    private JTextPane newDayTextPane;
    private JToolBar homeToolBar;
    private JTextField searchTextField;
    private JPanel wordDayPanel;
    private JPanel gridPanel;
    private JTextPane definitionHistoryTextPane;
    private JPanel quizPanel;
    private final JFrame frame;
    private JDialog addDialog;
    private JDialog editDialog;

    private final ArrayList<Slang> slangArrayList = new ArrayList<>();
    private final ArrayList<Slang> historyWord = new ArrayList<>();

    public SlangApp(JFrame frame, SlangMap slangMap) {
        // set up components
        this.frame = frame;
        JPanel newPanel = new QuizBegin(slangMap);
        QuizPanel.add(newPanel, BorderLayout.CENTER);

        // generate on this day word for every 5 minutes
        generateWord(5);
        loadHistory();
        DefaultListModel<String> historyListModel = new DefaultListModel<>();
        historyList.setModel(historyListModel);

        tabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                if (index == 2) { // History tab
                    historyListModel.clear();
                    if (historyWord.size() != 0 && historyWord.get(0) != null) {
                        for (Slang slang : historyWord) {
                            historyListModel.addElement(slang.getWord());
                            historyList.setSelectedIndex(0);
                            wordList.setSelectedIndex(0);
                        }
                        wordList.grabFocus();
                    } else {
                        historyListModel.addElement("Empty history!");
                        definitionHistoryTextPane.setText("No chosen word :(");
                    }
                }
                if (index == 3) { // Exit tab
                    JComponent comp = (JComponent) e.getSource();
                    if (JOptionPane.showConfirmDialog(comp,
                            "Do you really want to exit?", "Closing Window",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        try {
                            slangMap.saveDataStructure();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        Window win = SwingUtilities.getWindowAncestor(comp);
                        win.dispose();
                    }
                }
            }
        });

        // home tab
        DefaultListModel<String> wordListModel = new DefaultListModel<>();
        wordList.setModel(wordListModel);

        wordList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = wordList.getSelectedIndex();
                if (index != -1) {
                    if (slangArrayList.size() != 0) {
                        Slang slang = slangArrayList.get(index);
                        displaySlang(definitionTextPane, slangMap.searchByKey(slang.getWord()));
                    }
                }
            }
        });
        byWordRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                byWordRadioButton.setSelected(true);
                byDefinitionRadioButton.setSelected(false);
            }
        });
        byDefinitionRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                byDefinitionRadioButton.setSelected(true);
                byWordRadioButton.setSelected(false);
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!searchTextField.getText().isBlank()) {
                    slangArrayList.clear();
                    if (byWordRadioButton.isSelected()) {
                        Slang slang = slangMap.searchByKey(searchTextField.getText());
                        if (slang != null) {
                            slangArrayList.add(slang);
                            saveHistory(slangArrayList.get(0));
                        }
                    } else {
                        slangArrayList.addAll(slangMap.searchByDefinition(searchTextField.getText()));
                    }

                    wordListModel.clear();
                    if (slangArrayList.size() != 0 && slangArrayList.get(0) != null) {
                        for (Slang slang : slangArrayList) {
                            wordListModel.addElement(slang.getWord());
                            wordList.setSelectedIndex(0);
                        }
                        wordList.grabFocus();
                    } else {
                        wordListModel.addElement("No result found!");
                        definitionTextPane.setText("No result found!");
                    }
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (addDialog == null || !addDialog.isShowing()) {
                    addDialog = addSlang(frame);
                    addDialog.setTitle("Add a slang");
                    addDialog.pack();
                    addDialog.setVisible(true);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = wordList.getSelectedIndex();
                if (index != -1) {
                    if (slangArrayList.size() != 0) {
                        Slang slang = slangArrayList.get(index);
                        int confirm = JOptionPane.showConfirmDialog(HomePanel, "Do you really want to delete this slang '" +
                                slang.getWord() + "' ?", "Delete a slang", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            wordListModel.remove(index);
                            definitionTextPane.setText("deleted");
                            slangArrayList.remove(slang);
                            slangMap.delete(slang.getWord());
                            wordList.setSelectedIndex(0);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(HomePanel, "Please search and choose a slang first!",
                            "Delete a slang", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(HomePanel, "Do you really want to " +
                        "return slang list to original one?", "Reset slang list", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    slangMap.reset();
                    JOptionPane.showMessageDialog(HomePanel, "Slang list has been reset successfully");
                    wordListModel.clear();
                    definitionTextPane.setText("");
                }
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = wordList.getSelectedIndex();
                if (index != -1) {
                    if (slangArrayList.size() != 0) {
                        if (editDialog == null || !editDialog.isShowing()) {
                            Slang slang = slangArrayList.get(index);
                            editDialog = editSlang(frame, slang);
                            editDialog.setTitle("Edit a slang");
                            editDialog.pack();
                            editDialog.setVisible(true);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(HomePanel, "Please search and choose a slang first!",
                            "Edit a slang", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // history tab
        deleteAllHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearHistory();
                historyListModel.clear();
                historyListModel.addElement("Empty history!");
                definitionHistoryTextPane.setText("No chosen word :(");
            }
        });
        historyList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = historyList.getSelectedIndex();
                if (index != -1) {
                    if (historyWord.size() != 0) {
                        Slang slang = historyWord.get(index);
                        displaySlang(definitionHistoryTextPane, slang);
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        slangMap = new SlangMap();
        JFrame frame = new JFrame("Slang Dictionary App");
        frame.setContentPane(new SlangApp(frame, slangMap).MainPanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setBounds(200, 100, 800, 600);
        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame,
                        "Do you really want to exit?", "Closing Window",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    try {
                        slangMap.saveDataStructure();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        });
    }

    public void displaySlang(JTextPane textPane, Slang slang) {
        SimpleAttributeSet wordStyle = new SimpleAttributeSet();
        StyleConstants.setBold(wordStyle, true);

        // Show bold word
        textPane.setCharacterAttributes(wordStyle, true);
        textPane.setText("Slang: " + slang.getWord() + "\n");

        // show list of meaning
        SimpleAttributeSet meaningStyle = new SimpleAttributeSet();
        StyleConstants.setItalic(meaningStyle, true);

        Document doc = textPane.getStyledDocument();
        String[] slangDefi = slang.getDefinitionList().toArray(new String[0]);
        for (int i = 0; i < slangDefi.length; i++) {
            try {
                doc.insertString(doc.getLength(), "  - Meaning " + (i + 1) + " : " +
                        slangDefi[i].trim() + "\n", meaningStyle);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void loadHistory() {
        String file_in = "src/resources/data/history.txt";
        try {
            BufferedReader bw = new BufferedReader(new InputStreamReader
                    (new FileInputStream(file_in)));

            String line;
            while ((line = bw.readLine()) != null) {
                Slang slang = Slang.fromString(line);
                if (slang != null) {
                    historyWord.add(slang);
                }
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveHistory(Slang newSlang) {
        if (historyWord.contains(newSlang))
            return;

        historyWord.add(newSlang);
        String file_out = "src/resources/data/history.txt";
        try {
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file_out, true)));
            bw.write(newSlang.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearHistory() {
        historyWord.clear();
        String file_out = "src/resources/data/history.txt";
        try {
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file_out)));
            bw.write("");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JDialog addSlang(Frame frame) {
        JDialog addDialog = new JDialog(frame);
        addDialog.setLocation(frame.getLocation().x + 170,
                frame.getLocation().y + 100);
        JLabel name = new JLabel("      Slang:");
        JTextField wordTextField = new JTextField();
        JPanel wordPanel = new JPanel();
        wordPanel.setLayout(new BoxLayout(wordPanel, BoxLayout.LINE_AXIS));
        wordPanel.add(name);
        wordPanel.add(wordTextField);
        wordPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        JLabel meaning = new JLabel("Definition:");
        JTextArea definitionTextArea = new JTextArea("",10,10);
        JScrollPane scrollPane = new JScrollPane(definitionTextArea);
        JPanel meaningPanel = new JPanel();
        meaningPanel.setLayout(new BoxLayout(meaningPanel, BoxLayout.LINE_AXIS));
        meaningPanel.add(meaning);
        meaningPanel.add(scrollPane);
        meaningPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        JButton addBtn = new JButton("Add");
        JButton cancelBtn = new JButton("Cancel");
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (definitionTextArea.getText().isBlank() || wordTextField.getText().isBlank()) {
                    JOptionPane.showMessageDialog(addDialog, "Word and its definitions should not be blank",
                            "Invalid input", JOptionPane.WARNING_MESSAGE);
                } else {
                    String[] defiList = definitionTextArea.getText().split("\n");
                    Slang newSlang = new Slang(wordTextField.getText(), defiList);

                    if (slangMap.searchByKey(newSlang.getWord()) != null) {
                        int confirm = JOptionPane.showOptionDialog(HomePanel, "This slang: '" + newSlang.getWord()
                                        + "' already exists. Do you want to override or duplicate this word?", "Slang exists",
                                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                                new Object[]{"Override", "Duplicate", "Cancel"}, JOptionPane.YES_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            slangMap.add(newSlang, true);
                            JOptionPane.showMessageDialog(addDialog, "Add successfully");
                            addDialog.dispose();
                        } else if (confirm == JOptionPane.NO_OPTION) {
                            slangMap.add(newSlang, false);
                            JOptionPane.showMessageDialog(addDialog, "Add successfully");
                            addDialog.dispose();
                        }
                        int index = wordList.getSelectedIndex();
                        if (index != -1 && wordList.getModel().getElementAt(index).equals(newSlang.getWord())) {
                            displaySlang(definitionTextPane, slangMap.searchByKey(newSlang.getWord()));
                        }
                    } else {
                        slangMap.add(newSlang, true);
                        JOptionPane.showMessageDialog(addDialog, "Add successfully");
                        addDialog.dispose();
                    }
                }
            }
        });
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDialog.dispose();
            }
        });
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout());
        btnPanel.add(Box.createRigidArea(new Dimension(120, 0)));
        btnPanel.add(addBtn);
        btnPanel.add(cancelBtn);

        addDialog.add(wordPanel, BorderLayout.NORTH);
        addDialog.add(meaningPanel, BorderLayout.CENTER);
        addDialog.add(btnPanel, BorderLayout.SOUTH);

        return addDialog;
    }

    public JDialog editSlang(JFrame frame, Slang slang) {
        JDialog editDialog = new JDialog(frame);
        editDialog.setLocation(frame.getLocation().x + 170,
                frame.getLocation().y + 100);
        JLabel name = new JLabel("      Slang:");
        JTextField wordTextField = new JTextField(slang.getWord());
        wordTextField.setEditable(false);
        JPanel wordPanel = new JPanel();
        wordPanel.setLayout(new BoxLayout(wordPanel, BoxLayout.LINE_AXIS));
        wordPanel.add(name);
        wordPanel.add(wordTextField);
        wordPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        JLabel meaning = new JLabel("Definition:");
        JTextArea definitionTextArea = new JTextArea("", 10, 10);
        ArrayList<String> defiList = slangMap.searchByKey(slang.getWord()).getDefinitionList();
        for (String s : defiList) {
            definitionTextArea.append(s + "\n");
        }
        JScrollPane scrollPane = new JScrollPane(definitionTextArea);
        JPanel meaningPanel = new JPanel();
        meaningPanel.setLayout(new BoxLayout(meaningPanel, BoxLayout.LINE_AXIS));
        meaningPanel.add(meaning);
        meaningPanel.add(scrollPane);
        meaningPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        JButton editBtn = new JButton("Edit");
        JButton cancelBtn = new JButton("Cancel");
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (definitionTextArea.getText().isBlank() || wordTextField.getText().isBlank()) {
                    JOptionPane.showMessageDialog(editDialog, "Word and its definitions should not be blank",
                            "Invalid input", JOptionPane.WARNING_MESSAGE);
                } else {
                    String[] defiList = definitionTextArea.getText().split("\n");
                    Slang newSlang = new Slang(wordTextField.getText(), defiList);
                    slangMap.edit(slang.getWord(), newSlang);

                    JOptionPane.showMessageDialog(editDialog, "Edit successfully");
                    editDialog.dispose();
                    int index = wordList.getSelectedIndex();
                    if (index != -1 && wordList.getModel().getElementAt(index).equals(slang.getWord())) {
                        displaySlang(definitionTextPane, slangMap.searchByKey(slang.getWord()));
                    }
                }
            }
        });
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDialog.dispose();
            }
        });
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout());
        btnPanel.add(Box.createRigidArea(new Dimension(120, 0)));
        btnPanel.add(editBtn);
        btnPanel.add(cancelBtn);

        editDialog.add(wordPanel, BorderLayout.NORTH);
        editDialog.add(meaningPanel, BorderLayout.CENTER);
        editDialog.add(btnPanel, BorderLayout.SOUTH);

        return editDialog;
    }

    public void generateWord(int minutes) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = new Runnable() {
            public void run() {
                displaySlang(newDayTextPane, slangMap.randomSlang(1).get(0));
            }
        };
        scheduler.scheduleAtFixedRate(task, 0, minutes * 60L, SECONDS);
    }
}
