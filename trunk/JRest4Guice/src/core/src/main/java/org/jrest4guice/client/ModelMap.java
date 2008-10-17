package org.jrest4guice.client;

import java.util.HashMap;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class ModelMap<K, V> extends HashMap<K,V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2723421216525025616L;
	
	public static final String RPC_ARGS_KEY = "__rpc_args__";
	public static final String BYTE_ARRAY_ARGS_KEY = "__byte_array_args__";
	public static final String FILE_ITEM_ARGS_KEY = "__file_item_args__";
}
