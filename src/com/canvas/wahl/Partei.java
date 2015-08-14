package com.canvas.wahl;

import java.awt.*;

public class Partei{
    public String Name;
    public Double Ergebnis;
    public Color Farbe;
    public boolean Dark;
    public String Logo = "";

    public Partei(String Name,Double Ergebnis,Color Farbe,boolean Dark){
        this.Name = Name;
        this.Ergebnis = Ergebnis;
        this.Farbe = Farbe;
        this.Dark = Dark;

    }
}
