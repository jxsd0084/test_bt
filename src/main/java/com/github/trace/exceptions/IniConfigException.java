package com.github.trace.exceptions;

/**
 * Ini配置读取Excpetion
 * Created by wzk on 16/3/14.
 */
public class IniConfigException extends RuntimeException {

	public IniConfigException( String msg ) {

		super( msg );
	}

	public IniConfigException( String msg, Exception e ) {

		super( msg, e );
	}

}
