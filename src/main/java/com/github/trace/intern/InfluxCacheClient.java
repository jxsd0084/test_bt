package com.github.trace.intern;

import com.alibaba.fastjson.JSON;
import com.github.jedis.support.JedisCmd;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 截获query请求，增加cache功能
 * Created by hanmz on 2015/12/30.
 */
public class InfluxCacheClient implements InvocationHandler {
    private static final Logger LOG = LoggerFactory.getLogger(InfluxCacheClient.class);
    private final InfluxDB delegate;
    private final JedisCmd jedis;
    private final QueryResult empty;
    private final Pattern hot = Pattern.compile("time\\s*>\\s*now\\(\\)\\s*-\\s*\\d+[mh]");

    private final Set<String> running = Sets.newConcurrentHashSet();

    public InfluxCacheClient(InfluxDB delegate, JedisCmd jedis) {
        this.delegate = delegate;
        this.jedis = jedis;
        this.empty = new QueryResult();
        this.empty.setError("empty Result");
        this.empty.setResults(ImmutableList.of());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("query")) {
            Query q = (Query) args[0];
            String key = q.getDatabase() + "::" + q.getCommand();
            String json = jedis.get(key);
            if (running.contains(key)) {
                LOG.warn("duplicated: {}", key);
                return this.empty;
            }
            try {
                running.add(key);
                // 如果cache中为空，添加key-value到cache中
                String today = DateUtil.formatYmd(System.currentTimeMillis());
                if (Strings.isNullOrEmpty(json)) {
                    QueryResult result = (QueryResult) method.invoke(delegate, args);
                    if (hot.matcher(key).find()) {
                        jedis.setex(key, 30, JSON.toJSONString(result)); // 热数据设置30秒过期时间
                    } else if (key.contains("now()") || key.contains(today)) {
                        jedis.setex(key, 120, JSON.toJSONString(result)); // 次热数据设置2min过期时间
                    } else {
                        jedis.setex(key, 600, JSON.toJSONString(result)); // 冷数据设置10min过期时间
                    }
                    return result;
                } else {
                    return JSON.parseObject(json, QueryResult.class);
                }
            } finally {
                running.remove(key);
            }
        }
        return this.empty;
    }
}
