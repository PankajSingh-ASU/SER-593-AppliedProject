using Foundation;
using System;
using UIKit;

namespace LizApp.iOS
{
	public class TaxanomySource : UITableViewSource
	{

		string[] TableItems;
		string CellIdentifier = "TableCell";

		public TaxanomySource(string[] items)
		{
			TableItems = items;
		}

		public override nint RowsInSection(UITableView tableview, nint section)
		{
			return TableItems.Length;
		}

		public override UITableViewCell GetCell(UITableView tableView, NSIndexPath indexPath)
		{
			UITableViewCell cell = tableView.DequeueReusableCell(CellIdentifier);
			string item = TableItems[indexPath.Row];

			//---- if there are no cells to reuse, create a new one
			if (cell == null)
			{ cell = new UITableViewCell(UITableViewCellStyle.Default, CellIdentifier); }

			cell.TextLabel.Text = item;

			return cell;
		}
		public string GetItem(int id)
		{
			return TableItems[id];
		}
		public override void RowSelected(UITableView tableView, NSIndexPath indexPath)
		{
			

			tableView.DeselectRow(indexPath, true);
		}
	}
}