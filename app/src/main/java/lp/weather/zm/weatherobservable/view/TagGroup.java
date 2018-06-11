package lp.weather.zm.weatherobservable.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import lp.weather.zm.weatherobservable.R;


/**
 * Created by dxf on 2017/3/8.
 */

public class TagGroup extends ViewGroup{
    private static final int DEFAULT_HORIZONTAL_SPACING = 20;
    private static final int DEFAULT_VERTICAL_SPACING = 20;

    private int mVerticalSpacing;
    private int mHorizontalSpacing;
    private int ScreenWidth=0;
    private boolean isFirstLongView=false,isFirstLongViewLayout=false;
    private int lastTop=0,index=0,indexMeasure=0,lastLineHeight=0;

    public TagGroup(Context context) {
        super(context);
    }

    public TagGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagGroup);
        try {
            mHorizontalSpacing = a.getDimensionPixelSize(
                    R.styleable.TagGroup_horizontal_spacing, DEFAULT_HORIZONTAL_SPACING);
            mVerticalSpacing = a.getDimensionPixelSize(
                    R.styleable.TagGroup_vertical_spacing, DEFAULT_VERTICAL_SPACING);
        } finally {
            a.recycle();
        }
    }


    public void setHorizontalSpacing(int pixelSize) {
        mHorizontalSpacing = pixelSize;
    }

    public void setVerticalSpacing(int pixelSize) {
        mVerticalSpacing = pixelSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        index=0;
        lastTop=0;
        lastLineHeight=0;
        // width size & mode
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        this.ScreenWidth=widthSize;
        // height size & mode
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // padding
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        // the width & height final result
        int resultWidth = 0;
        int resultHeight = 0;

        int lineWidth = 0;
        int lineHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            // measure child
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            // 这里要记得加上子view的margin值
//            MarginLayoutParams childLayoutParams = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            lineHeight = Math.max(childHeight, lineHeight);


             if (lineWidth + childWidth > widthSize - paddingLeft - paddingRight) { // 需要换一行
                resultWidth = Math.max(resultWidth, lineWidth); // 每一行都进行比较，最终得到最宽的值
                resultHeight += mVerticalSpacing + lineHeight;


                     if(childWidth==widthSize){
                         if(resultHeight-lastTop>(mVerticalSpacing+lastLineHeight)){
                             int sub=resultHeight-lastTop-mVerticalSpacing-lastLineHeight;
                             resultHeight-=sub;
                         }
                         index++;
                     }

                lineWidth = (int) (childWidth + mHorizontalSpacing); // 新的一行的宽度
                lineHeight = childHeight; // 新的一行的高度
            }else {
                // 当前行的宽度
                lineWidth += childWidth + mHorizontalSpacing;
                // 当前行最大的高度
                lineHeight = Math.max(lineHeight, childHeight);
            }


            if(index==1){
                if(resultHeight-lastTop>(mVerticalSpacing+lastLineHeight)){
                    int sub=resultHeight-lastTop-mVerticalSpacing-lastLineHeight;
                    resultHeight-=sub;
                }
                index++;
            }
            lastTop=resultHeight;
            lastLineHeight=childHeight;



            // 最后一个, 需要再次比较宽
            if (i == getChildCount() - 1) {
                resultWidth = Math.max(resultWidth, lineWidth);
            }
        }

        resultWidth += paddingRight + paddingLeft;
        // 布局最终的高度  由于误差
        resultHeight += lineHeight + paddingBottom + paddingTop;

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : resultWidth, heightMode == MeasureSpec.EXACTLY ? heightSize : resultHeight);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        index=0;
        lastTop=0;
        lastLineHeight=0;
        int myWidth = r - l;

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();

        int childLeft = paddingLeft;
        int childTop = paddingTop;

        int lineHeight = 0;

        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            View childView = getChildAt(i);

            if (childView.getVisibility() == View.GONE) {
                continue;
            }

            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            lineHeight = Math.max(childHeight, lineHeight);

            if ((childLeft + childWidth + paddingRight) > myWidth) {
                childLeft = paddingLeft;
                childTop += mVerticalSpacing + lineHeight;

                    if(childWidth==ScreenWidth){
                        if(childTop-lastTop>(mVerticalSpacing+lastLineHeight)){
                            int sub=childTop-lastTop-mVerticalSpacing-lastLineHeight;
                            childTop-=sub;
                        }
                        index++;
                     }


                   lineHeight = childHeight;
            }

            if(index==1){
                if(childTop-lastTop>(mVerticalSpacing+lastLineHeight)){
                    int sub=childTop-lastTop-mVerticalSpacing-lastLineHeight;
                    childTop-=sub;
                }
                index++;
            }
            lastTop=childTop;
            lastLineHeight=lineHeight;
            childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            childLeft += childWidth + mHorizontalSpacing;
        }

    }
}
