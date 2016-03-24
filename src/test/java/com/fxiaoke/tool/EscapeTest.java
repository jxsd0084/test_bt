package com.fxiaoke.tool;

import com.github.trace.service.CEPService;
import com.google.common.escape.Escapers;
import com.google.common.net.UrlEscapers;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Test;

import java.net.URLDecoder;
import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

/**
 * Created by hadoop on 16-3-18.
 */
public class EscapeTest {
    @Test
    public void testEscape() throws Exception {
       // assertEquals("%5Cd", URLEncoder.encode("\\d", "UTF-8"));
       // assertEquals("\\d", URLDecoder.decode("%5cd", "UTF-8"));

       CEPService cepService =  new CEPService();
        //String compare = cepService.compare("dcx.MonitorRequest", "");
    }
}
