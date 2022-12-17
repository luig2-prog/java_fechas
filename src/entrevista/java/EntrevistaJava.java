/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entrevista.java;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 *
 * @author luis
 */
public class EntrevistaJava {

    private int year;
    private int easterMonth;
    private int easterDay;
//    private String diaString = "";
    private ArrayList<String> holidays;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sca = new Scanner(System.in);

        //Dia de pago ingresado por el usuario esta fecha sería el 1 de enero de 2021
        System.out.println("Año: ");
        int anio = sca.nextInt();
        System.out.println("Mes: ");
        int mes = sca.nextInt();
        System.out.println("Día: ");
        int dia = sca.nextInt();

        Calendar datePago = Calendar.getInstance();
        datePago.set(anio, mes - 1, dia);

        EntrevistaJava entrevistaJava = new EntrevistaJava(anio);
        
        System.out.println("Resultado: " + entrevistaJava.fechaPago(datePago));

    }

    public String diaToString(String fecha) {
        return "" + fecha.charAt(0) + fecha.charAt(1)+fecha.charAt(2) + "";
    }

    /**
     * @param fechaIni la fecha que ingresa el usuario, se tiene que ingresar de
     * tipo Calendar
     * @return la fecha de pago del empleado
     */
    public Date fechaPago(Calendar fechaIni) {
//        Date fecha = fechaIni;
        int diaPagoUsuario = fechaIni.getTime().getDate();
        int mesPagoUsuario = fechaIni.getTime().getMonth();
        int añoPagoUsuario = this.year;
        
        int diasHasta15 = 15 - diaPagoUsuario, diasHasta30 = 30 - diaPagoUsuario;

        if (diasHasta15 < 0) {
            diasHasta15 = diasHasta15 * -1;
        }

        if (diasHasta15 < diasHasta30) {
            diaPagoUsuario = 15;
        } else {
            diaPagoUsuario = 30;
        }
        
        fechaIni.set(this.year, mesPagoUsuario, diaPagoUsuario);
        
        String diaString = diaToString(fechaIni.getTime().toString());

        switch (diaString) {
            case "Sat":
                diaPagoUsuario = diaPagoUsuario - 1;
                fechaIni.set(this.year, mesPagoUsuario, diaPagoUsuario);
                break;
            case "Sun":
                System.out.println("suuuuuuu");
                if (isHoliday(mesPagoUsuario, diaPagoUsuario + 1)) {
                    diaPagoUsuario = diaPagoUsuario - 2;
                } else {
                    diaPagoUsuario += 1;
                }

                fechaIni.set(this.year, mesPagoUsuario, diaPagoUsuario);
                break;
            default:
                if (isHoliday(mesPagoUsuario, diaPagoUsuario)) {
                    diaPagoUsuario = diaPagoUsuario - 1;
                     fechaIni.setTime(fechaPago(fechaIni));
                }
                fechaIni.set(this.year, mesPagoUsuario, diaPagoUsuario);
               

        }

       
        return fechaIni.getTime();
    }

    public EntrevistaJava(int year) {
        this.year = year;
        this.holidays = new ArrayList<>();
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int g = (8 * b + 13) / 25;
        int h = (19 * a + b - d - g + 15) % 30;
        int j = c / 4;
        int k = c % 4;
        int m = (a + 11 * h) / 319;
        int r = (2 * e + 2 * j - k - h + m + 32) % 7;
        this.easterMonth = (h - m + r + 90) / 25;
        this.easterDay = (h - m + r + this.easterMonth + 19) % 32;
        this.easterMonth--;
        this.holidays.add("0:1");               // Primero de Enero
        this.holidays.add("4:1");               // Dia del trabajo 1 de mayo
        this.holidays.add("6:20");              //Independencia 20 de Julio
        this.holidays.add("7:7");               //Batalla de boyaca 7 de agosto
        this.holidays.add("11:8");              //Maria inmaculada 8 de diciembre
        this.holidays.add("11:25");             //Navidad 25 de diciembre
        this.calculateEmiliani(0, 6);           // Reyes magos 6 de enero
        this.calculateEmiliani(2, 19);          //San jose 19 de marzo
        this.calculateEmiliani(5, 29);          //San pedro y san pablo 29 de junio
        this.calculateEmiliani(7, 15);          //Asuncion 15 de agosto
        this.calculateEmiliani(9, 12);          //Descubrimiento de america 12 de octubre
        this.calculateEmiliani(10, 1);          //Todos los santos 1 de noviembre
        this.calculateEmiliani(10, 11);         //Independencia de cartagena 11 de noviembre
        this.calculateOtherHoliday(-3, false);  //jueves santos
        this.calculateOtherHoliday(-2, false);  //viernes santo
        this.calculateOtherHoliday(40, true);   //Asención del señor de pascua
        this.calculateOtherHoliday(60, true);   //Corpus cristi
        this.calculateOtherHoliday(68, true);   //Sagrado corazon
    }

    private void calculateOtherHoliday(int days, boolean emiliani) {
        Calendar date = Calendar.getInstance();
        date.set(this.year, this.easterMonth, this.easterDay);
        date.add(Calendar.DATE, days);
        if (emiliani) {
            this.calculateEmiliani(date.get(Calendar.MONTH), date.get(Calendar.DATE));
        } else {
            this.holidays.add(date.get(Calendar.MONTH) + ":" + date.get(Calendar.DATE));
        }
    }

    private void calculateEmiliani(int month, int day) {
        Calendar date = Calendar.getInstance();
        date.set(this.year, month, day);
        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                date.add(Calendar.DATE, 1);
                break;
            case 3:
                date.add(Calendar.DATE, 6);
                break;
            case 4:
                date.add(Calendar.DATE, 5);
                break;
            case 5:
                date.add(Calendar.DATE, 4);
                break;
            case 6:
                date.add(Calendar.DATE, 3);
                break;
            case 7:
                date.add(Calendar.DATE, 2);
                break;
            default:
                break;
        }
        this.holidays.add(date.get(Calendar.MONTH) + ":" + date.get(Calendar.DATE));
    }

    public boolean isHoliday(int month, int day) {
        return this.holidays.contains(month + ":" + day);
    }
}
