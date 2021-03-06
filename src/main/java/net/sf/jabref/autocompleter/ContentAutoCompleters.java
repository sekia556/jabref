package net.sf.jabref.autocompleter;

import net.sf.jabref.BibtexDatabase;
import net.sf.jabref.Globals;
import net.sf.jabref.MetaData;

import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class ContentAutoCompleters extends AutoCompleters {

    public ContentAutoCompleters(BibtexDatabase database, MetaData metaData) {
        String[] completeFields = Globals.prefs.getStringArray("autoCompleteFields");
        for (String field : completeFields) {
            AutoCompleter autoCompleter = AutoCompleterFactory.getFor(field);
            put(field, autoCompleter);
        }

        addDatabase(database);

        addJournalListToAutoCompleter();
        addContentSelectorValuesToAutoCompleters(metaData);
    }

    /**
     * For all fields with both autocompletion and content selector, add content selector
     * values to the autocompleter list:
     */
    public void addContentSelectorValuesToAutoCompleters(MetaData metaData) {
        for (Map.Entry<String, AutoCompleter> entry : this.autoCompleters.entrySet()) {
            AutoCompleter ac = entry.getValue();
            if (metaData.getData(Globals.SELECTOR_META_PREFIX + entry.getKey()) != null) {
                Vector<String> items = metaData.getData(Globals.SELECTOR_META_PREFIX + entry.getKey());
                if (items != null) {
                    for (String item : items) {
                        ac.addWordToIndex(item);
                    }
                }
            }
        }
    }

    /**
     * If an autocompleter exists for the "journal" field, add all
     * journal names in the journal abbreviation list to this autocompleter.
     */
    public void addJournalListToAutoCompleter() {
        AutoCompleter autoCompleter = get("journal");
        if(autoCompleter != null) {
            Set<String> journals = Globals.journalAbbrev.getJournals().keySet();
            for (String journal : journals) {
                autoCompleter.addWordToIndex(journal);
            }
        }
    }

}
