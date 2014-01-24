package fi.paivola.mapserver.models;

import fi.paivola.mapserver.core.ConnectionModel;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.Color;

/**
 *
 * @author Nassikka
 */
public class CriminalConnection extends ConnectionModel{
    public CriminalConnection(int id) {
        super(id);
        this.passthrough = true;
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {

    }

    @Override
    public void onEvent(Event e, DataFrame current) {

    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.color = new Color(64, 128, 256);
        sm.name = "CriminalConnection";
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {

    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {

    }
}
