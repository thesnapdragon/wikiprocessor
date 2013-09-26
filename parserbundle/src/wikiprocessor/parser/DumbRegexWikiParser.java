package wikiprocessor.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class provides a parser for turning wikitext 2 html.
 *
 * @author Mihály Héder at MTA SZTAKI
 */
public class DumbRegexWikiParser implements WikiParser {

    /**
     * This function parses wikitext to html.
     *
     *
     * @param wikiText The wikitext to parse
     * @return html based on wikitext
     */
    public static String wikitext2html(String wikiText) throws Exception {
        String ret = wikiText;


        // TODO: ref display, template display, random tests, italic para
        // Articles: Altair italic para
        // Lynx: table para

        //================================================
        // This file tries to parse the
        // wikitexts to the help description:
        // http://en.wikipedia.org/wiki/Help:Wiki_markup
        // BE CAREFUL when changing this file: the
        // order of the replaces is significant
        //================================================

        //================================================
        //Replace table, template and link patterns to
        //special characters, to make all regexes simpler
        //================================================

        // replace [[ and ]] sequences to unicode
        // 228f and 2290
        ret = ret.replaceAll("\\[\\[", "\u228f");
        ret = ret.replaceAll("\\]\\]", "\u2290");

        // replace {{ and }} to 22d0 and 22d1
        // ret = ret.replaceAll("\\{\\{", "\u22d0");
        // ret = ret.replaceAll("\\}\\}", "\u22d1");

        // replace table start {| and table end |}
        // to 2291 and 2292
        // only at the beginning of a line
        // ret = ret.replaceAll("(?m)\n[{][|]", "\n\u2291");
        // ret = ret.replaceAll("(?m)\n[|][}]", "\n\u2292");


        // document start
        ret = "<div>\n" + ret;

        //================================================
        //headings h1-h6
        //================================================
        ret = ret.replaceAll("(?m)\n======(.+)======\\s*\n", "\n<h6>$1</h6>\n");
        ret = ret.replaceAll("(?m)\n=====(.+)=====\\s*\n", "\n<h5>$1</h5>\n");
        ret = ret.replaceAll("(?m)\n====(.+)====\\s*\n", "\n<h4>$1</h4>\n");
        ret = ret.replaceAll("(?m)\n===(.+)===\\s*\n", "\n<h3>$1</h3>\n");
        ret = ret.replaceAll("(?m)\n==(.+)==\\s*\n", "\n<h2>$1</h2>\n");
        ret = ret.replaceAll("(?m)\n=(.+)=\\s*\n", "\n<h1>$1</h1>\n");

        //================================================
        //hr
        //================================================
        ret = ret.replaceAll("(?m)\n----\\s\n*", "\n<hr/>\n");


        //================================================
        //left indent
        //TODO
        //================================================

        //================================================
        //blockquote
        //TODO
        //================================================

        //================================================
        //center text
        //TODO
        //================================================

        //================================================
        //lists: http://en.wikipedia.org/wiki/Help:List
        //TODO definition lists are not supported yet
        //================================================

        // previous item
        ret = ret.replaceAll("(?m)\n[*#]{1,4}:(.+)\n", "<br/>$1\n");

        // unordered list levels 1-4
        ret = ret.replaceAll("(?m)\n(([*].*\n)+)", "\n<ul>\n$1</ul>\n");
        ret = ret.replaceAll("(?m)\n(([*][#*].*\n)+)", "\n<ul>\n$1</ul>\n");
        ret = ret.replaceAll("(?m)\n(([*][#*]{2}.*\n)+)", "\n<ul>\n$1</ul>\n");
        ret = ret.replaceAll("(?m)\n(([*][#*]{3}.*\n)+)", "\n<ul>\n$1</ul>\n");

        // ordered list levels 1-4
        ret = ret.replaceAll("(?m)\n(([#].*\n)+)", "\n<ol>\n$1</ol>\n");
        ret = ret.replaceAll("(?m)\n(([#][#*].*\n)+)", "\n<ol>\n$1</ol>\n");
        ret = ret.replaceAll("(?m)\n(([#][#*]{2}.*\n)+)", "\n<ol>\n$1</ol>\n");
        ret = ret.replaceAll("(?m)\n(([#][#*]{3}.*\n)+)", "\n<ol>\n$1</ol>\n");

        // inserting list items
        ret = ret.replaceAll("(?m)\n[#*][#*]{0,3}(.*)\n", "\n<li>$1</li>\n");
        ret = ret.replaceAll("(?m)\n[#*][#*]{0,3}(.*)\n", "\n<li>$1</li>\n");


        //================================================
        //poem
        //TODO or we just leave it
        //================================================

        //================================================
        //text formatting
        //================================================
        ret = ret.replaceAll("'''''([^\n]{1,100}?)'''''", "<i><b>$1</b></i>");
        ret = ret.replaceAll("'''([^\n]{1,100}?)'''", "<b>$1</b>");
        ret = ret.replaceAll("''([^\n]{1,100}?)''", "<i>$1</i>");

        //================================================
        //links and urls, external links
        //================================================

        //================================================
        //images, links and urls, external links
        //================================================
        for (int i = 0; i < 5; i++) {
            //templates with parameters
            Pattern p = Pattern.compile("\u228f(?:(?:[Ff]ile)|(?:[Ii]mage)):([^]|\\x5b\u228f\u2290]*)([^]\\x5b\u228f\u2290]*)\u2290");
            Matcher m = p.matcher(ret);
            StringBuffer s = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(s, "<span class=\"wiki-image\">" + m.group(1)
                        + "<div class=\"wiki-image-params\">"
                        + m.group(2).replaceAll("[|]([^|]*)", "<span class=\"wiki-image-param\">$1</span>")
                        + "</div></span>");
            }
            m.appendTail(s);
            ret = s.toString();
        }

