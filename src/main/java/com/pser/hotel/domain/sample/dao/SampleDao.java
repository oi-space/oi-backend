package com.pser.hotel.domain.sample.dao;

import com.pser.hotel.domain.sample.domain.Sample;
import com.pser.hotel.domain.sample.dto.SampleSearchRequest;
import com.pser.hotel.global.common.request.SearchQuery;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface SampleDao extends JpaRepository<Sample, Long>, SampleDaoCustom {
    @Override
    @NonNull
    Page<Sample> findAll(@NonNull Pageable pageable);

    @Override
    Page<Sample> search(SampleSearchRequest dto, SearchQuery searchQuery, @NonNull Pageable pageable);

    @Override
    @NonNull
    Optional<Sample> findById(@NonNull Long id);
}
