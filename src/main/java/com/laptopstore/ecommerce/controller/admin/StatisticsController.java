package com.laptopstore.ecommerce.controller.admin;

import com.laptopstore.ecommerce.exception.NotImplementException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/dashboard/statistics")
@Controller
public class StatisticsController {

    @GetMapping("")
    public String showStatisticsPage(Model model
    ){
        throw new NotImplementException();
    }
}
