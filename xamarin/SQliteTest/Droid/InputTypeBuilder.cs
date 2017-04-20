
using System;
using Android.Content;
using Android.Widget;
using Android.App;
namespace SQliteTest.Droid
{
	public class InputTypeBuilder
	{
		int textSize = 18;
		Android.Graphics.Color textColor = Android.Graphics.Color.Aqua;
		Android.Graphics.Color labelColor = Android.Graphics.Color.Blue;
		void setTextProperties(EditText ed, int id)
		{
			ed.TextSize = textSize;
			ed.SetTextColor(textColor);
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
	LinearLayout.LayoutParams.WrapContent,
	LinearLayout.LayoutParams.WrapContent,
	0.7f
);
			ed.LayoutParameters = param;
			ed.TextAlignment = Android.Views.TextAlignment.Center;
			ed.Id = id;
		

		}

		internal void createTextforString(Context context, LinearLayout l, TableInfo c)
		{
			EditText ed = new EditText(context);
			ed.InputType = Android.Text.InputTypes.ClassText;
			setTextProperties(ed, c.cid);

			l.AddView(ed);
		}

		internal void createCheckBox(Context context, LinearLayout l, TableInfo c)
		{
			CheckBox ch = new CheckBox(context);
			l.AddView(ch);
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
	LinearLayout.LayoutParams.WrapContent,
	LinearLayout.LayoutParams.WrapContent,
	0.5f
);
			ch.LayoutParameters = param;

		}

		public InputTypeBuilder() { }

		public void createTextforInteger(Context context, LinearLayout l, TableInfo c)
		{
			EditText ed = new EditText(context);
			ed.InputType = Android.Text.InputTypes.ClassNumber;
			setTextProperties(ed, c.cid);
			//ed.setLayoutParams(param);
			l.AddView(ed);


			//Android.Text.InputTypes.NumberFlagDecimal


		}

		public void createDatePicker(Context context, LinearLayout l, TableInfo c)
		{
	//		DatePicker dp = new DatePicker(context);
	//		l.AddView(dp);
	//		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
	//LinearLayout.LayoutParams.WrapContent,
	//LinearLayout.LayoutParams.WrapContent,
	//0.5f
	//);
	//		dp.LayoutParameters = param;
	//		dp.Id = c.cid;

			EditText editText = new EditText(context);
			DynamicDatePicker ddp = new DynamicDatePicker(editText);
			ddp.createDatePicker(context);
			setTextProperties(editText, c.cid);
			l.AddView(editText);


		}


		public void createLabel(Context context, LinearLayout l, TableInfo c)
		{
			TextView label = new TextView(context);
			label.Text = c.name;
			label.TextSize = textSize;
			label.SetTextColor(labelColor);
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
	LinearLayout.LayoutParams.WrapContent,
	LinearLayout.LayoutParams.WrapContent,
	0.3f
);
			label.LayoutParameters = param;
			l.AddView(label);
		}

		public Button createButton(Context context, string label)
		{
			Button but = new Button(context);
			but.Text = label;
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
	LinearLayout.LayoutParams.WrapContent,
	LinearLayout.LayoutParams.WrapContent,
	0.5f
	);
			but.LayoutParameters = param;
			return but;
	}
	}
}
