package fi.paivola.mapserver.models;

import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.setting.SettingDouble;
import fi.paivola.mapserver.core.setting.SettingInt;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.Icon;
import fi.paivola.mapserver.utils.RangeDouble;
import fi.paivola.mapserver.utils.RangeInt;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Nassikka
 */
public class CriminalPoint extends PointModel{
        private int population;
        
        //N‰m‰ kertovat kuinka paljon kriminaaleja on miss‰kin. criminals = basessa
        public int criminals;
        public int stealers;
        public int pirates;
        public int hijackers;
        
        //Rikollisten lis‰‰ntymisprosentti
        private double moreCriminals;
        
        //Kyl‰n ruoka ja rikollisten ruoka
        private int popFood;
        public int food;
        
        //Rikollisten raha ja tavarat
        public int money;
        public int items;
        private int itemValue;
        
        //Jakautuminen, eli kuinka monta % kussakin harrastuksessa
        public int stealing;
        public int piracy;
        public int hijack;
        
        //Kuinka paljon miss‰kin ryhm‰ss‰ on rikollisia
        private int stealGroup;
        private int piracyGroup;
        private int hijackGroup;
        
        //Viikkojen m‰‰r‰, mik‰ kest‰‰ miss‰kin harrastuksessa
        private int stealTime;
        private int piracyTime;
        private int hijackTime;
        
        //todenn‰kˆisyydet, ett‰ on sotilaita mukana
        private int stealingSoldier;
        private int piracySoldier;
        private int hijackingSoldier;
        
        //K‰ytet‰‰n kun etsit‰‰n rikollisten joukosta loput ryhm‰‰n kuuluvat
        private int remaining;
        
        //Todenn‰kˆisyys ett‰ varastaminen onnistuu
        private int probSteal;
        private int probPiracy;
        private int probHijack;
        
        //M‰‰r‰t, mit‰ saadaan mist‰kin onnistuneesta keikasta
        private int foodGotFromStealing;
        private int foodGotFromPirating;
        private int moneyGotFromPirating;
        private int itemsGotFromPirating;
        private int foodGotFromHijacking;
        private int moneyGotFromHijacking;
        private int itemsGotFromHijacking;
        
        //Lista kaikista kriminaaleista
        private ArrayList<Criminal> criminalList = new ArrayList<Criminal>();
    
    public CriminalPoint(int id) {
        super(id);
        population = 10000;
        criminals = (int)((population/100) * 1.25);
        stealers = 0;
        pirates = 0;
        hijackers = 0;
        moreCriminals = 0.05;
        popFood = population;
        food = 3*(int)((popFood/100) * 1.25);
        money = criminals;
        items = criminals;
        itemValue = 100;
        stealing = 30;
        piracy = 30;
        hijack = 40;
        stealGroup = 3;
        piracyGroup = 10;
        hijackGroup = 5;
        stealTime = 1;
        piracyTime = 3;
        hijackTime = 2;
        stealingSoldier = 5;
        piracySoldier = 40;
        hijackingSoldier = 30;
        probSteal = 60;
        probPiracy = 40;
        probHijack = 70;
        foodGotFromStealing = 5;
        foodGotFromPirating = 5;
        moneyGotFromPirating = 10;
        itemsGotFromPirating = 4;
        foodGotFromHijacking = 6;
        moneyGotFromHijacking = 6;
        itemsGotFromHijacking = 2; 
        
        //Luodaan aluksi kriminaalit
        for (int i=0;i<criminals;i++)
        {
            criminalList.add(new Criminal());
        }
    }
    
