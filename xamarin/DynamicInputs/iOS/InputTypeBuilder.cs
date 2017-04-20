using Foundation;
using System;
using UIKit;
using CoreGraphics;
namespace DynamicInputs.iOS
{
	public class InputTypeBuilder
	{
		public InputTypeBuilder()
		{
		}
		public void createTextforString(UIView pvc)
		{
			Console.WriteLine("inside createTextforString");
			UILabel label = new UILabel();
			label.Text = "Insert Name";
			label.Hidden = false;
			label.Highlighted = true;
			label.BackgroundColor = UIColor.White;
			int x = 10, y = 240;
			var frame = new CGRect(x, y, 150, 30);

			label.Frame = frame;

			UITextField text = new UITextField
			{

				Hidden = false,
				BackgroundColor = UIColor.LightGray,
				Highlighted = true,
				Frame = new CGRect(x + 150, y, 200, 30)
			};
			text.BecomeFirstResponder();

			pvc.AddSubview(label);
			pvc.AddSubview(text);
			Console.WriteLine("added Name ");
		}
		public void createTextforInteger(UIView pvc)
		{
			Console.WriteLine("inside createTextforInteger");
			UILabel label = new UILabel();
			label.Text = "Insert Age";
			label.Hidden = false;
			label.Highlighted = true;
			label.BackgroundColor = UIColor.White;
			int x = 10, y = 280;
			var frame = new CGRect(x, y, 150, 30);

			label.Frame = frame;

			UITextField text = new UITextField
			{
				KeyboardType = UIKeyboardType.NumberPad,
				Hidden = false,
				BackgroundColor = UIColor.LightGray,
				Highlighted = true,

					Frame = new CGRect(x+150, y, 200, 30)
			};
			//text.BecomeFirstResponder();

			pvc.AddSubview(label);
			pvc.AddSubview(text);
			Console.WriteLine("added text ");
		}


		public void createTextforDouble(UIView pvc)
		{
			Console.WriteLine("inside createTextforString");
			UILabel label = new UILabel();
			label.Text = "Insert height";
			label.Hidden = false;
			label.Highlighted = true;
			label.BackgroundColor = UIColor.White;
			int x = 10, y = 320;
			var frame = new CGRect(x, y, 150, 30);

			label.Frame = frame;

			UITextField text = new UITextField
			{

				Hidden = false,
				BackgroundColor = UIColor.LightGray,
				Highlighted = true,
				Frame = new CGRect(x + 150, y, 200, 30)
			};
			//text.BecomeFirstResponder();

			pvc.AddSubview(label);
			pvc.AddSubview(text);
			Console.WriteLine("added Floats ");
		}

		public void createDatePicker(UIView pvc)
		{
			Console.WriteLine("inside createTextforString");
			UILabel label = new UILabel();
			label.Text = "DOB";
			label.Hidden = false;
			label.Highlighted = true;
			label.BackgroundColor = UIColor.White;
			int x = 10, y = 360;
			var frame = new CGRect(x, y, 50, 30);

			label.Frame = frame;

			UIDatePicker dpPurchaseDate;
			dpPurchaseDate = new UIDatePicker(new CGRect(100, 360, pvc.Bounds.Width-100, 40));
			dpPurchaseDate.Mode = UIDatePickerMode.Date;
			dpPurchaseDate.MaximumDate = NSDate.Now;
			dpPurchaseDate.TimeZone = NSTimeZone.LocalTimeZone;
			dpPurchaseDate.UserInteractionEnabled = true;
			dpPurchaseDate.BackgroundColor = UIColor.White;

			pvc.AddSubview(label);
			pvc.AddSubview(dpPurchaseDate);
		}
	}
}
