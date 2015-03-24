package com.seedsoft.ykt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import android.content.Context;
import android.util.Log;

import com.seedsoft.ykt.activity.BuildConfig;
import com.seedsoft.ykt.bean.ActBean;
import com.seedsoft.ykt.bean.AdBean;
import com.seedsoft.ykt.bean.AppBean;
import com.seedsoft.ykt.bean.CarouselBean;
import com.seedsoft.ykt.bean.CommentBean;
import com.seedsoft.ykt.bean.FatherBean;
import com.seedsoft.ykt.bean.FatherModuleBean;
import com.seedsoft.ykt.bean.ImageBean;
import com.seedsoft.ykt.bean.ImageLibBean;
import com.seedsoft.ykt.bean.LinkBean;
import com.seedsoft.ykt.bean.MulChildrenBean;
import com.seedsoft.ykt.bean.MulListBean;
import com.seedsoft.ykt.bean.NewsBean;
import com.seedsoft.ykt.bean.RootBean;
import com.seedsoft.ykt.bean.TelCategoryBean;
import com.seedsoft.ykt.bean.TelNumBean;
import com.seedsoft.ykt.bean.VoteBean;
import com.seedsoft.ykt.bean.WeatherBean;

public class ParseXMLUtil {

	public final static String TAG = "ParseXMLUtil";