    @Override
    public void onTick(DataFrame last, DataFrame current) {
        /* These cat's are quite strange. 
         If there are cats in the world, there is a chance of atleast 
         one of them being atleast here. What a strange world do we live in.
        if (last.getGlobalInt("cats") > 0 && (new Random()).nextFloat() < this.skill) {
            // If we saw a cat, let's tell our neighbours about it!
            this.addEventToAll(current, new Event("I saw a cat!", Event.Type.NOTIFICATION, null));
            this.catSightings++;
        }

        this.saveInt("catsSeen", catSightings);
        */
        
        //Aluksi kriminaaleja tulee aina lis‰‰ tietty %m‰‰r‰ populaatiosta
            int added = (int)((population/100) * moreCriminals);
            criminals += added;
            
            for (int o = 0; o < added; o++)
            {
                criminalList.add(new Criminal());
            }
            
        //pistet‰‰n kriminaaleja varastamaan jos tarvetta
            while (stealers < ((float)criminalList.size()/(float)100)*(float)stealing && criminals >= stealGroup)
            {
                remaining = stealGroup;
                //K‰yd‰‰n l‰pi koko varaslista
                for (int k=0; k<criminalList.size();k++)
                {
                    //Lis‰t‰‰n tukikohdasta rikollisia varastamaan niin paljon kuin on tarvetta
                    if (criminalList.get(k).getLocation().equals("base"))
                    {
                        remaining--;
                        criminalList.get(k).setLocation("stealing");
                        criminalList.get(k).setToBeBack(stealTime);
                        criminals--;
                        stealers ++;
                    }
                    //Kun varastusryhm‰ on kasassa, otetaan tarvittavat tavarat tukikohdasta
                    if (remaining == 0){
                        food -= stealGroup*stealTime;
                        items -= stealGroup;
                        money -= stealGroup;
                        this.addEventToAll(current, new Event(stealGroup + " stealers started a business trip!", Event.Type.NOTIFICATION, null));
                        break;
                    }
                }
            }
            
            //pistet‰‰n kriminaaleja kaappaamaan laivoja jos tarvetta
            while (pirates < ((float)criminalList.size()/(float)100)*(float)piracy && criminals >= piracyGroup)
            {
                remaining = piracyGroup;
                //K‰yd‰‰n l‰pi koko varaslista
                for (int k=0; k<criminalList.size();k++)
                {
                    //tarkistetan, onko tukikohdassa varkaita, jotka voisi laittaa tˆihin
                    if (criminalList.get(k).getLocation().equals("base"))
                    {
                        remaining--;
                        criminalList.get(k).setLocation("pirating");
                        criminalList.get(k).setToBeBack(piracyTime);
                        criminals--;
                        pirates ++;
                    }
                    
                    //Kun remaining=0, eli kun piraattiryhm‰ on t‰ysi, otetaan tukikohdasta tarvittava m‰‰r‰ resursseja
                    if (remaining == 0){
                        food -= piracyGroup*piracyTime;
                        items -= piracyGroup;
                        money -= piracyGroup;
                        this.addEventToAll(current, new Event(piracyGroup + " pirates started a business trip!", Event.Type.NOTIFICATION, null));
                        break;
                    }
                }
            }
            
            //pistet‰‰n kriminaaleja kaappaamaan rekkoja jos tarvetta
            while (hijackers < ((float)criminalList.size()/(float)100)*(float)hijack && criminals >= hijackGroup)
            {
                //m‰‰ritell‰‰n kaappausryhm‰n koko
                remaining = hijackGroup;
                //k‰yd‰‰n l‰pi koko rikollislista
                for (int k=0; k<criminalList.size();k++)
                {
                    //tarkistetaan, onko tukikohdassa rikollisia, joita voitaisiin laittaa kaappaamaan
                    if (criminalList.get(k).getLocation().equals("base"))
                    {
                        remaining--;
                        criminalList.get(k).setLocation("hijacking");
                        criminalList.get(k).setToBeBack(3);
                        criminals--;
                        hijackers ++;
                    }
                    
                    //kun kaappauryhm‰ on t‰ysi, otetaan tarvittavat resurssit tukikohdasta
                    if (remaining == 0){
                        food -= hijackGroup*hijackTime;
                        items -= hijackGroup;
                        money -= hijackGroup;
                        this.addEventToAll(current, new Event(hijackGroup + " hijackers started a business trip!", Event.Type.NOTIFICATION, null));
                        break;
                    }
                }
            }
            
            //T‰m‰ tehd‰‰n kaikille rikollisille joka viikko: niiden takaisintulop‰iv‰‰ v‰hennet‰‰n yhdell‰
            //Ja jos se on 0, niin katsotaan mit‰ k‰y ja tehd‰‰n muille ryhm‰l‰isille sama
            for (int t = 0; t < criminalList.size(); t++)
            {
                //v‰hennet‰‰n viikkoja yhdell‰
                criminalList.get(t).setToBeBack(criminalList.get(t).getToBeBack()-1);
                
                //Jos rikolliset tulevat takaisin t‰ll‰ viikolla
                if (criminalList.get(t).getToBeBack() == 0 && !criminalList.get(t).getLocation().equals("base"))
                {
                    //tn, ett‰ onnistuu rikos
                    Boolean successing;
                    
                    //Katsotaan, mik‰ ryhm‰ on kyseess‰ ja katsotaan onnistutaanko
                    if (criminalList.get(t).getLocation().equals("stealing"))
                    {
                        successing = success(stealingSoldier, probSteal);
                        remaining = stealGroup -1;
                    } else if (criminalList.get(t).getLocation().equals("pirating"))
                    {
                        successing = success(piracySoldier, probPiracy);
                        remaining = piracyGroup -1;
                    } else 
                    {
                        successing = success(hijackingSoldier, probHijack);
                        remaining = hijackGroup -1;
                    }
                   
                    //Katsotaan mit‰ tehd‰‰n
                    switch (result(successing))
                    {
                        //jos success
                        case 1:
                            //Otetaan listasta niin monta kriminaalia kuin ryhm‰ss‰ on ja pistet‰‰n ne baseen
                            for (int k=t + 1; k<criminalList.size(); k++)
                            {
                                //Jos kuuluu samaan ryhm‰‰n
                                if (criminalList.get(k).getLocation().equals(criminalList.get(t).getLocation()) && criminalList.get(k).getToBeBack() == 1)
                                {
                                    //Pistet‰‰n takaisin baseen
                                    criminalList.get(k).setToBeBack(0);
                                    criminalList.get(k).setLocation("base");
                                    remaining--;
                                    criminals++;
                                    
                                    //V‰hennet‰‰n yksi criminaali harrastuksesta
                                    if (criminalList.get(t).getLocation().equals("stealing"))
                                    {
                                        stealers--;
                                    }
                                    else if (criminalList.get(t).getLocation().equals("pirating"))
                                    {
                                        pirates--;
                                    }
                                    else
                                    {
                                        hijackers--;
                                    }
                                }
                        
                                //Kun viimeinenkin ryhm‰n j‰sen on k‰yty niin tehd‰‰n operaatiot viel‰ ensimm‰isellekin
                                if (remaining == 0)
                                {  
                                    if (criminalList.get(t).getLocation().equals("stealing"))
                                    {
                                        //Varastettu ruokaa
                                        food += foodGotFromStealing * stealGroup;
                                        items += stealGroup;
                                        money += stealGroup;
                                        stealers--;
                                    } 
                                    else if (criminalList.get(t).getLocation().equals("pirating"))
                                    {
                                        //Saadaan kaikkea
                                        food += foodGotFromPirating * piracyGroup;
                                        items += itemsGotFromPirating * piracyGroup + piracyGroup;
                                        money += moneyGotFromPirating + piracyGroup;
                                        pirates--;
                                    } 
                                    else 
                                    {
                                        //Saadaan kaikkea
                                        food += foodGotFromHijacking * hijackGroup;
                                        items += itemsGotFromHijacking * hijackGroup + hijackGroup;
                                        money += moneyGotFromHijacking * hijackGroup + hijackGroup;
                                        hijackers--;
                                    }
                                    
                                    criminalList.get(t).setLocation("base");
                                    
                                    criminals++;
                                    
                                    break;
                                }
                            }
                            break;
                        
                        case 2:
                            //J‰‰d‰‰n kiinni itse ja tavarat, eli criminaalit poistuvat keskuudestamme!
                            
                            for (int k=t + 1; k<criminalList.size(); k++)
                            {
                                //Jos kuuluu samaan ryhm‰‰n
                                if (criminalList.get(k).getLocation().equals(criminalList.get(t).getLocation()) && criminalList.get(k).getToBeBack() == 1)
                                {
                                    //Poistetaan kriminaali olemasta
                                    criminalList.remove(k);
                                    remaining--;
                                    k--;
                                    
                                    if (criminalList.get(t).getLocation().equals("stealing"))
                                    {
                                        stealers--;
                                    }
                                    else if (criminalList.get(t).getLocation().equals("pirating"))
                                    {
                                        pirates--;
                                    }
                                    else
                                    {
                                        hijackers--;
                                    }
                                }
                                
                                //Jos kaikki ryhm‰l‰iset k‰yty, niin tehd‰‰n viel‰ operaatiot ensimm‰isellekin
                                if (remaining == 0)
                                {
                                    if (criminalList.get(t).getLocation().equals("stealing"))
                                    {
                                        stealers--;
                                    }
                                    else if (criminalList.get(t).getLocation().equals("pirating"))
                                    {
                                        pirates--;
                                    }
                                    else
                                    {
                                        hijackers--;
                                    }
                                    
                                    criminalList.remove(t);
                                    break;
                                }
                            }
                            break;
                        
                        case 3:
                            //T‰llˆin tavarat j‰‰v‰t ja rosmot p‰‰sev‰t pakoon
                            for (int k=t + 1; k<criminalList.size(); k++)
                            {
                                //Jos kuuluu samaan ryhm‰‰n
                                if (criminalList.get(k).getLocation().equals(criminalList.get(t).getLocation()) && criminalList.get(k).getToBeBack() == 1)
                                {
                                    //Pistet‰‰n takaisin baseen
                                    criminalList.get(k).setToBeBack(0);
                                    criminalList.get(k).setLocation("base");
                                    remaining--;
                                    criminals++;
                                    
                                    //V‰hennet‰‰n yksi criminaali harrastuksesta
                                    if (criminalList.get(t).getLocation().equals("stealing"))
                                    {
                                        stealers--;
                                    }
                                    else if (criminalList.get(t).getLocation().equals("pirating"))
                                    {
                                        pirates--;
                                    }
                                    else
                                    {
                                        hijackers--;
                                    }
                                }
                        
                                //Kun viimeinenkin ryhm‰n j‰sen on k‰yty niin tehd‰‰n operaatiot viel‰ ensimm‰isellekin
                                if (remaining == 0)
                                {  
                                    if (criminalList.get(t).getLocation().equals("stealing"))
                                    {
                                        stealers--;
                                    } 
                                    else if (criminalList.get(t).getLocation().equals("pirating"))
                                    {
                                        pirates--;
                                    } 
                                    else 
                                    {
                                        hijackers--;
                                    }
                                    
                                    criminalList.get(t).setLocation("base");
                                    
                                    criminals++;
                                    
                                    break;
                                }
                            }
                            break;
                        
                        case 4:
                            //T‰llˆin ei j‰‰d‰ kiinni eli ei mene tavarat eik‰ rosvot, mutta ei saada mit‰‰n uuttakaan
                            for (int k=t + 1; k<criminalList.size(); k++)
                            {
                                //Jos kuuluu samaan ryhm‰‰n
                                if (criminalList.get(k).getLocation().equals(criminalList.get(t).getLocation()) && criminalList.get(k).getToBeBack() == 1)
                                {
                                    //Pistet‰‰n takaisin baseen
                                    criminalList.get(k).setToBeBack(0);
                                    criminalList.get(k).setLocation("base");
                                    remaining--;
                                    criminals++;
                                    
                                    //V‰hennet‰‰n yksi criminaali harrastuksesta
                                    if (criminalList.get(t).getLocation().equals("stealing"))
                                    {
                                        stealers--;
                                    }
                                    else if (criminalList.get(t).getLocation().equals("pirating"))
                                    {
                                        pirates--;
                                    }
                                    else
                                    {
                                        hijackers--;
                                    }
                                }
                        
                                //Kun viimeinenkin ryhm‰n j‰sen on k‰yty niin tehd‰‰n operaatiot viel‰ ensimm‰isellekin
                                if (remaining == 0)
                                {  
                                    if (criminalList.get(t).getLocation().equals("stealing"))
                                    {
                                        items += stealGroup;
                                        money += stealGroup;
                                        
                                        stealers--;
                                    } 
                                    else if (criminalList.get(t).getLocation().equals("pirating"))
                                    {
                                        items += piracyGroup;
                                        money += piracyGroup;
                                        
                                        pirates--;
                                    } 
                                    else 
                                    {
                                        items += hijackGroup;
                                        money += hijackGroup;
                                        hijackers--;
                                    }
                                    
                                    criminalList.get(t).setLocation("base");
                                    
                                    criminals++;
                                    
                                    break;
                                }
                            }
                            break;    
                    }
                }
            }
            
            if (food - criminals >= 0)
                food -= criminals;
            else if (money + food - criminals >= 0)
            {
                int bought = -1 * (food - criminals);
                food = 0;
                money -= bought;
            }
            else if (items*itemValue + money + food - criminals >= 0)
            {
                int bought = -1 * (food - criminals);
                food = 0;
                bought -= money;
                money = 0;
                do
                {
                    items--;
                    money += itemValue;
                    if (bought > money)
                    {
                        money = 0;
                        bought -= itemValue;
                    } else
                    {
                        money -= bought;
                        break;
                    }
                } while (bought < 0);
            } else
            {
                food -= criminals;
            }
            this.saveInt("criminalFood", food);
            this.saveInt("criminalMoney", money);
            this.saveInt("criminalItems", items);
            this.saveInt("criminalsInBase", criminals);
            this.saveInt("stealers", stealers);
            this.saveInt("pirates", pirates);
            this.saveInt("hijackers", hijackers);
            this.saveInt("totalCriminals", criminalList.size());
    }
    
