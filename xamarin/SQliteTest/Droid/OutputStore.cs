using System;
using Android.Widget;
using System.Collections.Generic;
namespace SQliteTest.Droid
{
	public class OutputStore
	{
		public OutputStore()
		{
		}
		public void setUIValues(List<Object> uiList, List<TableInfo> columns,Dictionary<int, Object> dict)
		{
			
			foreach (var c in columns)
			{
				var ui = uiList[c.cid];
				var value = dict[c.cid];
				setUIValue(ui,value);
				dict.Add(c.cid, value);
			}


		}

		void setUIValue(object ui, object value)
		{
			var type = ui.GetType();
			string kind = type.ToString().ToLower();
			switch (kind)
			{
				case "edittext":
					EditText ed = (EditText)ui;
					string v = (string)value;
					ed.Text=v;
					break;
				case "checkbox":
					CheckBox ch = (CheckBox)ui;
					bool c = (bool)value;
					 ch.Selected=c;
					break;
			}
		}


	}
}
