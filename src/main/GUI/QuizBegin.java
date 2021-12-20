package main.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * main.GUI
 * Created by Hieu Tran Trung
 * Date 12/21/2021 - 1:26 AM
 * Description: ...
 */
public class QuizBegin extends JPanel {
    private JPanel mainPanel;
    private JTextArea youAreGoingToTextArea;
    private JRadioButton wordQuestionRadioButton;
    private JRadioButton definitionQuestionRadioButton;
    private JButton STARTButton;
    private JPanel textPanel;
    private JPanel controlPanel;
    private JPanel smallerControlPanel;

    private boolean isWord = true;

    public QuizBegin() {
        this.add(mainPanel);
        mainPanel.add(textPanel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.CENTER);
        controlPanel.add(smallerControlPanel, BorderLayout.CENTER);
        wordQuestionRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                definitionQuestionRadioButton.setSelected(false);
                isWord = true;
            }
        });
        definitionQuestionRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wordQuestionRadioButton.setSelected(false);
                isWord = false;
            }
        });
        STARTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public
}
