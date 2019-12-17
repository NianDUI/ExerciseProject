package top.niandui;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

//    @Bean
    public Converter converter1() {
        return new Converter<String, Date>() {
            @SneakyThrows
            @Override
            public Date convert(String source) {
                return sdf.parse(source);
            }
        };
    }

//    @Bean
    public Converter converter2() {
        return new Converter<Date, String>() {
            @SneakyThrows
            @Override
            public String convert(Date date) {
                return sdf.format(date);
            }
        };
    }

//    @Bean
    public HttpMessageConverter<Date> myHttpMessageConverter() {
        return new HttpMessageConverter<Date>() {

            @Override
            public boolean canRead(Class<?> clazz, MediaType mediaType) {
                return false;
            }

            @Override
            public boolean canWrite(Class<?> clazz, MediaType mediaType) {
                return false;
            }

            @Override
            public List<MediaType> getSupportedMediaTypes() {
                return null;
            }

            @Override
            public Date read(Class<? extends Date> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
                return null;
            }

            @Override
            public void write(Date date, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

            }
        };
    }


    public ObjectMapper myObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.
        return objectMapper;
    }

}
