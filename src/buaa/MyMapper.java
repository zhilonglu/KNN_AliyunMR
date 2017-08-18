package buaa;

import java.io.IOException;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.MapperBase;

public class MyMapper extends MapperBase {
	private Record key;
	private Record value;
	@Override
	public void setup(TaskContext context) throws IOException {
		key = context.createMapOutputKeyRecord();
		value = context.createMapOutputValueRecord();
	}
	@Override
	public void map(long recordNum, Record record, TaskContext context)
			throws IOException {
		key.set(new Object[]{record.get(0).toString()});
		String v="";
		for(int i=0; i<record.getColumnCount();i++){
			v+=record.get(i).toString()+"#";
		}
		value.set(new Object[]{v});
		context.write(key,value);
	}
	@Override
	public void cleanup(TaskContext context) throws IOException {
	}

}