    @Override
    public void onEvent(Event e, DataFrame current) {

    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.setIcon(Icon.TOWN); // ToDo: Have some effect.
        sm.color = new Color(255, 204, 255); // What color is displayed in client.
        sm.settings.put("criminalFood", new SettingInt("How much food do the criminals have?", 375, new RangeInt(3*Math.round(population*0.01f), 3*Math.round(population*0.1f))));
        sm.settings.put("criminalMoney", new SettingInt("How much money do the criminals have?", 125, new RangeInt(Math.round(population*0.01f), Math.round(population*0.1f))));
        sm.settings.put("criminalItems", new SettingInt("How many items do the criminals have?", 125, new RangeInt(Math.round(population*0.01f), Math.round(population*0.1f))));
        sm.settings.put("criminalsInBase", new SettingInt("How how many criminals there are in base?", 125, new RangeInt(Math.round(population*0.01f), Math.round(population*0.1f))));
        sm.settings.put("criminalsStealing", new SettingInt("How many stealers there are in one group?", 3, new RangeInt(1, 20)));
        sm.settings.put("criminalsPirating", new SettingInt("How many pirates there are in one group?", 10, new RangeInt(1, 20)));
        sm.settings.put("criminalsHijacking", new SettingInt("How many hijackers there are in one group?", 5, new RangeInt(1, 20)));
        sm.settings.put("totalCriminals", new SettingInt("How many criminals there are in total?", 125, new RangeInt(Math.round(population*0.01f), Math.round(population*0.1f))));
        sm.allowedNames.add("criminalConnection"); // The things trying to get connected to this need satisfy atleast one of these tags.
        sm.name = "criminalPoint";
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
        this.food = Integer.parseInt(sm.settings.get("criminalFood").getValue());
        this.money = Integer.parseInt(sm.settings.get("criminalMoney").getValue());
        this.items = Integer.parseInt(sm.settings.get("criminalItems").getValue());
        this.criminals = Integer.parseInt(sm.settings.get("criminalsInBase").getValue());
        this.stealGroup = Integer.parseInt(sm.settings.get("criminalsStealing").getValue());
        this.piracyGroup = Integer.parseInt(sm.settings.get("criminalsPirating").getValue());
        this.hijackGroup = Integer.parseInt(sm.settings.get("criminalsHijacking").getValue());
    }
    
