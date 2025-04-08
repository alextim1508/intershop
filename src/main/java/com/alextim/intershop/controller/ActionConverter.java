package com.alextim.intershop.controller;

import com.alextim.intershop.utils.Action;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ActionConverter implements Converter<String, Action> {
    @Override
    public Action convert(String source) {
        return Action.valueOf(source.toUpperCase());
    }
}