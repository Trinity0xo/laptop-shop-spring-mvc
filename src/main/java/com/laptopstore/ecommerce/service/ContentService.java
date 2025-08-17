package com.laptopstore.ecommerce.service;

import com.laptopstore.ecommerce.dto.DashboardContentDto;
import com.laptopstore.ecommerce.dto.HomeContentDto;

public interface ContentService {
    HomeContentDto getHomePageContent();
    DashboardContentDto getDashboardContent();
}
