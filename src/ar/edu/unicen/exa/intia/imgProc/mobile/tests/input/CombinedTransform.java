package ar.edu.unicen.exa.intia.imgProc.mobile.tests.input;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

public class CombinedTransform extends ImageTransformation {

	public enum ParamCombinationType {
        // Generate resulting X vector as list of all possible combinations of first and second args
        Full,
        // Largest argument vector used as is, the values for other vector is copied
        Extrapolate,
        // Smallest argument vector used as is, the values for other vector is interpolated from other
        Interpolate
    };
    
    private class Par {
    	double first, second;
    	
    	public Par(Double f, Double s) {
    		first = f;
    		second = s;
    	}
    }

    private List<Double> m_x;
    private List<Par> m_params;
    
    private ImageTransformation m_first;
    private ImageTransformation m_second;
    
	public CombinedTransform(ImageTransformation first, ImageTransformation second) {
		this(first, second, ParamCombinationType.Extrapolate);
	}

	public CombinedTransform(ImageTransformation first, ImageTransformation second, ParamCombinationType type) {
		super(first.name + "+" + second.name);
		m_first = first;
		m_second = second;
		
		List<Double> x1 = first.getConfigArguments();
		List<Double> x2 = second.getConfigArguments();
		
		m_x = new ArrayList<Double>();
		m_params = new ArrayList<CombinedTransform.Par>();
		    
		switch (type) {
			case Full: {
				int index = 0;
		        for (int i1 = 0; i1 < x1.size(); i1++) {
		        	for (int i2 = 0; i2 < x2.size(); i2++) {
		        		m_params.add(new Par(x1.get(i1), x2.get(i2)));
		        		m_x.add((double)index);
		        		index++;
		        	}
		        }
			}
			break;
			
			case Interpolate: {
				if (x1.size() > x2.size()) {
					int index = 0;
					for (int i2 = 0; i2 < x2.size(); i2++) {
						int i1 = (int)((float)(x1.size() * i2) / (float)x2.size() + 0.5f);
						m_params.add(new Par(x1.get(i1), x2.get(i2)));
						m_x.add((double)index);
						index++;
					}
				} else {
					int index = 0;
		            for (int i1 = 0; i1 < x1.size(); i1++) {
		            	int i2 = (int)((float)(x2.size() * i1) / (float)x1.size() + 0.5f);
		            	m_params.add(new Par(x1.get(i1), x2.get(i2)));
		            	m_x.add((double)index);
		            	index++;
		            }                
				}
			} 
			break;
		            
			case Extrapolate: {
				if (x1.size() > x2.size()) {
					int index = 0;
		            for (int i1 = 0; i1 < x1.size(); i1++) {
		            	int i2 = (int)((float)(x2.size() * i1) / (float)x1.size() );
		            	m_params.add(new Par(x1.get(i1), x2.get(i2)));
		            	m_x.add((double)index);
		            	index++;
		            }
				} else {
					int index = 0;
		            for (int i2 = 0; i2 < x2.size(); i2++) {
		            	int i1 = (int)((float)(x1.size() * i2) / (float)x2.size() );
	                    m_params.add(new Par(x1.get(i1), x2.get(i2)));
	                    m_x.add((double)index);
	                    index++;
	                }
	            }
			}
			break;
		            
			default:
				break;
		}
	}

	@Override
	public List<Double> getConfigArguments() {
		return m_x;
	}

	@Override
	public void transform(double t, Mat source, Mat result) {
	    int index = (int)t;
	    double t1 = m_params.get(index).first;
	    double t2 = m_params.get(index).second;
	    
	    Mat temp = new Mat();
	    m_first.transform(t1, source, temp);
	    m_second.transform(t2, temp, result);
	}
	
	@Override
	public boolean canTransformKeypoints() {
		return m_first.canTransformKeypoints() && m_second.canTransformKeypoints();
	}

	@Override
	public void transform(double t, MatOfKeyPoint source, MatOfKeyPoint result)  {
	    int index = (int)t;
	    float t1 = (float)m_params.get(index).first;
	    float t2 = (float)m_params.get(index).second;
	    
	    MatOfKeyPoint temp = new MatOfKeyPoint();
	    m_first.transform(t1, source, temp);
	    m_second.transform(t2, temp, result);
	    
	}

	@Override
	public Mat getHomography(double t, Mat source) {
	    int index = (int)t;
	    
	    double t1 = m_params.get(index).first;
	    double t2 = m_params.get(index).second;

	    Mat temp = new Mat();
	    m_first.transform(t1, source, temp);

	    Mat secondHomography = m_second.getHomography(t2, temp);
	    Mat firstHomography = m_first.getHomography(t1, source);
	    
	    return secondHomography.mul(firstHomography);
	}

	@Override
	public void importConfigFromJson(String jsonObj) {
		// TODO Auto-generated method stub
	}

	@Override
	public String exportConfigToJson() {
		// TODO Auto-generated method stub
		return null;
	}

}