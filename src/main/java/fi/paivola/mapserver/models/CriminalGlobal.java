package fi.paivola.mapserver.models;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GlobalModel;
import fi.paivola.mapserver.core.setting.SettingDouble;
import fi.paivola.mapserver.core.setting.SettingInt;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.RangeDouble;
import fi.paivola.mapserver.utils.RangeInt;
import static java.lang.Integer.parseInt;
import java.util.Random;

/**
 * Example global model READ THIS.
 *
 * @author nassikka
 */
public class CriminalGlobal extends GlobalModel {

    private int criminals = 0;

    public CriminalGlobal(int id) {
        super(id);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        // This right here saves a global piece of data. Others can get it by using getGlobalData.
        //current.saveGlobalData("criminals", last.getGlobalInt("criminals"));
    }

    @Override
    public void onEvent(Event e, DataFrame current) {

    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.name = "criminalGlobal";
        sm.settings.put("criminals", new SettingInt("How many criminals are there?", criminals, new RangeInt(0, 333)));
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        //df.saveGlobalData("criminals", 125);
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
       //criminals = parseInt(sm.settings.get("criminals").getValue());
    }

}
