/*  Copyright (C) 2003-2011 JabRef contributors.
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
package net.sf.jabref;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

import net.sf.jabref.undo.UndoablePreambleChange;

class PreambleEditor extends JDialog {

    // A reference to the entry this object works on.
    private final BibtexDatabase base;
    private final BasePanel panel;
    private final JabRefFrame baseFrame;
    private final JabRefPreferences prefs;

    // Layout objects.
    private final GridBagLayout gbl = new GridBagLayout();
    private final GridBagConstraints con = new GridBagConstraints();
    JLabel lab;
    private final Container conPane = getContentPane();
    //JToolBar tlb = new JToolBar();
    private final JPanel pan = new JPanel();
    private FieldEditor ed;


    public PreambleEditor(JabRefFrame baseFrame,
            BasePanel panel, BibtexDatabase base,
            JabRefPreferences prefs) {
        super(baseFrame);
        this.baseFrame = baseFrame;
        this.panel = panel;
        this.base = base;
        this.prefs = prefs;

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                closeAction.actionPerformed(null);
            }

            @Override
            public void windowOpened(WindowEvent e) {
                ed.requestFocus();
            }
        });
        setFocusTraversalPolicy(new LayoutFocusTraversalPolicy() {

            @Override
            protected boolean accept(Component c) {
                return (super.accept(c) && (c instanceof FieldEditor));
            }
        });

        int prefHeight = (int) (GUIGlobals.PE_HEIGHT * GUIGlobals.FORM_HEIGHT[prefs.getInt("entryTypeFormHeightFactor")]);
        setSize(GUIGlobals.FORM_WIDTH[prefs.getInt("entryTypeFormWidth")], prefHeight);

        pan.setLayout(gbl);
        con.fill = GridBagConstraints.BOTH;
        con.weighty = 1;
        con.insets = new Insets(10, 5, 10, 5);

        String content = base.getPreamble();

        ed = new FieldTextArea(Globals.lang("Preamble"), ((content != null) ? content : ""));
        //ed.addUndoableEditListener(panel.undoListener);
        setupJTextComponent((FieldTextArea) ed);

        gbl.setConstraints(ed.getLabel(), con);
        pan.add(ed.getLabel());

        con.weightx = 1;

        gbl.setConstraints(ed.getPane(), con);
        pan.add(ed.getPane());

        //tlb.add(closeAction);
        //conPane.add(tlb, BorderLayout.NORTH);
        conPane.add(pan, BorderLayout.CENTER);
        setTitle(Globals.lang("Edit preamble"));
    }

    private void setupJTextComponent(javax.swing.text.JTextComponent ta) {
        // Set up key bindings and focus listener for the FieldEditor.
        ta.getInputMap().put(prefs.getKey("Close preamble editor"), "close");
        ta.getActionMap().put("close", closeAction);
        ta.getInputMap().put(prefs.getKey("Preamble editor, store changes"), "store");
        ta.getActionMap().put("store", storeFieldAction);
        ta.getInputMap().put(prefs.getKey("Close preamble editor"), "close");
        ta.getActionMap().put("close", closeAction);

        ta.getInputMap().put(prefs.getKey("Undo"), "undo");
        ta.getActionMap().put("undo", undoAction);
        ta.getInputMap().put(prefs.getKey("Redo"), "redo");
        ta.getActionMap().put("redo", redoAction);

        ta.addFocusListener(new FieldListener());
    }

    public void updatePreamble() {
        ed.setText(base.getPreamble());
    }


    private class FieldListener extends FocusAdapter {

        /*
        * Focus listener that fires the storeFieldAction when a FieldTextArea
        * loses focus.
        */
        @Override
        public void focusLost(FocusEvent e) {
            if (!e.isTemporary()) {
                storeFieldAction.actionPerformed(new ActionEvent(e.getSource(), 0, ""));
            }
        }

    }


    private final StoreFieldAction storeFieldAction = new StoreFieldAction();


    class StoreFieldAction extends AbstractAction {

        public StoreFieldAction() {
            super("Store field value");
            putValue(Action.SHORT_DESCRIPTION, "Store field value");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String toSet = null;
            boolean set;
            if (!ed.getText().isEmpty()) {
                toSet = ed.getText();
            }
            // We check if the field has changed, since we don't want to mark the
            // base as changed unless we have a real change.
            if (toSet == null) {
                set = base.getPreamble() != null;
            } else {
                set = !((base.getPreamble() != null)
                        && toSet.equals(base.getPreamble()));
            }

            if (set) {
                panel.undoManager.addEdit(new UndoablePreambleChange
                        (base, panel, base.getPreamble(), toSet));
                base.setPreamble(toSet);
                if ((toSet != null) && (!toSet.isEmpty())) {
                    ed.setLabelColor(GUIGlobals.entryEditorLabelColor);
                    ed.setValidBackgroundColor();
                } else {
                    ed.setLabelColor(GUIGlobals.nullFieldColor);
                    ed.setValidBackgroundColor();
                }
                if (ed.getTextComponent().hasFocus()) {
                    ed.setActiveBackgroundColor();
                }
                panel.markBaseChanged();
            }

        }
    }


    private final UndoAction undoAction = new UndoAction();


    class UndoAction extends AbstractAction {

        public UndoAction() {
            super("Undo", GUIGlobals.getImage("undo"));
            putValue(Action.SHORT_DESCRIPTION, "Undo");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                panel.runCommand("undo");
            } catch (Throwable ignored) {
            }
        }
    }


    private final RedoAction redoAction = new RedoAction();


    class RedoAction extends AbstractAction {

        public RedoAction() {
            super("Undo", GUIGlobals.getImage("redo"));
            putValue(Action.SHORT_DESCRIPTION, "Redo");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                panel.runCommand("redo");
            } catch (Throwable ignored) {
            }
        }
    }


    // The action concerned with closing the window.
    private final CloseAction closeAction = new CloseAction();


    class CloseAction extends AbstractAction {

        public CloseAction() {
            super(Globals.lang("Close window"));
            //, new ImageIcon(GUIGlobals.closeIconFile));
            //putValue(SHORT_DESCRIPTION, "Close window (Ctrl-Q)");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            storeFieldAction.actionPerformed(null);
            panel.preambleEditorClosing();
            dispose();
        }
    }


    public FieldEditor getFieldEditor() {
        return ed;
    }

    public void storeCurrentEdit() {
        storeFieldAction.actionPerformed(null);
    }

}
