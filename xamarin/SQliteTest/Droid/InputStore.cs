using System;
using Android.Content;
using Android.Widget;
using System.Collections.Generic;

namespace SQliteTest.Droid
{
	public class InputStore
	{
		public InputStore()
		{
		}
		public Dictionary<int, Object> getInputValues(List<Object>uiList,List<TableInfo> columns)
		{
			Dictionary<int, Object> dict = new Dictionary<int, object>();
			foreach (var c in columns)
			{
				var ui = uiList[c.cid];
				var value = getUIValue(ui);
				dict.Add(c.cid, value);
			}
			return dict;

		}

		object getUIValue(object ui)
		{
			if (ui == null)
				return null;
			Console.WriteLine("Ui is :::::" + ui);
			var type = ui.GetType();
			Console.WriteLine("type is :::::" + type);
			string kind=type.ToString().ToLower();
			Console.WriteLine("kind is :::::" + kind);
			switch (kind)
			{
				case "android.widget.edittext":  EditText ed=(EditText)ui;
					return ed.Text;
				case "android.widget.checkbox": CheckBox ch = (CheckBox)ui;
					Boolean flag= ch.Checked;
					Console.WriteLine("flag: " + flag);
					if (flag)
						return 1;
					else
						return 0;
			}
			return null;
		}
}
}
