package bainuo.myapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import bainuo.myapplication.mockserver.AnnotationAnalyzer;
import bainuo.myapplication.mockserver.IMockModel;

public class MainActivity extends ActionBarActivity {

    AnnotationAnalyzer<IMockModel> dataAnnotationProcessor = new AnnotationAnalyzer<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleData data = new SimpleData();
        TextView textView = (TextView) findViewById(R.id.text);

        ComplexData complexData = new ComplexData();
        MoreComplexData moreComplexData = new MoreComplexData();
        dataAnnotationProcessor.fieldAnnotationValue(moreComplexData);

        data.name = "pan2";
        textView.setText(data.name + " | " + data.age);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
