package ar.edu.unicen.exa.intia.imgProc.mobile.tests.input;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

public class FeatureAlgorithm {

	private String description;
	
	private int idDetector;
	private int idExtractor;
	private int idMatcher;
	
	private FeatureDetector detector;
	private DescriptorExtractor extractor;
	private DescriptorMatcher matcher;
	
	//! If true, a KNN-matching and ratio test will be enabled for matching descriptors.
	private boolean knMatchSupported;
	
	public class Time {
		public long consumedTimeMsDetector;
		public long consumedTimeMsExtractor;
	}

	public FeatureAlgorithm(String description, int idDetector, int idExtractor, int idMatcher) {
		assert(description != null);
    	this.description = description;
    	this.idDetector = idDetector;
    	this.idExtractor = idExtractor;
    	this.idMatcher = idMatcher;

    	this.detector = FeatureDetector.create(this.idDetector);
    	this.extractor = DescriptorExtractor.create(this.idExtractor);
    	this.matcher = DescriptorMatcher.create(this.idMatcher);
    	this.knMatchSupported = false;
    }
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isKnMatchSupported() {
		return knMatchSupported;
	}

	public void setKnMatchSupported(boolean knMatchSupported) {
		this.knMatchSupported = knMatchSupported;
	}
	
    //! Extracts feature points and compute descriptors from given image.
	public Time extractFeatures(Mat image, MatOfKeyPoint kp, Mat desc) {
	    assert(detector != null);
	    assert(extractor != null);
	    assert(!image.empty());
		
	    long startD, endD, startE, endE;
	    
	    startD = Core.getTickCount();
		detector.detect(image, kp);
		endD = Core.getTickCount();
	    
	    if (kp.empty())
	        return null;
	    
	    startE = Core.getTickCount();
	    extractor.compute(image, kp, desc);
	    endE = Core.getTickCount();
	    
//	    return kp.total() > 0;
	    Time result = null;
	    if (kp.total() > 0) {
		    result = new Time();
		    result.consumedTimeMsDetector = endD - startD;
		    result.consumedTimeMsExtractor = endE - startE;
	    }
	    return result;
	}
    
    //! Finds correspondences using regular match.
    public void matchFeatures(Mat train, Mat query, MatOfDMatch matches) {
        matcher.match(query, train, matches);
    }
    
    //! KNN match features.
    public void matchFeatures(Mat train, Mat query, int k, List<MatOfDMatch> matches) {
        assert(knMatchSupported);
        matcher.knnMatch(query, train, matches, k);
    }

	public FeatureDetector getDetector() {
		return detector;
	}

	public DescriptorExtractor getExtractor() {
		return extractor;
	}

	public DescriptorMatcher getMatcher() {
		return matcher;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FeatureAlgorithm)) {
			return false;
		}
		FeatureAlgorithm other = (FeatureAlgorithm) obj;
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		return true;
	}

	public int getIdDetector() {
		return idDetector;
	}

	public int getIdExtractor() {
		return idExtractor;
	}

	public int getIdMatcher() {
		return idMatcher;
	}

}
