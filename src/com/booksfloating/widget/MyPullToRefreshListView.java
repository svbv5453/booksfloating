package com.booksfloating.widget;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xd.booksfloating.R;

/**
 * Created by Administrator on 2016/5/25.
 */
public class MyPullToRefreshListView extends ListView{

	 private Context mContext;
	    private ProgressBar headerPB = null;
	    private ProgressBar footerPB = null;
	    private TextView headerMsg = null;
	    private TextView headerTime = null;
	    private TextView footerMsg = null;
	    private ImageView headerArrow = null;

	    private MyOnRefreshListener mListener;

	    /**
	     * 定义下拉刷新的四种状态
	     * @param context
	     * @param attrs
	     */
	    public final int STATUS_PULL_TO_REFRESH = 0;
	    public final int STATUS_RELEASE_TO_REFRESH = 1;
	    public final int STATUS_REFRESHING = 2;
	    public final int STATUS_REFRESH_FINSHED = 3;
	    //下拉刷新的当前状态默认是下拉刷新
	    public int STATUS_CURRENT = STATUS_PULL_TO_REFRESH;
	    //当前的滚动状态
	    private int STATUS_SCROLL;

	    //判断是否滚到了底部
	    private boolean isScrolToBottom;

	    //判断是否正在加载更多
	    public boolean isLoadingMore;
	    /**
	     * 头部布局的高度
	     * 尾部布局的高度
	     * @param context
	     * @param attrs
	     */
	    private int headerHeight;
	    private int footerHeight;
	    //头部视图和尾部视图
	    private View headerView;
	    private View footerView;

	    /**
	     * 下拉箭头的两种选中，向下，和向上
	     * @param context
	     * @param attrs
	     */
	    private Animation upAnimation;
	    private Animation downAnimation;

	    /**
	     * 手指按下的高度
	     * @param context
	     * @param attrs
	     */
	    private int downY;

	    public MyPullToRefreshListView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        this.mContext = context;

