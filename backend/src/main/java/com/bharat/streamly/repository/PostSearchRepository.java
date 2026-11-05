package com.bharat.streamly.repository;

import com.bharat.streamly.document.PostDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, Long> {

    @Query("""
            { "multi_match": { "query": "?0", "fields": ["title", "description"], "fuzziness":"AUTO" } }
            """)
    Page<PostDocument> search(String q, Pageable pageable);
}

