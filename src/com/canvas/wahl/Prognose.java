package com.canvas.wahl;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Prognose extends JFrame{
    public ArrayList<Partei> ergebnis;
    public ArrayList<ParteiParameter> parameter;
    public ArrayList<Parteiwerte> parteiwerte;
    public ArrayList<Double> random_werte;
    public ArrayList<Prognose_result> results;
    public ArrayList<String> Partei_kat;
    public double neuVerteilung = 0.6;

    public class Werte{
        public String Name;
        public boolean wert;

        public Werte (String Name, boolean wert){
            this.Name = Name;
            this.wert = wert;
        }
    }

    public class Parteiwerte{
        public String Name;
        public ArrayList<Werte> werte;

        public Parteiwerte(String Name, ArrayList<Werte> werte){
            this.Name = Name;
            this.werte  = werte;
        }
    }

    public class Prognose_result{
        public String Name;
        public double wert;

        public Prognose_result(String Name, double wert){
            this.Name = Name;
            this.wert = wert;
        }
    }

    public class ParteiParameter{
        public String Pro;
        public String Con;
        public ArrayList<Werte> Pro_werte;

        public ParteiParameter(String Pro, String Con, ArrayList<Werte> Pro_werte) {
            this.Pro = Pro;
            this.Con = Con;
            this.Pro_werte = Pro_werte;
        }
    }

    public Prognose(ArrayList<Partei> ergebnis){
        super("Prognose");
        this.ergebnis = ergebnis;
        read_partei_ka();
        fillParteiParameter();
        fillParteiWerte();
        buildUI();
        pack();
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void buildUI(){
        getContentPane().removeAll();
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        Box vb = Box.createVerticalBox();
        for (int i= 0; i< parteiwerte.size(); i++) {
            Box Partei_row = Box.createHorizontalBox();

            Partei_row.add(new JLabel(ergebnis.get(i).Name));
            Partei_row.add(Box.createHorizontalGlue());

            final int index = i;

            for (int j = 0; j< parteiwerte.get(i).werte.size(); j++) {
                final int indexj = j;
                JCheckBox check = new JCheckBox(parteiwerte.get(i).werte.get(j).Name);
                check.setSelected(parteiwerte.get(index).werte.get(indexj).wert);
                check.addChangeListener(e->parteiwerte.get(index).werte.get(indexj).wert = check.isSelected());
                Partei_row.add(check);
            }

            vb.add(Partei_row);
        }
        add(vb);
        Box hb = Box.createHorizontalBox();
        JButton berechnen = new JButton("Berechnen");
        berechnen.addActionListener(e->{
            fillRandom();
            get_results();
            buildUI();
            pack();
        });
        hb.add(berechnen);
        hb.add(Box.createHorizontalGlue());
        hb.add(new JLabel("Anteil der neuverteilt wird: "));
        JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerNumberModel(neuVerteilung,0,1,0.1));
        spinner.addChangeListener(e ->{
            neuVerteilung = (double) spinner.getValue();
        });
        hb.add(spinner);
        add(hb);
        add(new JSeparator(SwingConstants.HORIZONTAL));
        if (random_werte.size()>0) {
            for (int i = 0; i < parameter.size(); i++) {
                final int index = i;
                Box row = Box.createHorizontalBox();
                row.add(new JLabel(parameter.get(i).Pro));
                JSlider slider = new JSlider();
                slider.setMinimum(0);
                slider.setMaximum(100);
                slider.setValue((int) Math.round(random_werte.get(i) * 100));
                slider.addChangeListener(e->{
                    random_werte.set(index,(double)slider.getValue() /100);
                    get_results();
                    buildUI();
                    pack();
                });
                row.add(slider);
                row.add(new JLabel(parameter.get(i).Con));
                add(row);
            }
            Box row = Box.createHorizontalBox();
            for (int i = 0; i< results.size(); i++){

                vb = Box.createVerticalBox();
                vb.add(new JLabel(results.get(i).Name));
                Double wert = (double)Math.round(results.get(i).wert*1000)/10;
                vb.add(new JLabel(wert.toString()));
                row.add(vb);
                if (i< results.size()-1)
                    row.add(new JSeparator(SwingConstants.VERTICAL));
            }
            add(row);
        }
        else{
            for (int i= 0; i< parameter.size(); i++){
                hb = Box.createHorizontalBox();
                final int index = i;
                JTextField Pro_field = new JTextField();
                Pro_field.setText(parameter.get(i).Pro);
                Pro_field.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        parameter.get(index).Pro = Pro_field.getText();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        parameter.get(index).Pro = Pro_field.getText();

                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {

                    }
                });
                hb.add(Pro_field);

                JTextField Con_field = new JTextField();
                Con_field.setText(parameter.get(i).Con);
                Con_field.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        parameter.get(index).Con = Con_field.getText();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        parameter.get(index).Con = Con_field.getText();

                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {

                    }
                });
                hb.add(Con_field);

                ArrayList<Werte> checkList = fillWerte();
                for (int j = 0; j< checkList.size(); j++)
                {
                    final int indexj = j;
                    JCheckBox check = new JCheckBox(checkList.get(j).Name);
                    check.setSelected(parameter.get(index).Pro_werte.get(j).wert);
                    check.addChangeListener(e->
                        parameter.get(index).Pro_werte.get(indexj).wert=check.isSelected()
                    );
                    hb.add(check);
                }
                JButton del = new JButton("del");
                del.addActionListener(e -> {
                    parameter.remove(index);
                    buildUI();
                    pack();
                });
                hb.add(del);
                add(hb);

            }
            hb = Box.createHorizontalBox();
            JButton plus = new JButton("+");
            plus.addActionListener(e->{
                parameter.add(new ParteiParameter("Pro","Con",fillWerte()));
                buildUI();
                pack();
            });
            hb.add(plus);
            hb.add(Box.createHorizontalGlue());
            add(hb);
        }


    }

    public boolean checkEqualWert(ArrayList<Werte> partei, ArrayList<Werte> par){
        for (int i = 0; i< par.size(); i++)
        {
            if (par.get(i).wert) {
                for (int j = 0; j < partei.size(); j++) {
                    if (partei.get(j).Name.equals(par.get(i).Name) && !partei.get(j).wert)
                        return false;
                }
            }
        }
        return true;
    }

    public void get_results(){
        results = new ArrayList<>();
        double freiStimmmen = 0;
        for (int i= 0; i< ergebnis.size(); i++)
        {
            double wert = ergebnis.get(i).Ergebnis*neuVerteilung;
            results.add(new Prognose_result(ergebnis.get(i).Name,ergebnis.get(i).Ergebnis-wert));
            freiStimmmen += wert;
        }
        freiStimmmen /= results.size();
        for (int i= 0; i< parameter.size(); i++)
        {
            int anzahlPro = 0;
            for (int j= 0; j< results.size(); j++)
            {
                if (checkEqualWert(parteiwerte.get(j).werte,parameter.get(i).Pro_werte))
                    anzahlPro+=1;
            }

            for (int j=0; j< results.size(); j++)
            {
                if (checkEqualWert(parteiwerte.get(j).werte,parameter.get(i).Pro_werte) && anzahlPro != 0) {
                    double wert = freiStimmmen * (1 - random_werte.get(i)) / anzahlPro;
                    results.get(j).wert += wert;
                }
                else
                    if (results.size() - anzahlPro > 0 ) {
                        double wert = freiStimmmen * (random_werte.get(i)) / (results.size() - anzahlPro);
                        results.get(j).wert += wert;
                    }

            }

        }
        double summe = 0;
        for (int i=0; i< results.size(); i++)
            summe += results.get(i).wert;
        summe = 1- summe;
        summe /= 4;
        for (int i=0; i< results.size(); i++) {
            results.get(i).wert += summe;
        }

    }

    public void fillRandom(){
        random_werte = new ArrayList<>();
        for (int i = 0; i< parameter.size(); i++)
            random_werte.add(Math.random());
    }

    public void fillParteiWerte(){
        parteiwerte = new ArrayList<>();
        for (int i= 0; i< ergebnis.size(); i++) {
            ArrayList<Werte> werte = fillWerte();
            parteiwerte.add(new Parteiwerte(ergebnis.get(i).Name,werte));
        }

        //Preset Werte
        try (BufferedReader br = new BufferedReader(new FileReader(new File("Partei_Kategorie.preset")))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(";");
                for (int i = 0; i< parteiwerte.size(); i++)
                {
                    if (parteiwerte.get(i).Name.equals(split[0]))
                    {
                        for (int j = 1; j< split.length; j++)
                            changeWert(parteiwerte.get(i).werte,true,split[j]);
                    }
                }

            }
        }
        catch (java.io.IOException ex){
            //Fehler
        }

    }

    public void read_partei_ka(){
        try (BufferedReader br = new BufferedReader(new FileReader(new File("Partei_kategorie.settings")))) {
            String line;
            Partei_kat = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                Partei_kat.add(line);
            }
        }
        catch (java.io.IOException ex){
            //Fehler
        }
    }

    public ArrayList<Werte> fillWerte(){
        ArrayList<Werte> werte = new ArrayList<>();
        for (int i = 0; i< Partei_kat.size(); i++)
            werte.add(new Werte(Partei_kat.get(i),false));
        return werte;

    }

    public void changeWert(ArrayList<Werte> werte, boolean value, String iden){
        for (int i =0; i< werte.size(); i++)
        {
            if (werte.get(i).Name.equals(iden)) {
                werte.get(i).wert = value;
                return;
            }
        }
    }
    public void fillParteiParameter(){
        parameter = new ArrayList<>();
        random_werte = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File("Parameter_kategorie.settings")))) {
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<Werte> werte = fillWerte();
                String[] split = line.split(";");
                for (int i = 2; i< split.length; i++)
                {
                    changeWert(werte,true,split[i]);
                }
                parameter.add(new ParteiParameter(split[0],split[1],werte));
            }
        }
        catch (java.io.IOException ex){
            //Fehler
        }
    }
}
