using Foundation;
using System;
using UIKit;

namespace LizApp.iOS
{
	public partial class LoginViewController : UIViewController
	{


		//partial void NextButton_TouchUpInside(UIButton sender)
		//{
		//	throw new NotImplementedException();
		//}

		public LoginViewController(IntPtr handle) : base(handle)
		{
			// As soon as the app is done launching, begin generating location updates in the location manag
			Manager = new LocationManager();
			Manager.StartLocationUpdates();
		}
		#region Computed Properties
		public static bool UserInterfaceIdiomIsPhone
		{
			get { return UIDevice.CurrentDevice.UserInterfaceIdiom == UIUserInterfaceIdiom.Phone; }
		}

		public static LocationManager Manager { get; set; }
		#endregion




		#region Override Methods
		public override void DidReceiveMemoryWarning()
		{
			base.DidReceiveMemoryWarning();
			// Release any cached data, images, etc that aren't in use.
		}
		#endregion

		#region Public Methods
		//public void HandleLocationChanged(object sender, LocationUpdatedEventArgs e)
		//{
		//	// Handle foreground update
		//	CLLocation location = e.Location;

		//	//lblAltitude.Text = location.Altitude + " meters";
		//	//lblLongitude.Text = location.Coordinate.Longitude.ToString();
		//	//lblLatitude.Text = location.Coordinate.Latitude.ToString();
		//	////lblCourse.Text = location.Course.ToString();
		//	//lblSpeed.Text = location.Speed.ToString();

		//	Console.WriteLine("foreground updated");
		//}

		#endregion

		#region View Lifecycle
		public override void ViewDidLoad()
		{
			Console.WriteLine("Inside ViewDidLoad");
			base.ViewDidLoad();
			Console.WriteLine("After base  ViewDidLoad");


			//picker = new UIPickerView();

			//picker.Model = modelTRap//Console.WriteLine("modTRaplues: " + picker.Model.ToString());

			SiteModel model = new SiteModel("GWA1", "GWA2", "GWA3", "TOT1");
			site.Model = model;

				model.SiteSelected += (object senderTrapntArgs, EventArgs e)=>
				 {
				  Console.WriteLine("Selecte Array is " + model.selectedSite);
				  if ("GWA1".Equals(model.selectedSite))
					  picker.Model = new ArrayPickerModel("ARRAY 1", "ARRAY 2", "ARRAY 3");
				  if ("GWA2".Equals(model.selectedSite))
					  picker.Model = new ArrayPickerModel("ARRAY 1", "ARRAY 2", "ARRAY 3");
				  if ("GWA3".Equals(model.selectedSite))
					  picker.Model = new ArrayPickerModel("ARRAY 1", "ARRAY 2");
				  if ("GWA4".Equals(model.selectedSite))
					  picker.Model = new ArrayPickerModel("ARRAY 1", "ARRAY 2", "ARRAY 3", "ARRAY 4");
				  if ("TOT1".Equals(model.selectedSite))
					  picker.Model = new ArrayPickerModel("ARRAY 1");
				 };

		}

		public override void PrepareForSegue(UIStoryboardSegue segue, NSObject sender)
		{
			Console.WriteLine("Before base  PrepareForSegue");

			if (validateInputs())
			{
				Console.WriteLine("Inputs  Validated  ");
			}
				//if (capture.On)
				//{
				//	PerformSegue("MyNamedSegue", this);
				//}
				//else {
				//	PerformSegue("MyNamedSegue", this);
				//}
				////SecondViewController = segue.DestinationViewController;

				// do your initialisation here
			Console.WriteLine("After base  PrepareForSegue");

		}
		public override Boolean ShouldPerformSegue(String segueIdentifier, NSObject sender)
		{
			Boolean flag = validateInputs();
			if (!flag)
			{
				UIAlertView alert = new UIAlertView()
				{
					Title = "Incomplete Details",
					Message = "Please provide inputs"
				};
				alert.AddButton("OK");
				alert.Show();
		}
		return flag;
		}

	public bool validateInputs()
		{
			if (this.recorder.Text.Length <= 0)
			{
				//UIAlertView
				this.recorder.BackgroundColor = UIColor.Yellow;
				return false;
			}
			if (this.handler.Text.Length <= 0)
			{
				this.handler.BackgroundColor = UIColor.Yellow;
				return false;
			}
			//if (this.site.Length <= 0)
			//{
			//	this.site.BackgroundColor = UIColor.Yellow;
			//	return false;
			//}
			//if (this.recorder.Text.Length <= 0)
			//{
			//	this.recorder.BackgroundColor = UIColor.Yellow;
			//	return false;
			//}

			return true;
		}

		#endregion
	}
}



