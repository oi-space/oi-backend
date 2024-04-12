package com.psear.hotel.domain.sample.dao;

import com.psear.hotel.domain.sample.domain.Sample;
import com.psear.hotel.domain.sample.dto.SampleSearchRequest;
import com.psear.hotel.global.common.request.SearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SampleDaoCustom {
    Page<Sample> search(SampleSearchRequest request, SearchQuery searchQuery, Pageable pageable);
}
