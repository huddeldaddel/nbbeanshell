/***********************************************************************************************************************
 *                                                                                                                     *
 *  This file is part of the BeanShell Java Scripting distribution.                                                    *
 *  Documentation and updates may be found at http://www.beanshell.org/                                                *
 *                                                                                                                     *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General  *
 *  Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)  *
 *  any later version.                                                                                                 *
 *                                                                                                                     *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for    *
 *  more details.                                                                                                      *
 *                                                                                                                     *
 *  You should have received a copy of the GNU Lesser General Public License along with this program.                  ***
 *  If not, see <http://www.gnu.org/licenses/>.                                                                        *
 *                                                                                                                     *
 *  Patrick Niemeyer (pat@pat.net)                                                                                     *
 *  Author of Learning Java, O'Reilly & Associates                                                                     *
 *  http://www.pat.net/~pat/                                                                                           *
 *                                                                                                                     *
 **********************************************************************************************************************/
package bsh.util;

import bsh.NameSource;
import bsh.StringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * NameCompletionTable is a utility that implements simple name completion for a collection of names, NameSources, and
 * other NameCompletionTables. This implementation uses a trivial linear search and comparison...
 */
public class NameCompletionTable extends ArrayList
        implements NameCompletion {

    /**
     * Unimplemented - need a collection here
     */
    NameCompletionTable table;
    List sources;

    /**
     * Unimplemented - need a collection of sources here
     */
    /**
     */
    public NameCompletionTable() {
    }

    /**
     * Add a NameCompletionTable, which is more optimized than the more general NameSource
     */
    public void add(NameCompletionTable table) {
        /**
         * Unimplemented - need a collection here
         */
        if (this.table != null) {
            throw new RuntimeException("Unimplemented usage error");
        }

        this.table = table;
    }

    /**
     * Add a NameSource which is monitored for names. Unimplemented - behavior is broken... no updates
     */
    public void add(NameSource source) {
        /*
         * Unimplemented - Need to add an inner class util here that holds the source and monitors it by registering a
         * listener
         */
        if (sources == null) {
            sources = new ArrayList();
        }

        sources.add(source);
    }

    /**
     * Add any matching names to list (including any from other tables)
     */
    protected void getMatchingNames(String part, List found) {
        // check our table
        for (int i = 0; i < size(); i++) {
            String name = (String) get(i);
            if (name.startsWith(part)) {
                found.add(name);
            }
        }

        // Check other tables.
        /**
         * Unimplemented - need a collection here
         */
        if (table != null) {
            table.getMatchingNames(part, found);
        }

        // Check other sources
        // note should add caching in source adapters
        if (sources != null) {
            for (int i = 0; i < sources.size(); i++) {
                NameSource src = (NameSource) sources.get(i);
                String[] names = src.getAllNames();
                for (int j = 0; j < names.length; j++) {
                    if (names[j].startsWith(part)) {
                        found.add(names[j]);
                    }
                }

            }
        }
    }

    public String[] completeName(String part) {
        List found = new ArrayList();
        getMatchingNames(part, found);

        if (found.size() == 0) {
            return new String[0];
        }

        // Find the max common prefix
        String maxCommon = (String) found.get(0);
        for (int i = 1; i < found.size() && maxCommon.length() > 0; i++) {
            maxCommon = StringUtil.maxCommonPrefix(
                    maxCommon, (String) found.get(i));

            // if maxCommon gets as small as part, stop trying
            if (maxCommon.equals(part)) {
                break;
            }
        }

        // Return max common or all ambiguous
        if (maxCommon.length() > part.length()) {
            return new String[]{maxCommon};
        } else {
            return (String[]) (found.toArray(new String[0]));
        }
    }
    /**
     * class SourceCache implements NameSource.Listener { NameSource src; SourceMonitor( NameSource src ) { this.src =
     * src; } public void nameSourceChanged( NameSource src ) { } }
     */
}
