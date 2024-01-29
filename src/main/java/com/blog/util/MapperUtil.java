package com.blog.util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.stream.Collectors;

public class MapperUtil {
    public static <D> D map (Object source, Class<D> destinationType) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(source, destinationType);
    }

    public static <D,T> List<D> mapAll (List<T> sourceList, Class<D> destinationType) {
        ModelMapper modelMapper = new ModelMapper();
        return sourceList.stream().map( source ->  modelMapper.map(source, destinationType)).collect(Collectors.toList()) ;
    }

    public static <D> D mapStringToObject(String jsonString, Class<D> destinationType) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, destinationType);
    }
}
