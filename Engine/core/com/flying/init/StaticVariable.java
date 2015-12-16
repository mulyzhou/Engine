package com.flying.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedisPool;

import com.flying.task.ITask;
/**
 * 
 * <B>描述：</B>系统配置区<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class StaticVariable {
	//本系统使用的数据库
	public static String DB = "";
	//是否编译的路径
	public static boolean DEBUG = false;
	//系统路径
	public static String PATH = "";
	//服务器真实路径
	public static String SERVER_PATH = "";
	//开发模式下，构建的系统简称
	public static String MODULE = "";
	//表别名
	public static String ALIAS = "";
	//权限开发
	public static boolean AUTHENTICATION = false;
	//是否开启单点登录
	public static boolean SSO_AUTH = false;
	//数据库日志开关
	public static boolean LOG = false;
	//redis缓存开关，true：开启	false：关闭
	public static boolean REDIS = false;
	//mongodb缓存开关，true：开启	false：关闭
	public static boolean MONGODB = false;
	//生产的根节点ID
	public static String ROOT_MENU = "";
	//是否使用maven管理
	public static boolean MAVEN = false;
	//配置文件的位置
	public static String CONFIG_PATH = "src";
	//是否启用字典表false：不启用；true：启用；默认不启用
	public static boolean ENABLE_DATATABLE = false;
	//mysql查询本库中含有多少用户表时，需要知道schema
	public static String DB_URL = "";
	//访问配置缓存
	public static final Map<String,Item> FLYINGCONFIG = new HashMap<String,Item>();
	//拦截器集合
	public static final List INTERCEPTOR_SYSTEM = new ArrayList();
	//拦截器集合
	public static final Map<String,List> INTERCEPTOR_COLLECTION = new HashMap<String,List>();
	//初始化运行的集合
	public static final List<ITask> TASKS = new ArrayList<ITask>();
	//登陆之后的白名单
	public static final Map<String,String> LOGIN_WHITE_LIST = new HashMap<String,String>();
	//无需登陆的白名单
	public static final Map<String,String> LOGOUT_WHITE_LIST = new HashMap<String,String>();
	//存放session
	public static final Map<String,Object> SESSIONS = new HashMap<String,Object>();
	//Redis单机客户端连接对象池
	public static JedisPool JEDIS_POOL = null;
	//Redis多机客户端连接对象
	public static ShardedJedisPool SHARDED_JEDIS_POOL = null;
}
