package com.javatraining.basics.defaultandstaticinterfacemethods;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultAndStaticInterfaceMethodsTest {

    @Test
    void htmlFormatter_format_wrapsInSpan() {
        Formatter formatter = new HtmlFormatter();
        assertThat(formatter.format("hello")).isEqualTo("<span>hello</span>");
    }

    @Test
    void htmlFormatter_formatUpperCase_callsFormatThenUpperCase() {
        Formatter formatter = new HtmlFormatter();
        // format("hello") -> "<span>hello</span>", then toUpperCase() -> "<SPAN>HELLO</SPAN>"
        assertThat(formatter.formatUpperCase("hello")).isEqualTo("<SPAN>HELLO</SPAN>");
    }

    @Test
    void htmlFormatter_formatWithBrackets_wrapsFormatResult() {
        Formatter formatter = new HtmlFormatter();
        assertThat(formatter.formatWithBrackets("hello")).isEqualTo("[<span>hello</span>]");
    }

    @Test
    void markdownFormatter_format_wrapsInBold() {
        Formatter formatter = new MarkdownFormatter();
        assertThat(formatter.format("hello")).isEqualTo("**hello**");
    }

    @Test
    void markdownFormatter_formatWithBrackets_wrapsFormatResult() {
        Formatter formatter = new MarkdownFormatter();
        assertThat(formatter.formatWithBrackets("hello")).isEqualTo("[**hello**]");
    }

    @Test
    void markdownFormatter_formatUpperCase_uppercasesFormattedOutput() {
        Formatter formatter = new MarkdownFormatter();
        // format("hello") -> "**hello**", then toUpperCase -> "**HELLO**"
        assertThat(formatter.formatUpperCase("hello")).isEqualTo("**HELLO**");
    }

    @Test
    void staticFactory_trimming_trimsWhitespace() {
        Formatter trimmer = Formatter.trimming();
        assertThat(trimmer.format("  hi  ")).isEqualTo("hi");
    }

    @Test
    void staticFactory_trimming_handleNoWhitespace() {
        Formatter trimmer = Formatter.trimming();
        assertThat(trimmer.format("hello")).isEqualTo("hello");
    }

    @Test
    void defaultMethod_canBeUsedPolymorphically() {
        Formatter html = new HtmlFormatter();
        Formatter md = new MarkdownFormatter();

        // Both inherit formatWithBrackets; it delegates to their own format()
        assertThat(html.formatWithBrackets("x")).startsWith("[");
        assertThat(md.formatWithBrackets("x")).startsWith("[");
    }
}
