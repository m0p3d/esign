package com.mipt.esign;

import java.util.ArrayList;
import java.util.List;

/*
 * AABDC
 * ABDCF
 * XABDC
 *
 * AABDCF
 * X-   -
 *
 * User provides a not precise info.
 * We shall choose the "features" of the signature
 * In this example features are BDC
 *
 *
 *
 */

class SignChecker {

	public final static double epsilon = 2.0f;
	public final static int maxFaultsAllowedOnCreate = 2;
	public final static int maxFaultsAllowed = 5;

	public List<CurveMeta> pattern = ArrayList<>();

	
	private List<CurveMeta> read(Curve sign) {
		sign.normalize();
		sign.smooth();
		sign.smooth();
		List<Curve> slices = sign.slice();
		List<CurveMeta> metas = new ArrayList<>();
		for (Curve slice : slices) {
			metas.add(slice.getMeta());
		}
		return metas;
	}


	private List<CurveMeta> getCommonPattern(List<CurveMeta> l1, List<CurveMeta> l2, double w1) {
		List<CurveMeta> commonPattern = new ArrayList<>();
		int i1 = 0;
		int i2 = 0;
		
		boolean found;
		do {
			int s1 = 0;
			int s2 = 0;
			found = false;
			while (i1 + s1 < l1.size() && i2 + s2 < l2.size()) {

				for (int j = i2; j < i2 + s2; j++) {
					if (l1.get(i1 + s1).distance(l2.get(j)) < epsilon) {
						found = true;
						i1 = i1 + s1;
						i2 = j;
						break;
					}
				}
				if (found) {
					break;
				}

				for (int j = i1; j < i1 +s1; j++) {
					if (l2.get(i2 + s2).distance(l1.get(j)) < epsilon) {
						found = true;
						i1 = j;
						i2 = i2 + s2;
						break;
					}
				}
				if (found) {
					break;
				}

				if (l1.get(i1 + s1).distance(l2.get(i2 + s2))) {
					found = true;
					i1 = i1 + s1;
					i2 = i2 + s2;
					break;
				}

				int incremented = 0;
				if (i1 + s1 < l1.size() - 1) {
					incremented++;
					s1++;
				}
				if (i2 + s2 < l2.size() - 1) {
					incremented++;
					s2++;
				}
				if (incremented == 0) {
					break;
				}
			}
			if (found) {
				/* Here is found a matching pair l1[i1] and l2[i2] */
				commonPattern.add(l1.get(i1).times(w1).plus(l2.get(i2).times(1.0f - w1)));
			}
		} while (found);
	}


	/* Input - several similar curves - iterations of creating signature. */
	public boolean create(List<Curve> signs) {
		if (signs.size() > 0) {
			/* Idea: 
			 * 1.Create pattern from the first sign;
			 * 2.Check it with the next signes 
			 *	 Metas, that do not match, drop from the pattern. 
			 *	 Metas, that match are averaged with the existing 
			 *	 ones in pattern. */
			pattern = read(signs.get(0));
			int maxSize = pattern.size();
			for (int i = 1; i < signs.size(); i++) {
				List<CurveMeta> newMetas = read(signs.get(i));
				if (newMetas.size() > maxSize) {
					maxSize = newMetas.size();
				}
				pattern = getCommonPattern(pattern, newMetas, 1.0f / (double)(i + 1));
			}
			int faults = maxSize - pattern.size();
			if (faults < maxFaultsAllowedOnCreate) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean check(Curve sign) {
		List<CurveMeta> metas = read(sign);
		int faults = 0;
		int j = 0;
		for (CurveMeta meta : metas) {
			if (j < pattern.size()) {
				if (meta.distance(pattern.get(j)) < epsilon) {
					j++;
				} else {
					faults++;
				}
			} else {
				/* User inserted an extra slice */
				faults++;
			}
		}
		if (j == pattern.size() && faults < maxFaultsAllowed) {
			/* Idea: since the signature is correct,
			 * it can be used to refine the pattern.
			 * ??? Do we need it? */
			return true;
		} else {
			return false;
		}
	}

}
