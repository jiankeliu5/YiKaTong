package com.seedsoft.ykt.util;

import java.io.File;

import com.seedsoft.ykt.activity.R;

/**
 * 配置常量
 * */
public class Constants {

	// 由于Windows和Linux等系统的路径左右斜杠不一致，则由File类的separator方法测试给出
	public static String separator = File.separator;

	public static final String ROOTPATH = separator + "yikatong";

	public static final String DOWNLOAD_PATH = ROOTPATH + separator
			+ "download";

	public static final String CONFIG_PATH = DOWNLOAD_PATH + separator
			+ "config";

	public static final String CUSTOM_PATH = DOWNLOAD_PATH + separator
			+ "custom";

	public static final String HTML_PATH = DOWNLOAD_PATH + separator
			+ "html_cache";

	public final static int COLUMN_COUNT = 2; // 图片列数设置

	public final static int PICTURE_COUNT_PER_LOAD = 15; // 每次加载图片数量

	public final static int PICTURE_TOTAL_COUNT = 10000; // 加载图片数量

	public final static int HANDLER_WHAT = 1;

	public final static int MESSAGE_DELAY = 200;

	public final static int TIME_OUT_MILLISECOND = 10000;// 超时时间默认5S

	public static final String SERVER_URL = "http://192.168.1.14:8888/palmxian";// 服务器根

	public static final String ACCESS_URL = SERVER_URL + "/public";// 项目根目

	public static final String VOTE_URL = ACCESS_URL + "/exame/vote.xml"; // 投票
	
	public static final String SURVEY_URL = ACCESS_URL + "/exame/survey.xml"; // 问卷
	
	public static final String REFRESH_TIME_URL = ACCESS_URL + "/refresh.xml"; // 主页配置更新时间文件

	public static final String MAIN_ACTION_URL = ACCESS_URL + "/main.xml"; // 主页配置文件

	public static final String SHARE_URL = SERVER_URL + "/download.action";// 好友分享下载地址

	public static final String UPDATE_URL = ACCESS_URL + "/version.xml";// 应用版本更新的xml文件地址

	public static final String REG_URL = SERVER_URL + "/reg.action";// 注册用户接口地址

	public static final String LOGIN_URL = SERVER_URL + "/auth.action";// 登录认证接口地址

	public static final String UPLOAD_URL = SERVER_URL + "/image.action";// 上传图片接口地址

	public static final String LIKE_URL = SERVER_URL + "/like.action";// 图片点赞接口地址

	public static final String RESET_PASSWD_URL = SERVER_URL + "/reset.action";// 重置密码接口地址

	public static final String IMAGE_LIB_URL = ACCESS_URL
			+ "/imagelib/main.xml";// 图库分类xml地址

	public static final String COMMENT_SUMMIT_URL = SERVER_URL
			+ "/comment.action";// 评论接口地址

	public static final String SP_SAVE_NAME = "yi_ka_tong";// 用户登录信息和定位信息在本地sharedpreference中保存的名字

	public static final String systemIdentify = "zhangshanghuayin";

	public static final String PIC_LINK = "pic_link";

	public static final String PIC_APP = "pic_app";

	public static final String TXT_LINK = "txt_link";

	public static final String TXT_APP = "txt_app";

	public static final String PUSH_FILE = "pushed.txt";

	public static final String CFG_FILE_NAME = "first.cfg";
	
	public static final String IMAGE_CACHE_DIR = "thumbs";
	
	public static final String TEL_NUM1 = "02986365038";//86365033  86365038
	
	public static final int[] ICONS = new int[] {
		R.drawable.perm_group_card, R.drawable.perm_group_sh,
		R.drawable.perm_group_yh, R.drawable.perm_group_gd };
	
	public static final int[] BG_ICONS = new int[] {
		R.drawable.ykt_normal, R.drawable.sh_normal,
		R.drawable.yh_normal, R.drawable.gd_normal };


}
