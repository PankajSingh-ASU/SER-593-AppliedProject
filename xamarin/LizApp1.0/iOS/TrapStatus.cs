using System;
using UIKit;
using CoreGraphics;
namespace LizApp.iOS
{
	public class TrapStatus : UIPickerViewModel
	{
		public string[] status = {"OPEN" ,"CHECKED","CHECKED & CLOSED"};
		int selectedIndex = 0;
		public string selectedArray { get; set; }
		public event EventHandler statuselected;
		public TrapStatus(params string[] _status)
		{
			//Console.WriteLine(_status[0]);
			//status = _status;
			//Console.WriteLine(status[1]);
		}
		public void onstatuselected()
		{
			if (statuselected != null)
			{
				statuselected(this, EventArgs.Empty);
			}
		}
		public string SelectedItem
		{
			get { return status[selectedIndex]; }
		}
		/// <summary>
		/// Called by the picker to determine how many rows are in a given spinner item
		/// </summary>
		public override nint GetRowsInComponent(UIPickerView picker, nint component)
		{
			return status.Length;
		}
		public override void Selected(UIPickerView picker, nint row, nint component)
		{
			selectedArray = status[row];
			onstatuselected();
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
		public override string GetTitle(UIPickerView picker, nint row, nint component)
		{

			if (component == 0)
				return status[row];
			else
				return row.ToString();
		}

	}


}
