package buaa;

import com.aliyun.odps.OdpsException;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.RunningJob;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.aliyun.odps.mapred.utils.SchemaUtils;

public class MyDriver {
	public static int kTime = 0;//默认是按上午来的 1表示下午，2表示晚上
	public static void main(String[] args) throws OdpsException {
		JobConf job = new JobConf();

		// TODO: specify map output types
		job.setMapOutputKeySchema(SchemaUtils.fromString("key:string"));
		job.setMapOutputValueSchema(SchemaUtils.fromString("value:string"));

		// TODO: specify input and output tables
		InputUtils.addTable(TableInfo.builder().tableName(args[0]).build(),
				job);
		OutputUtils.addTable(TableInfo.builder().tableName(args[1]).build(),
				job);
//		if(args[0].contains("zao")){
//			kTime = 0;
//		}
//		else if (args[0].contains("zhong")){
//			kTime = 1;
//		}else{
//			kTime = 2;
//		}
		// TODO: specify a mapper
		job.setMapperClass(MyMapper.class);
		// TODO: specify a reducer
		job.setReducerClass(MyReducer.class);

		RunningJob rj = JobClient.runJob(job);
		rj.waitForCompletion();
	}

}