	        initHeader();
	        initFooter();
	        //当滚动到底部加载更多
	        this.setOnScrollListener(new OnScrollListener() {
	            //当滚动状态改变的时候调用
	            @Override
	            public void onScrollStateChanged(AbsListView view, int scrollState) {

	                //如果滚动事件结束，或者持续滚动
	                if(STATUS_SCROLL == SCROLL_STATE_IDLE || STATUS_SCROLL == SCROLL_STATE_FLING){
	                    //判断是否到了底部并且是否正在加载更多
	                    if(isScrolToBottom && !isLoadingMore){
	                        isLoadingMore = true;
	                        footerView.setPadding(0, 0, 0, 0);

	                        /**
	                         * 这一步是干什么的
	                         * 当加载时停留在停留在底部
	                         */
	                        setSelection(getCount());
	                        if(mListener != null){
	                            mListener.myOnLoadingMore();
	                        }
	                    }
	                }

	            }

	            //当滚动的时候调用
	            @Override
	            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


	                //判断是否到了底部,当最后一个item可见时，说明到了底部
	                if(getLastVisiblePosition() == totalItemCount - 1){
	                    isScrolToBottom = true;
	                }else {
	                    isScrolToBottom = false;
	                }
	            }
	        });

	    }

	    /**
	     * 设置刷新事件监听，供外部调用
	     * @param listener
	     */
	    public void setOnRefreshListener(MyOnRefreshListener listener){
	        this.mListener = listener;
	    }

	    @Override
	    public boolean onTouchEvent(MotionEvent ev) {

	        switch (ev.getAction()){
	            case MotionEvent.ACTION_DOWN:
	                downY = (int) ev.getY();
	                break;
	            case MotionEvent.ACTION_MOVE:
	                int moveY = (int) ev.getY();
	                //手指移动的距离
	                int dis = (moveY - downY) / 2;
	                //-头部布局的高度+间距=paddingTop
	                int paddingTop = -headerHeight + dis;
	                /**
	                 * 手指向下滑动
	                 */
	                if(getFirstVisiblePosition() == 0 && -headerHeight < paddingTop){
	                    if(paddingTop > 0){
	                        //下拉头完全显示，当前状态变为释放刷新
	                        STATUS_CURRENT = STATUS_RELEASE_TO_REFRESH;
	                        refreshHeaderView();

	                    }else {
	                        //下拉头不完全显示，当前状态为下拉刷新
	                        STATUS_CURRENT = STATUS_PULL_TO_REFRESH;
	                        refreshHeaderView();
	                    }
	                    headerView.setPadding(0, paddingTop, 0, 0);
	                    return true;



	                }
	                break;
	            case MotionEvent.ACTION_UP:
	                //判断当前状态是释放刷新还是下拉刷新
	                if(STATUS_CURRENT == STATUS_RELEASE_TO_REFRESH){
	                    //如果当前状态是释放刷新，则将刷新头完全显示，将刷新状态设置为正在刷新，并设置回调接口，供处理刷新事件
	                    headerView.setPadding(0, 0, 0, 0);
	                    STATUS_CURRENT = STATUS_REFRESHING;
	                    refreshHeaderView();
	                    if(mListener != null){
	                        mListener.myOnDownPullRefresh();
	                    }

	                }else if(STATUS_CURRENT == STATUS_PULL_TO_REFRESH){
	                    //如果当前状态是释放刷新，则显示当前刷新头高度,
	                    headerView.setPadding(0, -headerHeight, 0, 0);
	                }
	                break;
	            default:
	                break;
	        }
	        return super.onTouchEvent(ev);

	    }

	    private void refreshHeaderView() {
	        /**
	         * 判断当前状态
	         */
	        switch (STATUS_CURRENT){
	            case STATUS_PULL_TO_REFRESH:
	                headerMsg.setText(getResources().getText(R.string.pull_to_refresh));
	                headerArrow.startAnimation(downAnimation);
	                break;
	            case STATUS_RELEASE_TO_REFRESH:
	                headerMsg.setText(getResources().getText(R.string.release_to_refresh));
	                headerArrow.startAnimation(upAnimation);
	                break;
	            case STATUS_REFRESHING:
	                headerArrow.clearAnimation();
	                headerArrow.setVisibility(View.GONE);
	                headerPB.setVisibility(View.VISIBLE);
	                headerMsg.setText(getResources().getText(R.string.refreshing));

	                break;
	            default:
	                break;

	        }
	    }

	    private void initHeader() {

	        headerView = LayoutInflater.from(mContext).inflate(R.layout.listview_header, null);
	        headerArrow = (ImageView) headerView.findViewById(R.id.iv_prt_arrow);
	        headerPB = (ProgressBar) headerView.findViewById(R.id.pb_prt_header);
	        headerMsg = (TextView) headerView.findViewById(R.id.tv_prt_headerMsg);
	        headerTime = (TextView) headerView.findViewById(R.id.tv_prt_time);

	        //设置最后刷新事件
	        headerTime.setText("最后刷新时间:" + getLastUpdateTime());
	        headerView.measure(0, 0);
	        headerHeight = headerView.getMeasuredHeight();
	        headerView.setPadding(0, -headerHeight, 0, 0);
	        this.addHeaderView(headerView);

	        initAnimation();
	    }

	    private void initAnimation() {
	        /**
	         *箭头两种动画状态，向下旋转，向上旋转
	         */
	        upAnimation = new RotateAnimation(0f, -180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	        upAnimation.setDuration(500);
	        upAnimation.setFillAfter(true);

	        downAnimation = new RotateAnimation(-180f, -360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	        downAnimation.setDuration(500);
	        downAnimation.setFillAfter(true);
	    }

	    private void initFooter() {
	        footerView = LayoutInflater.from(mContext).inflate(R.layout.sh_listview_footer, null);
	        footerPB = (ProgressBar) footerView.findViewById(R.id.pb_prt_footer);
	        footerMsg = (TextView) footerView.findViewById(R.id.tv_prt_footerMsg);
	        footerView.measure(0, 0);
	        footerHeight = footerView.getMeasuredHeight();
	        footerView.setPadding(0, -footerHeight, 0, 0);
	        this.addFooterView(footerView);
	    }

	    /**
	     * 获取系统当前时间
	     * @return
	     */
	    public String getLastUpdateTime(){
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
	        return sdf.format(System.currentTimeMillis());
	    }

	    /**
	     * 最后需要手动隐藏头布局和尾部局
	     */
	    public void hideHeaderView(){
	        headerView.setPadding(0, -headerHeight, 0, 0);
	        headerArrow.setVisibility(VISIBLE);
	        headerPB.setVisibility(GONE);
	        headerMsg.setText(getResources().getText(R.string.pull_to_refresh));
	        headerTime.setText("上次刷新时间" + getLastUpdateTime());
	        STATUS_CURRENT = STATUS_PULL_TO_REFRESH;
	    }
	    public void hideFooterView(){
	        footerView.setPadding(0, -footerHeight, 0 ,0);
	        isLoadingMore = false;
	    }



	    /**
	     * 定义接口
	     */
	    public interface MyOnRefreshListener{
	        /**
	         * 下拉刷新
	         */
	        void myOnDownPullRefresh();

	        /**
	         * 上拉加载更多
	         */
	        void myOnLoadingMore();
	    }
}