        //max nesting: 5
        for (int i = 0; i < 5; i++) {
//            ret = ret.replaceAll("\u228f(?:(?:[Ff]ile)|(?:[Ii]mage)):([^]|\\x5b\u228f\u2290]*)[|]([^]\\x5b\u228f\u2290]*)\u2290",
//                    "<span class=\"wiki-image\">$1<div class=\"wiki-image-params\">$2</div></span>");

            //renamed link
            ret = ret.replaceAll("\u228f([^|\u22f8\u2290]+)[|]([^\u228f\u2290]+)\u2290", "<a href=\"$1\">$2</a>");

            //auto renamed link (we do not do the renaming)
            ret = ret.replaceAll("\u228f([^|\u228f\u2290]*)[|]\u2290", "<a href=\"$1\">$1</a>");

            //Simple link
            ret = ret.replaceAll("\u228f([^\u228f\u2290|]*)\u2290", "<a href=\"$1\">$1</a>");


        }

        //named external link
        ret = ret.replaceAll("\\[(http[^\\[\\]\\s]+)\\s+([^\\[\\]]+)\\]", "<a href=\"$1\">$2</a>");

        //unnamed link
        ret = ret.replaceAll("\\[(http[^]\\s]+)\\]", "<a href=\"$1\">$1</a>");

        //bare link 
        //(we leave this) ret = ret.replaceAll("[^\"'](http[s]?://[^\\s]+)[^\"']", "<a href=\"$1\">$1</a>");

        //fix underscores in hrefs, max 5
        for (int i = 0; i < 5; i++) {
            ret = ret.replaceAll("href=\"([^\"\\s]+)[ ]([^\"]+)\"", "href=\"$1_$2\"");
        }


        //================================================
        //Categories
        //TODO We might not want to do anything
        //and keep them as links
        //================================================

        //================================================
        //tables
        //TODO:NESTED TABLES might bogus (not in td)
        //================================================

