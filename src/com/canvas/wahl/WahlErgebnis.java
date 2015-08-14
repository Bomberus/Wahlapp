package com.canvas.wahl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.String;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class WahlErgebnis extends JFrame implements ActionListener{

    public ArrayList<Partei> ergebnis = new ArrayList<>();

    public JMenuItem einstellungen;
    public JMenuItem parteien;
    public JMenuItem sitzverteilung;
    public JMenuItem prognose;


    public generalSettings Settings = new generalSettings();


    public WahlErgebnis(){
        super("WahlErgebnis");
        fillErgebnis();
        setContentPane(new WahlCanvas());
        setSize(800, 600);
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("Einstellungen");
        einstellungen = new JMenuItem("allg. Einstellungen");
        einstellungen.addActionListener(this);
        menu.add(einstellungen);

        parteien = new JMenuItem("Parteien verwalten");
        parteien.addActionListener(this);
        menu.add(parteien);

        menubar.add(menu);

        JMenu tool = new JMenu("Tools");
        sitzverteilung = new JMenuItem("Sitzverteilung");
        sitzverteilung.addActionListener(this);
        tool.add(sitzverteilung);
        prognose = new JMenuItem("Prognose");
        prognose.addActionListener(this);
        tool.add(prognose);
        menubar.add(tool);

        setJMenuBar(menubar);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void fillErgebnis(){
        try (BufferedReader br = new BufferedReader(new FileReader(new File("Wahl.ergebnis")))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(";");
                ergebnis.add(new Partei(split[0],Double.parseDouble(split[1]),
                        new Color(Integer.parseInt(split[2]),Integer.parseInt(split[3]),Integer.parseInt(split[4])),
                        Boolean.parseBoolean(split[5])));

            }
        }
        catch (java.io.IOException ex){
            //Fehler
        }
    }


    public static WahlErgebnis canvas;
    public static void main(String[] args) {
        canvas = new WahlErgebnis();
    }

    public class WahlCanvas extends JPanel {
        public void draw3dRectangle(Graphics g, int x, int y, int width, int height,int offset,Color color){
            g.setColor(new Color(0,0,0,50));
            g.fillRect(x + offset, y - offset, width, height + offset);
            g.setColor(color);
            g.fillRect(x,y,width,height);
            g.setColor(Color.BLACK);
            g.drawRect(x,y,width,height);
        }
        public void paint(Graphics g) {
            if (ergebnis.isEmpty())
                return;

            super.paintComponent(g);
            Integer width = getWidth();
            Integer height = getHeight();

            int w_dpi = Math.round(width/100);
            int h_dpi = Math.round(height/100);

            //größter Wert ermitteln
            double biggestscore = 0.0;
            for (int i = 0; i<ergebnis.size(); i++){
                if (ergebnis.get(i).Ergebnis> biggestscore)
                    biggestscore = ergebnis.get(i).Ergebnis;
            }

            if (!Settings.Background_Image.equals("")) {
                try {
                    File file = new File(Settings.Background_Image);
                    Image img = ImageIO.read(file);
                    g.drawImage(img, 0, 0, width, height, null);

                }
                catch (java.io.IOException ex)
                {
                    Settings.Background_Image = "";
                    repaint();
                }
            }else{
                g.setColor(Settings.Background_Color);
                g.fillRect(0,0,width,height);
                g.setColor(Color.BLACK);
                Font font = new Font("Consolas", Font.BOLD, w_dpi*2);
                FontMetrics fm = g.getFontMetrics();
                g.setFont(font);
                int linecount = 5;
                for(int i = 0; i<= linecount; i++){
                    Double wert = (double) i;
                    wert /= linecount;
                    wert *= biggestscore * 100;
                    g.drawString(String.valueOf(Math.round(wert*100)/100),0,height-(Settings.dimen_unten*h_dpi)-i*(Settings.dimen_draw/linecount)*h_dpi);
                    g.drawLine(0,height-(Settings.dimen_unten*h_dpi)-i*(Settings.dimen_draw/linecount)*h_dpi,width,height-(Settings.dimen_unten*h_dpi)-i*(Settings.dimen_draw/linecount)*h_dpi);
                }
            }


            //Balkeneinstellung
            int Balken_size =  (Settings.Balken_size /ergebnis.size())*w_dpi;
            int Balken_space = (Settings.Balken_space /ergebnis.size())*w_dpi;

            //Untere Region zeichnen
            g.setColor(Settings.Footer_Color);
            g.fillRect(0,height-(h_dpi*Settings.dimen_unten),width,(h_dpi*Settings.dimen_unten));

            //Balken zeichnen
            int offset = Math.round((width-(ergebnis.size()*(Balken_size+Balken_space)-Balken_space))/4);
            Balken_size += (offset/ergebnis.size());
            for (int i = 0; i< ergebnis.size(); i++) {
                draw3dRectangle(g,offset + i * (Balken_size + Balken_space),
                        height - (h_dpi * Settings.dimen_unten) - ((int) Math.round((ergebnis.get(i).Ergebnis/biggestscore) * Settings.dimen_draw) * h_dpi),
                        Balken_size, (int) Math.round((ergebnis.get(i).Ergebnis/biggestscore) * Settings.dimen_draw) * h_dpi,5,ergebnis.get(i).Farbe);

                if (!ergebnis.get(i).Logo.equals("")) {
                    try {
                        File file = new File(ergebnis.get(i).Logo);
                        Image img = ImageIO.read(file);
                        g.drawImage(img,offset + i * (Balken_size + Balken_space),
                                (height + 10) - (h_dpi * Settings.dimen_unten),
                                Balken_size, (Settings.dimen_unten * h_dpi) - 10, null);

                    }
                    catch (java.io.IOException ex)
                    {
                        ergebnis.get(i).Logo = "";
                        repaint();
                    }
                }else {
                    g.setColor(ergebnis.get(i).Farbe);
                    g.fillRect(offset + i * (Balken_size + Balken_space),
                            (height + 10) - (h_dpi * Settings.dimen_unten),
                            Balken_size, (Settings.dimen_unten * h_dpi) - 10);
                    //Untere Hinweisbox
                    if (ergebnis.get(i).Dark)
                        g.setColor(Color.WHITE);
                    else
                        g.setColor(Color.black);

                    int fontSize = 10;
                    Font font = new Font("Serif", Font.BOLD, fontSize);
                    g.setFont(font);
                    FontMetrics fm = g.getFontMetrics();
                    while (fm.stringWidth(ergebnis.get(i).Name) < Balken_size - 6 * w_dpi && (fm.getHeight() < h_dpi * (Settings.dimen_unten - 2))) {
                        g.setFont(font);
                        fm = g.getFontMetrics();
                        fontSize += 2;
                        font = new Font("Serif", Font.BOLD, fontSize);
                    }
                    g.drawString(ergebnis.get(i).Name, offset + i * (Balken_size + Balken_space) + ((Balken_size - fm.stringWidth(ergebnis.get(i).Name)) / 2), height - 2 * h_dpi);
                }
            }

            //Ergebnisbox
            int fontSize = 10;
            Font font = new Font("Consolas", Font.BOLD, fontSize);
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            while (fm.stringWidth("Die Violetten : 45.4 ")<Settings.xdimen_ergebnisbox*w_dpi){
                g.setFont(font);
                fm = g.getFontMetrics();
                fontSize += 1;
                font = new Font("Consolas", Font.BOLD, fontSize);
            }
            g.setColor(new Color(211,211,211,Settings.Ergebnis_trans));
            g.fillRoundRect(width - (Settings.xdimen_ergebnisbox * w_dpi), 0, Settings.xdimen_ergebnisbox * w_dpi, (ergebnis.size()+1) * (fm.getHeight()), 10, 10);


            for (int i = 0; i< ergebnis.size(); i++) {
                g.setColor(new Color(ergebnis.get(i).Farbe.getRed(),ergebnis.get(i).Farbe.getGreen(),ergebnis.get(i).Farbe.getBlue(),Settings.Ergebnis_trans));
                Double erg = (double)Math.round(ergebnis.get(i).Ergebnis * 1000)/10;
                g.drawString(ergebnis.get(i).Name+" : "+erg.toString(),width-(Settings.xdimen_ergebnisbox*w_dpi),fm.getHeight()+fm.getHeight()*i);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == einstellungen){
            new PopupSettings(ergebnis,Settings,canvas);
        }
        else if (e.getSource() == parteien){
            new PopupForm(ergebnis,Settings,canvas);
        }
        else if (e.getSource() == sitzverteilung){
            new Sitzverteilung(ergebnis);
        }
        else if (e.getSource() == prognose){
            new Prognose(ergebnis);
        }
    }

}
