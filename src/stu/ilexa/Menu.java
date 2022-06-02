package stu.ilexa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel
        implements ActionListener {
    JButton inputButton;
    JButton outputButton;
    JButton saveButton;
    JButton submitButton;

    JPanel outputPanel;
    JPanel inputPanel;
    JPanel savePanel;

    JFileChooser inputChooser;
    JFileChooser outputChooser;
    JFileChooser saveChooser;
    String inputTitle = "Ввод";
    String outputTitle = "Вывод";
    String saveTitle = "Сохранение";

    public Menu() {
        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 3, 2));
        outputPanel = new JPanel();
        inputPanel = new JPanel();
        savePanel = new JPanel();
        inputButton = new JButton("Выбрать папку ввода");
        inputButton.addActionListener(this);
        inputPanel.add(inputButton);
        outputButton = new JButton("Выбрать файл вывода");
        outputButton.addActionListener(this);
        outputPanel.add(outputButton);
        saveButton = new JButton("Выбрать файл сохранения");
        saveButton.addActionListener(this);
        savePanel.add(saveButton);
        submitButton = new JButton("Начать");
        submitButton.addActionListener(this);
        add(submitButton, BorderLayout.SOUTH);
        buttonPanel.add(inputPanel);
        buttonPanel.add(outputPanel);
        buttonPanel.add(savePanel);
        add(buttonPanel, BorderLayout.NORTH);
    }

    public void actionPerformed(ActionEvent e) {
        System.out.print(e.getActionCommand());
        switch (e.getActionCommand()) {
            case "Выбрать папку ввода":
                inputChooser = new JFileChooser();
                inputChooser.setCurrentDirectory(new java.io.File("."));
                inputChooser.setDialogTitle(inputTitle);
                inputChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                //
                // disable the "All files" option.
                //
                inputChooser.setAcceptAllFileFilterUsed(false);
                //
                if (inputChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    System.out.println("getCurrentDirectory(): "
                            + inputChooser.getCurrentDirectory());
                    System.out.println("getSelectedFile() : "
                            + inputChooser.getSelectedFile());
                    inputPanel.add(new JLabel(inputChooser.getSelectedFile().toString()));
                    revalidate();
                    Main.inputPath = inputChooser.getSelectedFile().getPath();
                } else {
                    System.out.println("No Selection ");
                }
                break;
            case "Выбрать файл вывода":
                outputChooser = new JFileChooser();
                outputChooser.setCurrentDirectory(new java.io.File("."));
                outputChooser.setDialogTitle(outputTitle);
                outputChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (outputChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    System.out.println("getCurrentDirectory(): "
                            + outputChooser.getCurrentDirectory());
                    System.out.println("getSelectedFile() : "
                            + outputChooser.getSelectedFile());
                    outputPanel.add(new JLabel(outputChooser.getSelectedFile().toString()));
                    revalidate();
                    Main.outputPath = outputChooser.getSelectedFile().getPath();
                } else {
                    System.out.println("No Selection ");
                }
                break;
            case "Выбрать файл сохранения":
                saveChooser = new JFileChooser();
                saveChooser.setCurrentDirectory(new java.io.File("."));
                saveChooser.setDialogTitle(saveTitle);
                saveChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (saveChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    System.out.println("getCurrentDirectory(): "
                            + saveChooser.getCurrentDirectory());
                    System.out.println("getSelectedFile() : "
                            + saveChooser.getSelectedFile());
                    savePanel.add(new JLabel(saveChooser.getSelectedFile().toString()));
                    revalidate();
                    Main.savePath=saveChooser.getSelectedFile().getPath();
                } else {
                    System.out.println("No Selection ");
                }
                break;
            case "Начать":
                synchronized (Main.signal) {
                    Main.shouldWait = false;
                    Main.signal.notify();
                }
                GUIHandler.menuFrame.dispose();
                break;
        }

    }

    public Dimension getPreferredSize() {
        return new Dimension(Main.preferedWidth, Main.preferedHeight);
    }
}
