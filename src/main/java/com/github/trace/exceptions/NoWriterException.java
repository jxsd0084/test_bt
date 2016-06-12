package com.github.trace.exceptions;

/**
 * NoWriterException
 * writers必须进行配置, 否则不知道消息发送到哪
 * Created by wzk on 16/3/11.
 */
public class NoWriterException extends RuntimeException {

	public NoWriterException( String message ) {

		super( message );
	}

}
