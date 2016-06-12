package com.github.trace.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjiezhao on 2016/4/12.
 */
public class SendLogCheckMonitor {

	private static final Logger LOG = LoggerFactory.getLogger( SendLogCheckMonitor.class );
	// 创建httppost
	private static HttpPost httpPost;

	// 创建参数队列
	private static List< NameValuePair > formParams = new ArrayList< NameValuePair >();

	public static void setFormParams( String title, String content ) {

		formParams.add( new BasicNameValuePair( "title", title ) );
		formParams.add( new BasicNameValuePair( "content", content ) );
	}

	public static boolean sendPostRequest( String title, String content ) {

		CloseableHttpClient httpclient = HttpClients.createDefault();

		String url = "http://172.17.0.35/qixin/send_qixin_for_sp.php";
		httpPost = new HttpPost( url );
		//httpPost=new HttpPost("http://172.17.0.35/qixin/send_qixin.php");

		setFormParams( title, content );
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity( formParams, "UTF-8" );
			httpPost.setEntity( uefEntity );

			System.out.println( "executing request " + httpPost.getURI() );
			CloseableHttpResponse response = httpclient.execute( httpPost );

			try {
				if ( response.getStatusLine().getStatusCode() == 200 ) {
					HttpEntity entity = response.getEntity();
					if ( entity != null ) {
						System.out.println( "--------------------------------------" );
						System.out.println( "Response content: " + EntityUtils.toString( entity, "UTF-8" ) );
						System.out.println( response.getEntity() );
						System.out.println( "--------------------------------------" );
					}
					return true;
				} else {
					return false;
				}
			} finally {
				response.close();
			}
		} catch ( ClientProtocolException e ) {
			LOG.warn( "Exception:", e );
			return false;
		} catch ( UnsupportedEncodingException e ) {
			LOG.warn( "Exception:", e );
			return false;
		} catch ( IOException e ) {
			LOG.warn( "Exception:", e );
			return false;
		} finally {
			try {
				httpclient.close();
			} catch ( IOException e ) {
				LOG.warn( "Exception:", e );
			}
		}
	}

//        public static void main(String... args){
//            sendPostRequest();
//
//        }
}




