package com.mipt.esign;

import math.geom2d.Vector2D;
import java.util.ArrayList;
import java.util.List;

class CurveMeta {
	public double angleStart;
	public double angleStop;
	public double angleShift;
	public double mainCurvature; // - concave, + convex

	/* Constructor */
	CurveMeta times(double k) {
		CurveMeta res;
		res.angleStart = angleStart * k;
		res.angleStop = angleStop * k;
		res.angleShift = angleShift * k;
		res.mainCurvature = mainCurvature * k;
		return res;
	}

	CurveMeta plus(CurveMeta other) {
		CurveMeta res;
		res.angleStart = angleStart + other.angleStart;
		res.angleStop = angleStop + other.angleStop;
		res.angleShift = angleShift + other.angleShift;
		res.mainCurvature = mainCurvature + other.mainCurvature;
		return res;
	}


	public final static double astWeight = 1.0f;
	public final static double aspWeight = 1.0f;
	public final static double ashWeight = 1.0f;
	public final static double mcWeight = 0.02f;

	double distance(CurveMeta other) {
		double astDist = astWeight * (angleStart - other.angleStart);
		double aspDist = aspWeight * (angleStop - other.angleStop);
		double ashDist = ashWeight * (angleShift - other.angleShift);
		double mcDist = mcWeight * (mainCurvature - other.mainCurvature);
		return Math.sqrt(
			astDist * astDist +
			aspDist * aspDist +
			ashDist * ashDist +
			mcDist * mcDist);
	}
};


class Curve {
	public List<Vector2D> dots = new ArrayList<>();

	
	public void smooth() {
		if (dots.size() > 1) {
			List<Vector2D> smoothedDots = new ArrayList<>();
			for (int i = 0; i < dots.size() - 1; i++) {
				smoothedDots.add(dots.get(i).plus(dots.get(i + 1)).times(0.5f));
			}
			dots = smoothedDots;
		}
	}

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
				double newY;
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

	public final static double angleThreshold = 0.5f; 
	/* If too much slices happen, try increasing angleThreshold 
	 * TODO: try 0.3f, 0.4f */


	public final static double curvatyAlphaThreshold = 0.2f;


	/* TODO: Test slice(): draw dots of sliced curve 
	 * with various colours. */
	public List<Curve> slice() {
		/* TODO: Finish it! */
		int sliceStart = 0;
		List<Curve> slices = new ArrayList<>();
		for(int i = 0; i < dots.size() - 2; i++) {
			/* Check dir mod */
			double angle = dots.get(i + 2).minus(dots.get(i + 1)).angle() - dots.get(i + 1).minus(dots.get(i)).angle();
			if (angle > angleThreshold || angle < -angleThreshold) { 
				/* The actual slicing. */
				Curve c = new Curve();
				c.dots = dots.subList(sliceStart, i + 1);
				slices.add(c);
				i += 2;
				sliceStart = i;
			}
			/// + add curvaty check
		}
		if (dots.size() > 0) {
			Curve finalC = new Curve();
			finalC.dots = dots.subList(sliceStart, dots.size() - 1);
			slices.add(finalC);
		}
		return slices;
	}

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
				if ( vLen == 0.0f ) {
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
