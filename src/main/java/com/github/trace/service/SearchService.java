package com.github.trace.service;

import com.github.trace.entity.SearchLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by chenlong on 2016/4/8.
 */
@Service
public class SearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger( SearchService.class );

	@Autowired
	private ElasticsearchService esService;

	public List<Map<String, Object>> searchES(SearchLog slog) {
		List<Map<String, Object>> results = null;
		try {
			results = esService.search(slog.getTopic(),
				                       slog.getKeyWord(),
                                       slog.getTag(),
				                       slog.getStartTime(),
                                       slog.getEndTime());
		} catch (Exception e) {
			LOGGER.error("search ElasticSearch failed !", e);
		}
		return results;
	}

	public List<Map<String, Object>> searchESWithSize(SearchLog slog) {
		List<Map<String, Object>> results = null;
		try {
			results = esService.search(slog.getTopic(),
				                       slog.getKeyWord(),
                                       slog.getTag(),
				                       slog.getStartTime(),
				                       slog.getEndTime(),
                                       slog.getPageStart(),
                                       slog.getPageSize());
		} catch (Exception e) {
			LOGGER.error("search ElasticSearch failed !", e);
		}
		return results;
	}

}
