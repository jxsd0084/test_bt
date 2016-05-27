package com.fxiaoke.tool;

import com.github.autoconf.ConfigFactory;
import com.github.autoconf.api.IChangeableConfig;
import com.github.trace.entity.LevelOneFields;
import com.github.trace.mapper.LevelTwoFieldsMapper;
import com.github.trace.service.CEPService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.escape.Escapers;
import com.google.common.net.UrlEscapers;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import scala.util.parsing.combinator.testing.Str;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

/**
 * Created by hadoop on 16-3-18.
 */
public class EscapeTest {
    private ConsumerConnector consumerConnector;
    @Test
    public void testEscape() throws Exception {
       // assertEquals("%5Cd", URLEncoder.encode("\\d", "UTF-8"));
       // assertEquals("\\d", URLDecoder.decode("%5cd", "UTF-8"));

       CEPService cepService =  new CEPService();
        //String compare = cepService.compare("dcx.MonitorRequest", "");
    }

    @Test
    public void testguava() throws ExecutionException {
        testPutcache();
        LoadingCache<String,String> cachebuilder = CacheBuilder.newBuilder()
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return "";
                    }
                });
        System.out.println("jerry value:"+cachebuilder.getUnchecked("hello"));
}

    @Test
    public void testPutcache() throws ExecutionException {
        Cache<String,String> cache = CacheBuilder.newBuilder().maximumSize(2000).build();
        cache.put("hello","test");
        String result = cache.get("hello1", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "test2";
            }
        });

        System.out.println(result);
    }
    @Test
    public void testGetConfig(){
        String topic = "datapersist";
        String key = "logBasePath";
        IChangeableConfig consumerConfig = ConfigFactory.getInstance().getConfig("datapersist");
        HierarchicalINIConfiguration iniConfiguration = new HierarchicalINIConfiguration();
        byte[] content = consumerConfig.getContent();
        InputStream inputStream = new ByteArrayInputStream(content);
        try {
            iniConfiguration.clear();
            iniConfiguration.load(inputStream);
            iniConfiguration.setThrowExceptionOnMissing(false);
        } catch (ConfigurationException e) {
//      throw new IniConfigException("Cannot load ini config " + config.getName(), e);
        }

        SubnodeConfiguration sectionConfig = iniConfiguration.getSection(topic);
        String result = (sectionConfig == null ? null : sectionConfig.getString(key, null));
        System.out.println("返回结果" + result);
    }
}
