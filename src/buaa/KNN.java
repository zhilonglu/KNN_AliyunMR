package buaa;
import java.util.ArrayList;  
import java.util.Collections;  
import java.util.Comparator;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
public class KNN {  
	public static double calDistance(double[] d1, double[] d2) {  
		double distance = 0.00;  
		for (int i = 0; i < d1.length; i++) {  
			distance += (d1[i] - d2[i]) * (d1[i] - d2[i]);  
		}  
		return distance;  
	}
	//单行预测，一次预测30个值
	public static double[] knn(double[][] x, double[][] y, double[] x_,int k) { 
		HashMap<String,Double> dis_value = new HashMap<String,Double>();
		for(int i=0;i<x.length;i++){
			double[] currData = x[i];  
			double[] value = y[i]; 
			String key = String.valueOf(i);
			for(int m=0;m<value.length;m++)
				key += "#"+String.valueOf(value[m]);
			double distance = calDistance(x_, currData);
			dis_value.put(key,distance);  
		}  
		List<Map.Entry<String, Double>> list_Data = new ArrayList<Map.Entry<String, Double>>(dis_value.entrySet());    
		Collections.sort(list_Data, new Comparator<Map.Entry<String, Double>>()    
				{     
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2)    
			{    
				if(o2.getValue()!=null&&o1.getValue()!=null&&o2.getValue().compareTo(o1.getValue())>0){    
					return -1;    
				}else{    
					return 1;    
				}    
			}
				}); 
		double[] pre_value = new double[30];
		for(int n=0;n<30;n++){
			for (int i = 0; i < k; i++){  
				double c = Double.valueOf(list_Data.get(i).getKey().split("#")[n+1]);
				pre_value[n] += 1/c;
			}
			pre_value[n] = k/pre_value[n] ;
		}
		return pre_value;  
	}  
}  