package com.javatraining.basics.defaultandstaticinterfacemethods;

/**
 * An HTML formatter that wraps input in a <span> tag.
 * Inherits the default methods formatUpperCase and formatWithBrackets.
 */
public class HtmlFormatter implements Formatter {

    @Override
    public String format(String input) {
        return "<span>" + input + "</span>";
    }
}
