package hello.typeconverter.formatter;

import hello.typeconverter.IpPort;
import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

import static org.assertj.core.api.Assertions.*;

public class FormattingConversionServiceTest {

    @Test
    void formattingConversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();

        //컨버터 등록
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        //포메터 등록
        conversionService.addFormatter(new MyNumberFormatter());

        //컨버터 사용
        IpPort result = conversionService.convert("127.0.0.1:8080", IpPort.class);
        assertThat(result).isEqualTo(new IpPort("127.0.0.1", 8080));

        //포메터 사용
        String result2 = conversionService.convert(10000, String.class);
        assertThat(result2).isEqualTo("10,000");
        Long result3 = conversionService.convert("10,000", Long.class);
        assertThat(result3).isEqualTo(10000L);
    }
}
