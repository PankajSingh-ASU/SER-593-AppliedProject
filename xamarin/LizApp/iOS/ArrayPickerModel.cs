using System;
using UIKit;
using CoreGraphics;
namespace LizApp.iOS
{
	public class ArrayPickerModel: UIPickerViewModel
	{
		public string[] arrays;
		int selectedIndex = 0;
		public string selectedArray { get; set; }
		public event EventHandler ArraySelected;
		public ArrayPickerModel(params string[] _arrays)
		{
			Console.WriteLine(_arrays[0]);
			arrays = _arrays;
			//Console.WriteLine(arrays[1]);
		}
		public void onArraySelected()
		{
			if (ArraySelected != null)
			{
				ArraySelected(this, EventArgs.Empty);
			}
		}
		public string SelectedItem
		{
			get { return arrays[selectedIndex]; }
		}
		/// <summary>
		/// Called by the picker to determine how many rows are in a given spinner item
		/// </summary>
		public override nint GetRowsInComponent(UIPickerView picker, nint component)
		{
			return arrays.Length;
		}
		public override void Selected(UIPickerView picker, nint row, nint component)
		{
			selectedArray = arrays[row];
			onArraySelected();
		}
		/// <summary>
		/// called by the picker to get the number of spinner items
		/// </summary>
		public override nint GetComponentCount(UIPickerView picker)
		{
			return 1;
		}


		/// <summary>
		/// Make the rows in the second component half the size of those in the first
		/// </summary>
		public override nfloat GetRowHeight(UIPickerView picker, nint component)
		{
			return 44 / (component % 2 + 1);
		}
		public override string  GetTitle(UIPickerView picker, nint row, nint component)
		{

			if (component == 0)
				return arrays[row];
			else
				return row.ToString();
		}

	}


}
