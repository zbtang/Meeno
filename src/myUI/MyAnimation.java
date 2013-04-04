package myUI;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class MyAnimation extends Animation {
	float centerX;
	float centerY;
	Camera mCamera = new Camera();

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		centerX = width / 2;
		centerY = height / 2;
		setDuration(3000);
		setFillAfter(true);
		setInterpolator(new LinearInterpolator());
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		super.applyTransformation(interpolatedTime, t);
		final Matrix matrix = t.getMatrix();
		mCamera.save();
		mCamera.translate(0, 0, 1300 * (1 - interpolatedTime));
		mCamera.rotateY(720 * interpolatedTime);
		mCamera.getMatrix(matrix);

		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
		mCamera.restore();
	}

}