	/**
	 * 解析组件发布更新时间xml
	 * 
	 * */
	public String parseRefreshXml(String path) {
		String time = null;
		try {
			URL url = new URL(path);
			URLConnection uc = url.openConnection();
			uc.setConnectTimeout(Constants.TIME_OUT_MILLISECOND);
			uc.setDoInput(true);

			SAXBuilder saxBuilder = new SAXBuilder(false);
			org.jdom.Document doc = saxBuilder.build(uc.getInputStream());
			org.jdom.Element root = doc.getRootElement();
			time = root.getChildText("time");
			time = time.replace(":", "-");

			if (BuildConfig.DEBUG) {
				Log.d(TAG, "--refreshtime.xml--" + time);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			return null;
		}

		return time;

	}

	/** 解析版本升级信息的xml */
	public String parseUpdateXml(String path) {
		String info = null;
		try {
			URL url = new URL(path);
			URLConnection uc = url.openConnection();
			uc.setConnectTimeout(Constants.TIME_OUT_MILLISECOND);
			uc.setDoInput(true);

			SAXBuilder saxBuilder = new SAXBuilder(false);
			org.jdom.Document doc = saxBuilder.build(uc.getInputStream());
			org.jdom.Element root = doc.getRootElement();
			info = root.getChild("title").getValue();
			info = info + "@" + root.getChild("release").getValue();
			info = info + "@" + root.getChild("size").getValue() + "@";
			info = info + root.getChild("link").getValue();

			if (BuildConfig.DEBUG) {
				Log.d(TAG, "--version.xml--" + info);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			return null;
		}

		return info;

	}

	/** 解析百度天气预报信息的xml */
	public List<WeatherBean> parseWeatherXml(String path) {
		List<WeatherBean> ls = new ArrayList<WeatherBean>();
		WeatherBean wb = null;
		try {
			URL url = new URL(path);
			URLConnection uc = url.openConnection();
			uc.setConnectTimeout(Constants.TIME_OUT_MILLISECOND);
			uc.setDoInput(true);

			SAXBuilder saxBuilder = new SAXBuilder(false);
			org.jdom.Document doc = saxBuilder.build(uc.getInputStream());
			org.jdom.Element root = doc.getRootElement();
			Element resultsEl = root.getChild("results");
			Element weatherEl = resultsEl.getChild("weather_data");
			int j = 1;
			for (int i = 0; i < weatherEl.getContentSize() - 2; i++) {
				i++;
				if (weatherEl.getContent(i).toString()
						.equals("[Element: <date/>]")) {
					// System.out.println("========================");
					wb = new WeatherBean();
				}
				switch (j) {
				case 1:
					wb.setDate(weatherEl.getContent(i).getValue());
					break;
				case 2:
					wb.setDayPictureUrl(weatherEl.getContent(i).getValue());
					break;
				case 3:
					wb.setNightPictureUrl(weatherEl.getContent(i).getValue());
					break;
				case 4:
					wb.setWeather(weatherEl.getContent(i).getValue());
					break;
				case 5:
					wb.setWind(weatherEl.getContent(i).getValue());
					break;
				case 6:
					wb.setTemperature(weatherEl.getContent(i).getValue());
					ls.add(wb);
					j = 0;
					break;

				default:
					break;
				}
				j++;
				// System.out.println("----weather---->"+weatherEl.getContent(i).getValue());

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			return null;
		}

		return ls;
	}

	/**
	 * 解析整个应用配置信息的xml
	 * 
	 * @param boolean hasUpdate; 服务器端 配置文件 有无更新 true：有更新 false：无更新
	 * */
	public static ArrayList<RootBean> parseMainXml(Context context,
			String path, boolean hasUpdate) {

		RootBean rootBean = null;
		FatherBean fatherBean = null;
		FatherModuleBean fatherModuleBean = null;
		CarouselBean carouselBean = null;
		MulListBean mulListBean = null;
		LinkBean linkBean = null;
		AppBean appBean = null;
		ActBean actBean = null;
		NewsBean newsBean = null;
		AdBean adBean = null;
		MulChildrenBean mulChildrenBean = null;

		ArrayList<RootBean> rootBeans = null;
		ArrayList<FatherBean> fatherBeans = null;
		ArrayList<FatherModuleBean> fatherModuleBeans = null;
		Object objects = null;
		ArrayList<AdBean> adBeans = null;
		ArrayList<MulChildrenBean> mulChildrenBeans = null;
		InputStream is = null;
		File[] children = null;
		String refreshTime = null;
		try {

			/**
			 * 判断是否存在自定义的配置文件 Y1.存在.读取自定义文件 N1.不存在.读取默认配置文件
			 * */
			File file = new File(Util.getSDCardPath(context)
					+ Constants.CUSTOM_PATH);
			if (!file.exists()) {
				file.mkdirs();
			}
			children = file.listFiles();
			if (children.length > 0 && children[0].length() > 0 && !hasUpdate) {
				path = children[0].getAbsolutePath();
			}

			if (BuildConfig.DEBUG) {
				Log.i(TAG, "-[parseMainXml:path]-" + path);
			}

			if (path.startsWith("/")) {
				is = new FileInputStream(path);
			} else {

				// URL url = new URL(path);
				// URLConnection urlConnection = url.openConnection();
				// urlConnection.setDoInput(true);
				// urlConnection.setConnectTimeout(Constants.TIME_OUT_MILLISECOND);
				// is = url.openStream();
				throw new Exception("path is from net !!" + path);
			}

			SAXBuilder saxBuilder = new SAXBuilder(false);
			Document doc = saxBuilder.build(is);
			Element root = doc.getRootElement();

			refreshTime = root.getChildText("time");
			refreshTime = refreshTime.replace(":", "-");
			refreshTime = refreshTime + ".cfg";

			File f = new File(path);
			// 初次解析配置文件，改名为当前组件刷新时间
			if (children.length == 0 && !(refreshTime.equals(f.getName()))) {
				f.renameTo(new File(Util.getSDCardPath(context)
						+ Constants.CONFIG_PATH + File.separator + refreshTime));
				System.out.println("初次改名字成功！-->" + refreshTime);
			}
			Element os = root.getChild("os");
			List list = os.getChildren("o");
			// System.out.println("大类别数目："+list.size()+"个");

			rootBeans = new ArrayList<RootBean>();// 初始化根目录列表

			for (int i = 0; i < list.size(); i++) {
				Element oEl = (Element) list.get(i);
				String title = oEl.getChildText("t");
				String frontImage = oEl.getChildText("fi");
				String backImage = oEl.getChildText("bi");
				String backColor = oEl.getChildText("bc");
				String showType = oEl.getChildText("st");
				// System.out.println("-oel-"+(i+1)+"-->"+title);
				// System.out.println("-oel-"+(i+1)+"-->"+frontImage);
				// System.out.println("-oel-"+(i+1)+"-->"+backImage);

				rootBean = new RootBean();// 初始化根目录
				// 给根目录赋值
				rootBean.setName(title);
				rootBean.setFrontImage(frontImage);
				rootBean.setBackImage(backImage);
				rootBean.setBackColor(backColor);
				rootBean.setShowType(showType);

				Element dsEl = oEl.getChild("ds");
				List dList = dsEl.getChildren("d");
				// System.out.println("大类别--（"+title+"）下的子分类数目："+dList.size()+"个");

				fatherBeans = new ArrayList<FatherBean>();// 初始化子分类列表

				for (int j = 0; j < dList.size(); j++) {

					fatherBean = new FatherBean();// 初始化子分类对象

					Element dEl = (Element) dList.get(j);
					String childTitle = dEl.getChildText("t");
					String childShow = dEl.getChildText("p");
					String fImage = dEl.getChildText("fi");
					String bImage = dEl.getChildText("bi");
					if (BuildConfig.DEBUG) {
						System.out.println("--childTitle-->" + childTitle);
						System.out.println("--childShow-->" + childShow);
						System.out.println("--fImage-->" + fImage);
						System.out.println("--bImage-->" + bImage);
					}
					// 给子分类赋值
					fatherBean.setName(childTitle);
					fatherBean.setIsShow(childShow);
					fatherBean.setFrontImage(fImage);
					fatherBean.setBackColor(bImage);

					Element isEl = dEl.getChild("is");
					List iList = isEl.getChildren("i");
					// System.out.println("子分类--（"+childTitle+"）下的组件数目："+iList.size()+"个");

					fatherModuleBeans = new ArrayList<FatherModuleBean>();// 初始化子分类下组件类型对象列表

					for (int k = 0; k < iList.size(); k++) {

						fatherModuleBean = new FatherModuleBean();// 初始化组件类型实体类

						Element iEl = (Element) iList.get(k);
						String iTitle = iEl.getChildText("t");
						String ctype = iEl.getChildText("c").trim();// 组件类型很重要，根据不同的类型，元数据会不同。当然，解析方法和展示方法也不同。
						// System.out.println("--iTitle-->"+iTitle);
						// System.out.println("--ctype-->"+ctype);

						// 给组件类型实体类赋值
						fatherModuleBean.setName(iTitle);
						fatherModuleBean.setType(ctype);

						// List mList = iEl.getChildren("m");
						// System.out.println("组件--（"+iTitle+"）下的元数据数目："+mList.size()+"个");

						Element mEl = iEl.getChild("m");

						objects = new Object();// 初始化不同组件的存放列表

						// 此处，开始根据组件类型，分情况解析
						if (ctype.equalsIgnoreCase("singletextlist")
								|| ctype.equalsIgnoreCase("singleimagetextlist")
								|| ctype.equalsIgnoreCase("carousel")) {

							carouselBean = new CarouselBean();// 初始化组件类型1实体类

							// for (int l = 0; l < mList.size(); l++) {
							// Element mEl =(Element) mList.get(l);
							String mUrl = mEl.getChildText("url");
							// System.out.println("--mUrl-->"+mUrl);

							carouselBean.setUrl(mUrl);// 赋值

							List adList = mEl.getChildren("ad");
							// System.out.println("元数据中广告的数目："+adList.size()+"个");

							adBeans = new ArrayList<AdBean>();// 初始化广告列表

							if (adList != null) {
								for (int m = 0; m < adList.size(); m++) {

									Element adEl = (Element) adList.get(m);
									String adType = adEl.getChildText("type");
									String adTitle = adEl.getChildText("title");
									String adImage = adEl.getChildText("image");
									String adUrl = adEl.getChildText("url");
									// System.out.println("--adType-->"+adType);
									// System.out.println("--adTitle-->"+adTitle);
									// System.out.println("--adImage-->"+adImage);
									// System.out.println("--adUrl-->"+adUrl);

									adBean = new AdBean();
									adBean.setTitle(adTitle);
									adBean.setType(adType);
									adBean.setImage(adImage);
									adBean.setUrl(adUrl);
									adBeans.add(adBean);
								}
							}
							// }

							carouselBean.setAdBeans(adBeans);
							objects = carouselBean;

						} else if (ctype.equalsIgnoreCase("multtextlist")
								|| ctype.equalsIgnoreCase("multimagetextlist")) {

							mulListBean = new MulListBean();
							mulChildrenBeans = new ArrayList<MulChildrenBean>();
							adBeans = new ArrayList<AdBean>();// 初始化广告列表

							// for (int l = 0; l < mList.size(); l++) {
							// Element mEl =(Element) mList.get(l);
							List columnList = mEl.getChildren("column");
							// System.out.println("元数据中(栏目)的数目："+columnList.size()+"个");
							if (columnList != null) {
								for (int m = 0; m < columnList.size(); m++) {
									Element columnEl = (Element) columnList
											.get(m);
									String columnTitle = columnEl
											.getChildText("title");
									String columnUrl = columnEl
											.getChildText("url");
									// System.out.println("--columnTitle-->"+columnTitle);
									// System.out.println("--columnUrl-->"+columnUrl);

									mulChildrenBean = new MulChildrenBean();
									mulChildrenBean.setTitle(columnTitle);
									mulChildrenBean.setUrl(columnUrl);
									mulChildrenBeans.add(mulChildrenBean);

								}
							}
							List adList = mEl.getChildren("ad");
							// System.out.println("元数据中（广告）的数目："+adList.size()+"个");
							if (adList != null) {
								for (int m = 0; m < adList.size(); m++) {
									Element adEl = (Element) adList.get(m);
									String adType = adEl.getChildText("type");
									String adTitle = adEl.getChildText("title");
									String adImage = adEl.getChildText("image");
									String adUrl = adEl.getChildText("url");
									// System.out.println("--adType-->"+adType);
									// System.out.println("--adTitle-->"+adTitle);
									// System.out.println("--adImage-->"+adImage);
									// System.out.println("--adUrl-->"+adUrl);

									adBean = new AdBean();
									adBean.setTitle(adTitle);
									adBean.setType(adType);
									adBean.setImage(adImage);
									adBean.setUrl(adUrl);
									adBeans.add(adBean);

								}
							}
							// }

							mulListBean.setMulChildrenBeans(mulChildrenBeans);
							mulListBean.setAdBeans(adBeans);
							objects = mulListBean;

						} else if (ctype.equalsIgnoreCase("imagelink")
								|| ctype.equalsIgnoreCase("textlink")) {

							linkBean = new LinkBean();
							adBeans = new ArrayList<AdBean>();// 初始化广告列表

							// for (int l = 0; l < mList.size(); l++) {
							// Element mEl =(Element) mList.get(l);
							List linkList = mEl.getChildren("link");
							// System.out.println("元数据中(link)的数目："+linkList.size()+"个");
							if (linkList != null) {
								for (int m = 0; m < linkList.size(); m++) {
									Element linkEl = (Element) linkList.get(m);
									String linkType = linkEl
											.getChildText("type");
									String linkTitle = linkEl
											.getChildText("title");
									String linkImage = linkEl
											.getChildText("image");
									String linkUrl = linkEl.getChildText("url");
									// System.out.println("--linkType-->"+linkType);
									// System.out.println("--linkTitle-->"+linkTitle);
									// System.out.println("--linkImage-->"+linkImage);
									// System.out.println("--linkUrl-->"+linkUrl);

									linkBean.setTitle(linkTitle);
									linkBean.setType(linkType);
									linkBean.setImage(linkImage);
									linkBean.setUrl(linkUrl);

								}
							}
							List adList = mEl.getChildren("ad");
							// System.out.println("元数据中（广告）的数目："+adList.size()+"个");
							if (adList != null) {
								for (int m = 0; m < adList.size(); m++) {
									Element adEl = (Element) adList.get(m);
									String adType = adEl.getChildText("type");
									String adTitle = adEl.getChildText("title");
									String adImage = adEl.getChildText("image");
									String adUrl = adEl.getChildText("url");
									// System.out.println("--adType-->"+adType);
									// System.out.println("--adTitle-->"+adTitle);
									// System.out.println("--adImage-->"+adImage);
									// System.out.println("--adUrl-->"+adUrl);

									adBean = new AdBean();
									adBean.setTitle(adTitle);
									adBean.setType(adType);
									adBean.setImage(adImage);
									adBean.setUrl(adUrl);
									adBeans.add(adBean);

								}
							}
							// }

							linkBean.setAdBeans(adBeans);
							objects = linkBean;

						} else if (ctype.equalsIgnoreCase("imageapp")
								|| ctype.equalsIgnoreCase("textapp")) {

							appBean = new AppBean();

							// for (int l = 0; l < mList.size(); l++) {
							// Element mEl =(Element) mList.get(l);
							List appList = mEl.getChildren("app");
							// System.out.println("元数据中(app)的数目："+appList.size()+"个");
							if (appList != null) {
								for (int m = 0; m < appList.size(); m++) {
									Element appkEl = (Element) appList.get(m);
									String appType = appkEl
											.getChildText("type");
									String appTitle = appkEl
											.getChildText("title");
									String appImage = appkEl
											.getChildText("image");
									String appUrl = appkEl.getChildText("url");
									String appPath = appkEl
											.getChildText("path");
									String appSize = appkEl
											.getChildText("size");
									// System.out.println("--appType-->"+appType);
									// System.out.println("--appTitle-->"+appTitle);
									// System.out.println("--appImage-->"+appImage);
									// System.out.println("--appUrl-->"+appUrl);
									// System.out.println("--appPath-->"+appPath);

									appBean.setTitle(appTitle);
									appBean.setType(appType);
									appBean.setImage(appImage);
									appBean.setUrl(appUrl);
									appBean.setPath(appPath);
									appBean.setSize(appSize);

								}
							}
							// }

							objects = appBean;

						} else if (ctype.equalsIgnoreCase("activity")) {

							actBean = new ActBean();

							// for (int l = 0; l < mList.size(); l++) {
							// Element cEl = (Element) mList.get(l);
							// String cPath = cEl.getText();
							String cPath = mEl.getChildText("path");
							// System.out.println("--cPath-->"+cPath);

							actBean.setPath(cPath);

							// }

							objects = actBean;

						} else if (ctype.equalsIgnoreCase("html5page")) {

							Element pageEl = mEl.getChild("page");
							String pageUrl = pageEl.getChildText("url");
							String pageId = pageEl.getChildText("b");
							String pageName = pageEl.getChildText("t");
							String pageCommentUrl = pageEl.getChildText("m");
							String pageIsAllowComment = pageEl
									.getChildText("p");
							String pageCommentNum = pageEl.getChildText("d");
							String pageLcation = pageEl.getChildText("l");

							newsBean = new NewsBean();
							newsBean.setUrl(pageUrl);
							newsBean.setId(pageId);
							newsBean.setTitle(pageName);
							newsBean.setCommentURL(pageCommentUrl);
							newsBean.setIsAllowComment(pageIsAllowComment);
							newsBean.setCommentNum(pageCommentNum);
							newsBean.setLocation(pageLcation);

							adBeans = new ArrayList<AdBean>();// 初始化广告列表
							List adList = mEl.getChildren("ad");
							// System.out.println("元数据中（广告）的数目："+adList.size()+"个");
							if (adList != null) {
								for (int m = 0; m < adList.size(); m++) {
									Element adEl = (Element) adList.get(m);
									String adType = adEl.getChildText("type");
									String adTitle = adEl.getChildText("title");
									String adImage = adEl.getChildText("image");
									String adUrl = adEl.getChildText("url");
									// System.out.println("--adType-->"+adType);
									// System.out.println("--adTitle-->"+adTitle);
									// System.out.println("--adImage-->"+adImage);
									// System.out.println("--adUrl-->"+adUrl);

									adBean = new AdBean();
									adBean.setTitle(adTitle);
									adBean.setType(adType);
									adBean.setImage(adImage);
									adBean.setUrl(adUrl);
									adBeans.add(adBean);

								}
							}

							newsBean.setAdBeans(adBeans);
							objects = newsBean;

						} else {
							// System.out.println("--!!!!!!-->"+"无此组件类型！！");
							return null;
						}
						fatherModuleBean.setObjects(objects);
						fatherModuleBeans.add(fatherModuleBean);
					}
					fatherBean.setFatherModuleBeans(fatherModuleBeans);
					fatherBeans.add(fatherBean);
				}
				rootBean.setFatherBeans(fatherBeans);
				rootBeans.add(rootBean);
				// System.out.println("--------------------------------");
			}

		} catch (IOException e) {

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return null;
		}

		/**
		 * 如果有更新，并且有自定义配置文件，则需要进入下面的代码，执行比较方法 ↓ ↓ ↓
		 * 1.取出更新后的cfg中，第一大类别的rootbean对象的名称和isshow属性
		 * 2.与custom下cfg中，第一大类的rootbean的相应属性对比。
		 * 3.若是当前的名字为新增的，则添加在custom下的未添加栏目中。 4.若custom中的名字在当前中的大类没有，则删掉该类
		 * ===========================================
		 * 注意：若需要比对其他类别的对象和属性，需要在此做修改！！ 目前版本只支持，第一大类，即首页的比对功能！！
		 * ===========================================
		 */
		if (hasUpdate && children.length > 0 && children[0].length() > 0) {

			// 定义布尔值标志，标示是否发生与自定义文件名字改动的地方
			// boolean isChangedFirst = false;
			// 步骤一：获取第一大类的类对象ROOTBEAN的子栏目集合
			// custom
			ArrayList<FatherBean> fbsCustom = parseMainXml(context,
					children[0].getAbsolutePath(), false).get(0)
					.getFatherBeans();
			// config
			ArrayList<FatherBean> fbsConfig = rootBeans.get(0).getFatherBeans();

			// 步骤二：经过3次循环,完成对两个名字集合对象的赋值。
			ArrayList<String> customNames = new ArrayList<String>();
			ArrayList<String> configNames = new ArrayList<String>();
			ArrayList<String> deleteCustomNames = new ArrayList<String>();

			// 1.第一次循环，负责给configNames集合赋值
			for (int j = 0; j < fbsConfig.size(); j++) {
				String configName = fbsConfig.get(j).getName();
				configNames.add(configName);
			}
			// 2.第二次循环，负责两件事
			// ①判断configNames中是否包含有customName？不包含则从fbsCustom中删掉；
			// ②包含则添加到customNames集合

			for (int i = 0; i < fbsCustom.size(); i++) {
				String customName = fbsCustom.get(i).getName();
				// System.out.println("--判断前的customname--"+customName);
				// System.out.println("-----------------------------");
				if (!configNames.contains(customName)) {
					// System.out.println("--要删除配置文件中不存在的名字--"+customName);
					fbsCustom.remove(i);
					i--;
					deleteCustomNames.add(customName);
					// isChangedFirst = true;
					// System.out.println("======删除配置文件中不存在的名字，完成===================");
				} else {
					// System.out.println("--判断后的customname-----"+customName);
					customNames.add(customName);
				}
			}

			// for (int i = 0; i < customNames.size(); i++) {
			// System.out.println("--customname--"+customNames.get(i));
			// }
			// System.out.println("=============");
			// for (int i = 0; i < configNames.size(); i++) {
			// System.out.println("--configName--"+configNames.get(i));
			// }
			// System.out.println("=============");
			// 3.第三次循环，判断customNames中是否包含configName？不包含则添加进fbsCustom
			for (int i = 0; i < fbsConfig.size(); i++) {
				String configName = fbsConfig.get(i).getName();
				if (!customNames.contains(configName)) {
					FatherBean fb = fbsConfig.get(i);
					fb.setNew(true);
					fbsCustom.add(fb);
					// isChangedFirst = true;
				}
			}

			// 步骤三：将更新后的自定义fbsCustom对象，保存进文件
			// if(isChangedFirst){
			// System.out.println("======进入步骤三===================");
			// 定义一个stringbuffer
			StringBuffer sb = new StringBuffer();

			// ⑴读取配置文件config文件夹下的配置文件
			File cfgFile = new File(Util.getSDCardPath(context)
					+ Constants.CONFIG_PATH);
			String cfgStr = null;
			String cfgOneBig = null;
			try {
				FileInputStream fin = new FileInputStream(
						(cfgFile.listFiles())[0]);
				byte b[] = new byte[fin.available()];
				fin.read(b);
				// 读取文件到字符串中
				cfgStr = new String(b);
				// 截取第一个大类别
				cfgOneBig = cfgStr.substring(
						cfgStr.indexOf("<os><o><t>我的首页</t>"),
						cfgStr.indexOf("</o>"));

				// System.out.println("======重复三次循环开始===================");

				// 重复上面的第三次循环，判断customNames中是否包含configName？不包含则截取该名字所在的<d><d/>标签部分
				for (int i = 0; i < fbsConfig.size(); i++) {
					String configName = fbsConfig.get(i).getName();
					if (!customNames.contains(configName)) {
						// System.out.println("--要添加的栏目--" + configName);
						String temp = cfgOneBig.substring(cfgOneBig
								.indexOf("<d><t>" + configName));
						sb.append(temp.subSequence(0, temp.indexOf("</d>") + 4));
					}
				}

				fin.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println("---sb--"+sb.toString());
			// ⑵读取配置文件custom文件夹下的配置文件
			String customFileName = children[0].getName();
			// System.out.println("---customFileName--"+customFileName);
			String customStr = null;
			String customOneBig = null;
			String customDlist = null;
			try {
				FileInputStream fin = new FileInputStream(children[0]);
				byte b[] = new byte[fin.available()];
				fin.read(b);
				// 读取文件到字符串中
				customStr = new String(b);
				// 截取第一个大类别
				customOneBig = customStr.substring(
						customStr.indexOf("<os><o><t>我的首页</t>"),
						customStr.indexOf("</o>"));
				customDlist = customOneBig.substring(
						customOneBig.indexOf("<ds>") + 4,
						customOneBig.indexOf("</ds>"));
				// 判断是否有删除的<d><d/>标签信息
				// System.out.println("==deleteCustomNames====="
				// + deleteCustomNames.size());
				String tempDlist = customDlist;
				if (deleteCustomNames.size() > 0) {
					for (int i = 0; i < deleteCustomNames.size(); i++) {
						String temp = customDlist.substring(customDlist
								.indexOf("<d><t>" + deleteCustomNames.get(i)));
						String deletedItem = temp.substring(0,
								temp.indexOf("</d>") + 4);
						tempDlist = tempDlist.replace(deletedItem, "").trim();
						// System.out.println("===deletedItem====" +
						// deletedItem);
						// System.out.println("===customDlist===="+customDlist);
					}
				}

				// System.out.println("===tempDlist====" + tempDlist);
				// ⑶替换变动信息
				String temp = customOneBig.replace(customDlist,
						tempDlist + sb.toString());
				// customStr = customStr.replace(customOneBig, temp);
				cfgStr = cfgStr.replace(cfgOneBig, temp);
				fin.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				// ⑷保存新的配置文件
				File file = new File(Util.getSDCardPath(context)
						+ Constants.CUSTOM_PATH);
				File[] fs = file.listFiles();
				if (fs.length > 0) {
					for (int i = 0; i < fs.length; i++) {
						fs[i].delete();
					}
				}
				File f = new File(Util.getSDCardPath(context)
						+ Constants.CUSTOM_PATH, customFileName);
				FileOutputStream fos = new FileOutputStream(f, false);
				fos.write(cfgStr.getBytes());
				fos.flush();
				fos.close();
				System.out.println("==更新自定义配置文件成功==");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rootBeans.get(0).setChanged(true);
			rootBeans.get(0).setFatherBeans(fbsCustom);
			// }
		}

		// if(BuildConfig.DEBUG){
		// int a = rootBeans.size();
		// for (int i = 0; i < a; i++) {
		// Log.e(TAG, "==========================");
		// RootBean rb = rootBeans.get(i);
		// Log.e(TAG, rb.getName());
		// Log.e(TAG, rb.getShowType());
		// ArrayList<FatherBean> afb = rb.getFatherBeans();
		// int b = afb.size();
		// for (int j = 0; j < b; j++) {
		// FatherBean fb = afb.get(j);
		// Log.e(TAG, fb.getName());
		// // Log.e(TAG, fb.getXmlData());
		// ArrayList<FatherModuleBean> afmb = fb.getFatherModuleBeans();
		// int c = afmb.size();
		// for (FatherModuleBean fmb : afmb) {
		// Log.e(TAG, fmb.getName());
		// Log.e(TAG, fmb.getType());
		// Log.v(TAG, "==========================");
		// }
		// }
		// }
		//
		// }
		//

		return rootBeans;
	}

	/**
	 * Old
	 * */
	public static ArrayList<RootBean> parseMainXml(String path) {

		RootBean rootBean = null;
		FatherBean fatherBean = null;
		FatherModuleBean fatherModuleBean = null;
		CarouselBean carouselBean = null;
		MulListBean mulListBean = null;
		LinkBean linkBean = null;
		AppBean appBean = null;
		ActBean actBean = null;
		NewsBean newsBean = null;
		AdBean adBean = null;
		MulChildrenBean mulChildrenBean = null;

		ArrayList<RootBean> rootBeans = null;
		ArrayList<FatherBean> fatherBeans = null;
		ArrayList<FatherModuleBean> fatherModuleBeans = null;
		Object objects = null;
		ArrayList<AdBean> adBeans = null;
		ArrayList<MulChildrenBean> mulChildrenBeans = null;
		InputStream is = null;
		try {

			URL url = new URL(path);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setDoInput(true);
			urlConnection.setConnectTimeout(Constants.TIME_OUT_MILLISECOND);
			is = url.openStream();

			SAXBuilder saxBuilder = new SAXBuilder(false);
			Document doc = saxBuilder.build(is);
			Element root = doc.getRootElement();
			Element os = root.getChild("os");
			List list = os.getChildren("o");
			// System.out.println("大类别数目："+list.size()+"个");

			rootBeans = new ArrayList<RootBean>();// 初始化根目录列表

			for (int i = 0; i < list.size(); i++) {
				Element oEl = (Element) list.get(i);
				String title = oEl.getChildText("t");
				String frontImage = oEl.getChildText("fi");
				String backImage = oEl.getChildText("bi");
				String backColor = oEl.getChildText("bc");
				String showType = oEl.getChildText("st");
				// System.out.println("-oel-"+(i+1)+"-->"+title);
				// System.out.println("-oel-"+(i+1)+"-->"+frontImage);
				// System.out.println("-oel-"+(i+1)+"-->"+backImage);

				rootBean = new RootBean();// 初始化根目录
				// 给根目录赋值
				rootBean.setName(title);
				rootBean.setFrontImage(frontImage);
				rootBean.setBackImage(backImage);
				rootBean.setBackColor(backColor);
				rootBean.setShowType(showType);

				Element dsEl = oEl.getChild("ds");
				List dList = dsEl.getChildren("d");
				// System.out.println("大类别--（"+title+"）下的子分类数目："+dList.size()+"个");

				fatherBeans = new ArrayList<FatherBean>();// 初始化子分类列表

				for (int j = 0; j < dList.size(); j++) {

					fatherBean = new FatherBean();// 初始化子分类对象

					Element dEl = (Element) dList.get(j);
					String childTitle = dEl.getChildText("t");
					String childShow = dEl.getChildText("p");
					String fImage = dEl.getChildText("fi");
					String bImage = dEl.getChildText("bi");
					if (BuildConfig.DEBUG) {
						System.out.println("--childTitle-->" + childTitle);
						System.out.println("--childShow-->" + childShow);
						System.out.println("--fImage-->" + fImage);
						System.out.println("--bImage-->" + bImage);
					}
					// 给子分类赋值
					fatherBean.setName(childTitle);
					fatherBean.setIsShow(childShow);
					fatherBean.setFrontImage(fImage);
					fatherBean.setBackColor(bImage);

					Element isEl = dEl.getChild("is");
					List iList = isEl.getChildren("i");
					// System.out.println("子分类--（"+childTitle+"）下的组件数目："+iList.size()+"个");

					fatherModuleBeans = new ArrayList<FatherModuleBean>();// 初始化子分类下组件类型对象列表

					for (int k = 0; k < iList.size(); k++) {

						fatherModuleBean = new FatherModuleBean();// 初始化组件类型实体类

						Element iEl = (Element) iList.get(k);
						String iTitle = iEl.getChildText("t");
						String ctype = iEl.getChildText("c").trim();// 组件类型很重要，根据不同的类型，元数据会不同。当然，解析方法和展示方法也不同。
						// System.out.println("--iTitle-->"+iTitle);
						// System.out.println("--ctype-->"+ctype);

						// 给组件类型实体类赋值
						fatherModuleBean.setName(iTitle);
						fatherModuleBean.setType(ctype);

						// List mList = iEl.getChildren("m");
						// System.out.println("组件--（"+iTitle+"）下的元数据数目："+mList.size()+"个");

						Element mEl = iEl.getChild("m");

						objects = new Object();// 初始化不同组件的存放列表

						// 此处，开始根据组件类型，分情况解析
						if (ctype.equalsIgnoreCase("singletextlist")
								|| ctype.equalsIgnoreCase("singleimagetextlist")
								|| ctype.equalsIgnoreCase("carousel")) {

							carouselBean = new CarouselBean();// 初始化组件类型1实体类

							// for (int l = 0; l < mList.size(); l++) {
							// Element mEl =(Element) mList.get(l);
							String mUrl = mEl.getChildText("url");
							// System.out.println("--mUrl-->"+mUrl);

							carouselBean.setUrl(mUrl);// 赋值

							List adList = mEl.getChildren("ad");
							// System.out.println("元数据中广告的数目："+adList.size()+"个");

							adBeans = new ArrayList<AdBean>();// 初始化广告列表

							if (adList != null) {
								for (int m = 0; m < adList.size(); m++) {

									Element adEl = (Element) adList.get(m);
									String adType = adEl.getChildText("type");
									String adTitle = adEl.getChildText("title");
									String adImage = adEl.getChildText("image");
									String adUrl = adEl.getChildText("url");
									// System.out.println("--adType-->"+adType);
									// System.out.println("--adTitle-->"+adTitle);
									// System.out.println("--adImage-->"+adImage);
									// System.out.println("--adUrl-->"+adUrl);

									adBean = new AdBean();
									adBean.setTitle(adTitle);
									adBean.setType(adType);
									adBean.setImage(adImage);
									adBean.setUrl(adUrl);
									adBeans.add(adBean);
								}
							}
							// }

							carouselBean.setAdBeans(adBeans);
							objects = carouselBean;

						} else if (ctype.equalsIgnoreCase("multtextlist")
								|| ctype.equalsIgnoreCase("multimagetextlist")) {

							mulListBean = new MulListBean();
							mulChildrenBeans = new ArrayList<MulChildrenBean>();
							adBeans = new ArrayList<AdBean>();// 初始化广告列表

							// for (int l = 0; l < mList.size(); l++) {
							// Element mEl =(Element) mList.get(l);
							List columnList = mEl.getChildren("column");
							// System.out.println("元数据中(栏目)的数目："+columnList.size()+"个");
							if (columnList != null) {
								for (int m = 0; m < columnList.size(); m++) {
									Element columnEl = (Element) columnList
											.get(m);
									String columnTitle = columnEl
											.getChildText("title");
									String columnUrl = columnEl
											.getChildText("url");
									// System.out.println("--columnTitle-->"+columnTitle);
									// System.out.println("--columnUrl-->"+columnUrl);

									mulChildrenBean = new MulChildrenBean();
									mulChildrenBean.setTitle(columnTitle);
									mulChildrenBean.setUrl(columnUrl);
									mulChildrenBeans.add(mulChildrenBean);

								}
							}
							List adList = mEl.getChildren("ad");
							// System.out.println("元数据中（广告）的数目："+adList.size()+"个");
							if (adList != null) {
								for (int m = 0; m < adList.size(); m++) {
									Element adEl = (Element) adList.get(m);
									String adType = adEl.getChildText("type");
									String adTitle = adEl.getChildText("title");
									String adImage = adEl.getChildText("image");
									String adUrl = adEl.getChildText("url");
									// System.out.println("--adType-->"+adType);
									// System.out.println("--adTitle-->"+adTitle);
									// System.out.println("--adImage-->"+adImage);
									// System.out.println("--adUrl-->"+adUrl);

									adBean = new AdBean();
									adBean.setTitle(adTitle);
									adBean.setType(adType);
									adBean.setImage(adImage);
									adBean.setUrl(adUrl);
									adBeans.add(adBean);

								}
							}
							// }

							mulListBean.setMulChildrenBeans(mulChildrenBeans);
							mulListBean.setAdBeans(adBeans);
							objects = mulListBean;

						} else if (ctype.equalsIgnoreCase("imagelink")
								|| ctype.equalsIgnoreCase("textlink")) {

							linkBean = new LinkBean();
							adBeans = new ArrayList<AdBean>();// 初始化广告列表

							// for (int l = 0; l < mList.size(); l++) {
							// Element mEl =(Element) mList.get(l);
							List linkList = mEl.getChildren("link");
							// System.out.println("元数据中(link)的数目："+linkList.size()+"个");
							if (linkList != null) {
								for (int m = 0; m < linkList.size(); m++) {
									Element linkEl = (Element) linkList.get(m);
									String linkType = linkEl
											.getChildText("type");
									String linkTitle = linkEl
											.getChildText("title");
									String linkImage = linkEl
											.getChildText("image");
									String linkUrl = linkEl.getChildText("url");
									// System.out.println("--linkType-->"+linkType);
									// System.out.println("--linkTitle-->"+linkTitle);
									// System.out.println("--linkImage-->"+linkImage);
									// System.out.println("--linkUrl-->"+linkUrl);

									linkBean.setTitle(linkTitle);
									linkBean.setType(linkType);
									linkBean.setImage(linkImage);
									linkBean.setUrl(linkUrl);

								}
							}
							List adList = mEl.getChildren("ad");
							// System.out.println("元数据中（广告）的数目："+adList.size()+"个");
							if (adList != null) {
								for (int m = 0; m < adList.size(); m++) {
									Element adEl = (Element) adList.get(m);
									String adType = adEl.getChildText("type");
									String adTitle = adEl.getChildText("title");
									String adImage = adEl.getChildText("image");
									String adUrl = adEl.getChildText("url");
									// System.out.println("--adType-->"+adType);
									// System.out.println("--adTitle-->"+adTitle);
									// System.out.println("--adImage-->"+adImage);
									// System.out.println("--adUrl-->"+adUrl);

									adBean = new AdBean();
									adBean.setTitle(adTitle);
									adBean.setType(adType);
									adBean.setImage(adImage);
									adBean.setUrl(adUrl);
									adBeans.add(adBean);

								}
							}
							// }

							linkBean.setAdBeans(adBeans);
							objects = linkBean;

						} else if (ctype.equalsIgnoreCase("imageapp")
								|| ctype.equalsIgnoreCase("textapp")) {

							appBean = new AppBean();

							// for (int l = 0; l < mList.size(); l++) {
							// Element mEl =(Element) mList.get(l);
							List appList = mEl.getChildren("app");
							// System.out.println("元数据中(app)的数目："+appList.size()+"个");
							if (appList != null) {
								for (int m = 0; m < appList.size(); m++) {
									Element appkEl = (Element) appList.get(m);
									String appType = appkEl
											.getChildText("type");
									String appTitle = appkEl
											.getChildText("title");
									String appImage = appkEl
											.getChildText("image");
									String appUrl = appkEl.getChildText("url");
									String appPath = appkEl
											.getChildText("path");
									String appSize = appkEl
											.getChildText("size");
									// System.out.println("--appType-->"+appType);
									// System.out.println("--appTitle-->"+appTitle);
									// System.out.println("--appImage-->"+appImage);
									// System.out.println("--appUrl-->"+appUrl);
									// System.out.println("--appPath-->"+appPath);

									appBean.setTitle(appTitle);
									appBean.setType(appType);
									appBean.setImage(appImage);
									appBean.setUrl(appUrl);
									appBean.setPath(appPath);
									appBean.setSize(appSize);

								}
							}
							// }

							objects = appBean;

						} else if (ctype.equalsIgnoreCase("activity")) {

							actBean = new ActBean();

							// for (int l = 0; l < mList.size(); l++) {
							// Element cEl = (Element) mList.get(l);
							// String cPath = cEl.getText();
							String cPath = mEl.getChildText("path");
							// System.out.println("--cPath-->"+cPath);

							actBean.setPath(cPath);

							// }

							objects = actBean;

						} else if (ctype.equalsIgnoreCase("html5page")) {

							Element pageEl = mEl.getChild("page");
							String pageUrl = pageEl.getChildText("url");
							String pageId = pageEl.getChildText("b");
							String pageName = pageEl.getChildText("t");
							String pageCommentUrl = pageEl.getChildText("m");
							String pageIsAllowComment = pageEl
									.getChildText("p");
							String pageCommentNum = pageEl.getChildText("d");
							String pageLcation = pageEl.getChildText("l");

							newsBean = new NewsBean();
							newsBean.setUrl(pageUrl);
							newsBean.setId(pageId);
							newsBean.setTitle(pageName);
							newsBean.setCommentURL(pageCommentUrl);
							newsBean.setIsAllowComment(pageIsAllowComment);
							newsBean.setCommentNum(pageCommentNum);
							newsBean.setLocation(pageLcation);

							adBeans = new ArrayList<AdBean>();// 初始化广告列表
							List adList = mEl.getChildren("ad");
							// System.out.println("元数据中（广告）的数目："+adList.size()+"个");
							if (adList != null) {
								for (int m = 0; m < adList.size(); m++) {
									Element adEl = (Element) adList.get(m);
									String adType = adEl.getChildText("type");
									String adTitle = adEl.getChildText("title");
									String adImage = adEl.getChildText("image");
									String adUrl = adEl.getChildText("url");
									// System.out.println("--adType-->"+adType);
									// System.out.println("--adTitle-->"+adTitle);
									// System.out.println("--adImage-->"+adImage);
									// System.out.println("--adUrl-->"+adUrl);

									adBean = new AdBean();
									adBean.setTitle(adTitle);
									adBean.setType(adType);
									adBean.setImage(adImage);
									adBean.setUrl(adUrl);
									adBeans.add(adBean);

								}
							}

							newsBean.setAdBeans(adBeans);
							objects = newsBean;

						} else {
							// System.out.println("--!!!!!!-->"+"无此组件类型！！");
							return null;
						}
						fatherModuleBean.setObjects(objects);
						fatherModuleBeans.add(fatherModuleBean);
					}
					fatherBean.setFatherModuleBeans(fatherModuleBeans);
					fatherBeans.add(fatherBean);
				}
				rootBean.setFatherBeans(fatherBeans);
				rootBeans.add(rootBean);
				// System.out.println("--------------------------------");
			}

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return null;
		}

		return rootBeans;
	}

	/**
	 * Old/newer
	 * */
	public static ArrayList<RootBean> parseMainXml(InputStream is) {
		
		RootBean rootBean = null;
		FatherBean fatherBean = null;
		FatherModuleBean fatherModuleBean = null;
		CarouselBean carouselBean = null;
		MulListBean mulListBean = null;
		LinkBean linkBean = null;
		AppBean appBean = null;
		ActBean actBean = null;
		NewsBean newsBean = null;
		AdBean adBean = null;
		MulChildrenBean mulChildrenBean = null;
		
		ArrayList<RootBean> rootBeans = null;
		ArrayList<FatherBean> fatherBeans = null;
		ArrayList<FatherModuleBean> fatherModuleBeans = null;
		Object objects = null;
		ArrayList<AdBean> adBeans = null;
		ArrayList<MulChildrenBean> mulChildrenBeans = null;
		
		try {								
			SAXBuilder saxBuilder = new SAXBuilder(false);
			Document doc = saxBuilder.build(is);
			Element root = doc.getRootElement();
			Element os = root.getChild("os");
			List list = os.getChildren("o");
			// System.out.println("大类别数目："+list.size()+"个");
			
			rootBeans = new ArrayList<RootBean>();// 初始化根目录列表
			
			for (int i = 0; i < list.size(); i++) {
				Element oEl = (Element) list.get(i);
				String title = oEl.getChildText("t");
				String frontImage = oEl.getChildText("fi");
				String backImage = oEl.getChildText("bi");
				String backColor = oEl.getChildText("bc");
				String showType = oEl.getChildText("st");
				// System.out.println("-oel-"+(i+1)+"-->"+title);
				// System.out.println("-oel-"+(i+1)+"-->"+frontImage);
				// System.out.println("-oel-"+(i+1)+"-->"+backImage);
				
				rootBean = new RootBean();// 初始化根目录
				// 给根目录赋值
				rootBean.setName(title);
				rootBean.setFrontImage(frontImage);
				rootBean.setBackImage(backImage);
				rootBean.setBackColor(backColor);
				rootBean.setShowType(showType);
				
				Element dsEl = oEl.getChild("ds");
				List dList = dsEl.getChildren("d");
				// System.out.println("大类别--（"+title+"）下的子分类数目："+dList.size()+"个");
				
				fatherBeans = new ArrayList<FatherBean>();// 初始化子分类列表
				
				for (int j = 0; j < dList.size(); j++) {
					
					fatherBean = new FatherBean();// 初始化子分类对象
					
					Element dEl = (Element) dList.get(j);
					String childTitle = dEl.getChildText("t");
					String childShow = dEl.getChildText("p");
					String fImage = dEl.getChildText("fi");
					String bImage = dEl.getChildText("bi");
					if (BuildConfig.DEBUG) {
						System.out.println("--childTitle-->" + childTitle);
						System.out.println("--childShow-->" + childShow);
						System.out.println("--fImage-->" + fImage);
						System.out.println("--bImage-->" + bImage);
					}
					// 给子分类赋值
					fatherBean.setName(childTitle);
					fatherBean.setIsShow(childShow);
					fatherBean.setFrontImage(fImage);
					fatherBean.setBackColor(bImage);
					
					Element isEl = dEl.getChild("is");
					List iList = isEl.getChildren("i");
					// System.out.println("子分类--（"+childTitle+"）下的组件数目："+iList.size()+"个");
					
					fatherModuleBeans = new ArrayList<FatherModuleBean>();// 初始化子分类下组件类型对象列表
					
					for (int k = 0; k < iList.size(); k++) {
						
						fatherModuleBean = new FatherModuleBean();// 初始化组件类型实体类
						
						Element iEl = (Element) iList.get(k);
						String iTitle = iEl.getChildText("t");
						String ctype = iEl.getChildText("c").trim();// 组件类型很重要，根据不同的类型，元数据会不同。当然，解析方法和展示方法也不同。
						// System.out.println("--iTitle-->"+iTitle);
						// System.out.println("--ctype-->"+ctype);
						
						// 给组件类型实体类赋值
						fatherModuleBean.setName(iTitle);
						fatherModuleBean.setType(ctype);
						
						// List mList = iEl.getChildren("m");
						// System.out.println("组件--（"+iTitle+"）下的元数据数目："+mList.size()+"个");
						
						Element mEl = iEl.getChild("m");
						
						objects = new Object();// 初始化不同组件的存放列表
						
						// 此处，开始根据组件类型，分情况解析
						if (ctype.equalsIgnoreCase("singletextlist")
								|| ctype.equalsIgnoreCase("singleimagetextlist")
								|| ctype.equalsIgnoreCase("carousel")) {
							
							carouselBean = new CarouselBean();// 初始化组件类型1实体类
							
							// for (int l = 0; l < mList.size(); l++) {
							// Element mEl =(Element) mList.get(l);
							String mUrl = mEl.getChildText("url");
							// System.out.println("--mUrl-->"+mUrl);
							
							carouselBean.setUrl(mUrl);// 赋值
							
							List adList = mEl.getChildren("ad");
							// System.out.println("元数据中广告的数目："+adList.size()+"个");
							
							adBeans = new ArrayList<AdBean>();// 初始化广告列表
							
							if (adList != null) {
								for (int m = 0; m < adList.size(); m++) {
									
									Element adEl = (Element) adList.get(m);
									String adType = adEl.getChildText("type");
									String adTitle = adEl.getChildText("title");
									String adImage = adEl.getChildText("image");
									String adUrl = adEl.getChildText("url");
									// System.out.println("--adType-->"+adType);
									// System.out.println("--adTitle-->"+adTitle);
									// System.out.println("--adImage-->"+adImage);
									// System.out.println("--adUrl-->"+adUrl);
									
									adBean = new AdBean();
									adBean.setTitle(adTitle);
									adBean.setType(adType);
									adBean.setImage(adImage);
									adBean.setUrl(adUrl);
									adBeans.add(adBean);
								}
							}
							// }
							
							carouselBean.setAdBeans(adBeans);
							objects = carouselBean;
							
						} else if (ctype.equalsIgnoreCase("multtextlist")
								|| ctype.equalsIgnoreCase("multimagetextlist")) {
							
							mulListBean = new MulListBean();
							mulChildrenBeans = new ArrayList<MulChildrenBean>();
							adBeans = new ArrayList<AdBean>();// 初始化广告列表
							
							// for (int l = 0; l < mList.size(); l++) {
							// Element mEl =(Element) mList.get(l);
							List columnList = mEl.getChildren("column");
							// System.out.println("元数据中(栏目)的数目："+columnList.size()+"个");
							if (columnList != null) {
								for (int m = 0; m < columnList.size(); m++) {
									Element columnEl = (Element) columnList
											.get(m);
									String columnTitle = columnEl
											.getChildText("title");
									String columnUrl = columnEl
											.getChildText("url");
									// System.out.println("--columnTitle-->"+columnTitle);
									// System.out.println("--columnUrl-->"+columnUrl);
									
									mulChildrenBean = new MulChildrenBean();
									mulChildrenBean.setTitle(columnTitle);
									mulChildrenBean.setUrl(columnUrl);
									mulChildrenBeans.add(mulChildrenBean);
									
								}
							}
							List adList = mEl.getChildren("ad");
							// System.out.println("元数据中（广告）的数目："+adList.size()+"个");
							if (adList != null) {
								for (int m = 0; m < adList.size(); m++) {
									Element adEl = (Element) adList.get(m);
									String adType = adEl.getChildText("type");
									String adTitle = adEl.getChildText("title");
									String adImage = adEl.getChildText("image");
									String adUrl = adEl.getChildText("url");
									// System.out.println("--adType-->"+adType);
									// System.out.println("--adTitle-->"+adTitle);
									// System.out.println("--adImage-->"+adImage);
									// System.out.println("--adUrl-->"+adUrl);
									
									adBean = new AdBean();
									adBean.setTitle(adTitle);
									adBean.setType(adType);
									adBean.setImage(adImage);
									adBean.setUrl(adUrl);
									adBeans.add(adBean);
									
								}
							}
							// }
							
							mulListBean.setMulChildrenBeans(mulChildrenBeans);
							mulListBean.setAdBeans(adBeans);
							objects = mulListBean;
							
						} else if (ctype.equalsIgnoreCase("imagelink")
								|| ctype.equalsIgnoreCase("textlink")) {
							
							linkBean = new LinkBean();
							adBeans = new ArrayList<AdBean>();// 初始化广告列表
							
							// for (int l = 0; l < mList.size(); l++) {
							// Element mEl =(Element) mList.get(l);
							List linkList = mEl.getChildren("link");
							// System.out.println("元数据中(link)的数目："+linkList.size()+"个");
							if (linkList != null) {
								for (int m = 0; m < linkList.size(); m++) {
									Element linkEl = (Element) linkList.get(m);
									String linkType = linkEl
											.getChildText("type");
									String linkTitle = linkEl
											.getChildText("title");
									String linkImage = linkEl
											.getChildText("image");
									String linkUrl = linkEl.getChildText("url");
									// System.out.println("--linkType-->"+linkType);
									// System.out.println("--linkTitle-->"+linkTitle);
									// System.out.println("--linkImage-->"+linkImage);
									// System.out.println("--linkUrl-->"+linkUrl);
									
									linkBean.setTitle(linkTitle);
									linkBean.setType(linkType);
									linkBean.setImage(linkImage);
									linkBean.setUrl(linkUrl);
									
								}
							}
							List adList = mEl.getChildren("ad");
							// System.out.println("元数据中（广告）的数目："+adList.size()+"个");
							if (adList != null) {
								for (int m = 0; m < adList.size(); m++) {
									Element adEl = (Element) adList.get(m);
									String adType = adEl.getChildText("type");
									String adTitle = adEl.getChildText("title");
									String adImage = adEl.getChildText("image");
									String adUrl = adEl.getChildText("url");
									// System.out.println("--adType-->"+adType);
									// System.out.println("--adTitle-->"+adTitle);
									// System.out.println("--adImage-->"+adImage);
									// System.out.println("--adUrl-->"+adUrl);
									
									adBean = new AdBean();
									adBean.setTitle(adTitle);
									adBean.setType(adType);
									adBean.setImage(adImage);
									adBean.setUrl(adUrl);
									adBeans.add(adBean);
									
								}
							}
							// }
							
							linkBean.setAdBeans(adBeans);
							objects = linkBean;
							
						} else if (ctype.equalsIgnoreCase("imageapp")
								|| ctype.equalsIgnoreCase("textapp")) {
							
							appBean = new AppBean();
							
							// for (int l = 0; l < mList.size(); l++) {
							// Element mEl =(Element) mList.get(l);
							List appList = mEl.getChildren("app");
							// System.out.println("元数据中(app)的数目："+appList.size()+"个");
							if (appList != null) {
								for (int m = 0; m < appList.size(); m++) {
									Element appkEl = (Element) appList.get(m);
									String appType = appkEl
											.getChildText("type");
									String appTitle = appkEl
											.getChildText("title");
									String appImage = appkEl
											.getChildText("image");
									String appUrl = appkEl.getChildText("url");
									String appPath = appkEl
											.getChildText("path");
									String appSize = appkEl
											.getChildText("size");
									// System.out.println("--appType-->"+appType);
									// System.out.println("--appTitle-->"+appTitle);
									// System.out.println("--appImage-->"+appImage);
									// System.out.println("--appUrl-->"+appUrl);
									// System.out.println("--appPath-->"+appPath);
									
									appBean.setTitle(appTitle);
									appBean.setType(appType);
									appBean.setImage(appImage);
									appBean.setUrl(appUrl);
									appBean.setPath(appPath);
									appBean.setSize(appSize);
									
								}
							}
							// }
							
							objects = appBean;
							
						} else if (ctype.equalsIgnoreCase("activity")) {
							
							actBean = new ActBean();
							
							// for (int l = 0; l < mList.size(); l++) {
							// Element cEl = (Element) mList.get(l);
							// String cPath = cEl.getText();
							String cPath = mEl.getChildText("path");
							// System.out.println("--cPath-->"+cPath);
							
							actBean.setPath(cPath);
							
							// }
							
							objects = actBean;
							
						} else if (ctype.equalsIgnoreCase("html5page")) {
							
							Element pageEl = mEl.getChild("page");
							String pageUrl = pageEl.getChildText("url");
							String pageId = pageEl.getChildText("b");
							String pageName = pageEl.getChildText("t");
							String pageCommentUrl = pageEl.getChildText("m");
							String pageIsAllowComment = pageEl
									.getChildText("p");
							String pageCommentNum = pageEl.getChildText("d");
							String pageLcation = pageEl.getChildText("l");
							
							newsBean = new NewsBean();
							newsBean.setUrl(pageUrl);
							newsBean.setId(pageId);
							newsBean.setTitle(pageName);
							newsBean.setCommentURL(pageCommentUrl);
							newsBean.setIsAllowComment(pageIsAllowComment);
							newsBean.setCommentNum(pageCommentNum);
							newsBean.setLocation(pageLcation);
							
							adBeans = new ArrayList<AdBean>();// 初始化广告列表
							List adList = mEl.getChildren("ad");
							// System.out.println("元数据中（广告）的数目："+adList.size()+"个");
							if (adList != null) {
								for (int m = 0; m < adList.size(); m++) {
									Element adEl = (Element) adList.get(m);
									String adType = adEl.getChildText("type");
									String adTitle = adEl.getChildText("title");
									String adImage = adEl.getChildText("image");
									String adUrl = adEl.getChildText("url");
									// System.out.println("--adType-->"+adType);
									// System.out.println("--adTitle-->"+adTitle);
									// System.out.println("--adImage-->"+adImage);
									// System.out.println("--adUrl-->"+adUrl);
									
									adBean = new AdBean();
									adBean.setTitle(adTitle);
									adBean.setType(adType);
									adBean.setImage(adImage);
									adBean.setUrl(adUrl);
									adBeans.add(adBean);
									
								}
							}
							
							newsBean.setAdBeans(adBeans);
							objects = newsBean;
							
						} else {
							// System.out.println("--!!!!!!-->"+"无此组件类型！！");
							return null;
						}
						fatherModuleBean.setObjects(objects);
						fatherModuleBeans.add(fatherModuleBean);
					}
					fatherBean.setFatherModuleBeans(fatherModuleBeans);
					fatherBeans.add(fatherBean);
				}
				rootBean.setFatherBeans(fatherBeans);
				rootBeans.add(rootBean);
				// System.out.println("--------------------------------");
			}
			
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return null;
		}
		
		return rootBeans;
	}
	
	/** 解析图库目录列表的xml */
	public List<ImageLibBean> parseImageLibXml(String path) {
		List<ImageLibBean> lsnb = null;
		ImageLibBean nb = null;
		try {
			URL url = new URL(path);
			URLConnection uc = url.openConnection();
			uc.setConnectTimeout(Constants.TIME_OUT_MILLISECOND);
			uc.setDoInput(true);
			
			SAXBuilder saxBuilder = new SAXBuilder(false);
			org.jdom.Document doc = saxBuilder.build(uc.getInputStream());
			org.jdom.Element root = doc.getRootElement();
			Element isElement = root.getChild("is");
			List list = isElement.getChildren("i");
			
			lsnb = new ArrayList<ImageLibBean>();
			for (int i = 0; i < list.size(); i++) {
				Element iElement = (Element) list.get(i);
				nb = new ImageLibBean();
				nb.setId(iElement.getChildText("b"));
				nb.setParent(iElement.getChildText("p"));
				nb.setName(iElement.getChildText("t"));
				nb.setUrl(Constants.ACCESS_URL + "/imagelib/" + nb.getId()
						+ "/list_1.xml");
				if (i != 0)
					lsnb.add(nb);
				
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "--parseImageLibXml--" + nb.getName());
					Log.d(TAG, "--parseImageLibXml--" + nb.getUrl());
					Log.d(TAG, "--------------------------------");
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			return null;
		}
		
		return lsnb;
		
	}

	/** 解析某类别下的图片列表xml */
	public List<ImageBean> parseImageXml(String path) {
		List<ImageBean> lsnb = null;
		ImageBean nb = null;
		try {
			URL url = new URL(path);
			URLConnection uc = url.openConnection();
			uc.setConnectTimeout(Constants.TIME_OUT_MILLISECOND);
			uc.setDoInput(true);

			SAXBuilder saxBuilder = new SAXBuilder(false);
			org.jdom.Document doc = saxBuilder.build(uc.getInputStream());
			org.jdom.Element root = doc.getRootElement();
			Element isElement = root.getChild("is");
			String count = root.getChildText("count");
			List list = isElement.getChildren("i");

			lsnb = new ArrayList<ImageBean>();
			for (int i = 0; i < list.size(); i++) {
				Element iElement = (Element) list.get(i);
				nb = new ImageBean();
				nb.setId(iElement.getChildText("b"));
				nb.setAuthor(iElement.getChildText("a"));
				nb.setDesp(iElement.getChildText("o"));
				nb.setImage(Constants.SERVER_URL + iElement.getChildText("i"));
				nb.setLocation(iElement.getChildText("l"));
				nb.setDate(iElement.getChildText("c"));
				nb.setCategory(iElement.getChildText("k"));
				nb.setLikeNum(iElement.getChildText("d"));
				nb.setPageCount(count);

				lsnb.add(nb);
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "--parseImageXml--" + nb.getAuthor());
					Log.d(TAG, "--parseImageXml--" + nb.getImage());
					Log.d(TAG, "--parseImageXml--" + nb.getLikeNum());
					Log.d(TAG, "--------------------------------");
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			return null;
		}

		return lsnb;

	}

	/** 解析新闻列表信息的xml */
	public List<NewsBean> parseNewsXml(List<NewsBean> lsnb, String path) {
		System.out.println("path:" + path);
		if (lsnb == null)
			lsnb = new ArrayList<NewsBean>();
		NewsBean nb = null;
		try {
			URL url = new URL(path);
			URLConnection uc = url.openConnection();
			uc.setConnectTimeout(Constants.TIME_OUT_MILLISECOND);
			uc.setDoInput(true);

			SAXBuilder saxBuilder = new SAXBuilder(false);
			org.jdom.Document doc = saxBuilder.build(uc.getInputStream());
			org.jdom.Element root = doc.getRootElement();
			Element isElement = root.getChild("is");
			String pageNum = root.getChildText("count");
			List list = isElement.getChildren("i");

			// lsnb = new ArrayList<NewsBean>();
			for (int i = 0; i < list.size(); i++) {
				Element iElement = (Element) list.get(i);
				nb = new NewsBean();
				if (pageNum != null)
					nb.setPageNum(pageNum);
				nb.setId(iElement.getChildText("b"));
				nb.setUrl(iElement.getChildText("h"));
				nb.setCommentURL(iElement.getChildText("m"));
				nb.setAuthor(iElement.getChildText("a"));
				nb.setTitle(iElement.getChildText("t"));
				nb.setDesp(iElement.getChildText("o"));
				nb.setSource(iElement.getChildText("s"));
				nb.setImage(iElement.getChildText("i"));
				nb.setVedio(iElement.getChildText("v"));
				nb.setLocation(iElement.getChildText("l"));
				nb.setDate(iElement.getChildText("c"));
				nb.setIsAllowComment(iElement.getChildText("p"));
				nb.setCommentNum(iElement.getChildText("d"));
				lsnb.add(nb);
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "--parseNewsxml--" + nb.getAuthor());
					Log.d(TAG, "--parseNewsxml--" + nb.getDate());
					Log.d(TAG, "--parseNewsxml--" + nb.getDesp());
					Log.d(TAG, "--parseNewsxml--" + nb.getImage());
					Log.d(TAG, "--parseNewsxml--" + nb.getLocation());
					Log.d(TAG, "--parseNewsxml--" + nb.getSource());
					Log.d(TAG, "--parseNewsxml--" + nb.getTitle());
					Log.d(TAG, "--parseNewsxml--" + nb.getUrl());
					Log.d(TAG, "--parseNewsxml--" + nb.getVedio());
					Log.d(TAG, "--------------------------------");
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			return null;
		}

		return lsnb;

	}

	/** 解析评论列表信息的xml */
	public List<CommentBean> parseCommentXml(String path) {

		List<CommentBean> lsnb = null;
		CommentBean nb = null;
		try {
			URL url = new URL(path);
			URLConnection uc = url.openConnection();
			uc.setConnectTimeout(Constants.TIME_OUT_MILLISECOND);
			uc.setDoInput(true);

			SAXBuilder saxBuilder = new SAXBuilder(false);
			org.jdom.Document doc = saxBuilder.build(uc.getInputStream());
			org.jdom.Element root = doc.getRootElement();

			List list = root.getChildren("i");

			lsnb = new ArrayList<CommentBean>();
			for (int i = 0; i < list.size(); i++) {
				Element iElement = (Element) list.get(i);
				nb = new CommentBean();
				nb.setAuthor(iElement.getChildText("a"));
				nb.setCommentContent(iElement.getChildText("o"));
				nb.setCommentTime(iElement.getChildText("c"));
				nb.setCommentLoction(iElement.getChildText("l"));
				lsnb.add(nb);
				// if(BuildConfig.DEBUG){
				// Log.d(TAG, "--parseCommentXml--"+nb.getAuthor());
				// Log.d(TAG, "--parseCommentXml--"+nb.getCommentContent());
				// Log.d(TAG, "--parseCommentXml--"+nb.getCommentLoction());
				// Log.d(TAG, "--parseCommentXml--"+nb.getCommentTime());
				// Log.d(TAG, "--------------------------------");
				// }
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			return null;
		}

		return lsnb;

	}

	/** 解析图片轮播列表信息的xml */
	public List<NewsBean> parseCarouselXml(CarouselBean cb) {
		List<NewsBean> lsnb = null;
		NewsBean nb = null;
		try {
			URL url = new URL(Constants.SERVER_URL + cb.getUrl());
			URLConnection uc = url.openConnection();
			uc.setConnectTimeout(Constants.TIME_OUT_MILLISECOND);
			uc.setDoInput(true);

			SAXBuilder saxBuilder = new SAXBuilder(false);
			org.jdom.Document doc = saxBuilder.build(uc.getInputStream());
			org.jdom.Element root = doc.getRootElement();
			List list = root.getChildren("i");

			lsnb = new ArrayList<NewsBean>();
			for (int i = 0; i < list.size(); i++) {
				Element iElement = (Element) list.get(i);
				nb = new NewsBean();
				nb.setId(iElement.getChildText("b"));
				nb.setUrl(iElement.getChildText("h"));
				nb.setCommentURL(iElement.getChildText("m"));
				nb.setImage(iElement.getChildText("s"));
				nb.setTitle(iElement.getChildText("t"));
				nb.setIsAllowComment(iElement.getChildText("p"));
				nb.setCommentNum(iElement.getChildText("d"));
				lsnb.add(nb);
				// if(BuildConfig.DEBUG){
				// Log.d(TAG, "--parseCarouselXml--"+nb.getImage());
				// Log.d(TAG, "--parseCarouselXml--"+nb.getTitle());
				// Log.d(TAG, "--parseCarouselXml--"+nb.getUrl());
				// Log.d(TAG, "--------------------------------");
				// }
			}

			ArrayList<AdBean> adBeans = cb.getAdBeans();
			for (int i = 0; i < adBeans.size(); i++) {
				nb = new NewsBean();
				nb.setTitle(adBeans.get(i).getTitle());
				nb.setImage(adBeans.get(i).getImage());
				nb.setUrl(adBeans.get(i).getUrl());
				lsnb.add(nb);
				// if(BuildConfig.DEBUG){
				// Log.d(TAG, "--parseCarouselXml--"+nb.getImage());
				// Log.d(TAG, "--parseCarouselXml--"+nb.getTitle());
				// Log.d(TAG, "--parseCarouselXml--"+nb.getUrl());
				// Log.d(TAG, "--------------------------------");
				// }
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			return null;
		}

		return lsnb;

	}

	/** 解析便民电话类别xml */
	public List<TelCategoryBean> parseXmlBMDH(String path)
			throws JDOMException, IOException {
		List<TelCategoryBean> lslzh = null;
		URL url = new URL(path);
		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(Constants.TIME_OUT_MILLISECOND);
		conn.setDoInput(true);

		SAXBuilder saxBuilder = new SAXBuilder(false);
		org.jdom.Document doc = saxBuilder.build(conn.getInputStream());
		org.jdom.Element root = doc.getRootElement();
		List list = root.getChildren("items");
		// System.out.println("-items-1-"+list.size());
		lslzh = new ArrayList<TelCategoryBean>();
		for (int i = 0; i < list.size(); i++) {
			org.jdom.Element el = (org.jdom.Element) list.get(i);
			List item = el.getChildren();
			// System.out.println("-item-8-"+item.size());
			for (int j = 0; j < item.size(); j++) {
				org.jdom.Element lzh = (org.jdom.Element) item.get(j);
				List ls = lzh.getChildren();
				TelCategoryBean lzhb = new TelCategoryBean();
				// System.out.println("-ls-6-"+ls.size());
				lzhb.setTitle(((org.jdom.Element) ls.get(0)).getValue());
				lzhb.setLink(((org.jdom.Element) ls.get(1)).getValue());
				lslzh.add(lzhb);
			}

		}
		// for (int i = 0; i < lslzh.size(); i++) {
		// System.out.println(i+"----"+lslzh.get(i).getTitle());
		// System.out.println(i+"----"+lslzh.get(i).getLink());
		// }

		return lslzh;

	}

	/** 解析便民电话号码xml */
	public List<TelNumBean> parseXmlBMTelNum(String path) throws JDOMException,
			IOException {
		List<TelNumBean> lslzh = null;
		URL url = new URL(path);
		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(Constants.TIME_OUT_MILLISECOND);
		conn.setDoInput(true);

		SAXBuilder saxBuilder = new SAXBuilder(false);
		org.jdom.Document doc = saxBuilder.build(conn.getInputStream());
		org.jdom.Element root = doc.getRootElement();
		List list = root.getChildren("items");
		// System.out.println("-items-1-"+list.size());
		lslzh = new ArrayList<TelNumBean>();
		for (int i = 0; i < list.size(); i++) {
			org.jdom.Element el = (org.jdom.Element) list.get(i);
			List item = el.getChildren();
			// System.out.println("-item-8-"+item.size());
			for (int j = 0; j < item.size(); j++) {
				org.jdom.Element lzh = (org.jdom.Element) item.get(j);
				List ls = lzh.getChildren();
				TelNumBean lzhb = new TelNumBean();
				// System.out.println("-ls-6-"+ls.size());
				lzhb.setTitle(((org.jdom.Element) ls.get(0)).getValue());
				lzhb.setNum(((org.jdom.Element) ls.get(1)).getValue());
				lslzh.add(lzhb);
			}

		}
		// for (int i = 0; i < lslzh.size(); i++) {
		// System.out.println(i+"----"+lslzh.get(i).getTitle());
		// System.out.println(i+"----"+lslzh.get(i).getNum());
		// }

		return lslzh;

	}

	/** 解析投票和问卷调查xml */
	public ArrayList<VoteBean> parseVoteSurvey(String path) {
		ArrayList<VoteBean> voteBeans = new ArrayList<VoteBean>();
		VoteBean vb = null;
		try {
			URL url = new URL(path);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(Constants.TIME_OUT_MILLISECOND);
			SAXBuilder builder = new SAXBuilder(false);
			Document doc = builder.build(conn.getInputStream());
			Element rootEl = doc.getRootElement();
			Element ele = rootEl.getChild("is");
			List list = ele.getChildren("i");
			for (int i = 0; i < list.size(); i++) {
				vb = new VoteBean();
				Element root = (Element) list.get(i);
				vb.setUrl(root.getChildText("h"));
				vb.setAuthor(root.getChildText("a"));
				vb.setName(root.getChildText("t"));
				vb.setStart(root.getChildText("s"));
				vb.setEnd(root.getChildText("e"));
				voteBeans.add(vb);
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "--vote:" + vb.getUrl());
					Log.d(TAG, "--vote:" + vb.getAuthor());
					Log.d(TAG, "--vote:" + vb.getTime());
					Log.d(TAG, "--vote:" + vb.getStart());
					Log.d(TAG, "--vote:" + vb.getEnd());
				}
			}
			

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return voteBeans;

	}

}
