package newusefy.com.internship;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

    @RestController
    public class HelloWorld {

        @GetMapping("/hello") // --- Это роут то есть /hello откроет текст "Hello world"
        public String hello() {
            return "Hello World";
        }
    }
