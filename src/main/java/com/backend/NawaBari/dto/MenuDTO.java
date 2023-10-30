package com.backend.NawaBari.dto;

import com.backend.NawaBari.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MenuDTO {
    private String name;
    private String price;

    public static MenuDTO convertToDTO(Menu menu) {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setName(menu.getName());
        menuDTO.setPrice(menu.getPrice());
        return menuDTO;
    }
}
