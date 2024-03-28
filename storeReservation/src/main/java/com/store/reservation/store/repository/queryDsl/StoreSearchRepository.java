package com.store.reservation.store.repository.queryDsl;

import com.store.reservation.store.domain.model.Store;
import com.store.reservation.store.dto.search.request.SearchStoreDto;
import com.store.reservation.store.repository.queryDsl.dto.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreSearchRepository {
    Page<Store> searchStoreByCondition(SearchDto searchDto);

    boolean existsStoreByRoadNameAndStoreName(String roadName, String StoreName);

}
