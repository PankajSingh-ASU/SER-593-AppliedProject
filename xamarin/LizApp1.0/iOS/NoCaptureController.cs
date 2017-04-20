using Foundation;
using System;
using UIKit;
namespace LizApp.iOS
{
	public partial class NoCaptureController : UIViewController
	{
		public NoCaptureController()
		{
		}
		public override void ViewDidLoad()
		{
			base.ViewDidLoad();
			TrapStatus model = new TrapStatus();
			this.status.Model = model;
		}
	}
}
