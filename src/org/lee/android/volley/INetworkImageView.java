package org.lee.android.volley;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.NetworkImageView;

public class INetworkImageView extends NetworkImageView {

	public INetworkImageView(Context context) {
		super(context);
		init();
	}

	public INetworkImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public INetworkImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		this.setScaleType(ScaleType.CENTER_INSIDE);
	}

	private int imageWidth;
	private int imageHeight;

	public void setImageWidth(int w) {
		imageWidth = w;
	}

	public void setImageHeight(int h) {
		imageHeight = h;
	}

	private boolean scaleToWidth = false; // this flag determines if should

	// measure height manually dependent
	// of width
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		/**
		 * if both width and height are set scale width first. modify in future
		 * if necessary
		 */

		if (widthMode == MeasureSpec.EXACTLY
				|| widthMode == MeasureSpec.AT_MOST) {
			scaleToWidth = true;
		} else if (heightMode == MeasureSpec.EXACTLY
				|| heightMode == MeasureSpec.AT_MOST) {
			scaleToWidth = false;
		} else
			throw new IllegalStateException(
					"width or height needs to be set to match_parent or a specific dimension");

		if (imageWidth == 0) {
			// nothing to measure
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			return;
		} else {
			if (scaleToWidth) {
				int iw = imageWidth;
				int ih = imageHeight;
				int heightC = width * ih / iw;
				if (height > 0)
					if (heightC > height) {
						// dont let hegiht be greater then set max
						heightC = height;
						width = heightC * iw / ih;
					}

				this.setScaleType(ScaleType.CENTER_CROP);
				setMeasuredDimension(width, heightC);

			} else {
				// need to scale to height instead
				int marg = 0;
				if (getParent() != null) {
					if (getParent().getParent() != null) {
						marg += ((RelativeLayout) getParent().getParent())
								.getPaddingTop();
						marg += ((RelativeLayout) getParent().getParent())
								.getPaddingBottom();
					}
				}

				int iw = imageWidth;
				int ih = imageHeight;

				width = height * iw / ih;
				height -= marg;
				setMeasuredDimension(width, height);
			}

		}
	}
}
