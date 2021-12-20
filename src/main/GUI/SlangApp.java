package main.GUI;

import main.Slang.SlangMap;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * com.GUI
 * Created by Hieu Tran Trung
 * Date 12/18/2021 - 6:08 PM
 * Description: ...
 */
public class SlangApp {
    private JPanel MainPanel;
    private JTabbedPane tabbedPane1;
    private JPanel HomePanel;
    private JPanel QuizPanel;
    private JPanel gridPanel;
    private JPanel searchPanel;
    private JButton searchButton;
    private JRadioButton byWordRadioButton;
    private JRadioButton byDefinitionRadioButton;
    private JList<Object> wordList;
    private JTextPane definitionTextPane;
    private JPanel wordDayPanel;
    private JPanel historyPanel;
    private JPanel exitPanel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton resetButton;
    private JList historyList;
    private JButton deleteAllHistoryButton;
    private JTextPane newDayTextPane;
    private JToolBar homeToolBar;
    private JTextField searchTextField;

    private boolean isSearchByWord = true;

    public SlangApp(SlangMap slangMap) {

        wordList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

            }
        });
        byWordRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                byDefinitionRadioButton.setSelected(false);
                isSearchByWord = true;
            }
        });
        byDefinitionRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                byWordRadioButton.setSelected(false);
                isSearchByWord = false;
            }
        });
        tabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                if (index == 3) { // Exit tab
                    JComponent comp = (JComponent) e.getSource();
                    if (JOptionPane.showConfirmDialog(comp,
                            "Do you really want to exit?", "Closing Window",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        Window win = SwingUtilities.getWindowAncestor(comp);
                        win.dispose();
                    }
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!searchTextField.getText().isBlank()) {

                    if (byWordRadioButton.isSelected()) {
                        wordList.setListData(slangMap.searchByKey(searchTextField.getText()));
                    }

                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Slang Dictionary App");
        frame.setContentPane(new SlangApp(new SlangMap()).MainPanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        ;
        frame.setBounds(200, 100, 1200, 600);
        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame,
                        "Do you really want to exit?", "Closing Window",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }
}
