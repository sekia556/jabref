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
package net.sf.jabref.imports;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.sf.jabref.BibtexEntry;
import net.sf.jabref.Globals;
import net.sf.jabref.OutputPrinter;
import net.sf.jabref.util.XMPUtil;

/**
 * Wraps the XMPUtility function to be used as an ImportFormat.
 * 
 * @author $Author$
 * @version $Revision$ ($Date$)
 * 
 */
public class PdfXmpImporter extends ImportFormat {

    @Override
    public String getFormatName() {
        return Globals.lang("XMP-annotated PDF");
    }

    /**
     * Returns a list of all BibtexEntries found in the inputstream.
     */
    @Override
    public List<BibtexEntry> importEntries(InputStream in, OutputPrinter status) throws IOException {
        return XMPUtil.readXMP(in);
    }

    /**
     * Returns whether the given stream contains data that is a.) a pdf and b.)
     * contains at least one BibtexEntry.
     */
    @Override
    public boolean isRecognizedFormat(InputStream in) throws IOException {
        return XMPUtil.hasMetadata(in);
    }

    /**
     * String used to identify this import filter on the command line.
     * 
     * @return "xmp"
     */
    public String getCLIid() {
        return "xmp";
    }

}
