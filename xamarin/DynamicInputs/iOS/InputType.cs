using System;
using UIKit;
namespace DynamicInputs.iOS
{

	public class InputType : UIPickerViewModel
	{
		static string[] names = new string[] {
			"Integer",
			"String",
			"Double",
			"List",
			"Boolean",
			"Date"

		};

		ViewController pvc;
		UIView view;
		public InputType(ViewController pvc, UIView view)
		{
			this.pvc = pvc;
			this.view = view;
		}
		public void setView(UIView view)
		{
			this.view = view; 
		}
		public override nint GetComponentCount(UIPickerView v)
		{
			return 1;
		}

		public override nint GetRowsInComponent(UIPickerView pickerView, nint component)
		{
			return names.Length;
		}

		public override string GetTitle(UIPickerView picker, nint row, nint component)
		{
			//if (component == 0)
				return names[row];
			//else
			//	return row.ToString();
		}

		public override void Selected(UIPickerView picker, nint row, nint component)
		{
			//pvc.label.Text = String.Format("{0} - {1}",
			//		names[picker.SelectedRowInComponent(0)],
			//		picker.SelectedRowInComponent(1));
			Console.WriteLine("Selected a value:");
			string str=names[picker.SelectedRowInComponent(0)];
			InputTypeBuilder builder = new InputTypeBuilder();


			switch (str)
			{
				case "Integer": builder.createTextforInteger(view);
							break;
				case "String": builder.createTextforString(view);
								break;
				case "Double": builder.createTextforDouble(view);
							break;
				case "Boolean": break;
				case "List": break;
				case "Date": builder.createDatePicker(view);
							break;	
					
			}
			//view.AddSubview(scrollView);

		}

		public override nfloat GetComponentWidth(UIPickerView picker, nint component)
		{
			if (component == 0)
				return 240f;
			else
				return 40f;
		}

		public override nfloat GetRowHeight(UIPickerView picker, nint component)
		{
			return 40f;
		}
	}
}
