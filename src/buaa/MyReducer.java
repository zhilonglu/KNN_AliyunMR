package buaa;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import sun.security.krb5.internal.tools.Ktab;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.ReducerBase;

import buaa.KNN;
public class MyReducer extends ReducerBase {
	static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
	private Record result = null;
	@Override
	public void setup(TaskContext context) throws IOException {
		result = context.createOutputRecord();
	}
	@Override
	public void reduce(Record key, Iterator<Record> values, TaskContext context)
			throws IOException {
//		int[] arrbaseHour = {8,15,18};
//		int baseHour=arrbaseHour[MyDriver.kTime];
		int baseHour = 18;
		int inputnum=60;
		int outputnum=30;
		ArrayList<double[]> trainX=new ArrayList<double[]>();
		ArrayList<double[]> trainY=new ArrayList<double[]>();
		ArrayList<double[]> preX=new ArrayList<double[]>();
		ArrayList<String> dates=new ArrayList<String>();
		String link=key.get(0).toString();
		while (values.hasNext()) {
			String line=(String)values.next().get(0);
			String[] lines=line.split("#");
			String date=(String)lines[1];
			double [] x= new double[inputnum];
			double [] y= new double[outputnum];
			for (int i=0;i<inputnum;i++){
				x[i]=Double.valueOf(lines[i+2]);
			}
			for (int i=0;i<outputnum;i++){
				y[i]=Double.valueOf(lines[i+62]);
			}
			if(date.compareTo("2017-07-01")<0){
				trainX.add(x);
				trainY.add(y);
			}else if(date.compareTo("2017-07-15")<=0){
				preX.add(x);
				dates.add(date);
			}
		}
		double[][] X = (double[][]) trainX.toArray(new double[trainX.size()][inputnum]);
		double[][] Y = (double[][]) trainY.toArray(new double[trainY.size()][outputnum]);
		double[][] X_ = (double[][]) preX.toArray(new double[preX.size()][inputnum]);
		for(int i=0; i<preX.size();i++){
			double[] pre_y = KNN.knn(X, Y, X_[i], 11);//按行预测，输出一行30个数据
			System.out.println(pre_y.length);
			for(int j=0; j<Y[0].length;j++){
				String date=dates.get(i);
				int startmnt=j*2;
				int endmnt=startmnt+2;
				double res=pre_y[j];
				String tstr="["+timestr(date, startmnt, baseHour)+","+timestr(date, endmnt, baseHour)+")";
				result.set(0,link);
				result.set(1,date);
				result.set(2,tstr);
				result.set(3,res);
				context.write(result);
			}
		}
	}
	@Override
	public void cleanup(TaskContext context) throws IOException {

	}
	public String timestr(String date, int mnt, int baseHour) {
		int hour = baseHour+mnt/60;
		mnt=mnt-(mnt/60)*60;
		return date+" "+pad0(hour)+":"+pad0(mnt)+":"+"00";
	}
	public String pad0(int num) {
		if(num<10){
			return "0"+num;
		}else{
			return ""+num;
		}
	}

}
