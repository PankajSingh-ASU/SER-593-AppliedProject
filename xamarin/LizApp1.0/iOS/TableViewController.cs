using Foundation;
using System;
using UIKit;

namespace LizApp.iOS
{
	public partial class TableViewController : UITableViewController
	{
		// Allow us to set the style of the TableView
		UITableView table;
		public TableViewController(UITableViewStyle style) : base(style)
		{
		}

		public override void ViewDidLoad()
		{
			base.ViewDidLoad();
			table = new UITableView(View.Bounds); // defaults to Plain style
			string[] tableItems = new string[] { "Arthropod", "Amphibian", "Lizards", "Mammal", "Snake" };
			Test1 t1 = new Test1();
				t1.abc();
			table.Source = new TaxanomySource(tableItems);
			Add(table);
		}
		public override void PrepareForSegue(UIStoryboardSegue segue, NSObject sender)
		{
			if (segue.Identifier == "taxaSegue")
			{ // set in Storyboard
				var navctlr = segue.DestinationViewController as DataCaptureController;
				if (navctlr != null)
				{
					var source = TableView.Source as TaxanomySource;
					var rowPath = TableView.IndexPathForSelectedRow;
					var item = source.GetItem(rowPath.Row);
					//navctlr.SetTask(this, item); // to be defined on the TaskDetailViewController
				}
			}
		}
	}
}
