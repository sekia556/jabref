/*  Copyright (C) 2012 JabRef contributors.
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/
package net.sf.jabref.specialfields;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import net.sf.jabref.GUIGlobals;
import net.sf.jabref.Globals;

public class Priority extends SpecialField {

    private static Priority INSTANCE = null;

    private final ImageIcon icon = new ImageIcon(GUIGlobals.getIconUrl("priority"));


    private Priority() {
        ArrayList<SpecialFieldValue> values = new ArrayList<SpecialFieldValue>();
        values.add(new SpecialFieldValue(this, Globals.lang("null"), "clearPriority", Globals.lang("Clear priority"), null, Globals.lang("No priority information")));
        ImageIcon icon;
        icon = GUIGlobals.getImage("red");
        values.add(new SpecialFieldValue(this, Globals.lang("prio1"), "setPriority1", Globals.lang("Set priority to high"), icon, Globals.lang("Priority high")));
        icon = GUIGlobals.getImage("orange");
        values.add(new SpecialFieldValue(this, Globals.lang("prio2"), "setPriority2", Globals.lang("Set priority to medium"), icon, Globals.lang("Priority medium")));
        icon = GUIGlobals.getImage("green");
        values.add(new SpecialFieldValue(this, Globals.lang("prio3"), "setPriority3", Globals.lang("Set priority to low"), icon, Globals.lang("Priority low")));
        this.setValues(values);
        TEXT_DONE_PATTERN = "Set priority to '%0' for %1 entries";
    }

    public static Priority getInstance() {
        if (Priority.INSTANCE == null) {
            Priority.INSTANCE = new Priority();
        }
        return Priority.INSTANCE;
    }

    @Override
    public String getFieldName() {
        return SpecialFieldsUtils.FIELDNAME_PRIORITY;
    }

    @Override
    public ImageIcon getRepresentingIcon() {
        return this.icon;
    }

    @Override
    public String getToolTip() {
        return Globals.lang("Priority");
    }

    @Override
    public String getMenuString() {
        return Globals.lang("Priority");
    }
}
