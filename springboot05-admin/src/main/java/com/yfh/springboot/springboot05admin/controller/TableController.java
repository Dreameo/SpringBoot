package com.yfh.springboot.springboot05admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TableController {

    /**
     * basic_table页面
     * @return
     */
    @GetMapping("/basic_table")
    public String toBasicTable() {
        return "table/basic_table";
    }

    @GetMapping("/dynamic_table")
    public String toDynamicTable(Model model) {

        return "table/dynamic_table";
    }

    @GetMapping("/editable_table")
    public String toDditableTable() {
        return "table/editable_table";
    }

    @GetMapping("/pricing_table")
    public String toPricingTable() {
        return "table/pricing_table";
    }

    @GetMapping("/responsive_table")
    public String toResponsiveTable() {
        return "table/responsive_table";
    }
}
