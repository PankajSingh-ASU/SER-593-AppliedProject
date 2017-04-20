using System;
using System.Collections.Generic;
using Android.Content;
using Android.Widget;

namespace SQliteTest.Droid
{
	public class DynamicUICreator
	{
		InputTypeBuilder builder = new InputTypeBuilder();
		public List<Object> uiList { get; set; }// input list to read values later 



		public DynamicUICreator()
		{
		}
		public ScrollView createDynamicUI(Context context, List<TableInfo> columns)
		{
			var scrollView = new ScrollView(context);

			var layout = new LinearLayout(context);
			layout.Orientation = Orientation.Vertical;
			scrollView.Id = 100;
			scrollView.AddView(layout);
			uiList = new List<Object>();
			foreach (var c in columns)
			{
				var childUI = getLinearUI(context, c);
				layout.AddView(childUI);
				uiList.Insert(c.cid,childUI.GetChildAt(1));// adding input fields in the list 
			}
			var bLayout=addButtons(context);
			layout.AddView(bLayout);
			return scrollView;

		}

		public LinearLayout addButtons(Context context)
		{
			LinearLayout l = new LinearLayout(context);
			l.Orientation = Orientation.Horizontal;
			Button b1=builder.createButton(context, "Save");
			b1.Id = 1001;
			Button b2=builder.createButton(context, "Clear");
			b2.Id = 1002;
			//b1.SetOnClickListener(saveValues);
			l.AddView(b1);
			l.AddView(b2);
			return l;
		}
		 

		LinearLayout getLinearUI(Context context, TableInfo c)
		{
			LinearLayout l = new LinearLayout(context);
			l.Orientation = Orientation.Horizontal;


			matchUI(context,l, c);
			return l;




		}

		//matches UI content to SQLite column type
		void matchUI(Context context, LinearLayout l, TableInfo c)
		{
			string type = c.type.ToLower();
			switch (type)
			{
				case "integer":  // this means or of cases
				case "int":

					builder.createLabel(context, l, c);
					builder.createTextforInteger(context,l,c);
					break;
				case "string":
				//case "TEXT":
				case "text":	
					builder.createLabel(context, l, c);
					builder.createTextforString(context, l,c);
					break;
				//case "Double":
				//	builder.createTextforDouble(view);
				//	break;
				case "boolean":
				//case "BOOLEAN": 
					builder.createLabel(context, l, c);
					builder.createCheckBox(context, l,c); 
								break;
				//case "List": break;
				case "date":
					builder.createLabel(context, l, c);
					builder.createDatePicker(context, l,c);
					break;

			}

		}


	}
}
