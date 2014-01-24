/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.paivola.mapserver.models;

/**
 *
 * @author Nassikka
 */
public class Criminal {
    String location;
    int toBeBack;
    
    public Criminal()
    {
        //määritellää luodulle rikolliselle paikaksi tukikohta ja tukikohtaan
        //palaamisajaksi 0
        location = "base";
        toBeBack = 0;
    }
    
    public Criminal(String loc, int back)
    {
        location = loc;
        toBeBack = back;
    }
    
    //Palauttaa rikollisen olinpaikan
    public String getLocation() { return location; };
    
    //Määrittää rikolliselle uuden olinpaikan (varastus/merirosvoilu/kaappaaminen)
    public void setLocation(String newLocation) { location = newLocation; };
    
    //Palauttaa rikollisen tukikohtaanpalaamisajan (=kuinka monta viikkoa, ennen kuin reissu loppuu)
    public int getToBeBack() { return toBeBack; };
    
    //Määrittää rikolliselle uuden paluuajan
    public void setToBeBack(int newTime) { toBeBack = newTime; };
}