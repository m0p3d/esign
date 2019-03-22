package com.mipt.esign;

import math.geom2d.Vector2D;
import java.util.ArrayList;
import java.util.List;

class CurveMeta {
	public double angleStart;
	public double angleStop;
	public double angleShift;
	public double mainCurvature; // - concave, + convex
};


class Curve {
	public List<Vector2D> dots = new ArrayList<>();

	public void normalize() {
		if (dots.size() > 0) {
			double minX = dots.get(0).x();
			double maxX = dots.get(0).x();
			double minY = dots.get(0).y();
			double maxY = dots.get(0).y();
			for (Vector2D v : dots) {
				if (v.x() < minX) {
					minX = v.x();
				}
				if (v.x() > maxX) {
					maxX = v.x();
				}
				if (v.y() < minY) {
					minY = v.y();
				}
				if (v.y() > maxY) {
					maxY = v.y();
				}
			}
			for (int i = 0; i < dots.size(); i++) {
				double newX;
				if (minX == maxX) {
					newX = 0.0f;
				} else {
					newX = (dots.get(i).x() - minX) / (maxX - minX);
				}
				if (minY == maxY) {
					newY = 0.0f;
				} else {
					newY = (dots.get(i).y() - minY) / (maxY - minY);
				}

				dots.set(i, new Vector2D(newX, newY));
			}
		}
	}

	public final static double angleThreshold = 0.2f; 
	/* If too much slices happen, try increasing angleThreshold 
	 * TODO: try 0.3f, 0.4f */

	
//	public ArrayList<Curve> slice() {
//		/* TODO: Finish it! */
//		int sliceStart = 0;
//		for(int i = 0; i < dots.size() - 2; i++) {
//			Vector2D v = dots.get(i + 2).minus(dots.get(i + 1));
//			double vAngle = dots.get(i + 2).minus(dots.get(i)).angle() - dots.get(i + 1).minus(dots.get(i)).angle();
//
//			if (vAngle > angleThreshold || vAngle < -angleThreshold) {
//				/* The actual slicing. */
//				sliceStart = 1 + 1;
//			}



//				double curvature;
//				if( vLen == 0.0f ) {
//					curvature = 0.0f;
//				} else {
//					curvature = (2.0f * Math.sin(vAngle)) / vLen;
//				}
				/* If angle is negative -> 
				 * sin is negative -> 
				 * curvature is negative
//				 * PERFECT */ 
//				if (curvature > maxCurvature) {
//					maxCurvature = curvature;
//				}
//				if (curvature < minCurvature) {
//					minCurvature = curvature;
//				}
//			}
//
//	}

	public CurveMeta getMeta() {
		CurveMeta meta = new CurveMeta();

		if (dots.size() >= 2) {
			meta.angleStart = dots.get(1).minus(dots.get(0)).angle();
			meta.angleStop = dots.get(dots.size() - 1).minus(dots.get(dots.size() - 2)).angle();
			meta.angleShift = dots.get(dots.size() - 1).minus(dots.get(0)).angle();

			double maxCurvature = 0.0f;
			double minCurvature = 0.0f;
			for(int i = 0; i < dots.size() - 2; i++) {
				Vector2D v = dots.get(i + 2).minus(dots.get(i + 1));

				double vLen = Math.sqrt(v.x() * v.x() + v.y() * v.y());
				double vAngle = dots.get(i + 2).minus(dots.get(i)).angle() - dots.get(i + 1).minus(dots.get(i)).angle();
				double curvature;
				if( vLen == 0.0f ) {
					curvature = 0.0f;
				} else {
					curvature = (2.0f * Math.sin(vAngle)) / vLen;
				}
				/* If angle is negative -> 
				 * sin is negative -> 
				 * curvature is negative
				 * PERFECT */ 
				if (curvature > maxCurvature) {
					maxCurvature = curvature;
				}
				if (curvature < minCurvature) {
					minCurvature = curvature;
				}
			}
			if (maxCurvature + minCurvature >= 0.0f) {
				meta.mainCurvature = maxCurvature;
			} else {
				meta.mainCurvature = minCurvature;
			}
		}
		return meta;
	}

}
