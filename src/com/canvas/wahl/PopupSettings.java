package com.canvas.wahl;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.ArrayList;

public class PopupSettings extends JFrame {

    public generalSettings Settings;
    public ArrayList<Partei> ergebnis;
    public static WahlErgebnis canvas;

    public PopupSettings(ArrayList<Partei> ergebnis, generalSettings Settings,WahlErgebnis canvas){
        super("Einstellungen");
        setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        this.ergebnis = ergebnis;
        this.Settings = Settings;
        this.canvas = canvas;

        Box content = Box.createVerticalBox();

        //Hintergrundfarbe
        Box hb = Box.createHorizontalBox();
        JLabel color_btn_lbl = new JLabel("Hintegrundfarbe: ");
        JButton color_btn = new JButton("Farbe");
        color_btn.setBackground(Settings.Background_Color);
        color_btn.addActionListener(e->{
            Color initialBackground = color_btn.getBackground();
            Color background = JColorChooser.showDialog(null,
                    "Farbe auswählen", initialBackground);
            if (background != null) {
                color_btn.setBackground(background);
                Settings.Background_Color = background;
                canvas.repaint();
            }
        });
        hb.add(color_btn_lbl);
        hb.add(color_btn);
        hb.add(Box.createHorizontalGlue());
        content.add(hb);

        //Hintergrundbild
        hb = Box.createHorizontalBox();
        JLabel back_img_lbl = new JLabel("Hintergrundbild: ");
        JButton back_img_btn = new JButton();
        JTextField back_img_sou = new JTextField();
        back_img_sou.setEnabled(false);
        back_img_btn.setIcon(UIManager.getIcon("Tree.leafIcon"));
        back_img_btn.addActionListener(e->{
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "JPG & PNG Images", "jpg", "png");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(getParent());
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                Settings.Background_Image = chooser.getSelectedFile().getAbsolutePath();
                back_img_sou.setText(chooser.getSelectedFile().getName());
                canvas.repaint();
            }
        });
        back_img_sou.setText(Settings.Background_Image);
        JButton del_img = new JButton();
        del_img.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
        del_img.addActionListener(e->{
            Settings.Background_Image = "";
            back_img_sou.setText("");
            canvas.repaint();
        });

        hb.add(back_img_lbl);
        hb.add(back_img_btn);
        hb.add(back_img_sou);
        hb.add(del_img);
        hb.add(Box.createHorizontalGlue());
        content.add(hb);

        //Footer-farbe
        hb = Box.createHorizontalBox();
        JLabel foo_color_btn_lbl = new JLabel("Footer-Farbe: ");
        JButton foo_color_btn = new JButton("Farbe");
        foo_color_btn.setBackground(Settings.Footer_Color);
        foo_color_btn.addActionListener(e->{
            Color initialBackground = foo_color_btn.getBackground();
            Color background = JColorChooser.showDialog(null,
                    "Farbe auswählen", initialBackground);
            if (background != null) {
                foo_color_btn.setBackground(background);
                Settings.Footer_Color = background;
                canvas.repaint();
            }
        });
        hb.add(foo_color_btn_lbl);
        hb.add(foo_color_btn);
        hb.add(Box.createHorizontalGlue());
        content.add(hb);

        //Transparenz-Ergebnis
        hb = Box.createHorizontalBox();
        JLabel trans_erg_lbl = new JLabel("Transparenz Ergebnisse: ");
        JSlider trans_erg_slid = new JSlider();
        trans_erg_slid.setMinimum(0);
        trans_erg_slid.setMaximum(255);
        trans_erg_slid.setValue(Settings.Ergebnis_trans);
        trans_erg_slid.addChangeListener(e->{
            trans_erg_lbl.setText("Transparenz: "+trans_erg_slid.getValue());
            Settings.Ergebnis_trans = trans_erg_slid.getValue();
            canvas.repaint();
        });
        hb.add(trans_erg_lbl);
        hb.add(trans_erg_slid);
        hb.add(Box.createHorizontalGlue());
        content.add(hb);

        //Zeichenfläche
        hb = Box.createHorizontalBox();
        JLabel draw_per = new JLabel("Zeichenflaeche in Prozent");
        JSlider draw_per_sli = new JSlider();
        draw_per_sli.setMinimum(50);
        draw_per_sli.setMaximum(95);
        draw_per_sli.setValue(Settings.dimen_draw);
        draw_per_sli.addChangeListener(e->{
            draw_per.setText("Zeichenflaeche: "+draw_per_sli.getValue()+"%)");
            Settings.dimen_draw = draw_per_sli.getValue();
            Settings.dimen_unten = 100 -Settings.dimen_draw;
            canvas.repaint();
        });
        hb.add(draw_per);
        hb.add(draw_per_sli);
        hb.add(Box.createHorizontalGlue());
        content.add(hb);

        //Ergebnisbox
        hb = Box.createHorizontalBox();
        JLabel erg_box = new JLabel("Weite Ergebnisbox");
        JSlider erg_box_sli = new JSlider();
        erg_box_sli.setMinimum(20);
        erg_box_sli.setMaximum(50);
        erg_box_sli.setValue(Settings.xdimen_ergebnisbox);
        erg_box_sli.addChangeListener(e->{
            erg_box.setText("Weite Ergebnisbox: "+erg_box_sli.getValue()+"%");
            Settings.xdimen_ergebnisbox= erg_box_sli.getValue();
            canvas.repaint();
        });
        hb.add(erg_box);
        hb.add(erg_box_sli);
        hb.add(Box.createHorizontalGlue());
        content.add(hb);

        //Weite Balken
        hb = Box.createHorizontalBox();
        JLabel balken = new JLabel("Weite Balken");
        JSlider balken_sli = new JSlider();
        balken_sli.setMinimum(40);
        balken_sli.setMaximum(75);
        balken_sli.setValue(Settings.Balken_size);
        balken_sli.addChangeListener(e->{
            balken.setText("Weite Balken: "+ balken_sli.getValue()+"%");
            Settings.Balken_size= balken_sli.getValue();
            Settings.Balken_space= 90 - Settings.Balken_size;
            canvas.repaint();
        });
        hb.add(balken);
        hb.add(balken_sli);
        hb.add(Box.createHorizontalGlue());
        content.add(hb);

        add(content);
        pack();
        setVisible(true);
    }
}
