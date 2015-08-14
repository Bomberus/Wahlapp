package com.canvas.wahl;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class Sitzverteilung extends JFrame{
    public ArrayList<Partei> ergebnis;

    public Sitzverteilung(ArrayList<Partei> ergebnis){
        super("Sitzverteilung");
        this.ergebnis = ergebnis;
        setResizable(false);
        setSize(new Dimension(800,600));
        setContentPane(new Canvas());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public class Canvas extends JPanel{
        @Override
        public void paint(Graphics g) {
            super.paintComponents(g);

            Integer width = getWidth();
            Integer height = getHeight();

            g.setColor(Color.WHITE);
            g.fillRect(0,0,width,height);


            int startangle = -45;
            for (int i = 0; i< ergebnis.size(); i++) {
                int angle = (int)Math.round(((ergebnis.get(i).Ergebnis * 631)* 270)/631);
                g.setColor(Color.black);
                g.drawArc(0, 0, width , height, startangle, angle);
                g.setColor(ergebnis.get(i).Farbe);
                g.fillArc(0, 0, width , height, startangle, angle);
                /*if (ergebnis.get(i).Dark)
                    g.setColor(Color.WHITE);
                else
                    g.setColor(Color.BLACK);*/
                g.setColor(Color.WHITE);
                String string = ergebnis.get(i).Name+" : "+(Math.round(ergebnis.get(i).Ergebnis * 631));
                int mod = angle/2;
                FontMetrics fm = getFontMetrics(getFont());

                g.fillRect((width / 2) + (int) Math.round(Math.cos((startangle + mod) * Math.PI / 180) * (width / 2.7)), (height / 2) - (int) Math.round(Math.sin((startangle + mod) * Math.PI / 180) * (height / 3.2))-fm.getHeight(),
                        fm.stringWidth(string),fm.getHeight());
                g.setColor(Color.BLACK);
                g.drawRect((width / 2) + (int) Math.round(Math.cos((startangle + mod) * Math.PI / 180) * (width / 2.7)), (height / 2) - (int) Math.round(Math.sin((startangle + mod) * Math.PI / 180) * (height / 3.2))-fm.getHeight(),
                        fm.stringWidth(string),fm.getHeight());
                g.drawString(string,(width/2)+(int)Math.round(Math.cos((startangle+mod)*Math.PI / 180)*(width/2.7)),(height/2)-(int)Math.round(Math.sin((startangle+mod)*Math.PI / 180)*(height/3.2)));
                startangle += angle;
            }
            g.setColor(Color.WHITE);
            FontMetrics fm = getFontMetrics(getFont());
            g.fillOval(width / 2 - width / 12, height / 2 - height / 12, width / 6, height / 6);
            g.setColor(Color.BLACK);
            g.drawString("631 Sitze",width/2-fm.stringWidth("631 Sitze")/2, height/2-fm.getHeight()/4);
        }
    }
}
