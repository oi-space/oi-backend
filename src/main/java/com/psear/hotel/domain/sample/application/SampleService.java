package com.psear.hotel.domain.sample.application;

import com.psear.hotel.domain.sample.dao.SampleDao;
import com.psear.hotel.domain.sample.domain.Sample;
import com.psear.hotel.domain.sample.dto.SampleCreateRequest;
import com.psear.hotel.domain.sample.dto.SampleMapper;
import com.psear.hotel.domain.sample.dto.SampleResponse;
import com.psear.hotel.domain.sample.dto.SampleSearchRequest;
import com.psear.hotel.domain.sample.dto.SampleUpdateRequest;
import com.psear.hotel.global.common.request.SearchQuery;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SampleService {
    private final SampleDao sampleDao;
    private final SampleMapper sampleMapper;

    public Page<SampleResponse> getAll(Pageable pageable) {
        return sampleDao.findAll(pageable).map(sampleMapper::toResponse);
    }

    public Page<SampleResponse> search(SampleSearchRequest request, SearchQuery searchQuery, Pageable pageable) {
        return sampleDao.search(request, searchQuery, pageable).map(sampleMapper::toResponse);
    }

    public SampleResponse getById(Long id) {
        Sample sample = findById(id);
        return sampleMapper.toResponse(sample);
    }

    public Long save(SampleCreateRequest request) {
        Sample sample = sampleDao.save(sampleMapper.toEntity(request));
        return sample.getId();
    }

    @Transactional
    public void update(Long id, SampleUpdateRequest request) {
        Sample sample = findById(id);
        sampleMapper.updateSampleFromDto(request, sample);
        sampleDao.save(sample);
    }

    @Transactional
    public void delete(Long id) {
        Sample sample = findById(id);
        sampleDao.delete(sample);
    }

    private Sample findById(Long id) {
        Optional<Sample> sample = sampleDao.findById(id);
        if (sample.isEmpty()) {
            throw new EntityNotFoundException("존재하지 않는 리소스");
        }
        return sample.get();
    }
}
