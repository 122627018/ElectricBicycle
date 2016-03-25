package com.wxxiaomi.electricbicycle.view.custom;

import java.util.List;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class HomeSnackBarBehavior extends CoordinatorLayout.Behavior<CoordinatorLayout> {

	public HomeSnackBarBehavior() {
	}

	public HomeSnackBarBehavior(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean layoutDependsOn(CoordinatorLayout parent, CoordinatorLayout child,
			View dependency) {
		 return dependency instanceof Snackbar.SnackbarLayout;
	}

	@Override
	public boolean onDependentViewChanged(CoordinatorLayout parent,
			final CoordinatorLayout child, View dependency) {
		final float translationY = getFabTranslationYForSnackbar(parent, child);
//		Log.i("wang", "translationY=" + translationY);
//		if(dependency instanceof Snackbar.SnackbarLayout){
//			child.setTranslationY(translationY);
//		}else{
//			if (translationY == 0) {
//				slideview(child, tempY, translationY);
//			} else {
//				tempY = translationY;
//				slideview(child, 0, translationY);
//			}
//		}
//		Log.i("wang", "m5,translationY="+translationY);
		child.setTranslationY(translationY);
		return false;
	}

//	private float tempY;

	private float getFabTranslationYForSnackbar(CoordinatorLayout parent,
			View fab) {
		float minOffset = 0;
		final List<View> dependencies = parent.getDependencies(fab);
//		Log.i("wang", "dependencies.size()=" + dependencies.size());
		for (int i = 0, z = dependencies.size(); i < z; i++) {
			final View view = dependencies.get(i);
			if (view instanceof Snackbar.SnackbarLayout &&parent.doViewsOverlap(fab, view)) {
				// if (view instanceof Snackbar.SnackbarLayout &&
				// parent.doViewsOverlap(fab, view)) {
				minOffset = Math.min(minOffset,
						ViewCompat.getTranslationY(view) - view.getHeight());
			}
		}

		return minOffset;
	}

	public void slideview(final View view, final float p1, final float p2) {
		TranslateAnimation animation;
		if (p2 == 0) {
			animation = new TranslateAnimation(0, 0, p1, 0);
		} else {
			animation = new TranslateAnimation(0, 0, p1, p2);
		}

		animation.setInterpolator(new OvershootInterpolator());
		animation.setDuration(1000);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				int left = view.getLeft();
				int top;
				if (p2 == 0) {
					top = view.getTop();
				} else {
					top = view.getTop() + (int) (p2 - p1);
				}

				int width = view.getWidth();
				int height = view.getHeight();
				view.clearAnimation();
//				Log.i("wang", "height=" + height);
//				Log.i("wang", "left=" + left + ",top=" + top + ",left+width"
//						+ left + width + ",top+height=" + top + height);
				view.layout(left, top, left + width, top + height);
			}
		});
		view.startAnimation(animation);
	}
}
