package com.javatraining.basics.defaultandstaticinterfacemethods;

/**
 * A Markdown formatter that wraps input in bold (**) markers. Inherits the default methods
 * formatUpperCase and formatWithBrackets.
 */
public class MarkdownFormatter implements Formatter {

  @Override
  public String format(String input) {
    return "**" + input + "**";
  }
}
