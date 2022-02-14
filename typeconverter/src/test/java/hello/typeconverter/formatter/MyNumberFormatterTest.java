package hello.typeconverter.formatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Locale;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MyNumberFormatterTest {

    MyNumberFormatter myNumberFormatter = new MyNumberFormatter();

    @Test
    void parse() throws ParseException {
        Number result = myNumberFormatter.parse("10,000", Locale.KOREA);
        assertThat(result).isEqualTo(10000L); // Long 타입 주의할 것.
    }

    @Test
    void print() {
        String result = myNumberFormatter.print(10000, Locale.KOREA);
        assertThat(result).isEqualTo("10,000");

    }

}