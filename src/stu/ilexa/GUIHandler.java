package stu.ilexa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

public class GUIHandler {

    public void launchFrame(Interpreter interpreter,ArrayList<String> addedParams, String article,Map<String,String> params){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(addedParams.size()+1,2));
        JScrollPane scrollPane = new JScrollPane(panel);
        Button bn = new Button("Menu");

        String[] choices = Parameters.getParameters().toArray(new String[0]);
        ArrayList<JComboBox> menus = new ArrayList<>();
        for (int i = 0; i < addedParams.size(); i++) {
            JLabel label = new JLabel(addedParams.get(i));
            label.setVisible(true);
            panel.add(label);
            menus.add(new JComboBox<String>(choices));
            int index=Parameters.findByName(addedParams.get(i));
            if(index>=0){
                menus.get(i).setSelectedItem(Parameters.getParameters().get(i));
            }
            menus.get(i).setVisible(true);
            panel.add(menus.get(i));
        }
        Button saveBn = new Button("Save");
        saveBn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> addedParams = new ArrayList<>();
                for (int i = 0; i < addedParams.size(); i++) {
                    if(menus.get(i).getSelectedItem().equals("Добавить")){
                        interpreter.addKey(addedParams.get(i),addedParams.get(i));
                        addedParams.add(addedParams.get(i));
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
                Parameters.addParameters(addedParams);
                //System.out.print(interpreter.getMap().toString());
                //launchFrame(interpreter);
                frame.dispose();
                System.out.print("Frame disposed");
                interpreter.transform(params, article);
                System.out.print("Output stream finished");
            }
        });
        panel.add(saveBn);
        frame.add(scrollPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        System.out.print("Display finished");
    }
}
