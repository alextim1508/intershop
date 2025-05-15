package com.alextim.intershop.mapper;

import com.alextim.intershop.dto.ActionDto;
import com.alextim.intershop.utils.Action;
import org.springframework.stereotype.Component;

@Component
public class ActionMapper {

    public Action to(ActionDto actionDto) {
        return Action.valueOf(actionDto.action.toUpperCase());
    }
}
