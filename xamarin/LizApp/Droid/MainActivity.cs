using Android.App;
using Android.Widget;
using Android.OS;
//using Plugin.Geolocator;

namespace LizApp.Droid
{
	[Activity(Label = "LizApp", MainLauncher = true, Icon = "@mipmap/icon")]
	public class MainActivity : Activity
	{
		int count = 1;

		protected override void OnCreate(Bundle savedInstanceState)
		{
			base.OnCreate(savedInstanceState);

			// Set our view from the "main" layout resource
			SetContentView(Resource.Layout.Main);

			//var position = await locator.GetPositionAsync(timeoutMilliseconds: 10000);

			//Console.WriteLine("Position Status: {0}", position.Timestamp);
			//Console.WriteLine("Position Latitude: {0}", position.Latitude);
			//Console.WriteLine("Position Longitude: {0}", position.Longitude);
			//locator.PositionChanged += (sender, e) =>
			//{
			//	position = e.Position;

			//	latitudeLabel.Text = position.Latitude;
			//	longitudeLabel.Text = position.Longitude;
			//};
		}
	}
}

