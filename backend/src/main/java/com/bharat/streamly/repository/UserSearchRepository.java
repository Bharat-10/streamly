package com.bharat.streamly.repository;


import com.bharat.streamly.document.UserDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserSearchRepository extends ElasticsearchRepository<UserDocument, Long> {

    @Query("""
            {"match": {"username": {"query": "?0", "fuzziness": "AUTO"}}}
            """)
    Page<UserDocument> search(String q, Pageable pageable);
}