        //templates with parameters
        Pattern pt = Pattern.compile("(?m)(\n[{][|][^\n]*(\n[!{|][^\n]*)+\n[|][}])");
        Matcher mt = pt.matcher(ret);
        StringBuffer st = new StringBuffer();
        while (mt.find()) {
            String tp = mt.group(1);
            //table head and foot
            tp = tp.replaceAll("(?m)\n[{][|]([^\n<>]*)", "\n<table $1>");
            tp = tp.replaceAll("(?m)\n[|][}]\\s*", "\n</table>\n");

            //table caption
            tp = tp.replaceAll("(?m)\n[|][+](.*)", "\n<caption>$1</caption>");

            //table rows
            tp = tp.replaceAll("(?m)\n[|]-(.*)\n(([|!][^-].*\n)+)", "\n<tr $1>\n$2</tr>\n");
            tp = tp.replaceAll("(?m)\n[|]-(.*)\n(([|!][^-].*\n)+)", "\n<tr $1>\n$2</tr>\n");

            //table cells
            //one cell in a line with attribs
            tp = tp.replaceAll("(?m)\n[|]([^-|\n]*)[|]([^|\n]*)\n", "\n<td $1>$2</td>\n");
            tp = tp.replaceAll("(?m)\n[|]([^-|\n]*)[|]([^|\n]*)\n", "\n<td $1>$2</td>\n");

            //break up multi cells in one line (max 12 cols, no recursion)
            for (int i = 0; i < 12; i++) {
                tp = tp.replaceAll("(?m)\n[|]([^-][^|\n]+)[|][|]([^\n]+)", "\n|$1\n|$2");
            }

            //one cell in a line with no attribs
            tp = tp.replaceAll("(?m)\n[|]([^-][^|\n]*)\n", "\n<td>$1</td>\n");

            //table headers
            //one header in a line with attribs
            tp = tp.replaceAll("(?m)\n!([^-][^|\n]*)[|]([^|\n]*)\n", "\n<th $1>$2</th>\n");
            tp = tp.replaceAll("(?m)\n!([^-][^|\n]*)[|]([^|\n]*)\n", "\n<th $1>$2</th>\n");

            //break up multi cells in one line (max 12 cols, no recursion)
            for (int i = 0; i < 12; i++) {
                tp = tp.replaceAll("(?m)\n!([^-][^!|\n]+)!!([^\n]+)", "\n!$1\n!$2");
            }

            //one cell in a line with no attribs
            tp = tp.replaceAll("(?m)\n!([^-][^|!\n]*)\n", "\n<th>$1</th>\n");
            tp = tp.replaceAll("(?m)\n!([^-][^|!\n]*)\n", "\n<th>$1</th>\n");

            //PASS2
            tp = tp.replaceAll("(?m)\n[{][|]([^\n<>]*)", "\n<table $1>\n");
            tp = tp.replaceAll("(?m)\n[|][}]\\s*", "\n</table>\n");

            //table caption
            tp = tp.replaceAll("(?m)\n[|][+](.*)", "\n<caption>$1</caption>");
            tp = tp.replaceAll("(?m)\n[|][+](.*)", "\n<caption>$1</caption>");

            //table rows
            tp = tp.replaceAll("(?m)\n[|]-(.*)\n(([|!][^-].*\n)+)", "\n<tr $1>\n$2</tr>\n");
            tp = tp.replaceAll("(?m)\n[|]-(.*)\n(([|!][^-].*\n)+)", "\n<tr $1>\n$2</tr>\n");

            //table cells
            //one cell in a line with attribs
            tp = tp.replaceAll("(?m)\n[|]([^-][^|\n]*)[|]([^|\n]*)\n", "\n<td $1>$2</td>\n");
            tp = tp.replaceAll("(?m)\n[|]([^-][^|\n]*)[|]([^|\n]*)\n", "\n<td $1>$2</td>\n");


            //one cell in a line with no attribs
            tp = tp.replaceAll("(?m)\n[|][^-]([^|\n]*)\n", "\n<td>$1</td>\n");
            tp = tp.replaceAll("(?m)\n[|][^-]([^|\n]*)\n", "\n<td>$1</td>\n");

            //table headers
            //one header in a line with attribs
            tp = tp.replaceAll("(?m)\n!([^-][^|\n]*)[|]([^|\n]*)\n", "\n<th $1>$2</th>\n");
            tp = tp.replaceAll("(?m)\n!([^-][^|\n]*)[|]([^|\n]*)\n", "\n<th $1>$2</th>\n");


            mt.appendReplacement(st, tp);
        }
        mt.appendTail(st);
        ret = st.toString();

