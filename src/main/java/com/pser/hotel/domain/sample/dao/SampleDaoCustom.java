package com.pser.hotel.domain.sample.dao;

import com.pser.hotel.domain.sample.domain.Sample;
import com.pser.hotel.domain.sample.dto.SampleSearchRequest;
import com.pser.hotel.global.common.request.SearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SampleDaoCustom {
    Page<Sample> search(SampleSearchRequest request, SearchQuery searchQuery, Pageable pageable);
}
