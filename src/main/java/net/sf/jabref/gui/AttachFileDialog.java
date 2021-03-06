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
package net.sf.jabref.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import net.sf.jabref.*;
import net.sf.jabref.external.ExternalFilePanel;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Created by IntelliJ IDEA.
 * User: alver
 * Date: May 18, 2005
 * Time: 9:59:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class AttachFileDialog extends JDialog {

    private final AttachFileDialog ths = this;
    private final FieldEditor editor;
    private final String fieldName;
    private JPanel main;
    private final JButton browse = new JButton(Globals.lang("Browse"));
    private final JButton download = new JButton(Globals.lang("Download"));
    private final JButton auto = new JButton(Globals.lang("Auto"));
    private final JButton ok = new JButton(Globals.lang("Ok"));
    private final JButton cancel = new JButton(Globals.lang("Cancel"));
    private final BibtexEntry entry;
    private final MetaData metaData;
    private boolean cancelled = true; // Default to true, so a pure close operation implies Cancel.


    public AttachFileDialog(Frame parent, MetaData metaData, BibtexEntry entry, String fieldName) {
        super(parent, true);
        this.metaData = metaData;
        this.entry = entry;
        this.fieldName = fieldName;
        this.editor = new FieldTextField(fieldName, entry.getField(fieldName), false);

        initGui();
    }

    public AttachFileDialog(Dialog parent, MetaData metaData, BibtexEntry entry, String fieldName) {
        super(parent, true);
        this.metaData = metaData;
        this.entry = entry;
        this.fieldName = fieldName;
        this.editor = new FieldTextField(fieldName, entry.getField(fieldName), false);

        initGui();
    }

    public boolean cancelled() {
        return cancelled;
    }

    public String getValue() {
        return editor.getText();
    }

    private void initGui() {

        final ExternalFilePanel extPan = new ExternalFilePanel(fieldName, metaData, entry,
                editor, Util.getFileFilterForField(fieldName));

        browse.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                extPan.browseFile(fieldName, editor);
            }
        });

        download.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                extPan.downLoadFile(fieldName, editor, ths);
            }
        });

        auto.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                JabRefExecutorService.INSTANCE.execute(extPan.autoSetFile(fieldName, editor));
            }
        });

        ActionListener okListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                cancelled = false;
                dispose();
            }
        };

        ok.addActionListener(okListener);
        ((JTextField) editor.getTextComponent()).addActionListener(okListener);

        AbstractAction cancelListener = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent event) {
                cancelled = true;
                dispose();
            }
        };

        cancel.addActionListener(cancelListener);
        editor.getTextComponent().getInputMap().put(Globals.prefs.getKey("Close dialog"), "close");
        editor.getTextComponent().getActionMap().put("close", cancelListener);

        FormLayout layout = new FormLayout("fill:160dlu, 4dlu, fill:pref", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        //builder.append(Util.nCase(fieldName));//(editor.getLabel());
        builder.appendSeparator(Util.nCase(fieldName));
        builder.append(editor.getTextComponent());
        builder.append(browse);

        ButtonBarBuilder bb = new ButtonBarBuilder();
        bb.addButton(download);
        bb.addButton(auto);
        builder.nextLine();
        builder.append(bb.getPanel());
        builder.nextLine();
        builder.appendSeparator();

        main = builder.getPanel();

        main.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        bb = new ButtonBarBuilder();
        bb.addGlue();
        bb.addButton(ok);
        bb.addButton(cancel);
        bb.addGlue();

        getContentPane().add(main, BorderLayout.CENTER);
        getContentPane().add(bb.getPanel(), BorderLayout.SOUTH);
        pack();
    }
}