        /*
        //table head and foot
        ret = ret.replaceAll("(?m)\n[{][|](.*)", "\n<table $1>");
        ret = ret.replaceAll("(?m)\n[|][}]\\s*", "\n</table>");

        //table caption
        ret = ret.replaceAll("(?m)\n[|][+](.*)", "\n<caption>$1</caption>");

        //table rows
        ret = ret.replaceAll("(?m)\n[|]-(.*)\n(([|!][^-].*\n)+)", "\n<tr $1>\n$2</tr>\n");
        ret = ret.replaceAll("(?m)\n[|]-(.*)\n(([|!][^-].*\n)+)", "\n<tr $1>\n$2</tr>\n");

        //table cells
        //one cell in a line with attribs
        ret = ret.replaceAll("(?m)\n[|]([^-|\n]*)[|]([^|\n]*)\n", "\n<td $1>$2</td>\n");
        ret = ret.replaceAll("(?m)\n[|]([^-|\n]*)[|]([^|\n]*)\n", "\n<td $1>$2</td>\n");

        //break up multi cells in one line (max 12 cols, no recursion)
        for (int i = 0; i < 12; i++) {
        ret = ret.replaceAll("(?m)\n[|]([^-|\n]+)[|][|]([^\n]+)", "\n|$1\n|$2");
        }

        //one cell in a line with no attribs
        ret = ret.replaceAll("(?m)\n[|]([^-|\n]*)\n", "\n<td>$1</td>\n");
        ret = ret.replaceAll("(?m)\n[|]([^-|\n]*)\n", "\n<td>$1</td>\n");

        //table headers
        //one header in a line with attribs
        ret = ret.replaceAll("(?m)\n!([^-|\n]*)[|]([^|\n]*)\n", "\n<th $1>$2</th>\n");
        ret = ret.replaceAll("(?m)\n!([^-|\n]*)[|]([^|\n]*)\n", "\n<th $1>$2</th>\n");

        //break up multi cells in one line (max 12 cols, no recursion)
        for (int i = 0; i < 12; i++) {
        ret = ret.replaceAll("(?m)\n!([^-!|\n]+)!!([^\n]+)", "\n!$1\n!$2");
        }

        //one cell in a line with no attribs
        ret = ret.replaceAll("(?m)\n!([^-|!\n]*)\n", "\n<th>$1</th>\n");
        ret = ret.replaceAll("(?m)\n!([^-|!\n]*)\n", "\n<th>$1</th>\n");

         */

        //================================================
        //references
        //TODO or maybe we leave that way
        //================================================

        //reference with name
        ret = ret.replaceAll("(?m)<ref\\s*([^>]*)>(.+?)</ref>",
                "<span class=\"wiki-ref-named\" $1><div class=\"wiki-ref-details\">$2</div></span>");

        //closed reference
        ret = ret.replaceAll("(?m)<ref\\s*([^>]*)/>",
                "<span class=\"wiki-ref-ref\" $1></span>");

        //reference without name
        ret = ret.replaceAll("(?m)<ref\\s*>(.+?)</ref>",
                "<span class=\"wiki-ref-simple\"><div class=\"wiki-ref-details\">$1</div></span>");


        //================================================
        //templates & infoboxes
        //================================================

        //max nesting: 5 levels
        for (int i = 0; i < 5; i++) {
            //templates with parameters
            Pattern p = Pattern.compile("(?m)\\{\\{([^|{}]+)([|][^{}]+)\\}\\}");
            Matcher m = p.matcher(ret);
            StringBuffer s = new StringBuffer();
            while (m.find()) {
                try {
                    m.appendReplacement(s, "<div class=\"wiki-template\""
                            + " onclick=\"edit_wiki_template(this)\" onmouseover=\"show_wiki_template(this)\">" 
                            + "<span class=\"wiki-template-title\">"
                            + m.group(1)
                            + "</span>"
                            + "<div class=\"wiki-template-params\">"
                            + m.group(2)
                            .replaceAll("[|]([^|]*)", "<div class=\"wiki-template-param\">$1</div>")
                            .replaceAll("([^\"<>=]+)=([^\"<>]+)",
                                "<span class=\"wiki-template-param-key\">$1</span>"
                                + "<span class=\"wiki-template-param-value\">$2</span>")
                            .replaceAll("<div class=\"wiki-template-param\">([^<>\"]*)</div>", 
                                "<div class=\"wiki-template-param\"><span class=\"wiki-template-param-value\">"
                                + "$1</span></div>")
                            + "</div></div>");
                } catch (Exception e) {
                }
            }
            m.appendTail(s);
            ret = s.toString();

            //templates with no parameters
            ret = ret.replaceAll("(?m)\\{\\{([^|{}]+)\\}\\}", "<span class=\"wiki-template-noparam\">$1</span>");
        }

        //================================================
        //new lines
        //================================================
        ret = ret.replaceAll("(?m)\n\n", "<br/>\n");

        //================================================
        // document end
        //================================================
        ret = ret + "\n</div>";

        return ret;
    }

    /**
     * @author Milán Unicsovics, u.milan at gmail dot com, MTA SZTAKI
     * implements WikiParser interface, catches all error
     */
	@Override
	public String parse(String wikiText) {
		String parsed = null;
		try {
			parsed = wikitext2html(wikiText);
		} catch (Exception e) {
			ParserActivator.logger.warn("DumbRegexWikiParser error!");
			ParserActivator.statistics.increaseWarningLogCount();
		}
		return parsed;
	}
}