    //funktio, joka ottaa parametrein‰ kriminaalien tekem‰n homman todenn‰kˆisyydet
     private Boolean success(int soldiers, int successRate)
    {
         Random random = new Random();
           //Jos on aseistettuja vartijoita, ei onnistuta         
         if ((float)(soldiers)/(float)(100) >= random.nextFloat())
         {
             return false;
         }
         //Jos onnistumisen tn on pienemp‰‰ kuin random tn, ei onnistuta
         else if ((float)(successRate)/(float)100 <= random.nextFloat())
         {
             return false;
         }
              
         return true;
    }
     
    //Funktio, joka ottaa parametrin‰ onnistumisen (true jos onnistuu), ja palautta intin, joka kertoo mit‰ tapahtuu
    private int result(Boolean success)
    {
        if (success)
        {
            //T‰llˆin saadaan varastettua tavarat ja kukaan ei j‰‰ kiinni
            return 1;
        }
        else
        {
            Random random = new Random();
            if ((float)0.5 <= random.nextFloat())
            {
                //T‰llˆin j‰‰d‰‰n kiinni
                if ((float)0.5 <= random.nextFloat())
                {
                    //T‰llˆin j‰‰d‰‰n itse kiinni ja myˆs tavarat menetet‰‰n
                    return 2;
                } 
                else
                {
                    //T‰llˆin vain tavarat j‰‰v‰t kiinni ja rosvot p‰‰sev‰t pakoon
                    return 3;
                }
            }
            else
            {
                //T‰llˆin ei j‰‰d‰ kiinni eli ei mene tavarat eiv‰tk‰ rosvot
                return 4;
            }
        }
       
    }
}
