package main.GUI;

import main.Slang.Slang;
import main.Slang.SlangMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
    private JLabel titleLabel;
    private JLabel scoreLabel;

    private int score = 0;
    private int nQuestion = 0;
    public ArrayList<QuizListener> quizListeners = new ArrayList<>();

    public QuizQuestion(SlangMap slangMap, boolean isWordQuestion) {
        add(MainPanel, BorderLayout.CENTER);
        MainPanel.add(questionPanel, BorderLayout.NORTH);
        MainPanel.add(answerPanel, BorderLayout.CENTER);
        MainPanel.add(controlPanel, BorderLayout.SOUTH);

        this.setPreferredSize(new Dimension(500, 300));

        showQuizGUI(nQuestion, slangMap, isWordQuestion);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showQuizGUI(nQuestion, slangMap, isWordQuestion);
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showResult();
            }
        });
    }

    private void showQuizGUI(int index, SlangMap slangMap, boolean isWordQuestion) {
        ArrayList<Slang> quiz = slangMap.randomSlang(4);
        int correctIndex = new Random().nextInt(quiz.size());
        Slang slangCorrect = quiz.get(correctIndex);

        String question;
        ArrayList<String> answers = new ArrayList<>();

        if (isWordQuestion) {
            question = slangCorrect.getWord();
            for (Slang slang : quiz) {
                ArrayList<String> answerList = slang.getDefinitionList();
                answers.add(slang.getDefinitionList().get(new Random().nextInt(answerList.size())));
            }
        } else {
            ArrayList<String> answerList = slangCorrect.getDefinitionList();
            question = slangCorrect.getDefinitionList().get(new Random().nextInt(answerList.size()));

            for (Slang slang : quiz) {
                answers.add(slang.getWord());
            }
        }

        // reset GUI
        aButton.setBackground(new JButton().getBackground());
        bButton.setBackground(new JButton().getBackground());
        cButton.setBackground(new JButton().getBackground());
        dButton.setBackground(new JButton().getBackground());
        nextButton.setEnabled(false);

        if (isWordQuestion) {
            titleLabel.setText("Question " + (index + 1) + ": What is the definition of this slang?");
        } else {
            titleLabel.setText("Question " + (index + 1) + ": Which slang has this definition?");
        }

        // show quiz question
        questionLabel.setText(question);

        // show quiz answers
        aButton.setText("A. " + answers.get(0));
        bButton.setText("B. " + answers.get(1));
        cButton.setText("C. " + answers.get(2));
        dButton.setText("D. " + answers.get(3));

        aButton.addActionListener(this);
        bButton.addActionListener(this);
        cButton.addActionListener(this);
        dButton.addActionListener(this);

        JButton[] buttons = new JButton[]{aButton, bButton, cButton, dButton};
        for (int j = 0; j < buttons.length; j++) {
            if (j == correctIndex) {
                buttons[j].setActionCommand("Correct");
            } else {
                buttons[j].setActionCommand("Incorrect");
            }
        }

        // show current score
        scoreLabel.setText("Your score: " + score + "/" + nQuestion);
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
        resultPanel.setPreferredSize(new Dimension(500, 600));

        MainPanel.add(resultPanel, BorderLayout.CENTER);
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
            ((JButton) e.getSource()).setBackground(Color.GREEN);
            score++;
        } else {
            ((JButton) e.getSource()).setBackground(Color.RED);
        }
        nQuestion++;
        nextButton.setEnabled(true);

        scoreLabel.setText("Your score: " + score + "/" + nQuestion);
        aButton.removeActionListener(this);
        bButton.removeActionListener(this);
        cButton.removeActionListener(this);
        dButton.removeActionListener(this);
    }

}
