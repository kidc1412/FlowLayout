package com.kidc.flowlayouttest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;



public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int mWidth = resolveSize(0, widthMeasureSpec);

        int mPaddingLeft = getPaddingLeft();
        int mPaddingRight = getPaddingRight();
        int mPaddingTop = getPaddingTop();
        int mPaddingBottom = getPaddingBottom();

        // 父控件的padding宽高
        int paddingWidth = mPaddingLeft + mPaddingRight;
        int paddingHeight = mPaddingTop + mPaddingBottom;

		// 行高
        int lineHeight = 0;
        int lineWidth = paddingWidth;
		// 叠加换行时候的行高
        int nextHeight = mPaddingTop;

        // 子控件数
        int cCount = getChildCount();
        // 遍历子控件
        for (int i = 0; i < cCount; i++){
            // 获取每个子控件
            View childView = getChildAt(i);
            // 判断子控件的状态
            if (childView.getVisibility() == View.GONE)
                continue;

            // 获取子控件的margin
            MarginLayoutParams childMarginlp = (MarginLayoutParams) childView.getLayoutParams();
            int childMarginW = childMarginlp.leftMargin + childMarginlp.rightMargin;
            int childMarginH = childMarginlp.topMargin + childMarginlp.bottomMargin;

            // 将父控件的padding和子控件自身的margin传入测量
            childView.measure(
                    getChildMeasureSpec(widthMeasureSpec, paddingWidth + childMarginW, childMarginlp.width),
                    getChildMeasureSpec(heightMeasureSpec, paddingHeight + childMarginH, childMarginlp.height)
            );

            // 子控件测量到的高宽
            int childMeasureW = childView.getMeasuredWidth();
            int childMeasureH = childView.getMeasuredHeight();

            // 子控件实际占用的宽高空间
            childMarginW += childMeasureW;
            childMarginH += childMeasureH;


            // 取子控件最高值为行高
            lineHeight = Math.max(lineHeight, childMarginH);
            // 判断子控件横向叠加的宽度是否大于父控件 大于则换行
            if (lineWidth + childMarginW > mWidth){
                nextHeight += lineHeight;
                lineHeight = childMarginH;
                lineWidth = paddingWidth;
            }
            // 子控件数横向叠加没有大于父控件则继续叠加
            lineWidth += childMarginW;
        }

        int mHeight = nextHeight + lineHeight + mPaddingBottom;
        setMeasuredDimension(mWidth, resolveSize(mHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int mWidth = i2 - i;

        int mPaddingLeft = getPaddingLeft();
        int mPaddingRight = getPaddingRight();
        int mPaddingTop = getPaddingTop();

        int paddingWidth = mPaddingLeft + mPaddingRight;

        int lineHeight = 0;
        int lineWidth = paddingWidth;
        int nextHeight = mPaddingTop;
        int paddingLeft = mPaddingLeft;

        int cCount = getChildCount();
        for (int j = 0; j < cCount; j++){
            View childView = getChildAt(j);
            if (childView.getVisibility() == View.GONE)
                continue;

            MarginLayoutParams childMarginlp = (MarginLayoutParams) childView.getLayoutParams();
            int childMarginW = childMarginlp.leftMargin + childMarginlp.rightMargin;
            int childMarginH = childMarginlp.topMargin + childMarginlp.bottomMargin;
            int childMarginL = childMarginlp.leftMargin;
            int childMarginT = childMarginlp.topMargin;

            int childMeasureW = childView.getMeasuredWidth();
            int childMeasureH = childView.getMeasuredHeight();

            childMarginW += childMeasureW;
            childMarginH += childMeasureH;

            int left = paddingLeft + childMarginL;
            int top = nextHeight + childMarginT;
            int right = left + childMeasureW;
            int bottom = top + childMeasureH;

            lineHeight = Math.max(lineHeight, childMarginH);
            if (lineWidth + childMarginW > mWidth){
                nextHeight += lineHeight;
                lineHeight = childMarginH;
                lineWidth = paddingWidth;
                paddingLeft = mPaddingLeft;

                left = paddingLeft + childMarginL;
                top = nextHeight + childMarginT;
                right = left + childMeasureW;
                bottom = top + childMeasureH;
            }
            childView.layout(left, top, right , bottom);

            lineWidth += childMarginW ;
            paddingLeft += childMarginW ;
        }

    }

	
	// 利用重写generateDefaultLayoutParams的返回值得到MarginLayoutParams 方便我们获取子控件的margin
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(super.generateDefaultLayoutParams());
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}
