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

	public final int R	;	// �돽
	public static byte
		RIGHTBOTTOM=1,
		CENTERBOTTOM=2,
		LEFTBOTTOM=3,
		LEFTCENTER=4,
		LEFTTOP=5,
		CENTERTOP=6,
		RIGHTTOP=7,
		RIGHTCENTER=8;
	
	private int pc; //λ�ô�̖
	private ViewGroup clayout; //��laoyout
	private final int amount; //�Ў׶������o
	private double fullangle=180.0;//�ڎ״���Ƕȃ��Ł�
	private byte xOri=1,yOri=1;  //x��yֵ�����򣬼�ϵ����߀������

	
	/**
	  * ���캯��
	  * @param comlayout �����������o��layout
	  * @param poscode λ�ô�̖���քe����RIGHTBOTTOM��CENTERBOTTOM��LEFTBOTTOM��LEFTCENTER��LEFTTOP��CENTERTOP��RIGHTTOP��RIGHTCENTER
	  * @param radius �돽
	*/
	public MyAnimations(ViewGroup comlayout,int poscode,int radius){
		this.pc=poscode;
		this.clayout=comlayout;
		this.amount=clayout.getChildCount();
		this.R=radius;
		if(poscode==RIGHTBOTTOM){      //���½�
			fullangle=90;
			xOri=-1;yOri=-1;
		}else if(poscode==CENTERBOTTOM){//����
			fullangle=180;
			xOri=-1;yOri=-1;
		}else if(poscode==LEFTBOTTOM){  //���½�
			fullangle=90;
			xOri=1;yOri=-1;
		}else if(poscode==LEFTCENTER){  //����
			fullangle=180;
			xOri=1;yOri=-1;
		}else if(poscode==LEFTTOP){     //���Ͻ�
			fullangle=90;
			xOri=1;yOri=1;
		}else if(poscode==CENTERTOP){   //����
			fullangle=180;
			xOri=-1;yOri=1;
		}else if(poscode==RIGHTTOP){    //���Ͻ�
			fullangle=90;
			xOri=-1;yOri=1;
		}else if(poscode==RIGHTCENTER){ //����
			fullangle=180;
			xOri=-1;yOri=-1;
		}
	}


	/**
	  * ���ׂ����o����
	  * @param durationMillis �Î׶��r�g
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
			 * ����ؾ��true�����ڌ��Hafter��λ�ÂS������λ��Ӌ�������������λ��׃�������L��һ����
			 * �����Ԍ�animation��λ��ҕ��Ӱ��λ�á��_ʼ�Ȇ���Ӱ��λ��ȥx������λ���Ԇ�0���؂��r��animation�Y�����͌�����λ���Ƶ�x�����set��fillafter������Ӱ�ӌ������λ�����ƶ�x������Ӱ��λ�Õ�׃��2x�������ض�һ�������true����
			 * ������Sfalse��Ԓ����Ӌ���animationEndͬafter�����ٕr�g������F�����Wһ�W��Ӱ����΢��2x��λ�ó��Fһ˲�g��ȥ��x���䌍Ӱ푶�������㲿�C���������������������ْ߻҉m���̺�׆����X�����}�K�ӽ�Q��
			 * ��ֻ�Џ������r�����؂����}�����������r��ֱ���O��visible��gone��
			 animation.setFillAfter(true);
			*/
			animation.setFillEnabled(true);//�Ӆ��ؾ�S�����������Wһ�W��������animation��Ӱ��λ�á�����layoutParams������λ��~�طN���������؂�ͬ��ͬ�������}��������settimeout��ȥ������λ�á�����һ��Ҫ��x���x����1��ȥ��ɣ�ÿ��0.1��͌�������λ�ø�׃x/10����
			animation.setDuration(durationMillis);
			animation.setStartOffset((i * 100) / (-1 + clayout.getChildCount()));
			animation.setInterpolator(new OvershootInterpolator(2F));

			//��
			inoutimagebutton.startAnimation(animation);
			//����͸����button�����Hλ�ã�animationֻϵ���ȥ���ڶȣ����Hλ����Ȼ�շ��f�r��ȣ�
			//���animation�S�����������ֱ��start��animate��set���o��λ�ã����п��܁�δ������w���^ȥ
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
	  * ����ׂ����o��ȥ
	  * @param durationMillis �Î׶��r�g
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
			animation.setStartOffset(((clayout.getChildCount() - i) * 100) / (-1 + clayout.getChildCount()));// ˳��һ�±Ƚ����
			animation.setInterpolator(new AnticipateInterpolator(2F));
			//��
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
	  * �@ȡλ�ô��a���䌍ò�ƶ���ؿ�ã�
	*/
	public int getPosCode(){
		return this.pc;
	}

	/*
	 *�f�Ѓ��ݣ�����ԭ����������ؼ�������λ�ã���Ҫģ�M�����c����layout�r��λ�ã����Ԓ��onclicklistener�����^�韩
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
	  * ���D������ԭ�����І��o�B������δ���w���������{�ã�
	  * @param fromDegrees �Ď׶��
	  * @param toDegrees ���׶��
	  * @param durationMillis �D����
	*/
	public static Animation getRotateAnimation(float fromDegrees, float toDegrees, int durationMillis) {
		RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(durationMillis);
		rotate.setFillAfter(true);
		return rotate;
	}
	
	
	
}