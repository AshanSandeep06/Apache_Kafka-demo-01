package lk.epic.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.jpos.iso.packager.ISO87APackager;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebAppInitializer {
    public static void main(String[] args) {
        SpringApplication.run(WebAppInitializer.class);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public ISO87APackager packager(){
        return new ISO87APackager();
    }
}
