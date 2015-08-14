package com.canvas.wahl;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class PopupForm extends JFrame {
    public generalSettings Settings;
    public ArrayList<Partei> ergebnis;
    public static WahlErgebnis canvas;

    public PopupForm(ArrayList<Partei> ergebnis, generalSettings Settings,WahlErgebnis canvas){
        super("Parteien verwalten");
        this.ergebnis = ergebnis;
        this.Settings = Settings;
        this.canvas = canvas;
        buildUI();
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

    }

    public  boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public JButton neu;

    public void buildUI()
    {
        getContentPane().removeAll();
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        for (int i =0; i< ergebnis.size(); i++)
        {
            final int index = i;
            Box bv = Box.createHorizontalBox();

            JTextField txt = new JTextField(ergebnis.get(i).Name);
            txt.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    ergebnis.get(index).Name = txt.getText();
                    canvas.repaint();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    ergebnis.get(index).Name = txt.getText();
                    canvas.repaint();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {

                }
            });
            bv.add(txt);

            JTextField erg = new JTextField(ergebnis.get(i).Ergebnis.toString());
            erg.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if (isDouble(erg.getText())) {
                        ergebnis.get(index).Ergebnis = Double.parseDouble(erg.getText());
                        canvas.repaint();
                    }


                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    if (isDouble(erg.getText())) {
                        ergebnis.get(index).Ergebnis = Double.parseDouble(erg.getText());
                        canvas.repaint();
                    }

                }

                @Override
                public void changedUpdate(DocumentEvent e) {

                }
            });
            bv.add(erg);

            JButton color = new JButton("Farbe");
            color.setBackground(ergebnis.get(i).Farbe);
            color.addActionListener(e -> {
                Color initialBackground = color.getBackground();
                Color background = JColorChooser.showDialog(null,
                        "Farbe auswählen", initialBackground);
                if (background != null) {
                    color.setBackground(background);
                    ergebnis.get(index).Farbe = background;
                    canvas.repaint();
                }
            });
            bv.add(color);

            JCheckBox dark = new JCheckBox("dunkle Schrift?");
            dark.setSelected(ergebnis.get(i).Dark);
            dark.addActionListener(e -> {ergebnis.get(index).Dark = dark.isSelected();canvas.repaint();});
            bv.add(dark);

            JButton addIcon = new JButton();
            addIcon.setIcon(UIManager.getIcon("Tree.leafIcon"));
            addIcon.addActionListener(e->{
                if (Files.exists(Paths.get("partei_logo/" + ergebnis.get(index).Name + ".png"))){
                    ergebnis.get(index).Logo = Paths.get("partei_logo/" + ergebnis.get(index).Name + ".png").toAbsolutePath().toString();
                    canvas.repaint();
                }
                else {
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "JPG & PNG Images", "jpg", "png");
                    chooser.setFileFilter(filter);
                    chooser.setCurrentDirectory(new java.io.File("partei_logo"));
                    int returnVal = chooser.showOpenDialog(getParent());
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        ergebnis.get(index).Logo = chooser.getSelectedFile().getAbsolutePath();
                        canvas.repaint();
                    }
                }
            });
            bv.add(addIcon);

            JButton delIcon = new JButton("del Logo");
            delIcon.addActionListener(e -> {
                ergebnis.get(index).Logo = "";
                canvas.repaint();
            });
            bv.add(delIcon);

            JButton delete = new JButton();
            delete.addActionListener(e -> {
                ergebnis.remove(index);
                buildUI();
                canvas.repaint();
            });
            delete.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
            bv.add(delete);
            getContentPane().add(bv);

        }
        Box hb = Box.createHorizontalBox();
        neu = new JButton("Neue Partei anlegen");
        neu.setIcon(UIManager.getIcon("FileView.fileIcon"));
        neu.addActionListener(e -> {
            ergebnis.add(new Partei("Namen",0.0,Color.white,false));
            buildUI();
            canvas.repaint();
        });
        neu.setSize(new Dimension(getContentPane().getWidth(),20));
        hb.add(neu);
        hb.add(Box.createHorizontalGlue());
        getContentPane().add(hb);
        pack();

    }

}