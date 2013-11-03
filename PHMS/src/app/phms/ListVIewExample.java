package app.phms;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ListVIewExample extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		PHMSDatabase database = new PHMSDatabase(this);
		
		String[] array = {"test1", "test2", "test3"};
		
		this.setListAdapter(new ArrayAdapter<String>(this,R.layout.list_item, R.id.label, array));
	}
}
