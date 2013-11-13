package com.gmapssimple;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class About extends Activity {
	SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		session = new SessionManager(getApplicationContext());

		TextView ab = (TextView) findViewById(R.id.textView);
		if (session.getLan())
			ab.setText("� �������� ZanteApp ����������� ��� �� ����� ������������ ��� ������� ���������� ��� ������������ "
					+ "��� �������� ��� ����� ��������� ������������ ����������� ��� ������������ ��� ������������� ������, ��� ��� �������� "
					+ "��� �������� �. ��������� ���������."
					+ " ������������ ��� ����������� ��� �������� ��� ��� ��������� �� ���������� ��� ������� ������� ��� ��������!");
		else
			ab.setText("Zante App was created by Mary Fragkogianni as a part of her thesis "
					+ "in Computer Engineering and Informatics department "
					+ "of University of Patras and by the guidance of Professor Athanasios Tsakalidis"
					+ ". Thank you for dowloading "
					+ "Zante App and I hope that you will enjoy learning and knowing the history of Zakynthos! ");

		DisplayMetrics metrics;
		metrics = getApplicationContext().getResources().getDisplayMetrics();
		float Textsize = ab.getTextSize() / metrics.density;
		ab.setTextSize(Textsize + 1);
	}

}
