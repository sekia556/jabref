package net.sf.jabref.specialfields;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import net.sf.jabref.GUIGlobals;
import net.sf.jabref.Globals;

public class RankExtended extends Rank {

    private static RankExtended INSTANCE = null;


    private RankExtended() {
        super();
        ArrayList<SpecialFieldValue> values = new ArrayList<SpecialFieldValue>();
        values.add(new SpecialFieldValue(this, Globals.lang("null"), "clearRank", Globals.lang("Clear rank"), null, Globals.lang("No rank information")));
        values.add(new SpecialFieldValue(this, Globals.lang("rank1"), "setRank1", Globals.lang("Set rank to one star"), GUIGlobals.getImage("rank1"), Globals.lang("One star")));
        values.add(new SpecialFieldValue(this, Globals.lang("rank2"), "setRank2", Globals.lang("Set rank to two stars"), GUIGlobals.getImage("rank2"), Globals.lang("Two stars")));
        values.add(new SpecialFieldValue(this, Globals.lang("rank3"), "setRank3", Globals.lang("Set rank to three stars"), GUIGlobals.getImage("rank3"), Globals.lang("Three stars")));
        values.add(new SpecialFieldValue(this, Globals.lang("rank4"), "setRank4", Globals.lang("Set rank to four stars"), GUIGlobals.getImage("rank4"), Globals.lang("Four stars")));
        values.add(new SpecialFieldValue(this, Globals.lang("rank5"), "setRank5", Globals.lang("Set rank to five stars"), GUIGlobals.getImage("rank5"), Globals.lang("Five stars")));
        this.setValues(values);
    }

    public static RankExtended getInstance() {
        if (RankExtended.INSTANCE == null) {
            RankExtended.INSTANCE = new RankExtended();
        }
        return RankExtended.INSTANCE;
    }

    @Override
    public ImageIcon getRepresentingIcon() {
        return this.getValues().get(1).getIcon();
    }

}
