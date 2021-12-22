package main.GUI;

import main.Main;
import main.Slang.Slang;
import main.Slang.SlangMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * main.GUI
 * Created by Hieu Tran Trung
 * Date 12/21/2021 - 2:32 AM
 * Description: ...
 */
public class QuizQuestion extends JPanel implements ActionListener {
    private JPanel MainPanel;
    private JButton aButton;
    private JButton bButton;
    private JButton cButton;
    private JButton dButton;
    private JButton stopButton;
    private JButton nextButton;
    private JLabel questionLabel;
    private JPanel questionPanel;
    private JPanel answerPanel;
    private JPanel controlPanel;

    private int score = 0;
    private int nQuestion = 0;
    public ArrayList<QuizListener> quizListeners = new ArrayList<>();

    public QuizQuestion(SlangMap slangMap, boolean isWordQuestion) {
        add(MainPanel, BorderLayout.CENTER);
        MainPanel.add(questionPanel, BorderLayout.NORTH);
        MainPanel.add(answerPanel, BorderLayout.CENTER);
        MainPanel.add(controlPanel, BorderLayout.SOUTH);

        this.setPreferredSize(new Dimension(500, 300));

        generateQuiz(nQuestion, slangMap, isWordQuestion);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateQuiz(nQuestion, slangMap, isWordQuestion);
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showResult();
            }
        });
    }

    private void generateQuiz(int index, SlangMap slangMap, boolean isWordQuestion) {
        aButton.setBackground(new JButton().getBackground());
        bButton.setBackground(new JButton().getBackground());
        cButton.setBackground(new JButton().getBackground());
        dButton.setBackground(new JButton().getBackground());
        nextButton.setEnabled(false);
        ArrayList<Slang> quiz = slangMap.randomSlang(4);
        Slang slangCorrect = quiz.get(0);
        Collections.shuffle(quiz, new Random());
        if (isWordQuestion) {
            questionLabel.setText("Question " + (index + 1) + ": What is the definition of '"
                    + slangCorrect.getWord() + "'?");

            int i = 0;
            ArrayList<String> answerA = quiz.get(i++).getDefinitionList();
            aButton.setText("A. " + answerA.get(new Random().nextInt(answerA.size())));

            ArrayList<String> answerB = quiz.get(i++).getDefinitionList();
            bButton.setText("B. " + answerB.get(new Random().nextInt(answerB.size())));

            ArrayList<String> answerC = quiz.get(i++).getDefinitionList();
            cButton.setText("C. " + answerC.get(new Random().nextInt(answerC.size())));

            ArrayList<String> answerD = quiz.get(i++).getDefinitionList();
            dButton.setText("D. " + answerD.get(new Random().nextInt(answerD.size())));

            aButton.addActionListener(this);
            bButton.addActionListener(this);
            cButton.addActionListener(this);
            dButton.addActionListener(this);

            i = quiz.indexOf(slangCorrect);
            JButton[] buttons = new JButton[]{aButton, bButton, cButton, dButton};
            for (int j = 0; j < buttons.length; j++) {
                if (j == i) {
                    buttons[j].setActionCommand("Correct");
                } else {
                    buttons[j].setActionCommand("Incorrect");
                }
            }
        }
    }

    public void showResult() {
        MainPanel.removeAll();

        JPanel resultPanel = new JPanel();
        JLabel result = new JLabel("Your correct answer: " + score + " out of " + nQuestion);
        result.setAlignmentX(CENTER_ALIGNMENT);
        result.setFont(new Font(null, Font.BOLD, 18));

        JButton returnBtn = new JButton("Return");
        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (QuizListener listener : quizListeners)
                    listener.finish();

                MainPanel.removeAll();
                MainPanel.add(questionPanel, BorderLayout.NORTH);
                MainPanel.add(answerPanel, BorderLayout.CENTER);
                MainPanel.add(controlPanel, BorderLayout.SOUTH);
                nQuestion = 0;
                score = 0;
            }
        });
        resultPanel.add(result, BorderLayout.NORTH);
        resultPanel.add(returnBtn, BorderLayout.CENTER);
        resultPanel.setAlignmentX(CENTER_ALIGNMENT);
        resultPanel.setAlignmentY(CENTER_ALIGNMENT);
        resultPanel.setPreferredSize(new Dimension(500,600));

        MainPanel.add(resultPanel);
    }

    public void addQuizListener(QuizListener quizListener) {
        quizListeners.add(quizListener);
    }

    public void removeQuizListener(QuizListener quizListener) {
        quizListeners.remove(quizListener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Correct")) {
            ((JButton)e.getSource()).setBackground(Color.GREEN);
            score++;
        } else {
            ((JButton)e.getSource()).setBackground(Color.RED);
        }
        nQuestion++;
        nextButton.setEnabled(true);

        aButton.removeActionListener(this);
        bButton.removeActionListener(this);
        cButton.removeActionListener(this);
        dButton.removeActionListener(this);
    }
}
