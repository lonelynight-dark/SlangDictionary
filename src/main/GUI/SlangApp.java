package main.GUI;

import javax.swing.*;

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
    private JTextField textField1;
    private JRadioButton byWordRadioButton;
    private JRadioButton byDefinitionRadioButton;
    private JList wordList;
    private JTextPane textPane1;
    private JPanel wordDayPanel;
    private JPanel historyPanel;
    private JPanel exitPanel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton resetButton;
    private JList historyList;
    private JButton deleteAllHistoryButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("SlangApp");
        frame.setContentPane(new SlangApp().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
