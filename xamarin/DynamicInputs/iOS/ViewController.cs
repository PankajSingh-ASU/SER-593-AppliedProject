using System;
using CoreGraphics;
using UIKit;

namespace DynamicInputs.iOS
{
	public partial class ViewController : UIViewController
	{
		//int count = 1;

		public ViewController(IntPtr handle) : base(handle)
		{
			
		}

		public override void ViewDidLoad()
		{
			
			base.ViewDidLoad();


			FileHelper fh = new FileHelper();
			string path=fh.GetLocalFilePath("db_sqlite-net.db");
			SampleModel sm = new SampleModel(path);
			sm.createConnection();
			string message = sm.createTable();
			Console.WriteLine(message);



			//UIPickerView myPickerView = new UIPickerView()
			//{
			//	AutoresizingMask = UIViewAutoresizing.FlexibleWidth,
			//	ShowSelectionIndicator = true,
			//	Model = new InputType(this,View),
			//	//BackgroundColor = backgroundColor,
			//	Hidden = false
			//};

			//View.AddSubview(myPickerView);
			/**var frame = new CGRect(10, 10, 300, 40);
			UITextField text = new UITextField
			{
				AutoresizingMask = UIViewAutoresizing.FlexibleWidth,
				KeyboardType = UIKeyboardType.NumberPad,
				Hidden = false,
				BackgroundColor = UIColor.Gray,
				Highlighted = true,
				Frame= frame
			};
			text.Layer.BorderColor = UIColor.White.CGColor;
			text.Layer.BorderWidth = 1f;
			text.BecomeFirstResponder();
			View.AddSubview(text);*/
		}

		public override void DidReceiveMemoryWarning()
		{
			base.DidReceiveMemoryWarning();
			// Release any cached data, images, etc that aren't in use.		
		}
	}
}
