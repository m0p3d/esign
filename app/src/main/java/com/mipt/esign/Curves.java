package com.mipt.esign;

import math.geom2d.Vector2D
import java.util.ArrayList

class CurveMeta {
	public double angleStart;
	public double angleStop;
	public double angleShift;
	public double mainCurvature; // - concave, + convex
};


class Curve {
	public ArrayList<Vector2D> dots;

	public void Normalize() {
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
			for (Vector2D v : dots) {
				v.x() = (v.x() - minX) / (maxX - minX);
				v.y() = (v.y() - minY) / (maxY - minY);
			}
		}
	}

	public ArrayList<Curve> slice() {
		/* TODO: Finish it! */
	}

	public CurveMeta getMeta() {
		CurveMeta meta;
		if (dots.size() >= 2) {
			meta.angleStart = dots.get(1).minus(dots.get(0)).angle();
			meta.angleStop = dots.get(dots.size() - 1).minus(dots.get(dots.size() - 2)).angle();
			meta.angleShift = dots.get(dots.size() - 1).minus(dots.get(0)).angle();

			double maxCurvature = 0.0f;
			double minCurvature = 0.0f;
			for(int i = 0; i < dots.size() - 2; i++) {
				Vector2D v = dots.get(i + 2).minus(dots.get(i + 1));
				v.angle() = 0.0f;
				double vLen = v.x();
				double vAngle = dots.get(i + 2).minus(dots.get(i)).angle() - dots.get(i + 1).minus(dots.get(i)).angle();
				double curvature;
				if( vLen == 0.0f ) {
					curvature = 0.0f
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
