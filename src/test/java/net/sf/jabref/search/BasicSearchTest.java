package net.sf.jabref.search;

import net.sf.jabref.*;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test case for BasicSearch.
 */
public class BasicSearchTest {

    @Test
    public void testBasicSearchParsing() {
        Globals.prefs = JabRefPreferences.getInstance();

        BibtexEntry be = makeBibtexEntry();
        BasicSearch bsCaseSensitive = new BasicSearch(true, false);
        BasicSearch bsCaseInsensitive = new BasicSearch(false, false);
        BasicSearch bsCaseSensitiveRegexp = new BasicSearch(true, true);
        BasicSearch bsCaseInsensitiveRegexp = new BasicSearch(false, true);

        String query = "marine 2001 shields";

        Assert.assertEquals(0, bsCaseSensitive.applyRule(query, be));
        Assert.assertEquals(1, bsCaseInsensitive.applyRule(query, be));
        Assert.assertEquals(0, bsCaseSensitiveRegexp.applyRule(query, be));
        Assert.assertEquals(1, bsCaseInsensitiveRegexp.applyRule(query, be));

        query = "\"marine larviculture\"";

        Assert.assertEquals(0, bsCaseSensitive.applyRule(query, be));
        Assert.assertEquals(0, bsCaseInsensitive.applyRule(query, be));
        Assert.assertEquals(0, bsCaseSensitiveRegexp.applyRule(query, be));
        Assert.assertEquals(0, bsCaseInsensitiveRegexp.applyRule(query, be));

        query = "\"marine [A-Za-z]* larviculture\"";

        Assert.assertEquals(0, bsCaseSensitive.applyRule(query, be));
        Assert.assertEquals(0, bsCaseInsensitive.applyRule(query, be));
        Assert.assertEquals(0, bsCaseSensitiveRegexp.applyRule(query, be));
        Assert.assertEquals(1, bsCaseInsensitiveRegexp.applyRule(query, be));

    }

    public BibtexEntry makeBibtexEntry() {
        BibtexEntry e = new BibtexEntry(IdGenerator.next(), BibtexEntryType.INCOLLECTION);
        e.setField("title", "Marine finfish larviculture in Europe");
        e.setField("bibtexkey", "shields01");
        e.setField("year", "2001");
        e
                .setField(
                        "author",
                        "Kevin Shields");
        return e;
    }
}
