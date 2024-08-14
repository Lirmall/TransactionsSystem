package ru.klokov.tstransactions.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.WritingConverter;
import ru.klokov.tstransactions.entities.enums.TransactionStatus;
import ru.klokov.tstransactions.entities.enums.TransactionType;
import ru.klokov.tstransactions.services.utils.TransactionStatusConverter;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }

//    @Bean
//    public ModelMapper modelMapper() {
//        Converter<TransactionStatus, String> statusToString = new Converter<>() {
//            @Override
//            public String convert(MappingContext<TransactionStatus, String> context) {
//                return context.getSource().getName();
//            }
//        };
//
//        Converter<String, TransactionStatus> stringToStatus = new Converter<>() {
//            @Override
//            public TransactionStatus convert(MappingContext<String, TransactionStatus> context) {
//                return TransactionStatus.getByName(context.getSource());
//            }
//        };
//        Converter<TransactionType, String> typeToString = new Converter<>() {
//            @Override
//            public String convert(MappingContext<TransactionType, String> context) {
//                return context.getSource().getName();
//            }
//        };
//
//        Converter<String, TransactionType> stringToType = new Converter<>() {
//            @Override
//            public TransactionType convert(MappingContext<String, TransactionType> context) {
//                return TransactionType.getByName(context.getSource());
//            }
//        };
//
//        ModelMapper mapper = new ModelMapper();
//
//        mapper.getConfiguration()
//                .setMatchingStrategy(MatchingStrategies.STRICT);
//
//        mapper.addConverter(statusToString);
//        mapper.addConverter(stringToStatus);
//        mapper.addConverter(stringToType);
//        mapper.addConverter(typeToString);
//        return mapper;
//    }
}
