package stu.ilexa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Map;

public class GUIHandler {
    public static JFrame menuFrame;
    public static void launchMenu(){
        menuFrame = new JFrame("");
        Menu panel = new Menu();
        menuFrame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        //Main.saveState();
                        System.exit(0);
                    }
                }
        );
        menuFrame.getContentPane().add(panel,"Center");
        menuFrame.setSize(panel.getPreferredSize());
        menuFrame.setVisible(true);
    }

    public static void launchFrame(Interpreter interpreter,ArrayList<String> addedParams){
        JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(addedParams.size()+1,1));
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        Button bn = new Button("Menu");

        String[] choices = Parameters.getParameters().toArray(new String[0]);
        ArrayList<JComboBox> menus = new ArrayList<>();
        for (int i = 0; i < addedParams.size(); i++) {
            JPanel panel = new JPanel(new GridLayout(1,2));
            JLabel label = new JLabel(addedParams.get(i));
            label.setVisible(true);
            panel.add(label);
            menus.add(new JComboBox<String>(choices));
            int index=Parameters.findByName(addedParams.get(i));
            if(index>=0){
                menus.get(i)
                        .setSelectedItem(Parameters
                                        .getParameters()
                                        .get(index));
            }
            else{
                panel.setBackground(Color.RED);
            }
            menus.get(i).setVisible(true);
            panel.add(menus.get(i));
            panel.setBorder(BorderFactory.createLineBorder(Color.black));
            mainPanel.add(panel);
        }
        Button saveBn = new Button("Save");
        saveBn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> bufferedParams = new ArrayList<>();
                for (int i = 0; i < addedParams.size(); i++) {
                    if(menus.get(i).getSelectedItem().equals("Добавить")){
                        interpreter.addKey(addedParams.get(i),addedParams.get(i));
                        bufferedParams.add(addedParams.get(i));
                    }
                    else{
                        if(!menus.get(i).getSelectedItem().equals("Удалить")){
                            interpreter.addKey(addedParams.get(i),menus.get(i).getSelectedItem().toString());
                        }
                        else{
                            interpreter.addKey(addedParams.get(i),"none");
                        }
                    }
                }
                Parameters.addParameters(bufferedParams);
                //System.out.print(interpreter.getMap().toString());
                //launchFrame(interpreter);
                frame.dispose();
                synchronized(Main.signal) {
                    Main.shouldWait = false;
                    Main.signal.notify();
                }
            }
        });
        mainPanel.add(saveBn);
        frame.add(scrollPane);
        frame.setTitle(interpreter.getSupplierCode()+" modification");
        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        Main.saveState();
                        System.exit(0);
                    }
                }
        );
        frame.pack();
        frame.setSize(Main.preferedWidth,Main.preferedHeight);
        frame.setVisible(true);
    }
}
