package fi.paivola.mapserver.models;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.ExtensionModel;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.setting.SettingDouble;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.RangeDouble;
import java.util.Random;

/**
 *
 * @author Nassikka
 */
public class CriminalExtender extends ExtensionModel{

    public CriminalExtender(int id) {
        super(id);
    }

    /**
     * Extending models have a tick that is run after the extensions master has
     * completed it's tick and events.
     *
     * @param last
     * @param current
     */
    @Override
    public void onTick(DataFrame last, DataFrame current) {
    }

    /**
     * This is triggered by each of the parents events.
     *
     * @param e
     */
    @Override
    public void onEvent(Event e, DataFrame current) {
        switch (e.name) {
        }
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.name = "criminalExtender"; 
        sm.exts = "criminalPoint";
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
    }
}
