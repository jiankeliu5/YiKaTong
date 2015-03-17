package com.seedsoft.ykt.widget;


import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MyAnimations {

	public final int R	;	// 半徑
	public static byte
		RIGHTBOTTOM=1,
		CENTERBOTTOM=2,
		LEFTBOTTOM=3,
		LEFTCENTER=4,
		LEFTTOP=5,
		CENTERTOP=6,
		RIGHTTOP=7,
		RIGHTCENTER=8;
	
	private int pc; //位置代號
	private ViewGroup clayout; //父laoyout
	private final int amount; //有幾多個按鈕
	private double fullangle=180.0;//在幾大嘅角度內排佈
	private byte xOri=1,yOri=1;  //x、y值嘅方向，即系向上還是向下

	
	/**
	  * 構造函數
	  * @param comlayout 包裹彈出按鈕嘅layout
	  * @param poscode 位置代號，分別對應RIGHTBOTTOM、CENTERBOTTOM、LEFTBOTTOM、LEFTCENTER、LEFTTOP、CENTERTOP、RIGHTTOP、RIGHTCENTER
	  * @param radius 半徑
	*/
	public MyAnimations(ViewGroup comlayout,int poscode,int radius){
		this.pc=poscode;
		this.clayout=comlayout;
		this.amount=clayout.getChildCount();
		this.R=radius;
		if(poscode==RIGHTBOTTOM){      //右下角
			fullangle=90;
			xOri=-1;yOri=-1;
		}else if(poscode==CENTERBOTTOM){//中下
			fullangle=180;
			xOri=-1;yOri=-1;
		}else if(poscode==LEFTBOTTOM){  //左下角
			fullangle=90;
			xOri=1;yOri=-1;
		}else if(poscode==LEFTCENTER){  //左中
			fullangle=180;
			xOri=1;yOri=-1;
		}else if(poscode==LEFTTOP){     //左上角
			fullangle=90;
			xOri=1;yOri=1;
		}else if(poscode==CENTERTOP){   //中上
			fullangle=180;
			xOri=-1;yOri=1;
		}else if(poscode==RIGHTTOP){    //右上角
			fullangle=90;
			xOri=-1;yOri=1;
		}else if(poscode==RIGHTCENTER){ //右中
			fullangle=180;
			xOri=-1;yOri=-1;
		}
	}


	/**
	  * 彈幾個按鈕出嚟
	  * @param durationMillis 用幾多時間
	*/
	public void startAnimationsIn(int durationMillis) {
		/*
		List<TouchObject> preTouch = preTouch(clayout);
		composerLayout layout = (composerLayout) clayout;
		if (!layout.isInit())
			layout.initTouches(preTouch);
		layout.setShow(true);		
		*/
		for (int i = 0; i < clayout.getChildCount(); i++) {
			final LinearLayout inoutimagebutton = (LinearLayout) clayout.getChildAt(i);			
			double offangle=fullangle/(amount-1);
			//double marginTop = Math.sin(offangle * i * Math.PI / 180) * R;
			//double marginRight = Math.cos(offangle * i * Math.PI / 180) * R;
			final double deltaY,deltaX;
			if(pc==LEFTCENTER || pc==RIGHTCENTER){
				deltaX = Math.sin(offangle * i * Math.PI / 180) * R;
				deltaY = Math.cos(offangle * i * Math.PI / 180) * R;		
			}else{
				deltaY = Math.sin(offangle * i * Math.PI / 180) * R;
				deltaX = Math.cos(offangle * i * Math.PI / 180) * R;
			}
			inoutimagebutton.setVisibility(View.VISIBLE);

			Animation animation = new TranslateAnimation(0, (float)(xOri* deltaX), 0, (float)(yOri* deltaY));
			/*
			 * 如果呢句為true，因為佢實際after嘅位置係從物理位置計起，最後因為物理位置變化咗而長咗一倍；
			 * （可以將animation嘅位置視為影子位置。開始先喐咗影子位置去x，物理位置仍喺0。呢個時候animation結束，就將物理位置移到x。由於set咗fillafter，所以影子對於物理位置又移多x，所以影子位置會變咗2x。所以呢度一定唔可以true嘅）
			 * 但如果係false嘅話，估計因為animationEnd同after有少少時間差，而出現最後閃一閃，影子稍微喺2x嘅位置出現一瞬間又去返x。其實影響都唔算大，你部機可能睇唔倒添。但好似有少少掃灰塵入地毯底嘅感覺，問題並冇解決。
			 * （只有彈出嘅時候有呢個問題，因為收埋嘅時候直接設咗visible做gone）
			 animation.setFillAfter(true);
			*/
			animation.setFillEnabled(true);//加咗呢句嘢，最後就唔會閃一閃。（先用animation移影子位置、再用layoutParams喐物理位置~呢種方法就有呢個同唔同步嘅問題，除非你settimeout咁去改物理位置。例如一共要移x距離，用1秒去完成，每隔0.1秒就將佢物理位置改變x/10。）
			animation.setDuration(durationMillis);
			animation.setStartOffset((i * 100) / (-1 + clayout.getChildCount()));
			animation.setInterpolator(new OvershootInterpolator(2F));

			//喐
			inoutimagebutton.startAnimation(animation);
			//喐完就改埋嗰啲button嘅實際位置（animation只系外表去咗第度，實際位置依然喺返舊時嗰度）
			//因為animation係異步嘅，如果直接start完animate就set按鈕嘅位置，就有可能佢未喐完就飛咗過去
			animation.setAnimationListener(new AnimationListener() {				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub					
				}				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub					
				}				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub					
					int l=inoutimagebutton.getLeft();
		            int t=inoutimagebutton.getTop();
		            RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
		          				LinearLayout.LayoutParams.WRAP_CONTENT,
		          				LinearLayout.LayoutParams.WRAP_CONTENT
		          	);
		            lps.leftMargin=(int)(l+xOri* deltaX);
		            lps.topMargin =(int)(t+yOri* deltaY);
		            //System.out.println("oldleft: "+l+"   leftmargin: "+lps.leftMargin);
		            //System.out.println("oldtop:  "+t+"   topmargin:  "+lps.topMargin);
		            inoutimagebutton.setLayoutParams(lps);
				}
			});

		}
	}
	
	/**
	  * 收埋幾個按鈕入去
	  * @param durationMillis 用幾多時間
	*/
	public void startAnimationsOut( int durationMillis) {
		for (int i = 0; i < clayout.getChildCount(); i++) {
			final LinearLayout inoutimagebutton = (LinearLayout) clayout.getChildAt(i);
			
			double offangle=fullangle/(amount-1);
			final double deltaY,deltaX;
			if(pc==LEFTCENTER || pc==RIGHTCENTER){
				deltaX = Math.sin(offangle * i * Math.PI / 180) * R;
				deltaY = Math.cos(offangle * i * Math.PI / 180) * R;		
			}else{
				deltaY = Math.sin(offangle * i * Math.PI / 180) * R;
				deltaX = Math.cos(offangle * i * Math.PI / 180) * R;
			}
			
			Animation animation = new TranslateAnimation(0,-(float)(xOri*deltaX), 0,  -(float)(yOri*deltaY));
			//animation.setFillAfter(true);
			animation.setDuration(durationMillis);
			animation.setStartOffset(((clayout.getChildCount() - i) * 100) / (-1 + clayout.getChildCount()));// 顺序倒一下比较舒服
			animation.setInterpolator(new AnticipateInterpolator(2F));
			//喐
			inoutimagebutton.startAnimation(animation);
			animation.setAnimationListener(new AnimationListener() {				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub					
				}				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub					
				}				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub					
					int l=inoutimagebutton.getLeft();
		            int t=inoutimagebutton.getTop();
		            RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
		          				LinearLayout.LayoutParams.WRAP_CONTENT,
		          				LinearLayout.LayoutParams.WRAP_CONTENT
		          	);
		            lps.leftMargin=(int)(l- xOri* deltaX);
		            lps.topMargin =(int)(t- yOri* deltaY);
		            //System.out.println("leftmargin: "+lps.leftMargin);
		            //System.out.println("topmargin:  "+lps.topMargin);
		            inoutimagebutton.setLayoutParams(lps);
		            inoutimagebutton.setVisibility(View.GONE);
				}
			});
		}

	}
	
	/**
	  * 獲取位置代碼（其實貌似都冇乜用）
	*/
	public int getPosCode(){
		return this.pc;
	}

	/*
	 *舊有內容，因為佢原來冇真正喐控件嘅物理位置，就要模擬返出點擊父layout時嘅位置，咁嘅話做onclicklistener會比較麻煩
	private List<TouchObject> preTouch(ViewGroup viewgroup) {
		List<TouchObject> objects = new ArrayList<TouchObject>();
		for (int i = 0; i < viewgroup.getChildCount(); i++) {
			final LinearLayout inoutimagebutton = (LinearLayout) viewgroup.getChildAt(i);
			double marginTop = Math.sin(36.0 * i * Math.PI / 180) * R;
			double marginRight = Math.cos(36.0 * i * Math.PI / 180) * R;
			Point point = new Point((int) marginRight, (int) marginTop);
			Rect animationRect = myAnimations.getAnimationRect(inoutimagebutton, point);
			TouchObject obj = new TouchObject();
			obj.setTouchId(inoutimagebutton.getId());
			obj.setTouchArea(animationRect);
			objects.add(obj);

		}
		return objects;
	}

	public static Rect getAnimationRect(LinearLayout btn, Point point) {
		Rect r = new Rect();
		r.left = btn.getLeft() - point.x;
		r.top = btn.getTop() - point.y;
		r.right = btn.getRight() - point.x;
		r.bottom = btn.getBottom() - point.y;
		return r;
	}
	*/

	
	
	
	
	/**
	  * 自轉函數（原本就有嘅靜態函數，未實體化都可以調用）
	  * @param fromDegrees 從幾多度
	  * @param toDegrees 到幾多度
	  * @param durationMillis 轉幾耐
	*/
	public static Animation getRotateAnimation(float fromDegrees, float toDegrees, int durationMillis) {
		RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(durationMillis);
		rotate.setFillAfter(true);
		return rotate;
	}
	
	
	
}